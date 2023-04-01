package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.gameboard.Square;

import java.util.Arrays;


public class BoardMessage extends Message{
    private final Square[][] board;

    /**
     * Default constructor
     * @param board
     */
    public BoardMessage(String username, Square[][] board){
        super(username, MessageType.BOARD);
        this.board = board;
    }

    public Square[][] getBoard() {
        return board;
    }
    @Override
    public String toString(){
        return "BoardMessage{" +
                "board=" + Arrays.deepToString(board) +
                '}';
    }
}
