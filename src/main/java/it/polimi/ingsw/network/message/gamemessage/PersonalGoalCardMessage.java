package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a message containing a personal goal card sent to a client.
 * Inherits from the {@link MessageToClient} class.
 */
public class PersonalGoalCardMessage extends MessageToClient {
    private final PersonalGoalCard personalGoalCard;
    private final String username;
    /**
     * Constructs a new PersonalGoalCardMessage.
     * @param username The username of the player receiving the message.
     * @param personalGoalCard  The personal goal card to be sent.
     */
    public PersonalGoalCardMessage(String username, PersonalGoalCard personalGoalCard) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.personalGoalCard = new PersonalGoalCard(personalGoalCard);
    }
    /**
     * Gets the personal goal card sent in the message.
     * @return The personal goal card sent in the message.
     */
    public PersonalGoalCard getPersonalGoalCard() {
        return personalGoalCard;
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
        view.onPersonalGoalCardMessage(this);
    }
}
