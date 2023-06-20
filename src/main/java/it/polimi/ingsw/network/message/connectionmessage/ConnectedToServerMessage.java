package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a message sent to the client indicating a successful connection to the server.
 * Inherits from the {@link MessageToClient} class.
 */
public class ConnectedToServerMessage extends MessageToClient{

    /**
     * Constructs ConnectedToServerMessage sent to client.
     * @param client The client associated with the message.
     */
    public ConnectedToServerMessage(Client client) {
        super(MessageType.CONNECTION_MSG);
    }

    /**
     * Executes the message on the provided view, triggering the onConnectedServerMessage callback.
     * @param view The view on which to execute the message.
     */
    @Override
    public void execute(View view){
        view.onConnectedServerMessage(this);
    }
}
