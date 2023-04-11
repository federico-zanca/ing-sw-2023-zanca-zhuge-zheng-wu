package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.commongoals.CommonGoalCard;

import java.util.ArrayList;

public class CommonGoalCardMessage extends Message{
    private ArrayList<CommonGoalCard> cards;

    public CommonGoalCardMessage(String username, ArrayList<CommonGoalCard> cards){
        super(username,MessageType.COMMONGOALCARD);
        this.cards = cards;
    }

    public ArrayList<CommonGoalCard> getCards(){
        return cards;
    }
}
