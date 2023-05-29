package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.network.message.MsgToServer;
import it.polimi.ingsw.view.View;

public abstract class ConnectionMessage extends Message implements MsgToClient, MsgToServer {
    private final ConnectionMessageType type;

    public ConnectionMessage(ConnectionMessageType type) {
        this.type = type;
    }

    public ConnectionMessageType getType() {
        return type;
    }

    public void execute(View view){
        System.err.println("Operation on client not allowed!");
    }

    public void execute(Client client, PreGameController preGameController){
        System.err.println("Operation on server not allowed!");
    }
}
