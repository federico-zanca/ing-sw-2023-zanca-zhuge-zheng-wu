package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.ItemType;

public class ItemTile {
    private ItemType type;

    public ItemTile(ItemType type){
        this.type = type;
    }
    public void setType(ItemType type) {
        this.type = type;
    }

    public ItemType getType() {
        return type;
    }
}
