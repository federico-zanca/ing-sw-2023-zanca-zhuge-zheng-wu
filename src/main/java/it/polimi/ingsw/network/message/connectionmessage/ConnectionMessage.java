package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.network.message.Message;

public abstract class ConnectionMessage extends Message{
    private ConnectionMessageType type;

    public ConnectionMessage(ConnectionMessageType type) {
        this.type = type;
    }

    public ConnectionMessageType getType() {
        return type;
    }
}
