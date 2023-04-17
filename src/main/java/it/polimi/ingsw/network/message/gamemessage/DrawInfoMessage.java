package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.Message;

public class DrawInfoMessage extends GameMessage {
    private final int maxNumItems;
    private final ItemTile[][] bookshelf;
    private final Square[][] board;

    public DrawInfoMessage(String username, Square[][] board, ItemTile[][] bookshelf, int maxNumTiles){
        super(username, GameMessageType.DRAW_INFO);
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
