package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.ClientState;
import it.polimi.ingsw.network.message.lobbymessage.*;
import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.commongoals.CommonGoalCard;
import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.gameboard.Board;
import it.polimi.ingsw.model.gameboard.Coordinates;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.network.message.connectionmessage.*;
import it.polimi.ingsw.network.message.gamemessage.*;
import it.polimi.ingsw.network.message.lobbymessage.ExitLobbyRequest;
import it.polimi.ingsw.network.message.lobbymessage.ExitLobbyResponse;
import it.polimi.ingsw.network.message.lobbymessage.LobbyMessage;
import it.polimi.ingsw.network.message.lobbymessage.StartGameRequest;
import it.polimi.ingsw.utils.Observable;

import java.util.*;

public class TextualUI extends Observable implements Runnable {

    private final InputValidator inputValidator = new InputValidator();
    private ClientState clientState;

    private ActionType actionType;
    private GameMessage lastMessage;
    private final Scanner s;
    private String myUsername;
    //private boolean isActive;

    ArrayList<Square> tilesToDraw;
    ArrayList<ItemTile> tilesToInsert;
    public TextualUI() {
        s = new Scanner(System.in);
        tilesToDraw = new ArrayList<>();
        tilesToInsert = new ArrayList<>();
    }

    @Override
    public void run() {
        String input;
        while (true) {
            System.out.print(">>> ");
            input = s.nextLine();
            elaborateInput(input);
        }
    }

    /**
     * Elaborates the input from the user
     * @param input the input from the user
     */
    private void elaborateInput(String input) {

        switch(clientState){
            case IN_SERVER:
                elaborateConnectionCommand(input);
                break;
            case IN_A_LOBBY:
                elaborateLobbyCommand(input);
                break;
            case IN_GAME:
                elaborateGameCommand(input);
                break;
            default:
                System.err.println("Stato client non supportato wtf bro");
        }
    }

    /**
     * Elaborates the input from the user when the client is in a game
     * @param input the input from the user
     */
    private void elaborateGameCommand(String input) {
        switch(actionType){
            case DRAW_TILES:
                DrawInfoMessage message = (DrawInfoMessage) lastMessage;
                elaborateDrawInput(input, message.getModel(), message.getMaxNumItems());
                break;
            case ORDER_HAND:
                elaborateOrderHandInput(input);
                break;
            case REORDER_THREE_TILES:
                elaborateReorderThreeTiles(input);
                break;
            case INSERT_HAND:
                elaborateInsertHand(input);
                break;
            case NONE:
                break;
            default:
                System.err.println("Should not send anything in this phase");
        }
    }

    /**
     * Elaborates the input from the user when insertion info is expected
     * @param input the input from the user
     */
    private void elaborateInsertHand(String input) {
        if(inputValidator.invalidColumnFormat(input)){
            System.out.println("Formato non valido! Inserisci la colonna nel formato: (colonna) :");
            return;
        }
        int column = Integer.parseInt(input.trim());
        if (column < 0 || column > 4) {
            System.out.println("Colonna non valida! Assicurati di inserire colonne che rientrano nella dimensione della libreria (0-4)");
            return;
        } else if (inputValidator.columnHasLessSpace(column, ((InsertInfoMessage) lastMessage).getEnabledColumns())) {
            System.out.println("La colonna scelta non ha sufficiente spazio per inserire la mano! Inserisci un'altra colonna: ");
            return;
        } else {
            setActionType(ActionType.NONE);
            notifyObservers(new InsertTilesMessage(myUsername, tilesToInsert, column));
        }
    }

