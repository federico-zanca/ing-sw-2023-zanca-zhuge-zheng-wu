package it.polimi.ingsw.model.gameboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

import java.io.Serializable;
/**
 Represents a square on a game board, characterized by its coordinates and item.
 */
public class Square implements Serializable {
    private static final long serialVersionUID = -5718430460179695362L;
    private ItemTile item;

    @JsonProperty("coords")
    private Coordinates coords;

    private boolean pickable = false;

    /**
     default Constructor of Square object.
     */
    public Square(){

    }

    /**
     Constructs a Square object with the specified row and column, setting item as empty.
     @param row the row coordinate of the square
     @param column the column coordinate of the square
     */
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

    /**
     Constructs a Square object with the specified coordinates and item type.
     @param inputCoords the coordinates of the square
     @param type the item type of the square
     */
    public Square(Coordinates inputCoords, ItemType type) {
        this.coords = inputCoords;
        this.item = new ItemTile(type);
    }

    /**
     Retrieves the item tile associated with the square.
     @return the item tile of the square
     */
    public ItemTile getItem() {
        return item;
    }

    /**
     Sets the item tile of the square.
     @param item the new item tile to be set
     */
    public void setItem(ItemTile item) {
        this.item = item;
    }

    /**
     Retrieves the coordinates of the square.
     @return the coordinates of the square
     */
    public Coordinates getCoordinates(){
        return coords;
    }

    /**
     Retrieves the row coordinate of the square.
     @return the row coordinate
     */
    public int getRow(){
        return coords.getRow();
    }

    /**
     Retrieves the column coordinate of the square.
     @return the column coordinate
     */
    public int getColumn(){
        return coords.getColumn();
    }

    /**
     Checks if the square is pickable.
     @return true if the square is pickable, false otherwise
     */
    public boolean isPickable() {
        return pickable;
    }

    /**
     Sets the pickable flag of the square.
     @param b the new pickable flag value
     */
    public void setPickable(boolean b){
        this.pickable = b;
    }
}
