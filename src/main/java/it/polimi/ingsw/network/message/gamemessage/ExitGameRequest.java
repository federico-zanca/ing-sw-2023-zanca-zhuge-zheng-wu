package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.MessageType;
/**
 * Represents a request to exit the game.
 * Inherits from the {@link MessageToServer} class.
 */
public class ExitGameRequest extends MessageToServer {
    private final String username;
    /**
     * Constructs a newExitGameRequest message.
     * @param username The username of the player requesting to exit the game.
     */
    public ExitGameRequest(String username) {
        super(MessageType.GAME_MSG);
        this.username = username;
    }
    /**
     * Gets the username of the player requesting to exit the game.
     *
     * @return The username of the player.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Executes the message on the server.
     * @param client The client sending the message.
     * @param preGameController  The pre-game controller handling the message.
     */
    @Override
    public void execute(Client client, PreGameController preGameController) {
        System.err.println("Should not call this method: ExitGameRequest.execute()"); //TODO fai sparire quando è tempo di farlo
    }
}
