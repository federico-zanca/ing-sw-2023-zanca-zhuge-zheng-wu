package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.view.View;

import java.awt.print.Book;

public class BookshelfMessage extends GameMessage implements MessageToClient {
    private final Bookshelf bookshelf;

    public BookshelfMessage(String username, Bookshelf bookshelf){
        super(username, GameMessageType.BOOKSHELF);
        this.bookshelf = new Bookshelf (bookshelf);
    }

    public Bookshelf getBookshelf(){
        return bookshelf;
    }

    @Override
    public void execute(View view) {
        view.onBookshelfMessage(this);
    }
}
