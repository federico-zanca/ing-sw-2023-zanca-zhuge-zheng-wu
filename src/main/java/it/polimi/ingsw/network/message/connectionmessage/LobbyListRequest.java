package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.MessageType;
/**
 * Represents a request message sent to the server to get the list of available lobbies.
 * Inherits from the {@link MessageToServer} class.
 */
public class LobbyListRequest extends MessageToServer {

    /**
     * Constructs a new LobbyListRequest message.
     */
    public LobbyListRequest() {
        super(MessageType.CONNECTION_MSG);
    }

    /**
     * Executes the request on the server, triggering the onLobbyListRequest callback in the pre-game controller.
     * @param client The client sending the request.
     * @param preGameController  The pre-game controller handling the request.
     */
    @Override
    public void execute(Client client, PreGameController preGameController) {
        preGameController.onLobbyListRequest(client, this);
    }
}
