package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.gamemessage.GameMessageType;
import it.polimi.ingsw.view.View;

public class ConnectedToServerMessage extends ConnectionMessage implements MessageToClient {
    public ConnectedToServerMessage(Client client) {
        super(ConnectionMessageType.CONNECTED_TO_SERVER);
    }

    @Override
    public void execute(View view){
        view.onConnectedServerMessage(this);
    }
}
