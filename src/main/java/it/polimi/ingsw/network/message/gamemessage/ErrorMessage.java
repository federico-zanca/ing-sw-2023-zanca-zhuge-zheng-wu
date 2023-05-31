package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

public class ErrorMessage extends MessageToClient {

    private final String content;
    public ErrorMessage(String username, String content) {
        super(MessageType.GAME_MSG);
        this.content=content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public void execute(View view) {
        System.out.println(content);
    }
}
