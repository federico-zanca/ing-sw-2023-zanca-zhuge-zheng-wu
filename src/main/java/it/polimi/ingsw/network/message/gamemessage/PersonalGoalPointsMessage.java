package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

public class PersonalGoalPointsMessage extends MessageToClient {
    private final PersonalGoalCard personalGoal;
    private final int points;
    private final String playerUsername;
    private final String username;

    public PersonalGoalPointsMessage(String username, String playerUsername, PersonalGoalCard personalGoal, int points) {
        super(MessageType.GAME_MSG);
        this.username = username;
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

    @Override
    public void execute(View view) {
        view.onPersonalGoalPointsMessage(this);
    }

    public String getUsername() {
        return username;
    }
}
