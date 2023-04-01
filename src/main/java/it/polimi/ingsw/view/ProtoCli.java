package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.gameboard.Board;
import it.polimi.ingsw.model.gameboard.Coordinates;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.BoardMessage;
import it.polimi.ingsw.network.message.LoginRequest;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageType;
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

    public void update(Message receivedMessage, Observable o){
        if(!(o instanceof Game)){
            System.err.println("Ignoring updates from " + o);
            return;
        }
        if(receivedMessage.getType() == MessageType.BOARD){
            showBoard(((BoardMessage) receivedMessage).getBoard());
        }
        else{
            System.err.println("Ignoring event from " + o);
        }
    }

    private void printColumnIndexes(){
        out.print("    ");
        for(int i=0; i<Board.DIMENSIONS; i++){
            out.print(" "+ i + "  ");
        }
        out.print("\n");
    }

    /**
     * Prints the GameBoard
     * @param board matrix of Squares to print
     */
    private void showBoard(Square[][] board) {
        printColumnIndexes();
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

}
