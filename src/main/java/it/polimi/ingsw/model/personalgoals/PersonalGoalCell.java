package it.polimi.ingsw.model.personalgoals;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.enumerations.ItemType;

import java.io.Serializable;

/**
 * Represents a cell in a personal goal card grid.
 * Each cell contains a row index, column index, and an ItemType value.
 */
public class PersonalGoalCell implements Serializable {
    private static final long serialVersionUID = 6427095625839280799L;
    private int row;
    private int col;
    private ItemType type;

    /**
     * Constructs a PersonalGoalCell object with the specified row, column, and ItemType.
     * @param row  the row index of the cell
     * @param col  the column index of the cell
     * @param type the ItemType value of the cell
     */
    @JsonCreator
    public PersonalGoalCell (@JsonProperty("row")int row, @JsonProperty("col")int col, @JsonProperty("type")ItemType type){
        this.row = row;
        this.col = col;
        this.type = type;
    }

    /**
     * Retrieves the row index of the cell.
     * @return the row index of the cell
     */
    public int getRow (){
        return row;
    }

    /**
     * Retrieves the column index of the cell.
     * @return the column index of the cell
     */
    public int getCol (){
        return col;
    }

    /**
     * Retrieves the ItemType value of the cell.
     * @return the ItemType value of the cell
     */
    public ItemType getType (){
        return type;
    }
}
