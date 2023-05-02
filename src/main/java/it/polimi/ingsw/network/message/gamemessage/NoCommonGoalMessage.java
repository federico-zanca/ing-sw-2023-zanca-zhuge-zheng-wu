package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.gamemessage.GameMessageType;
import it.polimi.ingsw.view.View;

//TODO accorpa con CommonGoalAchieved
public class NoCommonGoalMessage extends GameMessage implements MessageToClient {

    private final String content = "No common goal achieved this round :(";
    public NoCommonGoalMessage(String username) {
        super(username, GameMessageType.NO_COMMON_GOAL);
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "NoCommonGoalMessage{" +
                "username='" + getUsername() + '\'' +
                '}';
    }

    @Override
    public void execute(View view) {
        view.onNoCommonGoalMessage(this);
    }
}
