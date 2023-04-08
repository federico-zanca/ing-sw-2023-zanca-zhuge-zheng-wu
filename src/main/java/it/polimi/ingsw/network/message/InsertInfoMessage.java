package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.ItemTile;

import java.util.ArrayList;

public class InsertInfoMessage extends Message {
    private final ItemTile[][] shelfie;
    private final ArrayList<ItemTile> hand;
    private final ArrayList<Integer> insertableColumns;

    public InsertInfoMessage(String username, ArrayList<ItemTile> hand, ItemTile[][] shelfie, ArrayList<Integer> insertableColumns){
        super(username, MessageType.INSERT_INFO);
        this.shelfie = shelfie;
        this.hand = hand;
        this.insertableColumns = insertableColumns;
    }

    public ItemTile[][] getShelfie() {
        return shelfie;
    }

    public ArrayList<ItemTile> getHand() {
        return hand;
    }

    public ArrayList<Integer> getEnabledColumns() {
        return insertableColumns;
    }
}
