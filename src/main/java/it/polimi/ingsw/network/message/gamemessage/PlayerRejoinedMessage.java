package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.Message;

public class PlayerRejoinedMessage extends GameMessage {
    private final String player;

    public PlayerRejoinedMessage(String s, String player) {
        super(s, GameMessageType.PLAYER_REJOINED);
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }
}
