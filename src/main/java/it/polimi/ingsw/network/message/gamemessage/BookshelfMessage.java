package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

public class BookshelfMessage extends MessageToClient {
    private final Bookshelf bookshelf;
    private final String username;

    public BookshelfMessage(String username, Bookshelf bookshelf){
        super(MessageType.GAME_MSG);
        this.username = username;
        this.bookshelf = new Bookshelf (bookshelf);
    }

    public Bookshelf getBookshelf(){
        return bookshelf;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void execute(View view) {
        view.onBookshelfMessage(this);
    }
}
