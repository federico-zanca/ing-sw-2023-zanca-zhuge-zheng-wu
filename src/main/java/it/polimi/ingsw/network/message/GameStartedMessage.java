package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.commongoals.CommonGoalCard;
import it.polimi.ingsw.model.gameboard.Square;

import java.util.ArrayList;

public class GameStartedMessage extends Message {
    private final Square[][] gameboard;
    private final ArrayList<CommonGoalCard> commonGoals;

    public GameStartedMessage(String username, Square[][] gameboard, ArrayList<CommonGoalCard> commonGoals) {
        super(username, MessageType.GAME_STARTED);
        this.gameboard = gameboard;
        this.commonGoals = commonGoals;
    }

    public Square[][] getGameboard() {
        return gameboard;
    }

    public ArrayList<CommonGoalCard> getCommonGoals() {
        return commonGoals;
    }
}
