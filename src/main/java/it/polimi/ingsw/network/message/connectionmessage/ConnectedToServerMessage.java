package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

public class ConnectedToServerMessage extends ConnectionMessage implements MsgToClient {
    public ConnectedToServerMessage(Client client) {
        super(ConnectionMessageType.CONNECTED_TO_SERVER);
    }

    @Override
    public void execute(View view){
        view.onConnectedServerMessage(this);
    }
}
