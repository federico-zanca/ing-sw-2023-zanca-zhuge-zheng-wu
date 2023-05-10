package it.polimi.ingsw.view;

import com.sun.javafx.logging.Logger;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.distributed.ClientState;
import it.polimi.ingsw.model.enumerations.JoinType;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
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

public class TextualUI extends VirtualView implements View, Runnable {

    private final InputValidator inputValidator = new InputValidator();
    private final Printer printer = new Printer();
    private ClientState clientState;
    private ActionType actionType = ActionType.LOGIN;
    private GameMessage lastMessage;
    private final Scanner s;
    private String myUsername;
    private PersonalGoalCard personalGoalCard;
    private PlayerState playerState = PlayerState.WATCHING;

    private final Object lock = new Object();
    ArrayList<Square> tilesToDraw;
    ArrayList<ItemTile> tilesToInsert;
    private ArrayList<ChatMessage> chat;
    private boolean isChatting=false;

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
        chat = new ArrayList<>();
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
        System.out.println("Benvenuto in MyShelfie! Inserisci il tuo username:");
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
            //System.out.print(">>> ");
            input = s.nextLine();
            elaborateInput(input);
        }
    }

    /**
     * Elaborates the input from the user
     * @param input the input from the user
     */
    private void elaborateInput(String input) {
        if(isChatting){
            elaborateChatInput(input);
            return;
        }
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
                System.err.println("Stato client non supportato ");
        }
    }

    private void elaborateChatInput(String input) {
        if(input.equalsIgnoreCase("/quit")){
            isChatting=false;
            return;
        }
        sendChatMessage(input);
    }

    /**
     * Elaborates the input from the user when the client is in a game
     * @param input the input from the user
     */
    private void elaborateGameCommand(String input) {
        if(input .equals("chat")){
            startChatting();
            return;
        }else if(input.equalsIgnoreCase("exit")){
            setPlayerState(PlayerState.WATCHING);
            notifyObservers(new ExitGameRequest(myUsername));
            return;
        }

        if(input.equals("autoplay")){
            //TODO implement here, for debugging reasons: lo usiamo per avere un bot che giochi da solo al posto nostro e poter fare delle partite in fretta
            return;
        }
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
            System.out.println("Inserisci le coordinate della " + (tilesToDraw.size() + 1) + "° tessera, 'ok' se vuoi fermarti qui a pescare :");
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
        if(input .equals("chat")){
            startChatting();
            return;
        }
        String[] parts = input.split(" ");
        LobbyCommand lobbyCommand = null;
        for (LobbyCommand c : LobbyCommand.values()) {
            if (parts[0].toUpperCase().equals(c.toString())) {
                lobbyCommand = c;
                break;
            }
        }
        if(lobbyCommand == null){
            printer.showInvalidCommand();
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
                        printer.showInvalidCommand();
                        return;
                    } else if(!inputValidator.invalidNumOfPlayersFormat(parts[1])){
                        int chosenNum = Integer.parseInt(parts[1].trim());
                        setPlayerState(PlayerState.WATCHING);
                        notifyObservers(new ChangeNumOfPlayersRequest(chosenNum));
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

    private void startChatting() {
        isChatting = true;
        printer.printChat(chat);
        reduceChat();
    }

    private void reduceChat() {
        chat = new ArrayList<>(chat.subList(Math.max(chat.size() - 10, 0), chat.size()));
    }

    /**
     * Elaborates a ConnectionCommand from the user
     * @param input the input from the user
     */
    private void elaborateConnectionCommand(String input) {
        if(actionType == ActionType.LOGIN){
            elaborateLoginCommand(input);
            return;
        }
        String[] parts = input.split(" ");
        ConnectionCommand command = null;
        for (ConnectionCommand c : ConnectionCommand.values()) {
            if (parts[0].toUpperCase().equals(c.toString())) {
                command = c;
                break;
            }
        }
        if(command == null){
            printer.showInvalidCommand();
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
                    printer.showInvalidCommand();
                    return;
                } else {
                    setPlayerState(PlayerState.WATCHING);
                    notifyObservers(new JoinLobbyRequest(parts[1]));
                }
                break;
            case CREATE:
                if (parts.length != 2) {
                    printer.showInvalidCommand();
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

    private void elaborateLoginCommand(String input) {
        String[] parts = input.split(" ");
        if (parts.length != 1) {
            System.out.println("Username non valido!");
            return;
        } else {
            if (!inputValidator.isValidUsername(parts[0])) {
                System.out.println("Username non valido");
                return;
            } else {
                setPlayerState(PlayerState.WATCHING);
                notifyObservers(new LoginRequest(parts[0]));
                return;
            }
        }
    }

    /**
     * Processes and executes the "username" command, which sets a new username for the user.
     *
     * @param parts An array containing the command and its argument, which should be the new username.
     */
    private void elaborateUsernameCommand(String[] parts) {
        if (parts.length != 2) {
            printer.showInvalidCommand();
            return;
        } else {
            if (!inputValidator.isValidUsername(parts[1])) {
                System.out.println("Username non valido! Riprova");
                return;
            } else {
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
        printer.showBookshelves(model.getPlayers());
        //printer.showBookshelf(model.getCurrentPlayer().getUsername(), model.getCurrentPlayer().getBookshelf().getShelfie());
        printer.showBoard(model.getBoard().getGameboard());
        printer.showPersonalGoalCard(personalGoalCard.getObjective());

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
                "Try re-inserting the coordinates of the tiles you want to draw and if the error persists draw some other tiles because those you are trying to draw are invalid!");
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
        printer.showBookshelf(m.getUsername(), m.getBookshelf());
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
        setIsActive(message.getUsername().equals(myUsername));
        message.execute(this);
        /*
        switch (message.getType()) {
            case GAME_STARTED:

                break;
            case NEW_TURN:

                break;
            case BOARD:

                break;
            case LEADERBOARD:

                break;
            case BOOKSHELF:

                break;
            case PERSONALGOALCARD:

                break;
            case DRAW_INFO:

                //}
                break;
            case INSERT_INFO:

                //}
                break;
            case ACHIEVED_COMMON_GOAL:

                break;
            case NO_COMMON_GOAL:

                break;
            case LAST_TURN:

                break;
            case ADJACENT_ITEMS_POINTS:

                break;
            case PERSONAL_GOAL_POINTS:

                break;
            case END_GAME:

                break;
            case PLAYER_REJOINED:
                break;
            case EXIT_GAME_RESPONSE:

                break;
            case PLAYER_LEFT:
                break;
            default:
                System.err.println("Ignoring event from model");
                break;
        }
        */
        /*
        if(playerState == PlayerState.WATCHING){
            System.out.println();
            printer.displayPrompt();
        }
        */

        setPlayerState(PlayerState.ACTIVE);
    }

    /**
     * Handles a ConnectionMessage
     * @param message
     */
    private void onConnectionMessage(ConnectionMessage message) {
        message.execute(this);
        /*
        switch (message.getType()){
            case CONNECTED_TO_SERVER:

                break;
            case LOBBY_LIST_RESPONSE:
                break;
            case CREATE_LOBBY_RESPONSE:

                break;
            case JOIN_LOBBY_RESPONSE:

                break;
            case USERNAME_RESPONSE:
                break;
            case LOGIN_RESPONSE:

                break;
            case RECONNECTION:
                break;
            default:
                System.err.println("Ignoring ConnectionMessage from server");
                break;
        }
        */
        setPlayerState(PlayerState.ACTIVE);
    }

    /**
     * Handles a LobbyMessage
     * @param message the message to handle
     */
    private void onLobbyMessage(LobbyMessage message) {
        message.execute(this);
        /*
        switch(message.getType()){
            case EXIT_LOBBY_RESPONSE:

                break;
            case PLAYER_LIST_RESPONSE:
                printer.showLobbyPlayersList(((PlayerListResponse) message).getClients());
                break;
            case NEW_ADMIN:
                break;
            case GAME_NOT_READY:
                break;
            case NOT_ADMIN:
                break;
            case INVALID_COMMAND:
                break;
            case CHANGE_NUM_OF_PLAYER_RESPONSE:
                break;
            case NEW_PLAYER_IN_LOBBY:
                break;
            default:
                System.err.println("Ignoring LobbyMessage from server "+ message.getType().toString());
                break;
        }
        */
        /*
        if(playerState == PlayerState.WATCHING) {
            System.out.println();
            printer.displayPrompt();
        }
         */
        setPlayerState(PlayerState.ACTIVE);
    }

    /**
     * @param connectedToServerMessage
     */
    @Override
    public void onConnectedServerMessage(ConnectedToServerMessage connectedToServerMessage) {
        printer.showConnectedToServer();
        setClientState(ClientState.IN_SERVER);
        setPlayerState(PlayerState.ACTIVE);
    }

    /**
     * @param createLobbyResponse
     */
    @Override
    public void onCreateLobbyResponse(CreateLobbyResponse createLobbyResponse) {
        printer.printCreateLobbyResponse((createLobbyResponse).isSuccessful());
        if((createLobbyResponse).isSuccessful())
            setClientState(ClientState.IN_A_LOBBY);
    }

    /**
     * @param joinLobbyResponse
     */
    @Override
    public void onJoinLobbyResponse(JoinLobbyResponse joinLobbyResponse) {
        printer.showJoinLobbyResponse(joinLobbyResponse.getContent());
        if(joinLobbyResponse.getJoinType() == JoinType.JOINED){
            setClientState(ClientState.IN_A_LOBBY);
            printer.showLobbyPlayersList(joinLobbyResponse.getUsernames());
        } else if(joinLobbyResponse.getJoinType() == JoinType.REJOINED){
            setClientState(ClientState.IN_GAME);
            setActionType(ActionType.NONE);
            printer.showLobbyPlayersList(joinLobbyResponse.getUsernames());
        }
    }

    /**
     * @param lobbyListResponse
     */
    @Override
    public void onLobbyListResponse(LobbyListResponse lobbyListResponse) {
        printer.showLobbyList(lobbyListResponse.getLobbies());

    }

    /**
     * @param loginResponse
     */
    @Override
    public void onLoginResponse(LoginResponse loginResponse) {
        this.myUsername = loginResponse.getUsername();
        printer.showUsernameResponse(loginResponse.isSuccessful(), loginResponse.getUsername());
        if(loginResponse.isSuccessful())
            setActionType(ActionType.NONE);
    }

    /**
     * @param reconnectionMessage
     */
    @Override
    public void onReconnectionMessage(ReconnectionMessage reconnectionMessage) {
        setClientState(ClientState.IN_GAME);
        setActionType(ActionType.NONE);
        this.personalGoalCard = reconnectionMessage.getPersonalGoal();
        this.chat = reconnectionMessage.getChat();
        printer.showReconnection(reconnectionMessage.getModel(), reconnectionMessage.getContent(), reconnectionMessage.getPersonalGoal());

    }

    /**
     * @param usernameResponse
     */
    @Override
    public void onUsernameResponse(UsernameResponse usernameResponse) {
        this.myUsername = usernameResponse.getUsername();
        printer.showUsernameResponse(usernameResponse.isSuccessful(), usernameResponse.getUsername());

    }

    /**
     * @param achievedCommonGoalMessage
     */
    @Override
    public void onAchievedCommonGoalMessage(AchievedCommonGoalMessage achievedCommonGoalMessage) {
        lastMessage = achievedCommonGoalMessage;
        printer.showAchievedCommonGoal(achievedCommonGoalMessage);
    }

    /**
     * @param adjacentItemsPointsMessage
     */
    @Override
    public void onAdjacentItemsPointsMessage(AdjacentItemsPointsMessage adjacentItemsPointsMessage) {
        lastMessage = adjacentItemsPointsMessage;
        printer.showAdjacentItemsPoints(adjacentItemsPointsMessage);
    }

    /**
     * @param boardMessage
     */
    @Override
    public void onBoardMessage(BoardMessage boardMessage) {
        lastMessage = boardMessage;
        printer.showBoard(boardMessage.getBoard());
    }

    /**
     * @param bookshelfMessage
     */
    @Override
    public void onBookshelfMessage(BookshelfMessage bookshelfMessage) {
        lastMessage = bookshelfMessage;
        printer.showBookshelf(bookshelfMessage.getUsername(), bookshelfMessage.getBookshelf());
    }

    /**
     * @param drawInfoMessage
     */
    @Override
    public void onDrawInfoMessage(DrawInfoMessage drawInfoMessage) {
        lastMessage = drawInfoMessage;
        showDrawInfo(drawInfoMessage);
        //if(isActive) {
        System.out.println("Guardando la tua libreria, puoi prendere al massimo " + Math.min(3, drawInfoMessage.getMaxNumItems()) + " tessere. Di più non riusciresti a inserirne!");
        System.out.println("Inserisci le coordinate della 1° tessera separate da una virgola (es. riga, colonna) :");
        setActionType(ActionType.DRAW_TILES);
    }

    /**
     * @param endGameMessage
     */
    @Override
    public void onEndGameMessage(EndGameMessage endGameMessage) {
        lastMessage = endGameMessage;
        printer.showEndGame(endGameMessage.getRanking());
        setClientState(ClientState.IN_SERVER);
    }

    /**
     * @param exitGameResponse
     */
    @Override
    public void onExitGameResponse(ExitGameResponse exitGameResponse) {
        printer.showExitGameResponse(exitGameResponse.getContent());
        this.clientState = ClientState.IN_SERVER;
        chat = new ArrayList<>();
    }

    /**
     * @param gameStartedMessage
     */
    @Override
    public void onGameStartedMessage(GameStartedMessage gameStartedMessage) {
        lastMessage = gameStartedMessage;
        setClientState(ClientState.IN_GAME);
        //showGameStarted(((GameStartedMessage) message).getGameboard());
        printer.showGameStarted(gameStartedMessage.getGameView());
    }

    /**
     * @param insertInfoMessage
     */
    @Override
    public void onInsertInfoMessage(InsertInfoMessage insertInfoMessage) {
        lastMessage = insertInfoMessage;
        tilesToInsert = insertInfoMessage.getHand();
        showInsertInfo(insertInfoMessage);
        //if(isActive) {
        if (insertInfoMessage.getHand().size() == 1) {
            System.out.println("Inserisci la colonna in cui vuoi inserire la tessera");
            setActionType(ActionType.INSERT_HAND);
        }
        else {
            setActionType(ActionType.ORDER_HAND);
            askOrderHand(insertInfoMessage);
        }
    }

    /**
     * @param lastTurnMessage
     */
    @Override
    public void onLastTurnMessage(LastTurnMessage lastTurnMessage) {
        lastMessage = lastTurnMessage;
        printer.showLastTurn(lastTurnMessage);
    }

    /**
     * @param leaderBoardMessage
     */
    @Override
    public void onLeaderBoardMessage(LeaderBoardMessage leaderBoardMessage) {
        lastMessage = leaderBoardMessage;
        printer.showLeaderboard(leaderBoardMessage.getLeaderboard());
    }

    /**
     * @param newTurnMessage
     */
    @Override
    public void onNewTurnMessage(NewTurnMessage newTurnMessage) {
        lastMessage = newTurnMessage;
        tilesToInsert.clear();
        tilesToDraw.clear();
        printer.showNewTurn(newTurnMessage.getCurrentPlayer());
        setActionType(ActionType.NONE);
    }

    /**
     * @param noCommonGoalMessage
     */
    @Override
    public void onNoCommonGoalMessage(NoCommonGoalMessage noCommonGoalMessage) {
        lastMessage = noCommonGoalMessage;
        System.out.println(noCommonGoalMessage.getContent());
    }

    /**
     * @param personalGoalCardMessage
     */
    @Override
    public void onPersonalGoalCardMessage(PersonalGoalCardMessage personalGoalCardMessage) {
        lastMessage = personalGoalCardMessage;
        setPersonalGoalCard(personalGoalCardMessage.getPersonalGoalCard());
    }

    /**
     * @param personalGoalPointsMessage
     */
    @Override
    public void onPersonalGoalPointsMessage(PersonalGoalPointsMessage personalGoalPointsMessage) {
        lastMessage = personalGoalPointsMessage;
        printer.showPersonalGoalPoints(personalGoalPointsMessage);
    }

    /**
     * @param playerLeftMessage
     */
    @Override
    public void onPlayerLeftMessage(PlayerLeftMessage playerLeftMessage) {
        printer.showPlayerLeft(playerLeftMessage.getContent());
    }

    /**
     * @param playerRejoinedMessage
     */
    @Override
    public void onPlayerRejoinedMessage(PlayerRejoinedMessage playerRejoinedMessage) {
        printer.showPlayerRejoined(playerRejoinedMessage.getPlayer());
    }

    /**
     * @param changeNumOfPlayerResponse
     */
    @Override
    public void onChangeNumOfPlayerResponse(ChangeNumOfPlayerResponse changeNumOfPlayerResponse) {
        System.out.println(changeNumOfPlayerResponse.getContent());
    }

    /**
     * @param exitLobbyResponse
     */
    @Override
    public void onExitLobbyResponse(ExitLobbyResponse exitLobbyResponse) {
        printer.showExitLobbyResponse(exitLobbyResponse.isSuccessful());
        if(exitLobbyResponse.isSuccessful()) {
            chat = new ArrayList<>();
            setClientState(ClientState.IN_SERVER);
        }
    }

    /**
     * @param gameNotReadyMessage
     */
    @Override
    public void onGameNotReadyMessage(GameNotReadyMessage gameNotReadyMessage) {
        printer.showGameNotReady();
    }

    /**
     * @param invalidCommandMessage
     */
    @Override
    public void onInvalidCommandMessage(InvalidCommandMessage invalidCommandMessage) {
        printer.showInvalidCommand();
    }

    /**
     * @param newAdminMessage
     */
    @Override
    public void onNewAdminMessage(NewAdminMessage newAdminMessage) {
        printer.showNewAdmin(newAdminMessage.getOld_admin(), newAdminMessage.getNew_admin());
    }

    /**
     * @param notAdminMessage
     */
    @Override
    public void onNotAdminMessage(NotAdminMessage notAdminMessage) {
        printer.showNotAdmin();
    }

    /**
     * @param playerListResponse
     */
    @Override
    public void onPlayerListResponse(PlayerListResponse playerListResponse) {
        printer.showLobbyPlayersList(playerListResponse.getClients());
        return;
    }

    /**
     * @param playersInLobbyUpdate
     */
    @Override
    public void onPlayersInLobbyUpdate(PlayersInLobbyUpdate playersInLobbyUpdate) {
        printer.showNewPlayerInLobby(playersInLobbyUpdate.getAllPlayersUsernames(), playersInLobbyUpdate.getContent());
    }

    @Override
    public void update(Message message) {
        if (message instanceof GameMessage) {
            onGameMessage((GameMessage) message);
        }else if(message instanceof ConnectionMessage){
            onConnectionMessage((ConnectionMessage) message);
        } else if (message instanceof LobbyMessage) {
            onLobbyMessage((LobbyMessage) message);
        } else if (message instanceof ChatMessage) {
            onChatMessage((ChatMessage) message);
        } else
            //if(!(message instanceof HeartBeatMessage))
            {
            System.err.println("Ignoring message from server");
        }
    }

    public GameMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(GameMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setPersonalGoalCard(PersonalGoalCard personalGoalCard) {
        this.personalGoalCard = personalGoalCard;
    }

    private void onChatMessage(ChatMessage message) {
        chat.add(message);
        if(isChatting){
            printer.printChatMessage(message);
        }
    }

    public void sendChatMessage(String messageText) {
        String recipientusername = null;
        messageText = messageText.trim();
        if(messageText.isEmpty()) {
            return;
        }
        if(messageText.startsWith("@")) {
            if (messageText.indexOf(" ") == -1) {
                System.out.println(Color.BRIGHT_RED_TEXT + "Invalid message format!" + Color.NO_COLOR);
                return;
            }
            recipientusername = messageText.substring(1, messageText.indexOf(" "));
            messageText = messageText.substring(messageText.indexOf(" ") + 1);
        }
        ChatMessage chatMessage = new ChatMessage(messageText, recipientusername);
        notifyObservers(chatMessage);
    }

}
