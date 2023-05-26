package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.ItemType;

import java.io.Serializable;

public class ItemTile implements Serializable {
    private static final long serialVersionUID = 4467821091195695300L;
    private ItemType type;

    private int GroupId;

    public ItemTile(ItemType type){
        this.type = type;
        this.GroupId = 0;
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

    public boolean isEmpty(){
        return getType()==ItemType.EMPTY;
    }

    public boolean isForbidden(){
        return getType()==ItemType.FORBIDDEN;
    }

    public boolean hasSomething(){
        return !(getType()==ItemType.FORBIDDEN || getType()==ItemType.EMPTY);
    }

/*
    public String toColorString(){
        StringBuilder stringBuilder = new StringBuilder();
        TextColor c;
        switch(type){
            case CAT :
                c=TextColor.GREEN;
                break;
            case PLANT:
                c=TextColor.RED;
                break;
            case FRAME:
                c=TextColor.BLUE;
                break;
            case GAME:
                c=TextColor.YELLOW_BOLD;
                break;
            case TROPHY:
                c=TextColor.CYAN_BOLD;
                break;
            case BOOK:
                c=TextColor.WHITE;
                break;
            default:
                c=TextColor.NO_COLOR;
                break;
        }
        return stringBuilder.append(c).append(type).append(TextColor.NO_COLOR).toString();
    }

 */
    @Override
    public String toString(){
        return type.toString();
    }
}
