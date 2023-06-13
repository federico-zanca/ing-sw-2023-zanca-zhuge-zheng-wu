package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a message sent to the client indicating that the game is not ready to start (not enough players in lobby).
 * Inherits from the {@link MessageToClient} class.
 */
public class GameNotReadyMessage extends MessageToClient {

    /**
     * Constructs a new GameNotReadyMessage.
     *
     * @param game_not_ready_to_start The content of the message indicating that the game is not ready to start.
     */
    public GameNotReadyMessage(String game_not_ready_to_start) {
        super(MessageType.LOBBY_MSG);
    }

    /**
     * Executes the message on the provided view, triggering the onGameNotReadyMessage callback.
     *
     * @param view The view on which to execute the message.
     */
    @Override
    public void execute(View view) {
        view.onGameNotReadyMessage(this);
    }
}
