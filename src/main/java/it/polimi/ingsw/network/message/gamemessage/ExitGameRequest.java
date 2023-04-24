package it.polimi.ingsw.network.message.gamemessage;

public class ExitGameRequest extends GameMessage {
    public ExitGameRequest(String username) {
        super(username, GameMessageType.EXIT_GAME_REQUEST);
    }
}
