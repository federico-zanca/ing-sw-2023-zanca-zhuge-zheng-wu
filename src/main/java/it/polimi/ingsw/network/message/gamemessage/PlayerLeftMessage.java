package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

public class PlayerLeftMessage extends GameMessage implements MsgToClient {
    private final String content;

    public PlayerLeftMessage(String username, String content) {
        super(username, GameMessageType.PLAYER_LEFT);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public void execute(View view) {
        view.onPlayerLeftMessage(this);
    }
}
