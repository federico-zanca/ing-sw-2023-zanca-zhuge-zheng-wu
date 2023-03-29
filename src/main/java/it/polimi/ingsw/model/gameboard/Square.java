package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

public class Square {
    private ItemTile item;

    public Square(){
        this.item = new ItemTile(ItemType.EMPTY);
    }

    public ItemTile getItem() {
        return item;
    }

    public void setItem(ItemTile item) {
        this.item = item;
    }


}
