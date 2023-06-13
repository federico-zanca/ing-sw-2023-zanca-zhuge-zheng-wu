package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import it.polimi.ingsw.network.message.ChatMessage;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
/**
 * Represents a message sent to the client during reconnection to restore the game state.
 * Inherits from the {@link MessageToClient} class.
 */
public class ReconnectionMessage extends MessageToClient {
    private final GameView model;
    private final String content;
    private final PersonalGoalCard personalGoal;
    private final ArrayList<ChatMessage> chat;

    /**
     * Constructs a new ReconnectionMessage.
     *
     * @param gameView     The game view representing the current game state.
     * @param content      The additional content of the reconnection message.
     * @param personalGoal The personal goal card of the player.
     * @param chat         The list of chat messages in the game.
     */
    public ReconnectionMessage(GameView gameView, String content, PersonalGoalCard personalGoal, ArrayList<ChatMessage> chat) {
        super(MessageType.CONNECTION_MSG);
        this.model = gameView;
        this.content = content;
        this.personalGoal = personalGoal;
        this.chat = chat;
    }

    /**
     * Retrieves the game view representing the current game state.
     *
     * @return The game view.
     */
    public GameView getModel() {
        return model;
    }

    /**
     * Retrieves the content of the reconnection message.
     *
     * @return The content of the message.
     */
    public String getContent() {
        return content;
    }

    /**
     * Retrieves the personal goal card of the player.
     *
     * @return The personal goal card.
     */
    public PersonalGoalCard getPersonalGoal() {
        return personalGoal;
    }

    /**
     * Executes the message on the provided view, triggering the onReconnectionMessage callback.
     *
     * @param view The view on which to execute the message.
     */
    @Override
    public void execute(View view) {
        view.onReconnectionMessage(this);
    }

    /**
     * Retrieves the list of chat messages in the game.
     *
     * @return The list of chat messages.
     */
    public ArrayList<ChatMessage> getChat() {
        return chat;
    }
}
