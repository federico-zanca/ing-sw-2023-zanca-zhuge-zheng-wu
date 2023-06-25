package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.MessageType;

import java.rmi.RemoteException;

/**
 * Represents a request message sent to the server to join a lobby.
 * Inherits from the {@link MessageToServer} class.
 */
public class JoinLobbyRequest extends MessageToServer {
    private final String lobbyName;

    /**
     * Constructs a new JoinLobbyRequest message with the specified lobby name.
     * @param lobbyName The name of the lobby to join.
     */
    public JoinLobbyRequest(String lobbyName) {
        super(MessageType.CONNECTION_MSG);
        this.lobbyName = lobbyName;
    }

    /**
     * Retrieves the name of the lobby to join.
     * @return The name of the lobby.
     */
    public String getLobbyName() {
        return lobbyName;
    }


    /**
     * Executes the request on the server, triggering the onJoinLobbyRequest callback in the pre-game controller.
     * @param client  The client sending the request.
     * @param preGameController  The pre-game controller handling the request.
     */
    @Override
    public void execute(Client client, PreGameController preGameController) throws RemoteException {
        preGameController.onJoinLobbyRequest(client, this);
    }
}
