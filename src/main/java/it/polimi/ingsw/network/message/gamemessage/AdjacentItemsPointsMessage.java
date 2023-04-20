package it.polimi.ingsw.network.message.gamemessage;

public class AdjacentItemsPointsMessage extends GameMessage {
    private final int points;
    private final String playerUsername;

    public AdjacentItemsPointsMessage(String username, String playerUsername, int points) {
        super(username, GameMessageType.ADJACENT_ITEMS_POINTS);
        this.playerUsername = playerUsername;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public String getPlayerUsername() {
        return playerUsername;
    }
}
