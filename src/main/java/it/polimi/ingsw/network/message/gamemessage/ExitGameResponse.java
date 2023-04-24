package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.Message;

public class ExitGameResponse extends GameMessage {

    private final String content;

    public ExitGameResponse(String username, String content) {
        super(username, GameMessageType.EXIT_GAME_RESPONSE );
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
