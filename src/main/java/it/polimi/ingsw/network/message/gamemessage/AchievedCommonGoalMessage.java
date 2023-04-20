package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.commongoals.CommonGoalCard;

public class AchievedCommonGoalMessage extends GameMessage {
    private final String content;

    public AchievedCommonGoalMessage(String username, String content) {
        super(username, GameMessageType.ACHIEVED_COMMON_GOAL);
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
