package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

public class AchievedCommonGoalMessage extends GameMessage implements MsgToClient {
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
