package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ItemTile;
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
    public String askUsername(){
        out.print("Enter your name: ");
        Scanner s = new Scanner(System.in);
        String username = s.nextLine();
        return username;

    }

    public void askDraw(Square[][] board, int maxNumItems) {
        Game game = Game.getInstance();
        showBookshelf("amuro", game.getPlayerByUsername("amuro").getBookshelf().getShelfie());
        showBoard(board);
        ArrayList<Square> hand = new ArrayList<>();
        String continueResponse;
        Scanner s = new Scanner(System.in);
        out.println("Puoi prendere al massimo " + Math.min(3, maxNumItems) +" tessere.");
        out.println("Inserisci le coordinate della prima tessera separate da una virgola (es. riga, colonna) :");
        hand.add(inputFirstCoords(board));
        if(isPossibleToDrawMore(hand, board)) {
            if (maxNumItems > 1) {
                do {
                    out.println("Vuoi continuare a prendere tessere? (y/n)");
                    continueResponse = s.nextLine();
                } while (!isYesOrNo(continueResponse));
                if (isNo(continueResponse)) {
                    notifyObserver(new DrawTilesMessage("amuro", hand));
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
                        notifyObserver(new DrawTilesMessage("amuro", hand));
                        return;
                    }
                }


                out.println("Inserisci le coordinate della terza tessera separate da una virgola (es. riga, colonna) :");
                hand.add(inputCoords(hand, board));
            }
        }
        notifyObserver(new DrawTilesMessage("amuro", hand));
    }

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

    private Square inputCoords(ArrayList<Square> hand, Square[][] board) {
        Scanner s = new Scanner(System.in);
        String input = s.nextLine();
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
            }else if(!inLineTile(row, column, hand)){
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

    private boolean allCoordsAreEqual( ArrayList<Integer> x){
        for(int i=0; i<x.size()-1; i++){
            if (x.get(i)!=x.get(i + 1)) {
                return false;
            }
        }
        return true;
    }
    private boolean allCoordsAreAdjacent(ArrayList<Integer> x){
        Collections.sort(x);
        for(int i=0; i<x.size()-1; i++){
            if(x.get(i)!=x.get(i+1)-1) return false;
        }
        return true;
    }
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

    private boolean isTileAlreadyOnHand(int row, int column, ArrayList<Square> hand) {
        for(Square sq : hand){
            if(sq.getCoordinates().getRow()==row && sq.getCoordinates().getColumn()==column) return true;
        }
        return false;
    }

    public static boolean isNo(String input) {
        input = input.toLowerCase();
        return input.equals("n") || input.equals("no");
    }
    public static boolean isYesOrNo(String input) {
        input = input.toLowerCase();
        return input.equals("y") || input.equals("yes") || isNo(input);
    }

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
    private void printColumnIndexes(int columns){
        out.print("    ");
        for(int i=0; i<columns; i++){
            out.print(" "+ i + "  ");
        }
        out.print("\n");
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
            strBoard.append("    ");
            for(int j=0; j<Board.DIMENSIONS; j++){
                strBoard.append("+--+");
            }
            strBoard.append("\n");
            strBoard.append(" ").append(i).append(" |");
            for(int j=0; j<Board.DIMENSIONS; j++){
                strBoard.append(" ");
                if(board[i][j].isPickable()){
                    strBoard.append(board[i][j].getItem().toColorString());
                }else{
                    strBoard.append(board[i][j].getItem());
                }
                strBoard.append(" |");
            }
            strBoard.append("\n");
        }
        strBoard.append("    ");
        for(int j=0; j<Board.DIMENSIONS; j++){
            strBoard.append("+--+");
        }
        out.println(strBoard.toString());
    }

    /**
     * Prints the bookshelf of the player
     * @param username username of the player whose bookshelf is printed
     * @param shelfie matrix of ItemTiles
     */
    public void showBookshelf(String username, ItemTile[][] shelfie){
        out.println("BookShelf of player " + username);
        printColumnIndexes(Bookshelf.Columns); //TODO attenzione Bookshelf è del server
        StringBuilder strShelf = new StringBuilder();
        for(int i=0; i< Bookshelf.Rows; i++){
            strShelf.append("    ");
            for(int j=0; j<Bookshelf.Columns; j++){
                strShelf.append("+--+");
            }
            strShelf.append("\n");
            strShelf.append(" ").append(i).append(" |");
            for(int j=0; j<Bookshelf.Columns; j++){
                strShelf.append(" ").append(shelfie[i][j]).append(" |");
            }
            strShelf.append("\n");
        }
        strShelf.append("    ");
        for(int j = 0; j< Bookshelf.Columns; j++){
            strShelf.append("+--+");
        }
        out.println(strShelf.toString());
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
                showBookshelf(m1.getUsername(), m1.getBookshelf());
                showBoard(m1.getBoard());
                askDraw(m1.getBoard(), m1.getMaxNumItems());
            default:
                System.err.println("Ignoring event from " + o);
                break;
        }
    }

    public void showGreeting() {
        //for debugging
        out.println("Bravo!\n");
    }
}
