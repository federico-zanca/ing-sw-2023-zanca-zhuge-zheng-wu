package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.Message;

public abstract class GameMessage extends Message {

    private final String username;

    private final GameMessageType type;

    public GameMessage(String username, GameMessageType gametype) {
        this.username = username;
        this.type = gametype;
    }


    public String getUsername() {
        return username;
    }

    public GameMessageType getType() {
        return type;
    }

}
