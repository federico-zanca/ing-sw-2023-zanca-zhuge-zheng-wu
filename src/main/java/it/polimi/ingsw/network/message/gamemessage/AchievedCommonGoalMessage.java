package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a message sent to the client indicating that a player has achieved a common goal.
 * Inherits from the {@link MessageToClient} class.
 */
public class AchievedCommonGoalMessage extends MessageToClient {
    private final String content;
    private final String username;
    /**
     * Constructs a new AchievedCommonGoalMessage.
     * @param username The username of the player who achieved the common goal.
     * @param content The content of the message.
     */
    public AchievedCommonGoalMessage(String username, String content) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.content = content;
    }
    /**
     * Gets the content of the message.
     *
     * @return The content of the message.
     */
    public String getContent() {
        return content;
    }
    /**
     * Gets the username of the player who achieved the common goal.
     *
     * @return The username of the player.
     */
    public String getUsername() {
        return username;
    }
    /**
     * Executes the message on the provided view, triggering the onAchievedCommonGoalMessage callback.
     *
     * @param view The view on which to execute the message.
     */
    @Override
    public void execute(View view) {
        view.onAchievedCommonGoalMessage(this);
    }
}
