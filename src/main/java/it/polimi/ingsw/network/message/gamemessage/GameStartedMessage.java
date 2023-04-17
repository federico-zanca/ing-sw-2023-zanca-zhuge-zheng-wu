package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.model.commongoals.CommonGoalCard;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.gamemessage.GameMessageType;

import java.util.ArrayList;

public class GameStartedMessage extends GameMessage {
    private final GameView gameView;
    private final Square[][] gameboard;
    private final ArrayList<CommonGoalCard> commonGoals;

    public GameStartedMessage(String username, GameView gameView, Square[][] gameboard, ArrayList<CommonGoalCard> commonGoals) {
        super(username, GameMessageType.GAME_STARTED);
        this.gameboard = gameboard;
        this.commonGoals = commonGoals;
        this.gameView = gameView;
    }

    public Square[][] getGameboard() {
        return gameboard;
    }

    public ArrayList<CommonGoalCard> getCommonGoals() {
        return commonGoals;
    }

    public GameView getGameView() {
        return gameView;
    }
}
