package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import it.polimi.ingsw.network.message.Message;

public class PersonalGoalCardMessage extends GameMessage {
    private final PersonalGoalCard personalGoalCard;
    private final ItemType[][] objective;
    public PersonalGoalCardMessage(String username, PersonalGoalCard personalGoalCard, ItemType[][] objective) {
        super(username, GameMessageType.PERSONALGOALCARD);
        this.personalGoalCard = personalGoalCard;
        this.objective = objective;
    }

    public PersonalGoalCard getPersonalGoalCard() {
        return personalGoalCard;
    }

    public ItemType[][] getObjective() {
        return objective;
    }
}
