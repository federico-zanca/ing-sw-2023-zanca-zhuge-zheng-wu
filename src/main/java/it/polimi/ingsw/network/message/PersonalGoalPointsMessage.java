package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;

public class PersonalGoalPointsMessage extends Message {
    private final PersonalGoalCard personalGoal;
    private final int points;

    public PersonalGoalPointsMessage(String username, PersonalGoalCard personalGoal, int points) {
        super(username, MessageType.PERSONAL_GOAL_POINTS);
        this.personalGoal = personalGoal;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public PersonalGoalCard getPersonalGoal() {
        return personalGoal;
    }
}
