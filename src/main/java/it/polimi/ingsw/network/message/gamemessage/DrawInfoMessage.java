package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a message sent to the client containing information about
 * the maximum number of item tiles that can be drawn.
 * Inherits from the {@link MessageToClient} class.
 */
public class DrawInfoMessage extends MessageToClient {
    private final int maxNumItems;
    private final GameView model;
    private final String username;
    /**
     * Constructs a new DrawInfoMessage.
     * @param username  The username of the player receiving the draw info message.
     * @param model The game view containing the current state of the game.
     * @param maxNumTiles   The maximum number of tiles that can be drawn.
     */
    public DrawInfoMessage(String username, GameView model, int maxNumTiles){
        super(MessageType.GAME_MSG);
        this.username = username;
        this.model = model;
        this.maxNumItems = maxNumTiles;
    }
    /**
     * Gets the maximum number of tiles that can be drawn.
     *
     * @return The maximum number of tiles that can be drawn.
     */
    public int getMaxNumItems() {
        return maxNumItems;
    }
    /**
     * Gets the game view containing the current state of the game.
     *
     * @return The game view object.
     */
    public GameView getModel() {
        return model;
    }
    /**
     * Executes the message on the provided view, triggering the onDrawInfoMessage callback.
     *
     * @param view The view on which to execute the message.
     */
    @Override
    public void execute(View view) {
        view.onDrawInfoMessage(this);
    }
    /**
     * Gets the username of the player receiving the draw info message.
     *
     * @return The receiving player's username.
     */
    public String getUsername() {
        return username;
    }
}
