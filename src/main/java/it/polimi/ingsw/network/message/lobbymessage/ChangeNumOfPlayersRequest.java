package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;

public class ChangeNumOfPlayersRequest extends LobbyMessage {

    private final int chosenNum;
    public ChangeNumOfPlayersRequest(int chosenNum){
        super(LobbyMessageType.CHANGE_NUM_OF_PLAYERS_REQUEST);
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
