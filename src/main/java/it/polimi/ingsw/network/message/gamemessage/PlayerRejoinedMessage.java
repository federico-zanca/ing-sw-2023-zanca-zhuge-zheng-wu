package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.view.View;

public class PlayerRejoinedMessage extends GameMessage implements MessageToClient {
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
