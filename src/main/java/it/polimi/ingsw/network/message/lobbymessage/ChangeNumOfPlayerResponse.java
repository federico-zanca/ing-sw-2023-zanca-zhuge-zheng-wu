package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.view.View;

public class ChangeNumOfPlayerResponse extends LobbyMessage implements MessageToClient {

    private final boolean successful;
    private final String content;
    private Integer choseNum;

    public ChangeNumOfPlayerResponse(boolean successful, String content, int chosenNum) {
        super(LobbyMessageType.CHANGE_NUM_OF_PLAYER_RESPONSE);
        this.successful = successful;
        this.content = content;
        this.choseNum = chosenNum;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getContent() {
        return content;
    }
    public Integer getChosenNum(){return choseNum;}

    @Override
    public void execute(View view) {
        view.onChangeNumOfPlayerResponse(this);
    }
}
