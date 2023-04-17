package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.gamemessage.GameMessageType;

import java.util.ArrayList;
public class InsertTilesMessage extends GameMessage {
    private final ArrayList<ItemTile> items;


    private final int column;

    public InsertTilesMessage(String username, ArrayList<ItemTile> items, int column){
        super(username, GameMessageType.INSERT_TILES);
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
