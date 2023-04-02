package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.gameboard.Square;

public class DrawInfoMessage extends Message {
    private final int maxNumItems;
    private final ItemTile[][] bookshelf;
    private final Square[][] board;

    public DrawInfoMessage(String username, Square[][] board, ItemTile[][] bookshelf, int maxNumTiles){
        super(username, MessageType.DRAW_INFO);
        this.bookshelf = bookshelf;
        this.board = board;
        this.maxNumItems = maxNumTiles;
    }

    public ItemTile[][] getBookshelf() {
        return bookshelf;
    }

    public int getMaxNumItems() {
        return maxNumItems;
    }

    public Square[][] getBoard() {
        return board;
    }
}
