package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.gameboard.Board;
import it.polimi.ingsw.model.gameboard.Coordinates;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.utils.Observer;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class ProtoCli extends Observable implements Observer, Runnable {
    private final PrintStream out;

    public ProtoCli(){out = System.out;}

    @Override
    public void run(){
        out.println("███╗   ███╗██╗   ██╗███████╗██╗  ██╗███████╗██╗     ███████╗██╗███████╗\n" +
                    "████╗ ████║╚██╗ ██╔╝██╔════╝██║  ██║██╔════╝██║     ██╔════╝██║██╔════╝\n" +
                    "██╔████╔██║ ╚████╔╝ ███████╗███████║█████╗  ██║     █████╗  ██║█████╗  \n" +
                    "██║╚██╔╝██║  ╚██╔╝  ╚════██║██╔══██║██╔══╝  ██║     ██╔══╝  ██║██╔══╝  \n" +
                    "██║ ╚═╝ ██║   ██║   ███████║██║  ██║███████╗███████╗██║     ██║███████╗\n" +
                    "╚═╝     ╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝╚══════╝╚═╝     ╚═╝╚══════╝\n" +
                    "                                                                       \n" +
                    "\n");
        String username = askUsername();
        notifyObserver(new LoginRequest(username));
        //Coordinates choice = askPlayer();
    }

    /**
     * Checks whether the given username is valid according to certain criteria.
     *
     * @param username the string representing the username to be validated.
     * @return true if the username meets all of the validation criteria outlined below, false otherwise.
     *
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
        if (firstChar == '-' || firstChar == '_' || firstChar == '.'  || username.endsWith("-") || username.endsWith(".") || username.matches("[0-9]+")) {
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

    /**
     * Asks the player his username
     * @return the username inserted
     */
    public String askUsername(){
        String username;
        out.print("Enter your name: ");
        Scanner s = new Scanner(System.in);
        username = s.nextLine();
        while(!isValidUsername(username)){
            out.println("Invalid username! The username must contains only literals and numbers, the only allowed special characters are \".\", \"-\" and \"_\".\n" +
                    "Please insert your username again: ");
            username = s.nextLine();
        }while(!isValidUsername(username));
        return username;

    }

    /**
     * Shows the board, the player's bookshelf and proceeds asking the player to insert the coordinates of the tiles he wants to pick.
     * @param username username of the current player
     * @param board gameboard
     * @param bookshelf player's bookshelf
     * @param maxNumItems max free cells in a single column in player's bookshelf
     */
    public void askDraw(String username, Square[][] board, ItemTile[][] bookshelf, int maxNumItems) {
        //Game game = Game.getInstance();
        showBookshelf(username, bookshelf);
        showBoard(board);
        ArrayList<Square> hand = new ArrayList<>();
        String continueResponse;
        Scanner s = new Scanner(System.in);
        out.println("Guardando la tua libreria, puoi prendere al massimo " + Math.min(3, maxNumItems) +" tessere. Di più non riusciresti a inserirne!");

        for(int i=0; i<Math.min(3, maxNumItems); i++){
            out.println("Inserisci le coordinate della "+(i+1)+"° tessera separate da una virgola (es. riga, colonna) :");
            hand.add(inputCoords(hand, board, i));
            if(isPossibleToDrawMore(hand, board)) {
                if (maxNumItems > i+1) {
                    do {
                        out.println("Vuoi continuare a prendere tessere? (y/n)");
                        continueResponse = s.nextLine();
                    } while (!isYesOrNo(continueResponse));
                    if (isNo(continueResponse)) {
                        break;
                    }
                }
            }else{
                break;
            }
        }
        notifyObserver(new DrawTilesMessage(username, hand));
/*
        out.println("Inserisci le coordinate della prima tessera separate da una virgola (es. riga, colonna) :");
        hand.add(inputFirstCoords(board));
        if(isPossibleToDrawMore(hand, board)) {
            if (maxNumItems > 1) {
                do {
                    out.println("Vuoi continuare a prendere tessere? (y/n)");
                    continueResponse = s.nextLine();
                } while (!isYesOrNo(continueResponse));
                if (isNo(continueResponse)) {
                    notifyObserver(new DrawTilesMessage(username, hand));
                    return;
                }
            }

            out.println("Inserisci le coordinate della seconda tessera separate da una virgola (es. riga, colonna) :");
            hand.add(inputCoords(hand, board));
            if (isPossibleToDrawMore(hand, board)) {
                if (maxNumItems > 2) {
                    do {
                        out.println("Vuoi continuare a prendere tessere? (y/n)");
                        continueResponse = s.nextLine();
                    } while (!isYesOrNo(continueResponse));
                    if (isNo(continueResponse)) {
                        notifyObserver(new DrawTilesMessage(username, hand));
                        return;
                    }
                }
                out.println("Inserisci le coordinate della terza tessera separate da una virgola (es. riga, colonna) :");
                hand.add(inputCoords(hand, board));
            }
        }
        notifyObserver(new DrawTilesMessage(username, hand));
        */

    }
    
    public void rejectDrawRequest(String username, Square[][] board, ItemTile[][] bookshelf, int maxNumItems){
        out.println("Invalid draw request! It seems like your client misbehaved... " +
                "Try re-inserting the coordinates of the tiles you want to draw and if the error persists draw some other tiles because those you are trying to draware invalid!");
        askDraw(username, board, bookshelf, maxNumItems);
    }

    /**
     * Checks if the current board configuration allows the player to draw more tiles
     * @param hand ArrayList of tiles taken by the player in this turn
     * @param board gameboard
     * @return true if there are other tiles the player can pick (based on what he's already picked and the board configuration)
     */
    private boolean isPossibleToDrawMore(ArrayList<Square> hand, Square[][] board){
        ArrayList<Integer> rows = new ArrayList<>();
        ArrayList<Integer> columns = new ArrayList<>();
        for(Square sq : hand){
            rows.add(sq.getRow());
            columns.add(sq.getColumn());
        }
        if(allCoordsAreEqual(rows)){
            Collections.sort(columns);
            if(columns.get(0)!=0 && board[rows.get(0)][columns.get(0)-1].isPickable()) return true;
            if(columns.get(columns.size()-1)!= board.length-1 && board[rows.get(0)][columns.get(columns.size()-1)+1].isPickable()) return true;
        }
        if(allCoordsAreEqual(columns)){
            Collections.sort(rows);
            if(rows.get(0)!=0 && board[rows.get(0)-1][columns.get(0)].isPickable()) return true;
            if(rows.get(rows.size()-1)!= board.length-1 && board[rows.get(rows.size()-1)+1][columns.get(0)].isPickable()) return true;
        }
        return false;
    }

    /**
     * This method splits the input string on commas and checks if there are exactly two parts. If not, it returns `true`.
     *     If there are two parts, it attempts to parse them as integers using `Integer.parseInt()`.
     *     If either of them cannot be parsed as an integer (due to a non-numeric character being present, for example),
     *      the method returns `true`. Otherwise, it returns `false`.
     *     Note that this implementation assumes the input string contains only ASCII digits, whitespace, and commas.
     *     If other characters are allowed, the parsing logic would need to be adapted accordingly.
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
     * Prompts the user to insert the coordinates of the tile he wants to pick and checks if the coordinates are those of a valid tile.
     * If the coordinates inserted are not valid, it re-prompts the user to insert the input.
     * @param hand ArrayList of tiles taken by the player in this turn
     * @param board gameboard
     * @param n the n_th tile taken in this turn
     * @return the Square corresponding to the given coordinates
     */
    private Square inputCoords(ArrayList<Square> hand, Square[][] board, int n) {
        Scanner s = new Scanner(System.in);
        String input = s.nextLine();
        while(invalidCoordFormat(input)) {
            out.println("Formato non valido! Inserisci le coordinate nel formato: (riga, colonna) :");
            input = s.nextLine();
        }
        String[] tiles = input.split(",");
        int row = Integer.parseInt(tiles[0].trim());
        int column = Integer.parseInt(tiles[1].trim());
        while(true){
            if(row<0 || row>Board.DIMENSIONS-1 || column<0 || column>Board.DIMENSIONS-1){
                out.println("Coordinate non valide! Assicurati di inserire coordinate che rientrino nelle dimensioni della Board (0-"+ (Board.DIMENSIONS-1));
            }else if(isTileAlreadyOnHand(row, column, hand)){
                out.println("Non puoi prendere una tessera che hai già preso! Inserisci altre coordinate: ");
            }else if(!board[row][column].isPickable()){
                out.println("Coordinate non valide! Assicurati di inserire le coordinate di una tessera che sia prendibile secondo le regole di gioco!");
            }else if(n>0 && !inLineTile(row, column, hand)){
                out.println("Coordinate non valide! La tessera che prendi deve essere adiacente e in linea retta (orizzontale o verticale) con le tessere che hai già preso in questo turno! Inserisci le coordinate nuovamente: ");
            }else{
                break;
            }
            input = s.nextLine();
            tiles = input.split(",");
            row = Integer.parseInt(tiles[0].trim());
            column = Integer.parseInt(tiles[1].trim());
        }
        return new Square(new Coordinates(row, column), board[row][column].getItem().getType());
    }

    /**
     * Checks if the given array of Integers contains only equals elements
     * @param x ArrayList of integers
     * @return true if the elements of the passed ArrayList are all equal
     */
    private boolean allCoordsAreEqual( ArrayList<Integer> x){
        for(int i=0; i<x.size()-1; i++){
            if (x.get(i)!=x.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the integers int the given arraylist are consecutive (in whatever order they are presented)
     * @param x ArrayList of integers
     * @return true if the elements in the given arraylist are consecutive (in whatever order they are presented)
     */
    private boolean allCoordsAreAdjacent(ArrayList<Integer> x){
        Collections.sort(x);
        for(int i=0; i<x.size()-1; i++){
            if(x.get(i)!=x.get(i+1)-1) return false;
        }
        return true;
    }

    /**
     * Checks if the passed coordinates are those of a Square which is in a straight line (horizontal or vertical) and adjacent to the ones in the player's hand
     * @param row row number of the Square to check
     * @param column column number of the Square to check
     * @param hand List of Squares (tiles) already picked by the player during this turn
     * @return true if the passed coordinates are those of a Square which is in a straight line (horizontal or vertical) and adjacent to the ones in the player's hand
     */
    private boolean inLineTile(int row, int column, ArrayList<Square> hand) {
        ArrayList<Integer> rows = new ArrayList<>();
        ArrayList<Integer> columns = new ArrayList<>();
        rows.add(row);
        columns.add(column);
        for(Square sq : hand){
            rows.add(sq.getRow());
            columns.add(sq.getColumn());
        }
        return (allCoordsAreAdjacent(rows) && allCoordsAreEqual(columns)) || (allCoordsAreAdjacent(columns) && allCoordsAreEqual(rows));
    }

    /**
     * Checks if the tile has already been picked by the player this turn
     * @param row row number of the Square to check
     * @param column column number of the Square to check
     * @param hand List of Squares (tiles) already picked by the player during this turn
     * @return true if the passed coordinates are those of a tile which is already in the player's hand
     */
    private boolean isTileAlreadyOnHand(int row, int column, ArrayList<Square> hand) {
        if(hand.size()==0) return false;
        for(Square sq : hand){
            if(sq.getCoordinates().getRow()==row && sq.getCoordinates().getColumn()==column) return true;
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
/*
    private Square inputFirstCoords(Square[][] board){
        Scanner s = new Scanner(System.in);
        String input = s.nextLine();
        String[] tiles = input.split(",");
        int row = Integer.parseInt(tiles[0].trim());
        int column = Integer.parseInt(tiles[1].trim());
        while(true){
            if(row<0 || row>Board.DIMENSIONS-1 || column<0 || column>Board.DIMENSIONS-1){
                out.println("Coordinate non valide! Assicurati di inserire coordinate che rientrino nelle dimensioni della Board (0-"+ (Board.DIMENSIONS-1));
            }else if(!board[row][column].isPickable()){
                out.println("Coordinate non valide! Assicurati di inserire le coordinate di una tessera che sia prendibile secondo le regole di gioco!");
            }else{
                break;
            }
            input = s.nextLine();
            tiles = input.split(",");
            row = Integer.parseInt(tiles[0].trim());
            column = Integer.parseInt(tiles[1].trim());
        }
        return new Square(new Coordinates(row, column), board[row][column].getItem().getType());
    }

 */

    /**
     * Prints the numbers from 0 to the passed parameter-1 as column indexes (separated by 5 spaces each)
     * @param columns number of columns to print
     */
    private void printColumnIndexes(int columns){
        out.print("       ");
        for(int i=0; i<columns; i++){
            out.print("  "+ i + "   ");
        }
        out.print("\n");
    }

    /**
     * @param type ItemType
     * @return the ESC ColorCode to paint the background accordingly to the ItemType received
     */
    private String paintBG(ItemType type){

            StringBuilder stringBuilder = new StringBuilder();
            Color c;
            switch(type){
                case CAT :
                    c=Color.GREEN;
                    break;
                case PLANT:
                    c=Color.FUCSIA;
                    break;
                case FRAME:
                    c=Color.BLUE;
                    break;
                case GAME:
                    c=Color.YELLOW_BOLD;
                    break;
                case TROPHY:
                    c=Color.CYAN_BOLD;
                    break;
                case BOOK:
                    c=Color.WHITE;
                    break;
                default:
                    c=Color.NO_COLOR;
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
        switch(type){
            case CAT :
                c=Color.WGREEN;
                break;
            case PLANT:
                c=Color.WFUCSIA;
                break;
            case FRAME:
                c=Color.WBLUE;
                break;
            case GAME:
                c=Color.WYELLOW_BOLD;
                break;
            case TROPHY:
                c=Color.WCYAN_BOLD;
                break;
            case BOOK:
                c=Color.WWHITE;
                break;
            default:
                c=Color.NO_COLOR;
                break;
        }
        return stringBuilder.append(c).append("  ").append(type).append("  ").append(Color.NO_COLOR).toString();

    }

    /**
     * Prints the GameBoard
     * @param board matrix of Squares to print
     */
    private void showBoard(Square[][] board) {
        out.println("Game Board:");
        printColumnIndexes(Board.DIMENSIONS);
        StringBuilder strBoard = new StringBuilder();
        for(int i=0; i< Board.DIMENSIONS; i++){
            strBoard.append("      ");
            for(int j=0; j<Board.DIMENSIONS; j++){
                strBoard.append("+-----");
            }
            strBoard.append("+\n");
            strBoard.append("  ").append(i).append("   |");
            for(int j=0; j<Board.DIMENSIONS; j++){
                //strBoard.append("  ");
                if(board[i][j].isPickable()){
                    //strBoard.append(board[i][j].getItem().toColorString());
                    strBoard.append(paintBG(board[i][j].getItem().getType()));
               }else{
                   //strBoard.append("  ").append(board[i][j].getItem()).append("  ");
                   strBoard.append(paintFG(board[i][j].getItem().getType()));
                }
                //strBoard.append("  |");
                strBoard.append("|");
            }
            strBoard.append("\n");
        }
        strBoard.append("      ");
        for(int j=0; j<Board.DIMENSIONS; j++){
            strBoard.append("+-----");
        }
        strBoard.append("+");
        out.println(strBoard.toString());
    }

    /**
     * Prints the bookshelf of the player
     * @param username username of the player whose bookshelf is printed
     * @param shelfie matrix of ItemTiles
     */
    public void showBookshelf(String username, ItemTile[][] shelfie){
        out.println("BookShelf of player " + username);
        printColumnIndexes(Bookshelf.Columns);
        StringBuilder strShelf = new StringBuilder();
        for(int i=0; i< Bookshelf.Rows; i++){
            strShelf.append("      ");
            for(int j=0; j<Bookshelf.Columns; j++){
                strShelf.append("+-----");
            }
            strShelf.append("+\n");
            strShelf.append("  ").append(i).append("   |");
            for(int j=0; j<Bookshelf.Columns; j++){
                strShelf.append("  ").append(shelfie[i][j]).append("  |");
            }
            strShelf.append("\n");
        }
        strShelf.append("      ");
        for(int j = 0; j< Bookshelf.Columns; j++){
            strShelf.append("+-----");
        }
        strShelf.append("+");
        out.println(strShelf.toString());
    }

    /**
     * Only for testing
     */
    public void showGreeting() {
        //for debugging
        out.println("Bravo!\n");
    }

    public void update(Message message, Observable o){
        if(!(o instanceof Game)){
            System.err.println("Ignoring updates from " + o);
            return;
        }
        switch(message.getType()){
            case BOARD:
                showBoard(((BoardMessage) message).getBoard());
                break;
            case BOOKSHELF:
                BookshelfMessage m = (BookshelfMessage) message;
                showBookshelf(m.getUsername(), m.getBookshelf());
                break;
            case DRAW_INFO:
                DrawInfoMessage m1 = (DrawInfoMessage) message;
                //showBookshelf(m1.getUsername(), m1.getBookshelf());
                //showBoard(m1.getBoard());
                askDraw(m1.getUsername(), m1.getBoard(), m1.getBookshelf(), m1.getMaxNumItems());
            default:
                System.err.println("Ignoring event from " + o);
                break;
        }
    }

    public void askInsert(String username, ArrayList<Square> squares, Bookshelf bookshelf) {
        out.println("Inizia la insert phase\n");
    }
}
