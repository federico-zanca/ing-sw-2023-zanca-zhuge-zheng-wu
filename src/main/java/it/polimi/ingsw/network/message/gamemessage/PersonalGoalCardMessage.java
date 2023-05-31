package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

public class PersonalGoalCardMessage extends MessageToClient {
    private final PersonalGoalCard personalGoalCard;
    private final String username;

    public PersonalGoalCardMessage(String username, PersonalGoalCard personalGoalCard) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.personalGoalCard = new PersonalGoalCard(personalGoalCard);
    }

    public PersonalGoalCard getPersonalGoalCard() {
        return personalGoalCard;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void execute(View view) {
        view.onPersonalGoalCardMessage(this);
    }
}
