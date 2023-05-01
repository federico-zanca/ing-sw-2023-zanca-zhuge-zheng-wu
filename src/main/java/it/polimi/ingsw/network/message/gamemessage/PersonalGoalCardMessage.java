package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;

public class PersonalGoalCardMessage extends GameMessage {
    private final PersonalGoalCard personalGoalCard;
    public PersonalGoalCardMessage(String username, PersonalGoalCard personalGoalCard) {
        super(username, GameMessageType.PERSONALGOALCARD);
        this.personalGoalCard = new PersonalGoalCard(personalGoalCard);
    }

    public PersonalGoalCard getPersonalGoalCard() {
        return personalGoalCard;
    }
}
