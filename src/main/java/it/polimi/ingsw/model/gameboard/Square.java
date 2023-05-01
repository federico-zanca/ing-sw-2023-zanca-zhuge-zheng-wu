package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

import java.io.Serializable;

public class Square implements Serializable {
    private static final long serialVersionUID = -5718430460179695362L;
    private ItemTile item;

    private final Coordinates coords;

    private boolean pickable = false;
    public Square(int row, int column){
        this.coords = new Coordinates(row, column);
        this.item = new ItemTile(ItemType.EMPTY);
    }

    /**
     * This is a copy constructor used to make deep copies of the squares in board.
     * @param other
     */
    public Square(Square other){
        this.coords = other.coords;
        this.item = other.item;
    }

    public Square(Coordinates inputCoords, ItemType type) {
        this.coords = inputCoords;
        this.item = new ItemTile(type);
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

    public boolean isPickable() {
        return pickable;
    }
    public void setPickable(boolean b){
        this.pickable = b;
    }
}
