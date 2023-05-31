package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public class InsertInfoMessage extends MessageToClient {
    private final Bookshelf bookshelf;
    private final ArrayList<ItemTile> hand;
    private final ArrayList<Integer> insertableColumns;
    private final String username;

    public InsertInfoMessage(String username, ArrayList<ItemTile> hand, Bookshelf bookshelf, ArrayList<Integer> insertableColumns){
        super(MessageType.GAME_MSG);
        this.username = username;
        this.bookshelf = new Bookshelf(bookshelf);
        this.hand = hand;
        this.insertableColumns = insertableColumns;
    }

    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    public ArrayList<ItemTile> getHand() {
        return hand;
    }

    public ArrayList<Integer> getEnabledColumns() {
        return insertableColumns;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void execute(View view) {
        view.onInsertInfoMessage(this);
    }
}
