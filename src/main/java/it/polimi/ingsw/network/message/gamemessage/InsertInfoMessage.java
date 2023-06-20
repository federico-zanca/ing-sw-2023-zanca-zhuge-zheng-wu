package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
/**
 * Represents a message containing information about the insertion phase of the game.
 * Inherits from the {@link MessageToClient} class.
 */
public class InsertInfoMessage extends MessageToClient {
    private final Bookshelf bookshelf;
    private final ArrayList<ItemTile> hand;
    private final ArrayList<Integer> insertableColumns;
    private final String username;
    /**
     * Constructs a new InsertInfoMessage.
     * @param username   The username of the player.
     * @param hand   The list of item tiles in the player's hand.
     * @param bookshelf  The bookshelf of the player.
     * @param insertableColumns The list of columns in the bookshelf where tiles can be inserted.
     */
    public InsertInfoMessage(String username, ArrayList<ItemTile> hand, Bookshelf bookshelf, ArrayList<Integer> insertableColumns){
        super(MessageType.GAME_MSG);
        this.username = username;
        this.bookshelf = new Bookshelf(bookshelf);
        this.hand = hand;
        this.insertableColumns = insertableColumns;
    }
    /**
     * Gets the bookshelf of the player.
     *
     * @return The bookshelf.
     */
    public Bookshelf getBookshelf() {
        return bookshelf;
    }
    /**
     * Gets the list of item tiles in the player's hand.
     *
     * @return The list of item tiles.
     */
    public ArrayList<ItemTile> getHand() {
        return hand;
    }
    /**
     * Gets the list of columns in the bookshelf where tiles can be inserted.
     *
     * @return The list of insertable columns.
     */
    public ArrayList<Integer> getEnabledColumns() {
        return insertableColumns;
    }

    /**
     * Gets the username of the player.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }
    /**
     * Executes the message on the client's view.
     *
     * @param view The view that handles the message.
     */
    @Override
    public void execute(View view) {
        view.onInsertInfoMessage(this);
    }
}
