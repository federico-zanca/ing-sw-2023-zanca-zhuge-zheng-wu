package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.view.View;

public abstract class ConnectionMessage extends Message implements MessageToClient {
    private final ConnectionMessageType type;

    public ConnectionMessage(ConnectionMessageType type) {
        this.type = type;
    }

    public ConnectionMessageType getType() {
        return type;
    }

    public void execute(View view){
        System.err.println("Operation not allowed!");
    }
}
