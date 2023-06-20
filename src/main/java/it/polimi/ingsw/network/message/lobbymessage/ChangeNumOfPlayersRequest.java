package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.MessageType;
/**
 * Represents a request message sent to the server to change the number of players in the lobby.
 * Inherits from the {@link MessageToServer} class.
 */
public class ChangeNumOfPlayersRequest extends MessageToServer {

    private final int chosenNum;
    /**
     * Constructs a new ChangeNumOfPlayersRequest message.
     *
     * @param chosenNum The number of players chosen for the lobby.
     */
    public ChangeNumOfPlayersRequest(int chosenNum){
        super(MessageType.LOBBY_MSG);
        this.chosenNum = chosenNum;
    }

    /**
     * Gets the chosen number of players for the lobby.
     *
     * @return The chosen number of players.
     */
    public int getChosenNum() {
        return chosenNum;
    }

    /**
     * Executes the request on the server, triggering the onChangeNumOfPlayersRequest callback in the pre-game controller.
     *
     * @param client The client sending the request.
     * @param preGameController  The pre-game controller handling the request.
     */
    @Override
    public void execute(Client client, PreGameController preGameController) {
        preGameController.onChangeNumOfPlayersRequest(client, this);
    }
}
