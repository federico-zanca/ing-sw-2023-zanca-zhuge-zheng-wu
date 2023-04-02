package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.gameboard.Coordinates;

public class DrawTilesMessage extends Message {
    private final Coordinates coords;
    public DrawTilesMessage(String username, Coordinates coords){
        super(username, MessageType.DRAW_TILES);
        this.coords=coords;
    }


    public Coordinates getCoords() {
        return coords;
    }
}
