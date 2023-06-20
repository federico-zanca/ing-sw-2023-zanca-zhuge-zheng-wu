package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a message indicating that the game has started.
 * Inherits from the {@link MessageToClient} class.
 */
public class GameStartedMessage extends MessageToClient {
    private final GameView gameView;
    private final String username;
    /**
     * Constructs a new GameStartedMessage.
     * @param username The username of the player.
     * @param gameView  The game view containing the initial game state.
     */
    public GameStartedMessage(String username, GameView gameView) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.gameView = gameView;
    }
    /**
     * Gets the game view containing the initial game state.
     *
     * @return The game view.
     */
    public GameView getGameView() {
        return gameView;
    }
    /**
     * Gets the username of the player.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }
    /**
     * Executes the message on the client's view.
     *
     * @param view The view that handles the message.
     */
    @Override
    public void execute(View view) {
        view.onGameStartedMessage(this);
    }
}
