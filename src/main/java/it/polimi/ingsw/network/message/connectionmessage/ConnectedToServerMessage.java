package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

public class ConnectedToServerMessage extends MessageToClient{
    public ConnectedToServerMessage(Client client) {
        super(MessageType.CONNECTION_MSG);
    }

    @Override
    public void execute(View view){
        view.onConnectedServerMessage(this);
    }
}
