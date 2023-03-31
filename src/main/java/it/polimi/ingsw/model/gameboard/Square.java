package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

public class Square {
    private ItemTile item;

    private final Coordinates coords;

    private boolean pickable = false;
    public Square(int row, int column){
        this.coords = new Coordinates(row, column);
        this.item = new ItemTile(ItemType.EMPTY);
    }

    public ItemTile getItem() {
        return item;
    }

    public void setItem(ItemTile item) {
        this.item = item;
    }

    public Coordinates getCoordinates(){
        return coords;
    }

    public int getRow(){
        return coords.getRow();
    }

    public int getColumn(){
        return coords.getColumn();
    }

    public void setPickable(boolean b){
        this.pickable = b;
    }
}