    /**
     * Elaborates the input from the user when info for reordering three tile in the hand is expected
     * @param input the input from the user
     */
    private void elaborateReorderThreeTiles(String input) {
        if(InputValidator.invalidOrderFormat(input, 3)){
            System.out.println("Formato non valido! Questo è l'ordine delle tessere che hai in mano :");
            showHand(myUsername, tilesToInsert);
            System.out.println("Inserisci il nuovo ordine della mano: (ad es. 2,1,3 metterà la tessera 2 in prima posizione, la 1 in seconda e la 3 in terza posizione)");
            return;
        }
        String[] order = input.split(",");
        int first = Integer.parseInt(order[0].trim());
        int second = Integer.parseInt(order[1].trim());
        int third = Integer.parseInt(order[2].trim());
        if (first < 1 || first > 3 || second < 1 || second > 3 || third < 1 || third > 3) {
            System.out.println("Ordine inserito non riconosciuto. Assicurati che i numeri siano (1-3)");
            return;
        } else if (first == second || second == third || first == third) {
            System.out.println("Non si può avere due tessere nello stesso slot! Riprova");
            return;
        } else{
            Collections.swap(tilesToInsert, first - 1, 0);
            if (second != 1) {
                Collections.swap(tilesToInsert, second - 1, 1);
            } else {
                Collections.swap(tilesToInsert, third - 1, 2);
            }
            showHand(myUsername,tilesToInsert);
            System.out.println("Vuoi cambiare ancora l'ordine delle tessere? (y/n)");
            setActionType(ActionType.ORDER_HAND);
            return;
        }
    }

    /**
     * Elaborates the input from the user when info for ordering the hand is expected
     * @param input the input from the user
     */
    private void elaborateOrderHandInput(String input) {
        if(!InputValidator.isYesOrNo(input)){
            System.out.println("Risposta non valida! Inserisci 'y' o 'n'");
            return;
        }else if(inputValidator.isYes(input)){
            if(tilesToInsert.size() == 2){
                Collections.swap(tilesToInsert, 1, 0);
                showHand(myUsername, tilesToInsert);
                System.out.println("Vuoi invertire ancora l'ordine delle tessere? (y/n)");
                return;
            }else if(tilesToInsert.size()==3){
                setActionType(ActionType.REORDER_THREE_TILES);
                System.out.println("Inserisci il nuovo ordine della mano: (ad es. 2,1,3 mette la tessera 2 in prima posizione, la 1 in seconda e la 3 in terza posizione)");
                return;
            } else {
                System.err.println("Dimensioni mano illegali");
            }
        } else{
            setActionType(ActionType.INSERT_HAND);
            showInsertInfo((InsertInfoMessage) lastMessage);
            System.out.println("Inserisci la colonna in cui vuoi inserire la mano: ");
            return;
        }
    }

    /**
     * Elaborates the input from the user when info for drawing tiles is expected
     * @param input the input from the user
     * @param model the model of the game
     * @param maxNumItems the maximum number of items that can be drawn
     */
    private void elaborateDrawInput(String input, GameView model, int maxNumItems) {
        Square[][] board = model.getBoard().getGameboard();
        if(tilesToDraw.size() > 0 &&  input.equalsIgnoreCase("ok")){
            notifyObservers(new DrawTilesMessage(myUsername, tilesToDraw));
            return;
        }
        if(InputValidator.invalidCoordFormat(input)){
            System.out.println("Formato coordinate non valido!");
            return;
        }
        String[] coords = input.split(",");
        int row = Integer.parseInt(coords[0].trim());
        int column = Integer.parseInt(coords[1].trim());
        if (row < 0 || row > Board.DIMENSIONS - 1 || column < 0 || column > Board.DIMENSIONS - 1) {
            System.out.println("Coordinate non valide! Assicurati di inserire coordinate che rientrino nelle dimensioni della Board (0-" + (Board.DIMENSIONS - 1));
            return;
        } else if (inputValidator.isTileAlreadyOnHand(row, column, tilesToDraw)) {
            System.out.println("Non puoi prendere una tessera che hai già preso! Inserisci altre coordinate: ");
            return;
        } else if (!board[row][column].isPickable()) {
            System.out.println("Coordinate non valide! Assicurati di inserire le coordinate di una tessera che sia prendibile secondo le regole di gioco!");
            return;
        } else if (tilesToDraw.size() > 0 && !inputValidator.inLineTile(row, column, tilesToDraw)) {
            System.out.println("Coordinate non valide! La tessera che prendi deve essere adiacente e in linea retta (orizzontale o verticale) con le tessere che hai già preso in questo turno! Inserisci le coordinate nuovamente: ");
            return;
        }
        tilesToDraw.add(new Square(new Coordinates(row, column), board[row][column].getItem().getType()));
        if (inputValidator.isPossibleToDrawMore(tilesToDraw, board) && tilesToDraw.size()<Math.min(3,maxNumItems))
            System.out.println("Inserisci le coordinate della " + (tilesToDraw.size() + 1) + "° tessera separate da una virgola (es. riga, colonna) :");
        else{
            setActionType(ActionType.ORDER_HAND);
            notifyObservers(new DrawTilesMessage(myUsername, tilesToDraw));
        }
    }

