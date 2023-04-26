package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import it.polimi.ingsw.network.message.Message;

public class ReconnectionMessage extends ConnectionMessage {
    private final GameView model;
    private final String content;
    private final PersonalGoalCard personalGoal;

    public ReconnectionMessage(GameView gameView, String content, PersonalGoalCard personalGoal) {
        super(ConnectionMessageType.RECONNECTION);
        this.model = gameView;
        this.content = content;
        this.personalGoal = personalGoal;
    }

    public GameView getModel() {
        return model;
    }

    public String getContent() {
        return content;
    }

    public PersonalGoalCard getPersonalGoal() {
        return personalGoal;
    }
}
