package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.gamemessage.GameMessageType;

import java.util.ArrayList;

public class InsertInfoMessage extends GameMessage {
    private final ItemTile[][] shelfie;
    private final ArrayList<ItemTile> hand;
    private final ArrayList<Integer> insertableColumns;

    public InsertInfoMessage(String username, ArrayList<ItemTile> hand, ItemTile[][] shelfie, ArrayList<Integer> insertableColumns){
        super(username, GameMessageType.INSERT_INFO);
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
