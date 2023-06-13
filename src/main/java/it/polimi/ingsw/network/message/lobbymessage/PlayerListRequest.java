package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.MessageType;

/**
 * Represents a request from the client to get the list of players in the lobby.
 * Inherits from the {@link MessageToServer} class.
 */
public class PlayerListRequest extends MessageToServer {
    /**
     * Constructs a new PlayerListRequest.
     */
    public PlayerListRequest() {super(MessageType.LOBBY_MSG);}

    /**
     * Executes the message on the provided client and preGameController, triggering the onPlayerListRequest callback.
     *
     * @param client The client on which to execute the message.
     * @param preGameController   The preGameController on which to execute the message.
     */
    @Override
    public void execute(Client client, PreGameController preGameController) {
        preGameController.onPlayerListRequest(client, this);
    }
}
