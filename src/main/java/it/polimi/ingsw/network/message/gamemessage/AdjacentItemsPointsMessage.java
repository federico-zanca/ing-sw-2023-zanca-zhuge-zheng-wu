package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

public class AdjacentItemsPointsMessage extends MessageToClient {
    private final int points;
    private final String playerUsername;
    private final String username;

    public AdjacentItemsPointsMessage(String username, String playerUsername, int points) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.playerUsername = playerUsername;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public String getPlayerUsername() {
        return playerUsername;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void execute(View view) {
        view.onAdjacentItemsPointsMessage(this);
    }
}
