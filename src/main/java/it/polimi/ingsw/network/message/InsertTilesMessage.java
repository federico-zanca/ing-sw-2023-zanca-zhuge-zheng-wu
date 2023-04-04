package it.polimi.ingsw.network.message;
import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.gameboard.Square;

import java.util.ArrayList;
public class InsertTilesMessage extends Message{
    private final ArrayList<Square> squares;

    private final Bookshelf bookshelf;

    private final int column;

    public InsertTilesMessage(String username, ArrayList<Square> squares, Bookshelf bookshelf, int column){
        super(username, MessageType.INSERT_TILES);
        this.squares = squares;
        this.bookshelf = bookshelf;
        this.column = column;
    }

    public ArrayList<Square> getSquares() {
        return squares;
    }
    public Bookshelf getBookshelf() {
        return bookshelf;
    }
    public int getColumn() {
        return column;
    }
}
