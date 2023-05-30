package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

public class AchievedCommonGoalMessage extends MessageToClient {
    private final String content;
    private final String username;
    public AchievedCommonGoalMessage(String username, String content) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void execute(View view) {
        view.onAchievedCommonGoalMessage(this);
    }
}
