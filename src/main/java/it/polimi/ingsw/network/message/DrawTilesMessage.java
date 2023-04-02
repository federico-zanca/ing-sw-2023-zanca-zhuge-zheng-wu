package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.gameboard.Coordinates;
import it.polimi.ingsw.model.gameboard.Square;

import java.util.ArrayList;

public class DrawTilesMessage extends Message {
    private final ArrayList<Square> squares;

    public DrawTilesMessage(String username, ArrayList<Square> squares) {
        super(username, MessageType.DRAW_TILES);
        this.squares = squares;
    }

    public ArrayList<Square> getSquares() {
        return squares;
    }
}
