package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.distributed.ClientState;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.JoinType;
import it.polimi.ingsw.model.gameboard.Board;
import it.polimi.ingsw.model.gameboard.Coordinates;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import it.polimi.ingsw.network.message.ChatMessage;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.connectionmessage.*;
import it.polimi.ingsw.network.message.gamemessage.*;
import it.polimi.ingsw.network.message.lobbymessage.*;
import it.polimi.ingsw.view.InputValidator;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
/**
 * This class represents a textual user interface for the game.
 * It extends the VirtualView class and implements the View and Runnable interfaces.
 */
public class TextualUI extends VirtualView implements View, Runnable {

    private final InputValidator inputValidator = new InputValidator();
    private final Printer printer = new Printer();
    private ClientState clientState;
    private ActionType actionType = ActionType.LOGIN;
    private MessageToClient lastMessage;
    private final Scanner s;
    private String myUsername;
    private PersonalGoalCard personalGoalCard;
    private PlayerState playerState = PlayerState.WATCHING;

    private final Object lock = new Object();
    ArrayList<Square> tilesToDraw;
    ArrayList<ItemTile> tilesToInsert;
    private ArrayList<ChatMessage> chat;
    private boolean isChatting=false;
    /**
     * Gets the player state of the current user.
     *
     * @return the player state of the current user
     */
    private PlayerState getPlayerState() {
        synchronized (lock) {
            return playerState;
        }
    }
    /**
     * Sets the player state of the current user.
     *
     * @param playerState the player state to set
     */
    private void setPlayerState(PlayerState playerState) {
        synchronized (lock) {
            this.playerState = playerState;
            lock.notifyAll();
        }
    }
    /**
     Constructs a new instance of the TextualUI class.
     Initializes the scanner for user input, chat message list, and tile lists.
     */
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
        System.out.println(
                        "███╗   ███╗██╗   ██╗███████╗██╗  ██╗███████╗██╗     ███████╗██╗███████╗\n" +
                        "████╗ ████║╚██╗ ██╔╝██╔════╝██║  ██║██╔════╝██║     ██╔════╝██║██╔════╝\n" +
                        "██╔████╔██║ ╚████╔╝ ███████╗███████║█████╗  ██║     █████╗  ██║█████╗  \n" +
                        "██║╚██╔╝██║  ╚██╔╝  ╚════██║██╔══██║██╔══╝  ██║     ██╔══╝  ██║██╔══╝  \n" +
                        "██║ ╚═╝ ██║   ██║   ███████║██║  ██║███████╗███████╗██║     ██║███████╗\n" +
                        "╚═╝     ╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝╚══════╝╚═╝     ╚═╝╚══════╝\n" +
                        "                                                                       \n" +
                        "\n");
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

