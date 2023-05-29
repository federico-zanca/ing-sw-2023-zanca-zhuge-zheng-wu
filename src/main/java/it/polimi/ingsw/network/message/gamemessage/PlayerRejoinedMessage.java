package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

public class PlayerRejoinedMessage extends GameMessage implements MsgToClient {
    private final String player;

    public PlayerRejoinedMessage(String s, String player) {
        super(s, GameMessageType.PLAYER_REJOINED);
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    @Override
    public void execute(View view) {
        view.onPlayerRejoinedMessage(this);
    }
}
