package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

//TODO accorpa con CommonGoalAchieved
/**
 * Represents a message indicating that no common goal was achieved in the current round.
 * Inherits from the {@link MessageToClient} class.
 */
public class NoCommonGoalMessage extends MessageToClient {

    private final String username;
    /**
     * Constructs a new NoCommonGoalMessage.
     * @param username The username of the player receiving the message.
     */
    public NoCommonGoalMessage(String username) {
        super(MessageType.GAME_MSG);
        this.username = username;
    }
    /**
     * Gets the content of the message.
     * @return The content of the message.
     */
    public String getContent() {
        return "No common goal achieved this round :(";
    }
    /**
     * Returns a string representation of the NoCommonGoalMessage object.
     * @return A string representation of the NoCommonGoalMessage object.
     */
    @Override
    public String toString() {
        return "NoCommonGoalMessage{" +
                "username='" + getUsername() + '\'' +
                '}';
    }
    /**
     * Executes the message on the client's view.
     * @param view The client's view.
     */
    @Override
    public void execute(View view) {
        view.onNoCommonGoalMessage(this);
    }
    /**
     * Gets the username of the player receiving the message.
     * @return The username of the player receiving the message.
     */
    public String getUsername() {
        return username;
    }
}