    /**
     Elaborates on the input provided in the chat.
     @param input the input text in the chat
     */
    private void elaborateChatInput(String input) {
        if(input.equalsIgnoreCase("/quit")){
            isChatting=false;
            System.out.println(TextColor.BRIGHT_RED_TEXT+"Hai chiuso la chat"+ TextColor.NO_COLOR);
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
        /*
        if(input .equals("chat")){
            startChatting();
            return;
        }

         */
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
                startChatting();
                return;
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
    /**
     Starts the chat functionality.
     Sets the "isChatting" flag to true, prints the chat messages using the printer,
     and reduces the size of the chat if necessary.
     */
    private void startChatting() {
        isChatting = true;
        printer.printChat(chat);
        reduceChat();
    }
    /**
     Reduces the size of the chat by removing older chat messages.
     Keeps only the last 10 chat messages in the chat list.
     */
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
                System.exit(1);
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
    /**
     Elaborates on the login command provided in the input.
     @param input the input containing the login command
     */
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

    /**
     Gets the client state.
     @return the client state
     */
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
    private void onGameMessage(MessageToClient message) {
        //setIsActive(message.getUsername().equals(myUsername));
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

    private void onConnectionMessage(MessageToClient message) {
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
    private void onLobbyMessage(MessageToClient message) {
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

     This method is invoked when the client successfully connects to the server and receives a ConnectedToServerMessage.
     It updates the client state and player state accordingly.
     @param connectedToServerMessage The message received indicating a successful connection to the server.
     */
    @Override
    public void onConnectedServerMessage(ConnectedToServerMessage connectedToServerMessage) {
        printer.showConnectedToServer();
        setClientState(ClientState.IN_SERVER);
        setPlayerState(PlayerState.ACTIVE);
    }

    /**

     This method is invoked when the client receives a response to a lobby creation request.
     It handles the response by printing the result and updating the client state if the lobby creation was successful.
     @param createLobbyResponse The response received for the lobby creation request.
     */
    @Override
    public void onCreateLobbyResponse(CreateLobbyResponse createLobbyResponse) {
        printer.printCreateLobbyResponse((createLobbyResponse).isSuccessful());
        if((createLobbyResponse).isSuccessful())
            setClientState(ClientState.IN_A_LOBBY);
    }

    /**
     This method is invoked when the client receives a response to a lobby join request.
     It handles the response by printing the join lobby response content and updating the client state and action type based on the join type.
     Additionally, it displays the list of lobby players if the join type is "JOINED" or "REJOINED".
     @param joinLobbyResponse The response received for the lobby join request.
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

     This method is invoked when the client receives a response containing a list of available lobbies.
     It handles the response by displaying the list of lobbies using the printer.
     @param lobbyListResponse The response containing the list of available lobbies.
     */
    @Override
    public void onLobbyListResponse(LobbyListResponse lobbyListResponse) {
        printer.showLobbyList(lobbyListResponse.getLobbies());

    }

    /**

     This method is invoked when the client receives a response to a login request.
     It handles the response by setting the client's username, displaying the username response,
     and updating the client's action type if the login was successful.
     @param loginResponse The response received for the login request.
     */
    @Override
    public void onLoginResponse(LoginResponse loginResponse) {
        this.myUsername = loginResponse.getUsername();
        printer.showUsernameResponse(loginResponse.isSuccessful(), loginResponse.getUsername());
        if(loginResponse.isSuccessful())
            setActionType(ActionType.NONE);
    }

    /**

     This method is invoked when the client receives a reconnection message after reconnecting to a game.
     It handles the message by updating the client state and action type, setting the personal goal card and chat,
     and displaying the reconnection details using the printer.
     @param reconnectionMessage The reconnection message received after reconnecting to a game.
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

     This method is invoked when the client receives a response to a username request.
     It handles the response by setting the client's username and displaying the username response.
     @param usernameResponse The response received for the username request.
     */
    @Override
    public void onUsernameResponse(UsernameResponse usernameResponse) {
        this.myUsername = usernameResponse.getUsername();
        printer.showUsernameResponse(usernameResponse.isSuccessful(), usernameResponse.getUsername());

    }

    /**

     This method is invoked when the client receives a message indicating that a common goal has been achieved.
     It handles the message by updating the last received message with the achieved common goal message
     and displaying the achieved common goal using the printer.
     @param achievedCommonGoalMessage The message indicating the achievement of a common goal.
     */
    @Override
    public void onAchievedCommonGoalMessage(AchievedCommonGoalMessage achievedCommonGoalMessage) {
        lastMessage = achievedCommonGoalMessage;
        printer.showAchievedCommonGoal(achievedCommonGoalMessage);
    }

    /**
     * This method is invoked when the client receives an onAdjacentItemsPointsMessage
     * (which contains the points earned by a player for groups of similar adjacent items in the bookshelf).
     * It handles the message by updating the last received message with the AdjacentItemsPointsMessage
     * and displaying the points earned using the printer.
     * @param adjacentItemsPointsMessage The message containing the points earned by a player for groups of similar adjacent items in the bookshelf.
     */
    @Override
    public void onAdjacentItemsPointsMessage(AdjacentItemsPointsMessage adjacentItemsPointsMessage) {
        lastMessage = adjacentItemsPointsMessage;
        printer.showAdjacentItemsPoints(adjacentItemsPointsMessage);
    }

    /**

     This method is invoked when the client receives a board message containing the updated game board.
     It handles the message by updating the last received message with the board message
     and displaying the game board using the printer.
     @param boardMessage The message containing the updated game board.
     */
    @Override
    public void onBoardMessage(BoardMessage boardMessage) {
        lastMessage = boardMessage;
        printer.showBoard(boardMessage.getBoard());
    }

    /**

     This method is invoked when the client receives a bookshelf message containing the bookshelf of a specific user.
     It handles the message by updating the last received message with the bookshelf message
     and displaying the bookshelf information using the printer.
        @param bookshelfMessage The message containing the bookshelf of a specific user.
        */
     @Override
    public void onBookshelfMessage(BookshelfMessage bookshelfMessage) {
        lastMessage = bookshelfMessage;
        printer.showBookshelf(bookshelfMessage.getUsername(), bookshelfMessage.getBookshelf());
    }

    /**

     This method is invoked when the client receives a draw information message.
     It handles the message by updating the last received message with the draw information message,
     displaying the draw information, and setting the action type to "DRAW_TILES".
     @param drawInfoMessage The message containing the draw information.
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

     This method is invoked when the client receives an end game message indicating the end of the game.
     It handles the message by updating the last received message with the end game message,
     displaying the end game ranking using the printer, and setting the client state to "IN_SERVER".
     @param endGameMessage The message indicating the end of the game.
     */
    @Override
    public void onEndGameMessage(EndGameMessage endGameMessage) {
        lastMessage = endGameMessage;
        printer.showEndGame(endGameMessage.getRanking());
        setClientState(ClientState.IN_SERVER);
    }

    /**

     This method is invoked when the client receives a response to an exit game request.
     It handles the response by displaying the exit game response using the printer,
     setting the client state to "IN_SERVER", and resetting the chat to an empty list.
     @param exitGameResponse The response received for the exit game request.
     */
    @Override
    public void onExitGameResponse(ExitGameResponse exitGameResponse) {
        printer.showExitGameResponse(exitGameResponse.getContent());
        this.clientState = ClientState.IN_SERVER;
        chat = new ArrayList<>();
    }

    /**

     This method is invoked when the client receives a GameStartedMessage indicating the start of a game.
     It handles the message by updating the last received message with the game started message,
     setting the client state to "IN_GAME", and displaying the game started details using the printer.
     @param gameStartedMessage The message indicating the start of the game.
     */
    @Override
    public void onGameStartedMessage(GameStartedMessage gameStartedMessage) {
        lastMessage = gameStartedMessage;
        setClientState(ClientState.IN_GAME);
        //showGameStarted(((GameStartedMessage) message).getGameboard());
        printer.showGameStarted(gameStartedMessage.getGameView());
    }

    /**

     This method is invoked when the client receives an InsertInfoMessage.

     It handles the message by updating the last received message with the insert information message,

     setting the tiles to insert based on the message's hand,

     displaying the insert information, and determining the appropriate action type for the client.

     @param insertInfoMessage The message containing the insert information.
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

     This method is invoked when the client receives a LastTurnMessage indicating that it is the last turn of the game.
     It handles the message by updating the last received message with the last turn message,
     and displaying the last turn details using the printer.
     @param lastTurnMessage The message indicating the last turn of the game.
     */
    @Override
    public void onLastTurnMessage(LastTurnMessage lastTurnMessage) {
        lastMessage = lastTurnMessage;
        printer.showLastTurn(lastTurnMessage);
    }

    /**

     This method is invoked when the client receives a LeaderBoardMessage containing the current rankings of players.
     It handles the message by updating the last received message with the leader board message,
     and displaying the leader board using the printer.
     @param leaderBoardMessage The message containing the leader board information.
     */
    @Override
    public void onLeaderBoardMessage(LeaderBoardMessage leaderBoardMessage) {
        lastMessage = leaderBoardMessage;
        printer.showLeaderboard(leaderBoardMessage.getLeaderboard());
    }

    /**

     This method is invoked when the client receives a NewTurnMessage indicating the start of a new turn.
     It handles the message by updating the last received message with the new turn message,
     clearing the tiles to insert and tiles to draw lists,
     displaying the new turn details using the printer,
     and setting the action type to "NONE" to indicate no specific action is required from the client.
     @param newTurnMessage The message indicating the start of a new turn.
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

     This method is invoked when the client receives a NoCommonGoalMessage indicating that there is no common goal for the current round.
     It handles the message by updating the last received message with the no common goal message,
     and printing the message content to the console.
     @param noCommonGoalMessage The message indicating the absence of a common goal.
     */
    @Override
    public void onNoCommonGoalMessage(NoCommonGoalMessage noCommonGoalMessage) {
        lastMessage = noCommonGoalMessage;
        System.out.println(noCommonGoalMessage.getContent());
    }

    /**

    This method is invoked when the client receives a {@link PersonalGoalCardMessage} containing the personal goal card for the player.
    It handles the message by updating the last received message with the {@link PersonalGoalCardMessage},
    and setting the personal goal card for the player.
    @param personalGoalCardMessage The {@link PersonalGoalCardMessage} containing the personal goal card.
    */
    @Override
    public void onPersonalGoalCardMessage(PersonalGoalCardMessage personalGoalCardMessage) {
        lastMessage = personalGoalCardMessage;
        setPersonalGoalCard(personalGoalCardMessage.getPersonalGoalCard());
    }

    /**

     This method is invoked when the client receives a {@link PersonalGoalPointsMessage} containing the personal goal points earned by the player.
     It handles the message by updating the last received message with the {@link PersonalGoalPointsMessage},
     and displaying the personal goal points using the printer.
     @param personalGoalPointsMessage The {@link PersonalGoalPointsMessage} containing the personal goal points.
     */
    @Override
    public void onPersonalGoalPointsMessage(PersonalGoalPointsMessage personalGoalPointsMessage) {
        lastMessage = personalGoalPointsMessage;
        printer.showPersonalGoalPoints(personalGoalPointsMessage);
    }

    /**

     This method is invoked when the client receives a {@link PlayerLeftMessage} indicating that a player has left the game.
     It handles the message by displaying the player left message using the printer.
     @param playerLeftMessage The {@link PlayerLeftMessage} indicating the player who left the game.
     */
    @Override
    public void onPlayerLeftMessage(PlayerLeftMessage playerLeftMessage) {
        printer.showPlayerLeft(playerLeftMessage.getContent());
    }

    /**

     This method is invoked when the client receives a {@link PlayerRejoinedMessage} indicating that a player has rejoined the game.
     It handles the message by displaying the player rejoined message using the printer.
     @param playerRejoinedMessage The {@link PlayerRejoinedMessage} indicating the player who rejoined the game.
     */
    @Override
    public void onPlayerRejoinedMessage(PlayerRejoinedMessage playerRejoinedMessage) {
        printer.showPlayerRejoined(playerRejoinedMessage.getPlayer());
    }

    /**
     This method is invoked when the client receives a {@link ChangeNumOfPlayerResponse} indicating the response to a request to change the number of players in the game.
     It handles the message by printing the response content to the console.
     @param changeNumOfPlayerResponse The {@link ChangeNumOfPlayerResponse} containing the response to the change number of players request.
     */
    @Override
    public void onChangeNumOfPlayerResponse(ChangeNumOfPlayerResponse changeNumOfPlayerResponse) {
        System.out.println(changeNumOfPlayerResponse.getContent());
    }

    /**

     This method is invoked when the client receives an {@link ExitLobbyResponse} indicating the response to a request to exit the lobby.
     It handles the message by displaying the exit lobby response using the printer.
     If the exit is successful, it clears the chat and sets the client state to {@link ClientState#IN_SERVER}.
     @param exitLobbyResponse The {@link ExitLobbyResponse} containing the response to the exit lobby request.
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

     This method is invoked when the client receives a {@link GameNotReadyMessage} indicating that the game is not ready to start.
     It handles the message by displaying the game not ready message using the printer.
     @param gameNotReadyMessage The {@link GameNotReadyMessage} indicating that the game is not ready.
     */
    @Override
    public void onGameNotReadyMessage(GameNotReadyMessage gameNotReadyMessage) {
        printer.showGameNotReady();
    }

    /**

    This method is invoked when the client receives an {@link InvalidCommandMessage} indicating that the command sent by the client is invalid.
    It handles the message by displaying the invalid command message using the printer.
    @param invalidCommandMessage The {@link InvalidCommandMessage} indicating that the command is invalid.
            */


    @Override
    public void onInvalidCommandMessage(InvalidCommandMessage invalidCommandMessage) {
        printer.showInvalidCommand();
    }

    /**
     This method is invoked when the client receives a {@link NewAdminMessage} indicating a change in the game administrator.
     It handles the message by displaying the new administrator message using the printer.
     @param newAdminMessage The {@link NewAdminMessage} indicating the old and new administrators.
     */
    @Override
    public void onNewAdminMessage(NewAdminMessage newAdminMessage) {
        printer.showNewAdmin(newAdminMessage.getOld_admin(), newAdminMessage.getNew_admin());
    }

    /**

     This method is invoked when the client receives a {@link NotAdminMessage} indicating that the client is not the game administrator.
     It handles the message by displaying the not admin message using the printer.
     @param notAdminMessage The {@link NotAdminMessage} indicating that the client is not the game administrator.
     */
    @Override
    public void onNotAdminMessage(NotAdminMessage notAdminMessage) {
        printer.showNotAdmin();
    }

    /**

     This method is invoked when the client receives a {@link PlayerListResponse} containing the list of players in the lobby.
     It handles the message by displaying the lobby players list using the printer.
     @param playerListResponse The {@link PlayerListResponse} containing the list of players in the lobby.
     */
    @Override
    public void onPlayerListResponse(PlayerListResponse playerListResponse) {
        printer.showLobbyPlayersList(playerListResponse.getClients());
        return;
    }

    /**

     This method is invoked when the client receives a {@link PlayersInLobbyUpdate} indicating an update on the players in the lobby.
     It handles the message by displaying the new player in lobby message using the printer.
     @param playersInLobbyUpdate The {@link PlayersInLobbyUpdate} containing the updated information of players in the lobby.
     */
    @Override
    public void onPlayersInLobbyUpdate(PlayersInLobbyUpdate playersInLobbyUpdate) {
        printer.showNewPlayerInLobby(playersInLobbyUpdate.getAllPlayersUsernames(), playersInLobbyUpdate.getContent());
    }
    /**
     Updates the view based on the received message.
     @param message the message received
     */
    @Override
    public void update(Message message) {
        if (message.getType()==MessageType.GAME_MSG) {
            onGameMessage((MessageToClient) message);
        }else if(message.getType()==MessageType.CONNECTION_MSG){
            onConnectionMessage((MessageToClient) message);
        } else if (message.getType()==MessageType.LOBBY_MSG) {
            onLobbyMessage((MessageToClient) message);
        } else if (message instanceof ChatMessage) {
            onChatMessage((ChatMessage) message);
        } else
            //if(!(message instanceof HeartBeatMessage))
            {
            System.err.println("Ignoring message from server");
        }
    }
    /*
    public GameMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(GameMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

     */

    /**
     Sets the personal goal card for the current player.
     @param personalGoalCard the personal goal card to set
     */
    public void setPersonalGoalCard(PersonalGoalCard personalGoalCard) {
        this.personalGoalCard = personalGoalCard;
    }
    /**
     Handles a received chat message.
     @param message the chat message to handle
     */
    private void onChatMessage(ChatMessage message) {
        chat.add(message);
        if(isChatting){
            printer.printChatMessage(message);
        }
    }
    /**
     Sends a chat message.
     @param messageText the text of the chat message to send
     */
    public void sendChatMessage(String messageText) {
        String recipientusername = null;
        messageText = messageText.trim();
        if(messageText.isEmpty()) {
            return;
        }
        if(messageText.startsWith("@")) {
            if (!messageText.contains(" ")) {
                System.out.println(TextColor.BRIGHT_RED_TEXT + "Invalid message format!" + TextColor.NO_COLOR);
                return;
            }
            recipientusername = messageText.substring(1, messageText.indexOf(" "));
            messageText = messageText.substring(messageText.indexOf(" ") + 1);
        }
        ChatMessage chatMessage = new ChatMessage(messageText, recipientusername);
        notifyObservers(chatMessage);
    }

}
