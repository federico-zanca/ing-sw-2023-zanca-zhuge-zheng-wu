package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a message sent to the client containing the bookshelf state.
 * Inherits from the {@link MessageToClient} class.
 */
public class BookshelfMessage extends MessageToClient {
    private final Bookshelf bookshelf;
    private final String username;
    /**
     * Constructs a new BookshelfMessage.
     * @param username The username of the player receiving the bookshelf message.
     * @param bookshelf The bookshelf state.
     */
    public BookshelfMessage(String username, Bookshelf bookshelf){
        super(MessageType.GAME_MSG);
        this.username = username;
        this.bookshelf = new Bookshelf (bookshelf);
    }
    /**
     * Gets the bookshelf state.
     * @return The bookshelf object representing the player's bookshelf.
     */
    public Bookshelf getBookshelf(){
        return bookshelf;
    }
    /**
     * Gets the username of the player receiving the bookshelf message.
     * @return The receiving player's username.
     */
    public String getUsername() {
        return username;
    }
    /**
     * Executes the message on the provided view, triggering the onBookshelfMessage callback.
     * @param view The view on which to execute the message.
     */
    @Override
    public void execute(View view) {
        view.onBookshelfMessage(this);
    }
}
