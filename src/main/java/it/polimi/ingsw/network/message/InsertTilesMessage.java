package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.ItemTile;

import java.util.ArrayList;
public class InsertTilesMessage extends Message{
    private final ArrayList<ItemTile> items;


    private final int column;

    public InsertTilesMessage(String username, ArrayList<ItemTile> items, int column){
        super(username, MessageType.INSERT_TILES);
        this.items = items;
        this.column = column;
    }

    public ArrayList<ItemTile> getItems() {
        return items;
    }
    public int getColumn() {
        return column;
    }
}
