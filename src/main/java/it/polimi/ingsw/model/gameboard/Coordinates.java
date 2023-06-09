package it.polimi.ingsw.model.gameboard;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
/**
 * Represents a set of coordinates, used by Square.
 */
public class Coordinates implements Serializable {
    private static final long serialVersionUID = 3852798630854592187L;

    @JsonProperty("row")
    private int row;

    @JsonProperty("column")
    private int column;

    public Coordinates(){}

    /**
     * Constructs a new Coordinates object with the specified row and column values.
     * @param row the row value of the coordinates
     * @param column the column value of the coordinates
     */
    public Coordinates(int row, int column){
        this.row = row;
        this.column = column;
    }

    /**
     * Returns the row value of the coordinates.ù
     * @return the row value
     */
    public int getRow(){
        return row;
    }

    /**
     * Returns the column value of the coordinates.
     * @return the column value
     */
    public int getColumn(){
        return column;
    }

    /**
     * Sets the row value of the coordinates.
     * @param row the new row value
     */
    public int setRow(int row){
        this.row = row;
        return row;
    }

    /**
     * Sets the column value of the coordinates.
     * @param column the new column value
     */
    public int setColumn(int column){
        this.column = column;
        return column;
    }
}
