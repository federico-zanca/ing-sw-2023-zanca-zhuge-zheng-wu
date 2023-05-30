package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.MessageType;

public class ChangeNumOfPlayersRequest extends MessageToServer {

    private final int chosenNum;
    public ChangeNumOfPlayersRequest(int chosenNum){
        super(MessageType.LOBBY_MSG);
        this.chosenNum = chosenNum;
    }

    public int getChosenNum() {
        return chosenNum;
    }

    /**
     * @param client
     * @param preGameController
     */
    @Override
    public void execute(Client client, PreGameController preGameController) {
        preGameController.onChangeNumOfPlayersRequest(client, this);
    }
}
