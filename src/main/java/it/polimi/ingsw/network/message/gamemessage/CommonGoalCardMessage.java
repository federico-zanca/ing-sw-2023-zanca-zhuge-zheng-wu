package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.commongoals.CommonGoalCard;

import java.util.ArrayList;

public class CommonGoalCardMessage extends GameMessage {
    private ArrayList<CommonGoalCard> cards;

    public CommonGoalCardMessage(String username, ArrayList<CommonGoalCard> cards){
        super(username, GameMessageType.COMMONGOALCARD);
        this.cards = cards;
    }

    public ArrayList<CommonGoalCard> getCards(){
        return cards;
    }
}