    /**
     * Elaborates a LobbyCommand from the user
     * @param input the input from the user
     */
    private void elaborateLobbyCommand(String input) {
        String[] parts = input.split(" ");
        LobbyCommand lobbyCommand = null;
        for (LobbyCommand c : LobbyCommand.values()) {
            if (parts[0].toUpperCase().equals(c.toString())) {
                lobbyCommand = c;
                break;
            }
        }
        if(lobbyCommand == null){
            System.out.println("Comando non valido!");
            return;
        }
        switch (lobbyCommand) {
            case HELP:
                System.out.println("Lista dei comandi disponibili nella lobby:");
                for (LobbyCommand c : LobbyCommand.values()) {
                    System.out.println(c.toString() + " - " + c.getDescription());
                }
                break;
            case START:
                notifyObservers(new StartGameRequest());
                break;
            case EXIT:
                notifyObservers(new ExitLobbyRequest());
                break;
            case PLAYERLIST:
                notifyObservers(new PlayerListRequest());
                break;
            case CHAT:
                System.err.println(lobbyCommand + " not implemented yet");
                break;
            case KICK:
                System.err.println(lobbyCommand + " not implemented yet");
                break;
            case NUMPLAYERS:
                    if (parts.length != 2) {
                        System.out.println("Comando non valido!");
                        return;
                    } else if(!inputValidator.invalidNumOfPlayersFormat(parts[1])){
                        int chosenNum = Integer.parseInt(parts[1].trim());
                        notifyObservers(new ChangeNumOfPlayerRequest(chosenNum));
                    }else{
                        System.out.println("Numero non valido! La partita deve avere minimo " + GameController.MIN_PLAYERS + " giocatori e massimo " + GameController.MAX_PLAYERS + " giocatori");
                        return;
                    }
                break;
            default:
                System.err.println("Comando non valido, should never reach this state");
                break;
        }
    }

    /**
     * Elaborates a ConnectionCommand from the user
     * @param input the input from the user
     */
    private void elaborateConnectionCommand(String input) {
        String[] parts = input.split(" ");
        ConnectionCommand command = null;
        for (ConnectionCommand c : ConnectionCommand.values()) {
            if (parts[0].toUpperCase().equals(c.toString())) {
                command = c;
                break;
            }
        }
        if(command == null){
            System.out.println("Comando non valido!");
            return;
        }
        switch (command) {
            case HELP:
                for (ConnectionCommand c : ConnectionCommand.values()) {
                    System.out.println(c.toString() + " " + c.getDescription());
                }
                break;
            case USERNAME:
                if (parts.length != 2) {
                    System.out.println("Comando non valido!");
                    return;
                } else {
                    if (!inputValidator.isValidUsername(parts[1])) {
                        System.out.println("Username non valido");
                        return;
                    } else {
                        System.out.println("Username impostato a " + parts[1]);
                        notifyObservers(new UsernameRequest(parts[1]));
                    }
                }
                break;
            case EXIT:
                //TODO disconnettiti dal server
                break;
            case GAMES:
                notifyObservers(new LobbyListRequest());
                break;
            case JOIN:
                if (parts.length != 2) {
                    System.out.println("Comando non valido!");
                    return;
                } else {
                    notifyObservers(new JoinLobbyRequest(parts[1]));
                }
                break;
            case CREATE:
                if (parts.length != 2) {
                    System.out.println("Comando non valido!");
                    return;
                } else {
                    notifyObservers(new CreateLobbyRequest(parts[1]));
                }
                break;
            default:
                System.err.println("Comando non valido, should never reach this state");
                break;
        }
    }

    /**
     * Sets the client state
     * @param clientState the client state
     */
    public void setClientState(ClientState clientState) {
        this.clientState = clientState;
    }


