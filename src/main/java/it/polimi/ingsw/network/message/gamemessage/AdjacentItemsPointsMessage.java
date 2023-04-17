package it.polimi.ingsw.network.message.gamemessage;

public class AdjacentItemsPointsMessage extends GameMessage {
    private final int points;

    public AdjacentItemsPointsMessage(String username, int points) {
        super(username, GameMessageType.ADJACENT_ITEMS_POINTS);
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
