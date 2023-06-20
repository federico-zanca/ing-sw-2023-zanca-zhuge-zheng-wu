package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.MessageType;
/**
 * Represents a request message sent to the server to create a lobby.
 * Inherits from the {@link MessageToServer} class.
 */
public class CreateLobbyRequest extends MessageToServer {
    private final String lobbyName;

    /**
     * Constructs a new CreateLobbyRequest message.
     * @param lobbyName The name of the lobby to be created.
     */
    public CreateLobbyRequest(String lobbyName) {
        super(MessageType.CONNECTION_MSG);
        this.lobbyName = lobbyName;
    }

    /**
     * Retrieves the name of the lobby.
     * @return The name of the lobby.
     */
    public String getLobbyName() {
        return lobbyName;
    }

    /**
     * Executes the request on the server, triggering the onCreateLobbyRequest callback in the pre-game controller.
     * @param client  The client sending the request.
     * @param preGameController  The pre-game controller handling the request.
     */
    @Override
    public void execute(Client client, PreGameController preGameController) {
        preGameController.onCreateLobbyRequest(client, this);
    }
}
