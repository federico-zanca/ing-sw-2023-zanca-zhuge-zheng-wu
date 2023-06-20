package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a message containing personal goal points sent to a client.
 * Inherits from the {@link MessageToClient} class.
 */
public class PersonalGoalPointsMessage extends MessageToClient {
    private final PersonalGoalCard personalGoal;
    private final int points;
    private final String playerUsername;
    private final String username;
    /**
     * Constructs a new PersonalGoalPointsMessage.
     * @param username  The username of the player receiving the message.
     * @param playerUsername  The username of the player who achieved the personal goal.
     * @param personalGoal  The personal goal card for which points are awarded.
     * @param points  The points awarded for achieving the personal goal.
     */
    public PersonalGoalPointsMessage(String username, String playerUsername, PersonalGoalCard personalGoal, int points) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.personalGoal = personalGoal;
        this.playerUsername = playerUsername;
        this.points = points;
    }
    /**
     * Gets the points awarded for achieving the personal goal.
     * @return The points awarded for achieving the personal goal.
     */
    public int getPoints() {
        return points;
    }
    /**
     * Gets the personal goal card for which points are awarded.
     * @return The personal goal card for which points are awarded.
     */
    public PersonalGoalCard getPersonalGoal() {
        return personalGoal;
    }
    /**
     * Gets the username of the player who achieved the personal goal.
     * @return The username of the player who achieved the personal goal.
     */
    public String getPlayerUsername() {
        return playerUsername;
    }
    /**
     * Executes the message on the client's view.
     * @param view The client's view.
     */
    @Override
    public void execute(View view) {
        view.onPersonalGoalPointsMessage(this);
    }
    /**
     * Gets the username of the player receiving the message.
     * @return The username of the player receiving the message.
     */
    public String getUsername() {
        return username;
    }
}
