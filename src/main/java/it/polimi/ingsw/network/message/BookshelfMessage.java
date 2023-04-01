package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;

public class BookshelfMessage extends Message{
    private final ItemTile[][] bookshelf;

    public BookshelfMessage(String username, ItemTile[][] bookshelf){
        super(username,MessageType.BOOKSHELF);
        this.bookshelf = bookshelf;
    }

    public ItemTile[][] getBookshelf(){
        return bookshelf;
    }
}
