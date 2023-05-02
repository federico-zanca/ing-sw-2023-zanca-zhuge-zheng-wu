package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.commongoals.CommonGoalCard;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.view.View;

public class AchievedCommonGoalMessage extends GameMessage implements MessageToClient {
    private final String content;

    public AchievedCommonGoalMessage(String username, String content) {
        super(username, GameMessageType.ACHIEVED_COMMON_GOAL);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public void execute(View view) {
        view.onAchievedCommonGoalMessage(this);
    }
}
