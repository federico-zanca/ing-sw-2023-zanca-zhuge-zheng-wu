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
        String username = s.next();
        return username;

    }

    public Coordinates askPlayer() {
        Scanner s = new Scanner(System.in);
        System.out.println("Inserisci le coordinate della tessera da prendere: ");
        return new Coordinates(0, 0);
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
                strBoard.append(" ").append(board[i][j].getItem()).append(" |");
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

    public void update(Message receivedMessage, Observable o){
        if(!(o instanceof Game)){
            System.err.println("Ignoring updates from " + o);
            return;
        }
        switch(receivedMessage.getType()){
            case BOARD:
                showBoard(((BoardMessage) receivedMessage).getBoard());
                break;
            case BOOKSHELF:
                BookshelfMessage m = (BookshelfMessage) receivedMessage;
                showBookshelf(m.getUsername(), m.getBookshelf());
                break;
            default:
                System.err.println("Ignoring event from " + o);
                break;
        }
    }
}
