package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.ItemType;

public class ItemTile {
    private ItemType type;

    private int GroupId;

    public ItemTile(ItemType type){
        this.type = type;
    }
    public void setType(ItemType type) {
        this.type = type;
    }

    public void setGroupId(int GroupId){ this.GroupId = GroupId; }
    public int getGroupId(){ return GroupId; }

    public ItemType getType() {
        return type;
    }
}
