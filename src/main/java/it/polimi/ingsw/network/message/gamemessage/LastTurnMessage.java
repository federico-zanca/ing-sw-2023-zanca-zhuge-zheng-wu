package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a message indicating the last turn of the game.
 * Inherits from the {@link MessageToClient} class.
 */
public class LastTurnMessage extends MessageToClient {
    private final String currentPlayer;
    private final String username;
    /**
     * Constructs a new LastTurnMessage.
     * @param username  The username of the player receiving the message.
     * @param currentPlayer The username of the current player.
     */
    public LastTurnMessage(String username, String currentPlayer) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.currentPlayer = currentPlayer;
    }
    /**
     * Gets the username of the current player.
     * @return The username of the current player.
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }
    /**
     * Gets the username of the player receiving the message.
     * @return The username of the player receiving the message.
     */
    public String getUsername() {
        return username;
    }
    /**
     * Executes the message on the client's view.
     * @param view The client's view.
     */
    @Override
    public void execute(View view) {
        view.onLastTurnMessage(this);
    }
}
