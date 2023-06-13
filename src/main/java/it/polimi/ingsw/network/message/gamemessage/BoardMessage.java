package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.gameboard.Board;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

import java.util.Arrays;
/**
 * Represents a message sent to the client containing the game board state.
 * Inherits from the {@link MessageToClient} class.
 */
public class BoardMessage extends MessageToClient {
    private final Square[][] board;
    private final String username;
    /**
     * Constructs a new BoardMessage.
     * @param username The username of the player receiving the board message.
     * @param board The game board state.
     */
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

    /**
     * Gets the game board state.
     *
     * @return The game board as a 2D array of Square objects.
     */
    public Square[][] getBoard() {
        return board;
    }

    /**
     * Gets the username of the player receiving the board message.
     * @return The receiving player's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns a string representation of the BoardMessage object.
     * @return The string representation.
     */
    @Override
    public String toString(){
        return "BoardMessage{" +
                "board=" + Arrays.deepToString(board) +
                '}';
    }
    /**
     * Executes the message on the provided view, triggering the onBoardMessage callback.
     * @param view The view on which to execute the message.
     */
    @Override
    public void execute(View view) {
        view.onBoardMessage(this);
    }
}
