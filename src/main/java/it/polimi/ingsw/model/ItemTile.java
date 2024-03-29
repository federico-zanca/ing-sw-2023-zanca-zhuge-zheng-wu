package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.ItemType;

import java.io.Serializable;
/**
 * Represents an item tile.
 */
public class ItemTile implements Serializable {
    private static final long serialVersionUID = 4467821091195695300L;
    private ItemType type;
    private int imageId;
    private int GroupId;

    public ItemTile(){}
    /**
     * Constructs an ItemTile with the specified ItemType.
     * The GroupId is set to 0 by default.
     * @param type the type of the item
     */
    public ItemTile(ItemType type){
        this.type = type;
        this.GroupId = 0;
    }

    public void setImageId(int id){
        this.imageId = id;
    }
    public int getImageId(){
        return this.imageId;
    }

    /**
     * Sets the type of this to the passed parameter
     * @param type the ItemType to be set
     */
    public void setType(ItemType type) {
        this.type = type;
    }

    /**
     * Sets GroupId attribute of this to the passed parameter
     * @param GroupId the GroupId you want to set the ItemTile to
     */
    public void setGroupId(int GroupId){ this.GroupId = GroupId; }

    /**
     * Returns the GroupID of the object
     * @return the GroupID of the object
     */
    public int getGroupId(){ return GroupId; }

    /**
     * Returns this.type
     * @return this.type
     */
    public ItemType getType() {
        return type;
    }

    /**
     * Checks if the ItemTile is empty.
     * @return true if the ItemTile is empty, false otherwise
     */
    public boolean isEmpty(){
        return getType()==ItemType.EMPTY;
    }

    /**
     * Checks if the ItemTile is forbidden.
     * @return true if the ItemTile is forbidden, false otherwise
     */
    public boolean isForbidden(){
        return getType()==ItemType.FORBIDDEN;
    }

    /**
     * Checks if the ItemTile is not empty/forbidden.
     * @return true if the ItemTile is not empty/forbidden, false otherwise
     */
    public boolean hasSomething(){
        return !(getType()==ItemType.FORBIDDEN || getType()==ItemType.EMPTY);
    }

@Override
    public String toString(){
        return type.toString();
    }
}
