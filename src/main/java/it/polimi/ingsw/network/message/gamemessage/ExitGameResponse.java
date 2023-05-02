package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.view.View;

public class ExitGameResponse extends GameMessage implements MessageToClient {

    private final String content;

    public ExitGameResponse(String username, String content) {
        super(username, GameMessageType.EXIT_GAME_RESPONSE );
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public void execute(View view) {
        view.onExitGameResponse(this);
    }
}
