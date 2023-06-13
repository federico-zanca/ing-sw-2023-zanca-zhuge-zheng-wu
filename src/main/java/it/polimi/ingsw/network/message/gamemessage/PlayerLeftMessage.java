package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a message indicating that a player has left the game, sent to a client.
 * Inherits from the {@link MessageToClient} class.
 */
public class PlayerLeftMessage extends MessageToClient {
    private final String content;
    private final String username;
    /**
     * Constructs a new PlayerLeftMessage.
     * @param username The username of the player receiving the message.
     * @param content The content of the message indicating that a player has left.
     */
    public PlayerLeftMessage(String username, String content) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.content = content;
    }
    /**
     * Gets the content of the message indicating that a player has left.
     * @return The content of the message.
     */
    public String getContent() {
        return content;
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
        view.onPlayerLeftMessage(this);
    }
}
