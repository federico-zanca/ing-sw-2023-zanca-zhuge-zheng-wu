package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.view.View;

public abstract class LobbyMessage extends Message implements MessageToClient {
    private final LobbyMessageType type;

    public LobbyMessage(LobbyMessageType type) {
        this.type = type;
    }

    public LobbyMessageType getType() {
        return type;
    }

    public void execute(View view){
        System.err.println("Operation not allowed!");
    }
}