    //DRAW PHASE stuff

    /**
     * Shows the board, the player's bookshelf and proceeds asking the player to insert the coordinates of the tiles he wants to pick.
     *
     */
    public void showDrawInfo(DrawInfoMessage message) {
        GameView model = message.getModel();

        showBookshelf(model.getCurrentPlayer().getUsername(), model.getCurrentPlayer().getBookshelf().getShelfie());
        showBoard(model.getBoard().getGameboard());
    }

    public ClientState getClientState() {
        return clientState;
    }

    /**
     * Recalls askDraw() if the tiles previously taken are rejected by the Server because they are found invalid
     *
     * @param username    username of the player
     * @param board       gameboard
     * @param bookshelf   player's bookshelf
     * @param maxNumItems max num of items the player can draw according to its bookshelf
     */
    public void rejectDrawRequest(String username, Square[][] board, ItemTile[][] bookshelf, int maxNumItems) {
        System.out.println("Invalid draw request! It seems like your client misbehaved... " +
                "Try re-inserting the coordinates of the tiles you want to draw and if the error persists draw some other tiles because those you are trying to draware invalid!");
        //showDrawInfo(username, board, bookshelf, maxNumItems);
    }

    //INSERT PHASE stuff

    /**
     * Prints the numbers from 0 to the passed parameter-1 as column indexes (separated by 5 spaces each)
     *
     * @param columns number of columns to print
     */
    private void printColumnIndexes(int columns) {
        System.out.print("       ");
        for (int i = 0; i < columns; i++) {
            System.out.print("  " + i + "   ");
        }
        System.out.print("\n");
    }

    /**
     * @param type ItemType
     * @return the ESC ColorCode to paint the background accordingly to the ItemType received
     */
    private String paintBG(ItemType type) {

        StringBuilder stringBuilder = new StringBuilder();
        Color c;
        switch (type) {
            case CAT:
                c = Color.GREEN;
                break;
            case PLANT:
                c = Color.FUCSIA;
                break;
            case FRAME:
                c = Color.BLUE;
                break;
            case GAME:
                c = Color.YELLOW_BOLD;
                break;
            case TROPHY:
                c = Color.CYAN_BOLD;
                break;
            case BOOK:
                c = Color.WHITE;
                break;
            default:
                c = Color.NO_COLOR;
                break;
        }
        return stringBuilder.append(c).append("  ").append(type).append("  ").append(Color.NO_COLOR).toString();

    }

    /**
     * @param type ItemType
     * @return the ESC ColorCode to paint the foreground accordingly to the ItemType received
     */
    private String paintFG(ItemType type) {
        StringBuilder stringBuilder = new StringBuilder();
        Color c;
        switch (type) {
            case CAT:
                c = Color.WGREEN;
                break;
            case PLANT:
                c = Color.WFUCSIA;
                break;
            case FRAME:
                c = Color.WBLUE;
                break;
            case GAME:
                c = Color.WYELLOW_BOLD;
                break;
            case TROPHY:
                c = Color.WCYAN_BOLD;
                break;
            case BOOK:
                c = Color.WWHITE;
                break;
            default:
                c = Color.NO_COLOR;
                break;
        }
        return stringBuilder.append(c).append("  ").append(type).append("  ").append(Color.NO_COLOR).toString();

    }

    /**
     * Prints the GameBoard
     *
     * @param board matrix of Squares to print
     */
    private void showBoard(Square[][] board) {
        System.out.println("Game Board:");
        printColumnIndexes(Board.DIMENSIONS);
        StringBuilder strBoard = new StringBuilder();
        for (int i = 0; i < Board.DIMENSIONS; i++) {
            strBoard.append("      ");
            for (int j = 0; j < Board.DIMENSIONS; j++) {
                strBoard.append("+-----");
            }
            strBoard.append("+\n");
            strBoard.append("  ").append(i).append("   |");
            for (int j = 0; j < Board.DIMENSIONS; j++) {
                //strBoard.append("  ");
                if (board[i][j].isPickable()) {
                    //strBoard.append(board[i][j].getItem().toColorString());
                    strBoard.append(paintBG(board[i][j].getItem().getType()));
                } else {
                    //strBoard.append("  ").append(board[i][j].getItem()).append("  ");
                    strBoard.append(paintFG(board[i][j].getItem().getType()));
                }
                //strBoard.append("  |");
                strBoard.append("|");
            }
            strBoard.append("\n");
        }
        strBoard.append("      ");
        for (int j = 0; j < Board.DIMENSIONS; j++) {
            strBoard.append("+-----");
        }
        strBoard.append("+");
        System.out.println(strBoard);
    }

