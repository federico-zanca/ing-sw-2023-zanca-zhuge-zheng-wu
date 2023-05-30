package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

public class ChangeNumOfPlayerResponse extends MessageToClient {

    private final boolean successful;
    private final String content;
    private final Integer choseNum;

    public ChangeNumOfPlayerResponse(boolean successful, String content, int chosenNum) {
        super(MessageType.LOBBY_MSG);
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
