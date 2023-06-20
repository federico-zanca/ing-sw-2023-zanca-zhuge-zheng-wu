package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a message sent to the client indicating the points earned by a player for adjacent items.
 * Inherits from the {@link MessageToClient} class.
 */
public class AdjacentItemsPointsMessage extends MessageToClient {
    private final int points;
    private final String playerUsername;
    private final String username;
    /**
     * Constructs a new AdjacentItemsPointsMessage.
     * @param username The username of the player receiving the points.
     * @param playerUsername The username of the player who earned the points.
     * @param points The number of points earned.
     */
    public AdjacentItemsPointsMessage(String username, String playerUsername, int points) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.playerUsername = playerUsername;
        this.points = points;
    }
    /**
     * Gets the number of points earned.
     * @return The number of points.
     */
    public int getPoints() {
        return points;
    }
    /**
     * Gets the username of the player who earned the points.
     * @return The player's username.
     */
    public String getPlayerUsername() {
        return playerUsername;
    }
    /**
     * Gets the username of the player receiving the points.
     * @return The receiving player's username.
     */
    public String getUsername() {
        return username;
    }
    /**
     * Executes the message on the provided view, triggering the onAdjacentItemsPointsMessage callback.
     * @param view The view on which to execute the message.
     */
    @Override
    public void execute(View view) {
        view.onAdjacentItemsPointsMessage(this);
    }
}
