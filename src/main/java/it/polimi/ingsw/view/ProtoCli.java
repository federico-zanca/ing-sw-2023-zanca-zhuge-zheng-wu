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

    public void askDraw(Square[][] board) {
        showBoard(board);
        out.println("Inserisci le coordinate della tessera separate da una virgola (es. riga, colonna) :");
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
        notifyObserver(new DrawTilesMessage("", new Coordinates(row, column)));
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
    private void showBookshelf(String username, ItemTile[][] shelfie){
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
