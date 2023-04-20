package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.gamemessage.GameMessageType;

public class PersonalGoalPointsMessage extends GameMessage {
    private final PersonalGoalCard personalGoal;
    private final int points;
    private final String playerUsername;

    public PersonalGoalPointsMessage(String username, String playerUsername, PersonalGoalCard personalGoal, int points) {
        super(username, GameMessageType.PERSONAL_GOAL_POINTS);
        this.personalGoal = personalGoal;
        this.playerUsername = playerUsername;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public PersonalGoalCard getPersonalGoal() {
        return personalGoal;
    }

    public String getPlayerUsername() {
        return playerUsername;
    }
}
