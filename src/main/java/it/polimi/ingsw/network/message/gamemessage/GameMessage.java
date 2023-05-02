package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.view.View;

public abstract class GameMessage extends Message implements MessageToClient {

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


    public void execute(View view){
        System.err.println("Operation not allowed!");
    }
}
