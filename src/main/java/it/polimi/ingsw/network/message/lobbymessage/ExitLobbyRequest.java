package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.MessageType;
/**
 * Represents a request message sent to the server to exit the lobby.
 * Inherits from the {@link MessageToServer} class.
 */
public class ExitLobbyRequest extends MessageToServer {
    /**
     * Constructs a new ExitLobbyRequest message.
     */
    public ExitLobbyRequest() {
        super(MessageType.LOBBY_MSG);
    }

    /**
     * Executes the request on the server, triggering the onExitLobbyRequest callback in the pre-game controller.
     *
     * @param client  The client sending the request.
     * @param preGameController  The pre-game controller handling the request.
     */
    public void execute(Client client, PreGameController preGameController){
        preGameController.onExitLobbyRequest(client, this);
    }
}
