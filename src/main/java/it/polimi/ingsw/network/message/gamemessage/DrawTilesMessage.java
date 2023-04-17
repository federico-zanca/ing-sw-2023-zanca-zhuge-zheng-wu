package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.Message;

import java.util.ArrayList;

public class DrawTilesMessage extends GameMessage {
    private final ArrayList<Square> squares;

    public DrawTilesMessage(String username, ArrayList<Square> squares) {
        super(username, GameMessageType.DRAW_TILES);
        this.squares = squares;
    }

    public ArrayList<Square> getSquares() {
        return squares;
    }
}
