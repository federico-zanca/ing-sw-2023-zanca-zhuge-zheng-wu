package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.MessageType;

import java.rmi.RemoteException;

/**
 * Represents a request message sent to the server for user login.
 * Inherits from the {@link MessageToServer} class.
 */
public class LoginRequest extends MessageToServer {
    private final String username;

    /**
     * Constructs a new LoginRequest message.
     * @param username The username for the login request.
     */
    public LoginRequest(String username) {
        super(MessageType.CONNECTION_MSG);
        this.username = username;
    }

    /**
     * Retrieves the username associated with the login request.
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Executes the request on the server, triggering the onLoginRequest callback in the pre-game controller.
     * @param client The client sending the request.
     * @param preGameController  The pre-game controller handling the request.
     */
    @Override
    public void execute(Client client, PreGameController preGameController) throws RemoteException {
        preGameController.onLoginRequest(client, this);
    }
}
