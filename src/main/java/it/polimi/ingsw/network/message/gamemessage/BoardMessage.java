package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.gameboard.Board;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

import java.util.Arrays;


public class BoardMessage extends MessageToClient {
    private final Square[][] board;
    private final String username;

    public BoardMessage(String username, Square[][] board){
        super(MessageType.GAME_MSG);
        this.username = username;
        this.board = new Square[Board.DIMENSIONS][Board.DIMENSIONS];

        for (int i = 0; i < Board.DIMENSIONS; i++) {
            for (int j = 0; j < Board.DIMENSIONS; j++) {
                this.board[i][j] = new Square(board[i][j]);
            }
        }
    }

    public Square[][] getBoard() {
        return board;
    }
    public String getUsername() {
        return username;
    }

    @Override
    public String toString(){
        return "BoardMessage{" +
                "board=" + Arrays.deepToString(board) +
                '}';
    }

    @Override
    public void execute(View view) {
        view.onBoardMessage(this);
    }
}
