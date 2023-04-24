package it.polimi.ingsw.network.message.gamemessage;

public class PlayerLeftMessage extends GameMessage {
    private final String content;

    public PlayerLeftMessage(String username, String content) {
        super(username, GameMessageType.PLAYER_LEFT);
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
