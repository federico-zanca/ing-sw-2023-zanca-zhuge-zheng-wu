package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.MessageType;

import java.util.ArrayList;
/**
 * Represents a message sent to the server to draw tiles.
 * Inherits from the {@link MessageToServer} class.
 */
public class DrawTilesMessage extends MessageToServer {
    private final ArrayList<Square> squares;
    private final String username;
    /**
     * Constructs a new DrawTilesMessage.
     * @param username The username of the player sending the draw tiles message.
     * @param squares The list of squares representing the tiles to be drawn.
     */
    public DrawTilesMessage(String username, ArrayList<Square> squares) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.squares = squares;
    }
    /**
     * Gets the list of squares containing the tiles to be drawn.
     * @return The list of squares.
     */
    public ArrayList<Square> getSquares() {
        return squares;
    }
    /**
     * Gets the username of the player sending the draw tiles message.
     *
     * @return The sending player's username.
     */
    public String getUsername() {
        return username;
    }
    /**
     * Executes the message on the server.
     * @param client The client sending the message.
     * @param preGameController  The pre-game controller handling the message.
     */
    @Override
    public void execute(Client client, PreGameController preGameController) {
        System.err.println("Should not call this method: DrawTilesMessage.execute()"); //TODO fai sparire quando è tempo di farlo
    }
}