    /**
     * Prints the bookshelf of the player
     *
     * @param username username of the player whose bookshelf is printed
     * @param shelfie  matrix of ItemTiles
     */
    public void showBookshelf(String username, ItemTile[][] shelfie) {
        System.out.println("BookShelf of player " + username);
        printColumnIndexes(Bookshelf.Columns);
        StringBuilder strShelf = new StringBuilder();
        for (int i = 0; i < Bookshelf.Rows; i++) {
            strShelf.append("      ");
            for (int j = 0; j < Bookshelf.Columns; j++) {
                strShelf.append("+-----");
            }
            strShelf.append("+\n");
            strShelf.append("  ").append(i).append("   |");
            for (int j = 0; j < Bookshelf.Columns; j++) {
                strShelf.append(paintFG(shelfie[i][j].getType())).append("|");
            }
            strShelf.append("\n");
        }
        strShelf.append("      ");
        for (int j = 0; j < Bookshelf.Columns; j++) {
            strShelf.append("+-----");
        }
        strShelf.append("+");
        System.out.println(strShelf);
    }


    /**
     * Prints the number of points scored by the player when he/she has achieved a common goal
     * @param message the message containing the number of points scored
     */
    private void showAchievedCommonGoal(AchievedCommonGoalMessage message) {
        System.out.println(message.getContent());
    }

    /**
     * Prints the contents of a player's hand.
     *
     * @param username the name of the player whose hand is being displayed
     * @param hand the ArrayList of ItemTiles representing the player's hand
     */
    public void showHand(String username, ArrayList<ItemTile> hand) {
        System.out.println("Hand of player " + username);
        for (ItemTile item : hand) {
            System.out.print(" " + paintFG(item.getType()) + " ");
        }
        System.out.println();
    }

    /**
     * Displays a message indicating that the game has started, the game board, the common goals, and the leaderboard.
     *
     * @param model The GameView object containing information about the game.
     */
    private void showGameStarted(GameView model) {
        System.out.println("######################################\n" +
                "#        La partita è iniziata!      #\n" +
                "######################################");
        showBoard(model.getBoard().getGameboard());
        showCommonGoals(model);
        showLeaderboard(model.getLeaderboard());
    }

    /**
     * Displays a message indicating that a new turn has started for a specific user.
     *
     * @param username The username of the user whose turn it is.
     */
    private void showNewTurn(String username) {
        System.out.println("######################################\n" +
                "    E' il turno di " + username + "\t\n" +           //migliorabile
                "######################################");
    }

    /**
     * Displays the leaderboard with each player's rank, username, and score.
     *
     * @param sortedMap A LinkedHashMap containing players' usernames and scores sorted in descending order.
     */
    public void showLeaderboard(LinkedHashMap<String, Integer> sortedMap) {
        int rank = 1;
        System.out.println("Leaderboard :");
        for (String key : sortedMap.keySet()) {
            System.out.println("#" + rank + ". " + key + " : " + sortedMap.get(key));
            rank++;
        }
    }

    /**
     * Displays a message indicating that the client has successfully connected to the server.
     */
    private void showConnectedToServer() {
        System.out.println("Connesso al server!");

    }
    /**
     * Prints the first and second common goals from the provided GameView object.
     *
     * @param model The GameView object containing the common goals to display.
     */
    private void showCommonGoals(GameView model) {
        System.out.println("First Common Goal: " + model.getCommonGoals().get(0).toString() +
                "\nSecond Common Goal: " + model.getCommonGoals().get(1).toString());
    }
    /**
     * Prints a message with the username and points obtained for a personal goal.
     *
     * @param message The PersonalGoalPointsMessage object containing the username and points to display.
     */
    private void showPersonalGoalPoints(PersonalGoalPointsMessage message) {
        System.out.println("######################################\n" +
                message.getPlayerUsername() + " ha ottenuto " + message.getPoints() + " punti per il suo obiettivo personale!\n" +
                "######################################");
    }

