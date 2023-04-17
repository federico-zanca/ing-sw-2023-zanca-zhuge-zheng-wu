package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.ItemTile;

public class BookshelfMessage extends GameMessage {
    private final ItemTile[][] bookshelf;

    public BookshelfMessage(String username, ItemTile[][] bookshelf){
        super(username, GameMessageType.BOOKSHELF);
        this.bookshelf = bookshelf;
    }

    public ItemTile[][] getBookshelf(){
        return bookshelf;
    }
}
