package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.gamemessage.GameMessageType;

public class ConnectedToServerMessage extends ConnectionMessage {
    public ConnectedToServerMessage(Client client) {
        super(ConnectionMessageType.CONNECTED_TO_SERVER);
    }
}