    /**
     * Prints a message with the username and points obtained for adjacent items in the library.
     *
     * @param message The AdjacentItemsPointsMessage object containing the username and points to display.
     */
    private void showAdjacentItemsPoints(AdjacentItemsPointsMessage message) {
        System.out.println("######################################\n" +
                message.getPlayerUsername() + " ha ottenuto " + message.getPoints() + " punti per i gruppi di tessere uguali adiacenti nella libreria!\n" +
                "######################################");
    }

    /**
     * Prints a message indicating that the current turn is the last turn of the game for the provided player.
     *
     * @param message The LastTurnMessage object containing the username of the player who triggered the last turn.
     */
    private void showLastTurn(LastTurnMessage message) {
        System.out.println("######################################\n" +
                message.getCurrentPlayer() + " ha riempito la sua libreria!\n" +
                "Questo è l'ultimo giro di gioco!\n" +
                "######################################");
    }

    /**
     * Prints the end game message as well as the leaderboard and the winner of the game.
     *
     * @param ranking The LinkedHashMap object containing the ranking and scores of all players.
     */
    private void showEndGame(LinkedHashMap<String, Integer> ranking) {
        System.out.println("######################################\n" +
                "#        La partita è finita         #\n" +
                "######################################");
        showLeaderboard(ranking);
        System.out.println("Il vincitore della partita è " + ranking.keySet().toArray()[0] + " con " + ranking.values().toArray()[0] + " punti!");

    }

    /**
     * Displays a list of available game lobbies.
     *
     * @param lobbies HashMap containing the available game lobbies.
     */
    private void showLobbyList(HashMap<String, Map.Entry<Integer, Integer>> lobbies) {
        System.out.println("Lista delle partite disponibili:");
        if(lobbies.size()==0){
            System.out.println("Non ci sono partite disponibili! Usa il comando create per crearne una nuova!");
        }
        else{
            for (String key : lobbies.keySet()) {
                System.out.println("Nome partita: " + key + " | Numero giocatori: " + lobbies.get(key).getKey() + "/" + lobbies.get(key).getValue());
            }
        }
    }

    /**
     * Displays the response for changing a username.
     *
     * @param successful A boolean indicating if the username change was successful or not.
     * @param username The new username.
     */
    private void showUsernameResponse(boolean successful, String username) {
        if(successful){
            System.out.println("Username cambiato in " + username + "!");
        }else{
            System.out.println("Username " + username+" già in uso! Riprova con un altro username!");
        }
         
    }

    /**
     * Shows the response of joining a lobby
     *
     * @param successful a boolean to indicate if the joining was successful.
     * @param content    the string content of the response
     */
    private void showJoinLobbyResponse(boolean successful, String content) {
        System.out.println(content);
        if(!successful){
            System.out.println(content);
        }
        else {
            setClientState(ClientState.IN_A_LOBBY);
            printEnteredLobby();
        }
    }

    /**
     * Prints the response of creating a lobby
     *
     * @param successful a boolean to indicate if the creation was successful.
     */
    private void printCreateLobbyResponse(boolean successful) {
        if(successful){
            setClientState(ClientState.IN_A_LOBBY);
            System.out.println("Partita creata con successo!");
            System.out.println("In attesa di altri giocatori...");

              
        }else{
            System.out.println("Partita non creata! Riprova con un altro nome!");
        }
    }

    /**
     * Prints a message to confirm the client has entered a lobby
     */
    private void printEnteredLobby() {
        System.out.println("Sei entrato nella partita!");
    }

    /**
     * Displays a response indicating if the user has successfully exited the lobby.
     * If successful, update the client state to IN_SERVER.
     *
     * @param successful a boolean indicating if the exit was successful or not
     */
    private void showExitLobbyResponse(boolean successful) {
        if(successful){
            System.out.println("Uscito dalla lobby");
            setClientState(ClientState.IN_SERVER);
        }else{
            System.out.println("Errore nell'uscire dalla lobby, riprova");

        }
    }

