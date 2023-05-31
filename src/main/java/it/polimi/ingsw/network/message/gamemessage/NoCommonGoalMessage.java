package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

//TODO accorpa con CommonGoalAchieved
public class NoCommonGoalMessage extends MessageToClient {

    private final String username;

    public NoCommonGoalMessage(String username) {
        super(MessageType.GAME_MSG);
        this.username = username;
    }

    public String getContent() {
        return "No common goal achieved this round :(";
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

    public String getUsername() {
        return username;
    }
}
