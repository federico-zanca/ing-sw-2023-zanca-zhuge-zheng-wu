package it.polimi.ingsw.network.message.lobbymessage;

public class ChangeNumOfPlayerRequest extends LobbyMessage {

    private final int chosenNum;
    public ChangeNumOfPlayerRequest(int chosenNum){
        super(LobbyMessageType.CHANGE_NUM_OF_PLAYER_REQUEST);
        this.chosenNum = chosenNum;
    }

    public int getChosenNum() {
        return chosenNum;
    }
}