    /**
     * Displays a message indicating that the game is not ready to start.
     */
    private void showGameNotReady() {
        System.out.println("Non ci sono le condizioni per iniziare la partita");

    }

    /**
     * Shows a message indicating that the command entered is invalid.
     */
    private void showInvalidCommand() {
        System.out.println("Il comando è invalido");
    }

    /**
     * Shows a message indicating that the user is not the admin.
     */
    private void showNotAdmin() {
        System.out.println("You are not the admin.");
    }

    /**
     * Shows a message indicating that the old admin has been removed and a new admin has been set.
     * @param old_admin The username of the old admin that was removed.
     * @param new_admin The username of the new admin that has been set.
     */
    private void showNewAdmin(String old_admin, String new_admin) {
        System.out.println("Il vecchio admin " + old_admin + " è stato rimosso");
        System.out.println("Il nuovo admin è " + new_admin);
    }

    private void showPlayerListResponse(ArrayList<String> clients) {
        System.out.println("Lista dei giocatori nel lobby:");
        System.out.println("Non ci sono partite disponibili! Usa il comando create per crearne una nuova!");
        for (String client : clients) {
            if(client.equals(clients.get(0)))
                System.out.println("Nome admin: " + client);
            else{
                System.out.println("Nome giocatore: " + client);
            }
        }
    }

    /**
     * Asks the user whether they want to change or reverse the order of tiles in their hand.
     * @param m2 The message containing the player's current hand.
     */
    private void askOrderHand(InsertInfoMessage m2) {
        if(m2.getHand().size()==2){
            System.out.println("Vuoi invertire l'ordine delle tessere? (y/n)");
        } else if(m2.getHand().size()==3){
            System.out.println("Vuoi cambiare l'ordine delle tessere? (y/n)");
        } else {
            System.err.println("Hand size illegal");
        }
    }

    /**
     * Displays information about the current insert phase, including the player's bookshelf and hand.
     * @param m The message containing the player's username, bookshelf, and hand.
     */
    private void showInsertInfo(InsertInfoMessage m) {
        System.out.println("Inizia la insert phase\n");
        showBookshelf(m.getUsername(), m.getShelfie());
        showHand(m.getUsername(), tilesToInsert);
    }

    /**
     * Sets whether the player is currently active (i.e. it is their turn).
     * @param isActive True if the player is active, false otherwise.
     */
    private void setIsActive(boolean isActive) {
        //this.isActive = isActive;
    }

    /**
     * Sets the action type
     * @param actionType the action type
     */
    private void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    /**
     * Handles a game message.
     * @param message The game message.
     */
    private void onGameMessage(GameMessage message) {
        lastMessage = message;
        setIsActive(message.getUsername().equals(myUsername));
        switch (message.getType()) {
            case GAME_STARTED:
                setClientState(ClientState.IN_GAME);
                //showGameStarted(((GameStartedMessage) message).getGameboard());
                showGameStarted(((GameStartedMessage) message).getGameView());
                break;
            case NEW_TURN:
                tilesToInsert.clear();
                tilesToDraw.clear();
                showNewTurn(((NewTurnMessage) message).getCurrentPlayer());
                setActionType(ActionType.NONE);
                break;
            case BOARD:
                showBoard(((BoardMessage) message).getBoard());
                break;
            case LEADERBOARD:
                showLeaderboard(((LeaderBoardMessage) message).getLeaderboard());
                break;
            case BOOKSHELF:
                BookshelfMessage m = (BookshelfMessage) message;
                showBookshelf(m.getUsername(), m.getBookshelf());
                break;
            case DRAW_INFO:
                DrawInfoMessage m1 = (DrawInfoMessage) message;
                showDrawInfo(m1);
                //if(isActive) {
                    System.out.println("Guardando la tua libreria, puoi prendere al massimo " + Math.min(3, m1.getMaxNumItems()) + " tessere. Di più non riusciresti a inserirne!");
                    System.out.println("Inserisci le coordinate della 1° tessera separate da una virgola (es. riga, colonna) :");
                    setActionType(ActionType.DRAW_TILES);
                //}
                break;
            case INSERT_INFO:
                InsertInfoMessage m2 = (InsertInfoMessage) message;
                tilesToInsert = m2.getHand();
                showInsertInfo(m2);
                //if(isActive) {
                    if (m2.getHand().size() == 1) {
                        System.out.println("Inserisci la colonna in cui vuoi inserire la tessera");
                        setActionType(ActionType.INSERT_HAND);
                    }
                    else {
                        setActionType(ActionType.ORDER_HAND);
                        askOrderHand(m2);
                    }
                //}

                break;
            case ACHIEVED_COMMON_GOAL:
                AchievedCommonGoalMessage m3 = (AchievedCommonGoalMessage) message;
                showAchievedCommonGoal(m3);
                break;
            case NO_COMMON_GOAL:
                System.out.println(((NoCommonGoalMessage) message).getContent());
                break;
            case LAST_TURN:
                showLastTurn((LastTurnMessage) message);
                break;
            case ADJACENT_ITEMS_POINTS:
                showAdjacentItemsPoints((AdjacentItemsPointsMessage) message);
                break;
            case PERSONAL_GOAL_POINTS:
                showPersonalGoalPoints((PersonalGoalPointsMessage) message);
                break;
            case END_GAME:
                EndGameMessage m4 = (EndGameMessage) message;
                showEndGame(m4.getRanking());
                break;
            default:
                System.err.println("Ignoring event from model");
                break;
        }
    }

