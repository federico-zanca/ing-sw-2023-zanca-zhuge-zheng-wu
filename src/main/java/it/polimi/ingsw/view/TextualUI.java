package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.distributed.ClientState;
import it.polimi.ingsw.network.message.lobbymessage.*;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.model.ItemTile;
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
    private final Printer printer = new Printer();
    private ClientState clientState;
    private ActionType actionType;
    private GameMessage lastMessage;
    private final Scanner s;
    private String myUsername;

    private PlayerState playerState = PlayerState.WATCHING;

    private final Object lock = new Object();
    ArrayList<Square> tilesToDraw;
    ArrayList<ItemTile> tilesToInsert;

    private PlayerState getPlayerState() {
        synchronized (lock) {
            return playerState;
        }
    }

    private void setPlayerState(PlayerState playerState) {
        synchronized (lock) {
            this.playerState = playerState;
            lock.notifyAll();
        }
    }

    public TextualUI() {
        s = new Scanner(System.in);
        tilesToDraw = new ArrayList<>();
        tilesToInsert = new ArrayList<>();
    }


    /**

     As an implementation of Runnable interface, this method is the entry point of a new thread.
     It prompts the user to enter their username and validates it.
     If the entered username is not valid, it keeps prompting until a valid username is entered.
     Once a valid username is entered, it notifies its observers with a UsernameRequest object
     and calls inputListener method. */
    @Override
    public void run() {
        while(getPlayerState() == PlayerState.WATCHING){
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    System.err.println("Interrupted while waiting for server: " + e.getMessage());
                }
            }
        }
        String input;
        System.out.println("Benvenuto in MyShelfie! Inserisci il tuo username:");
        System.out.print(">>> ");
        input = s.nextLine();
        String[] parts = input.split(" ");
        while(parts.length != 1 || !inputValidator.isValidUsername(parts[0].trim())){
            System.err.println("Username non valido");
            System.out.print(">>> ");
            input = s.nextLine();
            parts = input.split(" ");
        }
        setPlayerState(PlayerState.WATCHING);
        notifyObservers(new UsernameRequest(parts[0].trim()));
        inputListener();
    }

    /**
     * Listens for user input and processes it until the program is terminated.
     * Prompts the user with the ">>>" symbol to indicate that input is expected.
     * The input is passed to the elaborateInput() method for processing.
     */
    public void inputListener(){
        String input;
        while (true) {
            while(getPlayerState() == PlayerState.WATCHING){
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        System.err.println("Interrupted while waiting for server: " + e.getMessage());
                    }
                }
            }
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
            setPlayerState(PlayerState.WATCHING);
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
            printer.showHand(myUsername, tilesToInsert);
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
            printer.showHand(myUsername, tilesToInsert);
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
                printer.showHand(myUsername, tilesToInsert);
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
            setPlayerState(PlayerState.WATCHING);
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
            setPlayerState(PlayerState.WATCHING);
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
                setPlayerState(PlayerState.WATCHING);
                notifyObservers(new StartGameRequest());
                break;
            case EXIT:
                setPlayerState(PlayerState.WATCHING);
                notifyObservers(new ExitLobbyRequest());
                break;
            case PLAYERLIST:
                setPlayerState(PlayerState.WATCHING);
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
                        setPlayerState(PlayerState.WATCHING);
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
                elaborateUsernameCommand(parts);
                break;
            case EXIT:
                //TODO disconnettiti dal server
                break;
            case GAMES:
                setPlayerState(PlayerState.WATCHING);
                notifyObservers(new LobbyListRequest());
                break;
            case JOIN:
                if (parts.length != 2) {
                    System.out.println("Comando non valido!");
                    return;
                } else {
                    setPlayerState(PlayerState.WATCHING);
                    notifyObservers(new JoinLobbyRequest(parts[1]));
                }
                break;
            case CREATE:
                if (parts.length != 2) {
                    System.out.println("Comando non valido!");
                    return;
                } else {
                    setPlayerState(PlayerState.WATCHING);
                    notifyObservers(new CreateLobbyRequest(parts[1]));
                }
                break;
            default:
                System.err.println("Comando non valido, should never reach this state");
                break;
        }
    }

    /**
     * Processes and executes the "username" command, which sets a new username for the user.
     *
     * @param parts An array containing the command and its argument, which should be the new username.
     */
    private void elaborateUsernameCommand(String[] parts) {
        if (parts.length != 2) {
            System.out.println("Comando non valido!");
            return;
        } else {
            if (!inputValidator.isValidUsername(parts[1])) {
                System.out.println("Username non valido");
                return;
            } else {
                System.out.println("Username impostato a " + parts[1]);
                setPlayerState(PlayerState.WATCHING);
                notifyObservers(new UsernameRequest(parts[1]));
                return;
            }
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
    private void showDrawInfo(DrawInfoMessage message) {
        GameView model = message.getModel();

        printer.showBookshelf(model.getCurrentPlayer().getUsername(), model.getCurrentPlayer().getBookshelf().getShelfie());
        printer.showBoard(model.getBoard().getGameboard());
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
    private void rejectDrawRequest(String username, Square[][] board, ItemTile[][] bookshelf, int maxNumItems) {
        System.out.println("Invalid draw request! It seems like your client misbehaved... " +
                "Try re-inserting the coordinates of the tiles you want to draw and if the error persists draw some other tiles because those you are trying to draware invalid!");
        //showDrawInfo(username, board, bookshelf, maxNumItems);
    }

    //INSERT PHASE stuff


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
        printer.showBookshelf(m.getUsername(), m.getShelfie());
        printer.showHand(m.getUsername(), tilesToInsert);
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
                printer.showGameStarted(((GameStartedMessage) message).getGameView());
                break;
            case NEW_TURN:
                tilesToInsert.clear();
                tilesToDraw.clear();
                printer.showNewTurn(((NewTurnMessage) message).getCurrentPlayer());
                setActionType(ActionType.NONE);
                break;
            case BOARD:
                printer.showBoard(((BoardMessage) message).getBoard());
                break;
            case LEADERBOARD:
                printer.showLeaderboard(((LeaderBoardMessage) message).getLeaderboard());
                break;
            case BOOKSHELF:
                BookshelfMessage m = (BookshelfMessage) message;
                printer.showBookshelf(m.getUsername(), m.getBookshelf());
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
                printer.showAchievedCommonGoal(m3);
                break;
            case NO_COMMON_GOAL:
                System.out.println(((NoCommonGoalMessage) message).getContent());
                break;
            case LAST_TURN:
                printer.showLastTurn((LastTurnMessage) message);
                break;
            case ADJACENT_ITEMS_POINTS:
                printer.showAdjacentItemsPoints((AdjacentItemsPointsMessage) message);
                break;
            case PERSONAL_GOAL_POINTS:
                printer.showPersonalGoalPoints((PersonalGoalPointsMessage) message);
                break;
            case END_GAME:
                EndGameMessage m4 = (EndGameMessage) message;
                printer.showEndGame(m4.getRanking());
                break;
            default:
                System.err.println("Ignoring event from model");
                break;
        }
        setPlayerState(PlayerState.ACTIVE);
    }

    /**
     * Handles a ConnectionMessage
     * @param message
     */
    private void onConnectionMessage(ConnectionMessage message) {
        switch (message.getType()){
            case CONNECTED_TO_SERVER:
                printer.showConnectedToServer();
                setClientState(ClientState.IN_SERVER);
                setPlayerState(PlayerState.ACTIVE);
                break;
            case LOBBY_LIST_RESPONSE:
                printer.showLobbyList(((LobbyListResponse) message).getLobbies());
                break;
            case CREATE_LOBBY_RESPONSE:
                printer.printCreateLobbyResponse(((CreateLobbyResponse) message).isSuccessful());
                if(((CreateLobbyResponse) message).isSuccessful())
                    setClientState(ClientState.IN_A_LOBBY);
                break;
            case JOIN_LOBBY_RESPONSE:
                printer.showJoinLobbyResponse(((JoinLobbyResponse) message).isSuccessful(), ((JoinLobbyResponse) message).getContent());
                if(((JoinLobbyResponse) message).isSuccessful())
                    setClientState(ClientState.IN_A_LOBBY);
                break;
            case USERNAME_RESPONSE:
                this.myUsername = ((UsernameResponse) message).getUsername();
                printer.showUsernameResponse(((UsernameResponse) message).isSuccessful(), ((UsernameResponse) message).getUsername());
                break;
            default:
                System.err.println("Ignoring ConnectionMessage from server");
                break;
        }
        setPlayerState(PlayerState.ACTIVE);
    }

    /**
     * Handles a LobbyMessage
     * @param message the message to handle
     */
    private void onLobbyMessage(LobbyMessage message) {
        switch(message.getType()){
            case EXIT_LOBBY_RESPONSE:
                printer.showExitLobbyResponse(((ExitLobbyResponse) message).isSuccessful());
                if(((ExitLobbyResponse) message).isSuccessful())
                    setClientState(ClientState.IN_SERVER);
                break;
            case PLAYER_LIST_RESPONSE:
                printer.showPlayerListResponse(((PlayerListResponse) message).getClients());
                break;
            case NEW_ADMIN:
                printer.showNewAdmin(((NewAdminMessage) message).getOld_admin(), ((NewAdminMessage) message).getNew_admin());
                break;
            case GAME_NOT_READY:
                printer.showGameNotReady();
                break;
            case NOT_ADMIN:
                printer.showNotAdmin();
                break;
            case INVALID_COMMAND:
                printer.showInvalidCommand();
                break;
            case CHANGE_NUM_OF_PLAYER_RESPONSE:
                System.out.println(((ChangeNumOfPlayerResponse) message).getContent());
                break;
            default:
                System.err.println("Ignoring LobbyMessage from server "+ message.getType().toString());
                break;
        }
        setPlayerState(PlayerState.ACTIVE);
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
