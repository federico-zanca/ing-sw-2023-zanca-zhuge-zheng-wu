package it.polimi.ingsw.view;

import it.polimi.ingsw.distributed.ClientState;
import it.polimi.ingsw.network.message.lobbymessage.NewAdminMessage;
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

    private ClientState clientState;

    private ActionType actionType;

    private ArrayList<CommonGoalCard> cards;

    private GameMessage lastMessage;

    private Scanner s;
    private String myUsername;
    private boolean isActive;

    ArrayList<Square> tilesToDraw;
    ArrayList<ItemTile> tilesToInsert;
    public TextualUI() {
        s = new Scanner(System.in);
        cards = null;
        tilesToDraw = new ArrayList<>();
        tilesToInsert = new ArrayList<>();
    }

    public void setCards(ArrayList<CommonGoalCard> cards) {
        this.cards = cards;
    }

    public ArrayList<CommonGoalCard> getCards() {
        return cards;
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

    private void elaborateInsertHand(String input) {
        if(invalidColumnFormat(input)){
            System.out.println("Formato non valido! Inserisci la colonna nel formato: (colonna) :");
            return;
        }
        int column = Integer.parseInt(input.trim());
        if (column < 0 || column > 4) {
            System.out.println("Colonna non valida! Assicurati di inserire colonne che rientrano nella dimensione della libreria (0-4)");
            return;
        } else if (columnHasLessSpace(column, ((InsertInfoMessage) lastMessage).getEnabledColumns())) {
            System.out.println("La colonna scelta non ha sufficiente spazio per inserire la mano! Inserisci un'altra colonna: ");
            return;
        } else {
            setActionType(ActionType.NONE);
            notifyObservers(new InsertTilesMessage(myUsername, tilesToInsert, column));
        }
    }

    private void elaborateReorderThreeTiles(String input) {
        if(invalidOrderFormat(input, 3)){
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
            setActionType(ActionType.INSERT_HAND);
            showInsertInfo((InsertInfoMessage) lastMessage);
            System.out.println("Inserisci la colonna in cui vuoi inserire la mano: ");
        }
    }

    private void elaborateOrderHandInput(String input) {
        if(!isYesOrNo(input)){
            System.out.println("Risposta non valida! Inserisci 'y' o 'n'");
            return;
        }else if(isYes(input)){
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
                System.err.println("Dimensioni mano illegali");;
            }
        } else{
            setActionType(ActionType.INSERT_HAND);
            showInsertInfo((InsertInfoMessage) lastMessage);
            System.out.println("Inserisci la colonna in cui vuoi inserire la mano: ");
            return;
        }
    }

    private void elaborateDrawInput(String input, GameView model, int maxNumItems) {
        Square[][] board = model.getBoard().getGameboard();
        if(tilesToDraw.size() > 0 &&  input.equalsIgnoreCase("ok")){
            notifyObservers(new DrawTilesMessage(myUsername, tilesToDraw));
            return;
        }
        if(invalidCoordFormat(input)){
            System.out.println("Formato coordinate non valido!");
            return;
        }
        String[] coords = input.split(",");
        int row = Integer.parseInt(coords[0].trim());
        int column = Integer.parseInt(coords[1].trim());
        if (row < 0 || row > Board.DIMENSIONS - 1 || column < 0 || column > Board.DIMENSIONS - 1) {
            System.out.println("Coordinate non valide! Assicurati di inserire coordinate che rientrino nelle dimensioni della Board (0-" + (Board.DIMENSIONS - 1));
            return;
        } else if (isTileAlreadyOnHand(row, column, tilesToDraw)) {
            System.out.println("Non puoi prendere una tessera che hai già preso! Inserisci altre coordinate: ");
            return;
        } else if (!board[row][column].isPickable()) {
            System.out.println("Coordinate non valide! Assicurati di inserire le coordinate di una tessera che sia prendibile secondo le regole di gioco!");
            return;
        } else if (tilesToDraw.size() > 0 && !inLineTile(row, column, tilesToDraw)) {
            System.out.println("Coordinate non valide! La tessera che prendi deve essere adiacente e in linea retta (orizzontale o verticale) con le tessere che hai già preso in questo turno! Inserisci le coordinate nuovamente: ");
            return;
        }
        tilesToDraw.add(new Square(new Coordinates(row, column), board[row][column].getItem().getType()));
        if (isPossibleToDrawMore(tilesToDraw, board) && tilesToDraw.size()<Math.min(3,maxNumItems))
            System.out.println("Inserisci le coordinate della " + (tilesToDraw.size() + 1) + "° tessera separate da una virgola (es. riga, colonna) :");
        else{
            setActionType(ActionType.ORDER_HAND);
            notifyObservers(new DrawTilesMessage(myUsername, tilesToDraw));
        }
    }

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
            case CHAT:
                System.err.println(lobbyCommand.toString() + " not implemented yet");
                break;
            case READY:
                System.err.println(lobbyCommand.toString() + " not implemented yet");
                break;
            case UNREADY:
                System.err.println(lobbyCommand.toString() + " not implemented yet");
                break;
            case KICK:
                System.err.println(lobbyCommand.toString() + " not implemented yet");
                break;
            case CHANGE_NUMBER_OF_PLAYERS:
                System.err.println(lobbyCommand.toString() + " not implemented yet");
                break;
            default:
                System.err.println("Comando non valido, should never reach this state");
                break;
        }
    }

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
                    if (!isValidUsername(parts[1])) {
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


    /* USERNAME insertion stuff */
    /**
     * Asks the player his username
     *
     * @return the username inserted
     */
    public String askUsername() {
        String username;
        System.out.print("Enter your name: ");

        username = s.nextLine();
        while (!isValidUsername(username)) {
            System.out.println("Invalid username! The username must contains only literals and numbers, the only allowed special characters are \".\", \"-\" and \"_\".\n" +
                    "Please insert your username again: ");
            username = s.nextLine();
        }
        return username;

    }
    /**
     * Checks whether the given username is valid according to certain criteria.
     *
     * @param username the string representing the username to be validated.
     * @return true if the username meets all of the validation criteria outlined below, false otherwise.
     * <p>
     * Criteria for a valid username:
     * 1. Does not contain any spaces.
     * 2. Does not start with a special character (-, _, or .).
     * 3. Does not end with - or .
     * 4. Does not contain any characters that are not letters or digits or one of the allowed special characters (-, _, or .).
     */
    private boolean isValidUsername(String username) {
        // Check for spaces in username
        if (username.contains(" ")) {
            return false;
        }

        // Check if username starts or ends with a special character or if it is solely composed of numbers
        char firstChar = username.charAt(0);
        if (firstChar == '-' || firstChar == '_' || firstChar == '.' || username.endsWith("-") || username.endsWith(".") || username.matches("[0-9]+")) {
            return false;
        }

        // Check for non-literal or non-numeric characters other than '-', '_' and '.'
        String pattern = "[^a-zA-Z0-9\\-_\\.]";
        if (username.matches(".*" + pattern + ".*")) {
            return false;
        }

        // All checks passed, username is valid
        return true;
    }



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

    /**
     * Checks if the current board configuration allows the player to draw more tiles
     *
     * @param hand  ArrayList of tiles taken by the player in this turn
     * @param board gameboard
     * @return true if there are other tiles the player can pick (based on what he's already picked and the board configuration)
     */
    private boolean isPossibleToDrawMore(ArrayList<Square> hand, Square[][] board) {
        ArrayList<Integer> rows = new ArrayList<>();
        ArrayList<Integer> columns = new ArrayList<>();
        for (Square sq : hand) {
            rows.add(sq.getRow());
            columns.add(sq.getColumn());
        }
        if (allCoordsAreEqual(rows)) {
            Collections.sort(columns);
            if (columns.get(0) != 0 && board[rows.get(0)][columns.get(0) - 1].isPickable()) return true;
            if (columns.get(columns.size() - 1) != board.length - 1 && board[rows.get(0)][columns.get(columns.size() - 1) + 1].isPickable())
                return true;
        }
        if (allCoordsAreEqual(columns)) {
            Collections.sort(rows);
            if (rows.get(0) != 0 && board[rows.get(0) - 1][columns.get(0)].isPickable()) return true;
            if (rows.get(rows.size() - 1) != board.length - 1 && board[rows.get(rows.size() - 1) + 1][columns.get(0)].isPickable())
                return true;
        }
        return false;
    }

    /**
     * This method splits the input string on commas and checks if there are exactly two parts. If not, it returns `true`.
     * If there are two parts, it attempts to parse them as integers using `Integer.parseInt()`.
     * If either of them cannot be parsed as an integer (due to a non-numeric character being present, for example),
     * the method returns `true`. Otherwise, it returns `false`.
     * Note that this implementation assumes the input string contains only ASCII digits, whitespace, and commas.
     * If other characters are allowed, the parsing logic would need to be adapted accordingly.
     *
     * @param input the string which format is to be evaluated
     * @return true if the String format is invalid
     */
    public static boolean invalidCoordFormat(String input) {
        String[] parts = input.split(",");

        // Check for two parts and trim any whitespace
        if (parts.length != 2) {
            return true;
        }

        try {
            // Attempt to parse integers from string parts : DON'T TOUCH!!!!!
            int first = Integer.parseInt(parts[0].trim());
            int second = Integer.parseInt(parts[1].trim());

            // Return false if we parsed two valid integers
            //System.err.println("Valid format");
            return false;

        } catch (NumberFormatException e) {
            // Catch any exception thrown by parseInt()
            //System.err.println("Invalid format");
            return true;
        }
    }

    /**
     * Checks if the input format is one exact number which fits in the bookshelf's dimension. If not, it returns 'true'.
     *
     * @param input chosen column
     * @return valid column
     */
    private boolean invalidColumnFormat(String input) {
        try {
            int col = Integer.parseInt(input.trim());
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    /**
     * Prompts the user to insert the coordinates of the tile he wants to pick and checks if the coordinates are those of a valid tile.
     * If the coordinates inserted are not valid, it re-prompts the user to insert the input.
     *
     * @param hand  ArrayList of tiles taken by the player in this turn
     * @param board gameboard
     * @param n     the n_th tile taken in this turn
     * @return the Square corresponding to the given coordinates
     */
    private Square inputCoords(ArrayList<Square> hand, Square[][] board, int n) {

        String input = s.nextLine();
        while (invalidCoordFormat(input)) {
            System.out.println("Formato non valido! Inserisci le coordinate nel formato: (riga, colonna) :");
            input = s.nextLine();
        }
        String[] tiles = input.split(",");
        int row = Integer.parseInt(tiles[0].trim());
        int column = Integer.parseInt(tiles[1].trim());
        while (true) {
            if (row < 0 || row > Board.DIMENSIONS - 1 || column < 0 || column > Board.DIMENSIONS - 1) {
                System.out.println("Coordinate non valide! Assicurati di inserire coordinate che rientrino nelle dimensioni della Board (0-" + (Board.DIMENSIONS - 1));
            } else if (isTileAlreadyOnHand(row, column, hand)) {
                System.out.println("Non puoi prendere una tessera che hai già preso! Inserisci altre coordinate: ");
            } else if (!board[row][column].isPickable()) {
                System.out.println("Coordinate non valide! Assicurati di inserire le coordinate di una tessera che sia prendibile secondo le regole di gioco!");
            } else if (n > 0 && !inLineTile(row, column, hand)) {
                System.out.println("Coordinate non valide! La tessera che prendi deve essere adiacente e in linea retta (orizzontale o verticale) con le tessere che hai già preso in questo turno! Inserisci le coordinate nuovamente: ");
            } else {
                break;
            }
            input = s.nextLine();
            while (invalidCoordFormat(input)) {
                System.out.println("Formato non valido! Inserisci le coordinate nel formato: (riga, colonna) :");
                input = s.nextLine();
            }
            tiles = input.split(",");
            row = Integer.parseInt(tiles[0].trim());
            column = Integer.parseInt(tiles[1].trim());
        }
        return new Square(new Coordinates(row, column), board[row][column].getItem().getType());
    }

    /**
     * Checks if the given array of Integers contains only equals elements
     *
     * @param x ArrayList of integers
     * @return true if the elements of the passed ArrayList are all equal
     */
    private boolean allCoordsAreEqual(ArrayList<Integer> x) {
        for (int i = 0; i < x.size() - 1; i++) {
            if (x.get(i) != x.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the integers int the given arraylist are consecutive (in whatever order they are presented)
     *
     * @param x ArrayList of integers
     * @return true if the elements in the given arraylist are consecutive (in whatever order they are presented)
     */
    private boolean allCoordsAreAdjacent(ArrayList<Integer> x) {
        Collections.sort(x);
        for (int i = 0; i < x.size() - 1; i++) {
            if (x.get(i) != x.get(i + 1) - 1) return false;
        }
        return true;
    }

    /**
     * Checks if the passed coordinates are those of a Square which is in a straight line (horizontal or vertical) and adjacent to the ones in the player's hand
     *
     * @param row    row number of the Square to check
     * @param column column number of the Square to check
     * @param hand   List of Squares (tiles) already picked by the player during this turn
     * @return true if the passed coordinates are those of a Square which is in a straight line (horizontal or vertical) and adjacent to the ones in the player's hand
     */
    private boolean inLineTile(int row, int column, ArrayList<Square> hand) {
        ArrayList<Integer> rows = new ArrayList<>();
        ArrayList<Integer> columns = new ArrayList<>();
        rows.add(row);
        columns.add(column);
        for (Square sq : hand) {
            rows.add(sq.getRow());
            columns.add(sq.getColumn());
        }
        return (allCoordsAreAdjacent(rows) && allCoordsAreEqual(columns)) || (allCoordsAreAdjacent(columns) && allCoordsAreEqual(rows));
    }

    /**
     * Checks if the tile has already been picked by the player this turn
     *
     * @param row    row number of the Square to check
     * @param column column number of the Square to check
     * @param hand   List of Squares (tiles) already picked by the player during this turn
     * @return true if the passed coordinates are those of a tile which is already in the player's hand
     */
    private boolean isTileAlreadyOnHand(int row, int column, ArrayList<Square> hand) {
        if (hand.size() == 0) return false;
        for (Square sq : hand) {
            if (sq.getCoordinates().getRow() == row && sq.getCoordinates().getColumn() == column) return true;
        }
        return false;
    }

    /**
     * @param input String to check
     * @return true if the string passed is equal to N/No/n/no
     */
    public static boolean isNo(String input) {
        input = input.toLowerCase();
        return input.equals("n") || input.equals("no");
    }

    /**
     * @param input String to check
     * @return true if the string passed is equal to N/No/n/no/Y/y/Yes/yes
     */
    public static boolean isYesOrNo(String input) {
        input = input.toLowerCase();
        return input.equals("y") || input.equals("yes") || isNo(input);
    }


    //INSERT PHASE stuff

    public void askInsert(String username, ItemTile[][] bookshelf, ArrayList<ItemTile> hand, ArrayList<Integer> columns) {
        System.out.println("Inizia la insert phase\n");
        showBookshelf(username, bookshelf);
        showHand(username, hand);
        orderHand(username, hand);
        System.out.println("Inserisci la colonna in cui vuoi inserire la mano: ");
        int column = inputColumn(hand, bookshelf, columns);
        notifyObservers(new InsertTilesMessage(username, hand, column));
    }


    private int inputColumn(ArrayList<ItemTile> items, ItemTile[][] bookshelf, ArrayList<Integer> columns) {

        String input = s.nextLine();
        while (invalidColumnFormat(input)) {
            System.out.println("Formato non valido! Inserisci la colonna nel formato: (colonna) :");
            input = s.nextLine();
        }
        int column = Integer.parseInt(input.trim());
        while (true) {
            if (column < 0 || column > 4) {
                System.out.println("Colonna non valida! Assicurati di inserire colonne che rientrano nella dimensione della libreria (0-4)");
            } else if (columnHasLessSpace(column, columns)) {
                System.out.println("La colonna scelta non ha sufficiente spazio per inserire la mano! Inserisci un'altra colonna: ");
            } else {
                break;
            }
            input = s.nextLine();
            column = Integer.parseInt(input.trim());
        }
        return column;
    }

    /**
     * Checks if the column chosen by the player has enough space to insert the players hand. If not it returns 'true'.
     *
     * @param column  column that the player chose
     * @param columns arraylist of available columns
     * @return valid chosen column
     */
    private boolean columnHasLessSpace(int column, ArrayList<Integer> columns) {
        return !columns.contains(column);
    }

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
        System.out.println(strBoard.toString());
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
        System.out.println(strShelf.toString());
    }


    private void showAchievedCommonGoal(String username, CommonGoalCard goal, int points) {
        System.out.println("Player " + username + " has achieved the common goal " + goal + " and has earned " + points + " points!");
    }

    public void showHand(String username, ArrayList<ItemTile> hand) {
        System.out.println("Hand of player " + username);
        for (ItemTile item : hand) {
            System.out.print(" " + paintFG(item.getType()) + " ");
        }
        System.out.println();
    }

    /**
     * Orders the player's hand.
     *
     * @param username name of player
     * @param hand     hand of player
     */
    public void orderHand(String username, ArrayList<ItemTile> hand) {
        String continueResponse;

        if (hand.size() == 2) {
            System.out.println("Vuoi invertire l'ordine delle tessere? (y/n)");
            continueResponse = s.nextLine();

            while (!isYesOrNo(continueResponse) || isYes(continueResponse)) {
                if (!isYesOrNo(continueResponse))
                    System.out.println("Inserisci y o n");
                else {
                    Collections.swap(hand, 1, 0);
                    showHand(username, hand);
                    System.out.println("Vuoi invertire ancora l'ordine delle tessere? (y/n)");
                }
                continueResponse = s.nextLine();
            }
        } else if (hand.size() == 3) {
            System.out.println("Vuoi cambiare l'ordine delle tessere? (y/n)");
            continueResponse = s.nextLine();
            while (!isYesOrNo(continueResponse) || isYes(continueResponse)) {
                if (!isYesOrNo(continueResponse))
                    System.out.println("Inserisci y o n");
                else {
                    System.out.println("Inserisci il nuovo ordine della mano: (ad es. 2,1,3 mette la tessera 2 in prima posizione, la 1 in seconda e la 3 in terza posizione)");
                    inputOrder(hand, username);
                    showHand(username, hand);
                    System.out.println("Ordine cambiato! Vuoi cambiare ancora l'ordine delle tessere? (y/n)");
                }
                continueResponse = s.nextLine();
            }
        }
    }

    private boolean isYes(String input) {
        input = input.toLowerCase();
        return input.equals("y") || input.equals("yes");
    }

    /**
     * Checks if the order in input is correct and actually orders the hand.
     *
     * @param hand hand of player
     */
    private void inputOrder(ArrayList<ItemTile> hand, String username) {

        String input = s.nextLine();
        while (invalidOrderFormat(input, 3)) {
            System.out.println("Formato non valido! Questo è l'ordine delle tessere che hai in mano :");
            showHand(username, hand);
            System.out.println("Inserisci il nuovo ordine della mano: (ad es. 2,1,3 metterà la tessera 2 in prima posizione, la 1 in seconda e la 3 in terza posizione)");
            input = s.nextLine();
        }
        String[] order = input.split(",");
        int first = Integer.parseInt(order[0].trim());
        int second = Integer.parseInt(order[1].trim());
        int third = Integer.parseInt(order[2].trim());
        while (true) {
            if (first < 1 || first > 3 || second < 1 || second > 3 || third < 1 || third > 3) {
                System.out.println("Ordine inserito non riconosciuto. Assicurati che i numeri siano (1-3)");
            } else if (first == second || second == third || first == third) {
                System.out.println("Non si può avere due tessere nello stesso slot! Riprova");
            } else {
                break;
            }
            input = s.nextLine();
            order = input.split(",");
            first = Integer.parseInt(order[0].trim());
            second = Integer.parseInt(order[1].trim());
            third = Integer.parseInt(order[2].trim());
        }

        Collections.swap(hand, first - 1, 0);
        if (second != 1) {
            Collections.swap(hand, second - 1, 1);
        } else {
            Collections.swap(hand, third - 1, 2);
        }
    }

    /**
     * Same as invalidCoordsFormat but is generic
     *
     * @param input cli input
     * @param n     number of numbers separated by the coma that you have in the input string
     * @return
     */
    public static boolean invalidOrderFormat(String input, int n) {
        String[] parts = input.split(",");

        // Check for two parts and trim any whitespace
        if (parts.length != n) {
            return true;
        }

        try {
            // Attempt to parse integers from string parts : DON'T TOUCH!!!!!
            for (int i = 0; i < n; i++) {
                int num = Integer.parseInt(parts[i].trim());
            }

            // Return false if we parsed two valid integers
            //System.err.println("Valid format");
            return false;

        } catch (NumberFormatException e) {
            // Catch any exception thrown by parseInt()
            //System.err.println("Invalid format");
            return true;
        }
    }

    /**
     * Only for testing
     */
    public void showGreeting() {
        //for debugging
        System.out.println("Bravo!\n");
    }

    private void showGameStarted(GameView model) {
        System.out.println("######################################\n" +
                "#        La partita è iniziata!      #\n" +
                "######################################");
        showBoard(model.getBoard().getGameboard());
        showCommonGoals(model);
        showLeaderboard(model.getLeaderboard());
    }

    private void showNewTurn(String username) {
        System.out.println("######################################\n" +
                "    E' il turno di " + username + "\t\n" +           //migliorabile
                "######################################");
    }

    public void showLeaderboard(LinkedHashMap<String, Integer> sortedMap) {
        int rank = 1;
        System.out.println("Leaderboard :");
        for (String key : sortedMap.keySet()) {
            System.out.println("#" + rank + ". " + key + " : " + sortedMap.get(key));
            rank++;
        }
    }

    private void showConnectedToServer() {
        System.out.println("Connesso al server!");

    }

    private void showCommonGoals(GameView model) {
        System.out.println("First Common Goal: " + model.getCommonGoals().get(0).toString() +
                "\nSecond Common Goal: " + model.getCommonGoals().get(1).toString());
    }

    private void showPersonalGoalPoints(PersonalGoalPointsMessage message) {
        System.out.println("######################################\n" +
                message.getUsername() + " ha ottenuto " + message.getPoints() + " punti per il suo obiettivo personale!\n" +
                "######################################");
    }

    private void showAdjacentItemsPoints(AdjacentItemsPointsMessage message) {
        System.out.println("######################################\n" +
                message.getUsername() + " ha ottenuto " + message.getPoints() + " punti per i gruppi di tessere uguali adiacenti nella libreria!\n" +
                "######################################");
    }

    private void showLastTurn(LastTurnMessage message) {
        System.out.println("######################################\n" +
                message.getUsername() + " ha riempito la sua libreria!\n" +
                "Questo è l'ultimo giro di gioco!\n" +
                "######################################");
    }

    private void showEndGame(LinkedHashMap<String, Integer> ranking) {
        System.out.println("######################################\n" +
                "#        La partita è finita         #\n" +
                "######################################");
        showLeaderboard(ranking);
        System.out.println("Il vincitore della partita è " + ranking.keySet().toArray()[0] + " con " + ranking.values().toArray()[0] + " punti!");

    }

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


    private void showUsernameResponse(boolean successful, String username) {
        if(successful){
            System.out.println("Username cambiato in " + username + "!");
        }else{
            System.out.println("Username " + username+" già in uso! Riprova con un altro username!");
        }
         
    }

    private void showJoinLobbyResponse(boolean successful, String content) {
        System.out.println(content);
        if(!successful){
            System.out.println(content);;
        }
        else {
            setClientState(ClientState.IN_A_LOBBY);
            showEnteredLobby();
        }
    }

    private void showCreateLobbyResponse(boolean successful) {
        if(successful){
            setClientState(ClientState.IN_A_LOBBY);
            System.out.println("Partita creata con successo!");
            System.out.println("In attesa di altri giocatori...");

              
        }else{
            System.out.println("Partita non creata! Riprova con un altro nome!");
        }
    }

    private void showEnteredLobby() {
        System.out.println("Sei entrato nella partita!");
    }

    private void showExitLobbyResponse(boolean successful) {
        if(successful){
            System.out.println("Uscito dalla lobby");
            setClientState(ClientState.IN_SERVER);
        }else{
            System.out.println("Errore nell'uscire dalla lobby, riprova");
              
        }
    }

    private void showGameNotReady() {
        System.out.println("Non ci sono le condizioni per iniziare la partita");
          
    }

    private void showNewAdmin(String old_admin, String new_admin) {
        System.out.println("Il vecchio admin " + old_admin + " è stato rimosso");
        System.out.println("Il nuovo admin è " + new_admin);
    }

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
                showNewTurn(message.getUsername());
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
            case COMMONGOALCARD:
                CommonGoalCardMessage cgm = (CommonGoalCardMessage) message;
                setCards(cgm.getCards());
                break;
            case DRAW_INFO:
                DrawInfoMessage m1 = (DrawInfoMessage) message;
                showDrawInfo(m1);
                if(isActive) {
                    System.out.println("Guardando la tua libreria, puoi prendere al massimo " + Math.min(3, m1.getMaxNumItems()) + " tessere. Di più non riusciresti a inserirne!");
                    System.out.println("Inserisci le coordinate della 1° tessera separate da una virgola (es. riga, colonna) :");
                    setActionType(ActionType.DRAW_TILES);
                }
                //askDraw(m1.getUsername(), m1.getBoard(), m1.getBookshelf(), m1.getMaxNumItems());
                break;
            case INSERT_INFO:
                InsertInfoMessage m2 = (InsertInfoMessage) message;
                tilesToInsert = m2.getHand();
                showInsertInfo(m2);
                if(isActive) {
                    if (m2.getHand().size() == 1) {
                        System.out.println("Inserisci la colonna in cui vuoi inserire la tessera");
                        setActionType(ActionType.INSERT_HAND);
                    }
                    else {
                        setActionType(ActionType.ORDER_HAND);
                        askOrderHand(m2);
                    }
                }

                break;
            case ACHIEVED_COMMON_GOAL:
                AchievedCommonGoalMessage m3 = (AchievedCommonGoalMessage) message;
                showAchievedCommonGoal(m3.getUsername(), m3.getGoal(), m3.getPoints());
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

    private void askOrderHand(InsertInfoMessage m2) {
        if(m2.getHand().size()==2){
            System.out.println("Vuoi invertire l'ordine delle tessere? (y/n)");
        } else if(m2.getHand().size()==3){
            System.out.println("Vuoi cambiare l'ordine delle tessere? (y/n)");
        } else {
            System.err.println("Hand size illegal");
        }
    }

    private void showInsertInfo(InsertInfoMessage m) {
        System.out.println("Inizia la insert phase\n");
        showBookshelf(m.getUsername(), m.getShelfie());
        showHand(m.getUsername(), tilesToInsert);
    }

    private void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    private void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

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
                showCreateLobbyResponse(((CreateLobbyResponse) message).isSuccessful());
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

    private void onLobbyMessage(LobbyMessage message) {
        switch(message.getType()){
            case EXIT_LOBBY_RESPONSE:
                showExitLobbyResponse(((ExitLobbyResponse) message).isSuccessful());
                break;
            case PLAYERS_UPDATE:
                System.err.println("PLAYERS_UPDATE not implemented yet");
                break;
            case NEW_ADMIN:
                showNewAdmin(((NewAdminMessage) message).getOld_admin(), ((NewAdminMessage) message).getNew_admin());
                break;
            case GAME_NOT_READY:
                showGameNotReady();
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