    /**
     * Handles a ConnectionMessage
     * @param message
     */
    private void onConnectionMessage(ConnectionMessage message) {
        switch (message.getType()){
            case CONNECTED_TO_SERVER:
                showConnectedToServer();
                setClientState(ClientState.IN_SERVER);
                break;
            case LOBBY_LIST_RESPONSE:
                showLobbyList(((LobbyListResponse) message).getLobbies());
                break;
            case CREATE_LOBBY_RESPONSE:
                printCreateLobbyResponse(((CreateLobbyResponse) message).isSuccessful());
                break;
            case JOIN_LOBBY_RESPONSE:
                showJoinLobbyResponse(((JoinLobbyResponse) message).isSuccessful(), ((JoinLobbyResponse) message).getContent());
                break;
            case USERNAME_RESPONSE:
                this.myUsername = ((UsernameResponse) message).getUsername();
                showUsernameResponse(((UsernameResponse) message).isSuccessful(), ((UsernameResponse) message).getUsername());
                break;
            default:
                System.err.println("Ignoring ConnectionMessage from server");
                break;
        }
    }

    /**
     * Handles a LobbyMessage
     * @param message the message to handle
     */
    private void onLobbyMessage(LobbyMessage message) {
        switch(message.getType()){
            case EXIT_LOBBY_RESPONSE:
                showExitLobbyResponse(((ExitLobbyResponse) message).isSuccessful());
                break;
            case PLAYER_LIST_REQUEST:
                showPlayerListResponse(((PlayerListResponse) message).getClients());
                break;
            case NEW_ADMIN:
                showNewAdmin(((NewAdminMessage) message).getOld_admin(), ((NewAdminMessage) message).getNew_admin());
                break;
            case GAME_NOT_READY:
                showGameNotReady();
                break;
            case NOT_ADMIN:
                showNotAdmin();
                break;
            case INVALID_COMMAND:
                showInvalidCommand();
                break;
            case CHANGE_NUM_OF_PLAYER_RESPONSE:
                System.out.println(((ChangeNumOfPlayerResponse) message).getContent());
                break;
            default:
                System.err.println("Ignoring LobbyMessage from server "+ message.getType().toString());
                break;
        }
    }

    public void update(Message message) {
        if (message instanceof GameMessage) {
            onGameMessage((GameMessage) message);
        }else if(message instanceof ConnectionMessage){
            onConnectionMessage((ConnectionMessage) message);
        } else if (message instanceof LobbyMessage) {
            onLobbyMessage((LobbyMessage) message);
        } else {
            System.err.println("Ignoring message from server");
        }
    }

    public GameMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(GameMessage lastMessage) {
        this.lastMessage = lastMessage;
    }
}
