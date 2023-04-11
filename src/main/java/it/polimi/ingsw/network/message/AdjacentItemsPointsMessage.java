package it.polimi.ingsw.network.message;

public class AdjacentItemsPointsMessage extends Message {
    private final int points;

    public AdjacentItemsPointsMessage(String username, int points) {
        super(username, MessageType.ADJACENT_ITEMS_POINTS);
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
