package it.polimi.ingsw.network.message;
import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.gameboard.Square;

import java.util.ArrayList;
public class InsertTilesMessage extends Message{
    private final ArrayList<Square> squares;

    private final Bookshelf bookshelf;

    public InsertTilesMessage(String username, ArrayList<Square> squares, Bookshelf bookshelf){
        super(username, MessageType.INSERT_TILES);
        this.squares = squares;
        this.bookshelf = bookshelf;
    }
    public ArrayList<Square> getSquares() {
        return squares;
    }
    public Bookshelf getBookshelf() {
        return bookshelf;
    }
}
