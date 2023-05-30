package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

public class PlayerRejoinedMessage extends MessageToClient {
    private final String player;
    private final String username;

    public PlayerRejoinedMessage(String username, String player) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    @Override
    public void execute(View view) {
        view.onPlayerRejoinedMessage(this);
    }

    public String getUsername() {
        return username;
    }
}
