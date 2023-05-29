package it.polimi.ingsw.network.message.gamemessage;

public class ErrorMessage extends GameMessage {

    private final String content;
    public ErrorMessage(String username, String content) {
        super(username, GameMessageType.ERROR);
        this.content=content;
    }

    public String getContent() {
        return content;
    }
}
