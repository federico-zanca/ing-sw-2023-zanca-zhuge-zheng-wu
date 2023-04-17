package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.commongoals.CommonGoalCard;

public class AchievedCommonGoalMessage extends GameMessage {

    private final CommonGoalCard goal; //TODO potrebbe essere insicuro passare la commongoal card

    private final int points;
    public AchievedCommonGoalMessage(String username, CommonGoalCard commonGoal, int points) {
        super(username, GameMessageType.ACHIEVED_COMMON_GOAL);

        this.goal = commonGoal;
        this.points = points;
    }

    public CommonGoalCard getGoal() {
        return goal;
    }

    public int getPoints() {
        return points;
    }
}
