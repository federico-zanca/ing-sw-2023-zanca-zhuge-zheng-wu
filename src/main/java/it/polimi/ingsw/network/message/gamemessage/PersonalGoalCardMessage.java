package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

public class PersonalGoalCardMessage extends GameMessage implements MsgToClient {
    private final PersonalGoalCard personalGoalCard;
    public PersonalGoalCardMessage(String username, PersonalGoalCard personalGoalCard) {
        super(username, GameMessageType.PERSONALGOALCARD);
        this.personalGoalCard = new PersonalGoalCard(personalGoalCard);
    }

    public PersonalGoalCard getPersonalGoalCard() {
        return personalGoalCard;
    }

    @Override
    public void execute(View view) {
        view.onPersonalGoalCardMessage(this);
    }
}
