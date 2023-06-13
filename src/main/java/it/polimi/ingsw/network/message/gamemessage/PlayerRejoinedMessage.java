package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a message indicating that a player has rejoined the game, sent to a client.
 * Inherits from the {@link MessageToClient} class.
 */
public class PlayerRejoinedMessage extends MessageToClient {
    private final String player;
    private final String username;
    /**
     * Constructs a new PlayerRejoinedMessage.
     * @param username The username of the player receiving the message.
     * @param player The username of the player who has rejoined the game.
     */
    public PlayerRejoinedMessage(String username, String player) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.player = player;
    }
    /**
     * Gets the username of the player who has rejoined the game.
     * @return The username of the rejoined player.
     */
    public String getPlayer() {
        return player;
    }
    /**
     * Executes the message on the client's view.
     * @param view The client's view.
     */
    @Override
    public void execute(View view) {
        view.onPlayerRejoinedMessage(this);
    }
    /**
     * Gets the username of the player receiving the message.
     * @return The username of the player receiving the message.
     */
    public String getUsername() {
        return username;
    }
}
