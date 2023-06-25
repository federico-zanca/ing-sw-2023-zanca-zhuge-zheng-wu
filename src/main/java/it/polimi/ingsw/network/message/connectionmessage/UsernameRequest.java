package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.MessageType;

import java.rmi.RemoteException;

/**
 * Represents a request message sent to the server for changing the username.
 * Inherits from the {@link MessageToServer} class.
 */
public class UsernameRequest extends MessageToServer {
    private final String username;
    /**
     * Constructs a new UsernameRequest message .
     *
     * @param username The username requested by the client.
     */
    public UsernameRequest(String username) {
        super(MessageType.CONNECTION_MSG);
        this.username = username;
    }

    /**
     * Retrieves the new username.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Executes the request on the server, triggering the onUsernameRequest callback in the pre-game controller.
     *
     * @param client  The client sending the request.
     * @param preGameController  The pre-game controller handling the request.
     */
    @Override
    public void execute(Client client, PreGameController preGameController) throws RemoteException {
        preGameController.onUsernameRequest(client, this);
    }
}
