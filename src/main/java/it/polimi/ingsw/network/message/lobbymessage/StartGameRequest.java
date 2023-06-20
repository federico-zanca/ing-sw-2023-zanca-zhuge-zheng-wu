package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.MessageType;
/**
 * Represents a request from the client to start the game.
 * Inherits from the {@link MessageToServer} class.
 */
public class StartGameRequest extends MessageToServer {
    /**
     * Constructs a new StartGameRequest message.
     */
    public StartGameRequest() {
        super(MessageType.LOBBY_MSG);
    }

    /**
     * Executes the message on the provided client and pre-game controller, triggering the onStartGameRequest callback.
     *
     * @param client  The client on which to execute the message.
     * @param preGameController The pre-game controller on which to execute the message.
     */
    @Override
    public void execute(Client client, PreGameController preGameController) {
        preGameController.onStartGameRequest(client, this);
    }
}
