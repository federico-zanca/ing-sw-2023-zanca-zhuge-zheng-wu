package it.polimi.ingsw.model.personalgoals;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.enumerations.ItemType;

public class PersonalGoalCell {
    private int row;
    private int col;
    private ItemType type;

    @JsonCreator
    public PersonalGoalCell (@JsonProperty("row")int row, @JsonProperty("col")int col, @JsonProperty("type")ItemType type){
        this.row = row;
        this.col = col;
        this.type = type;
    }

    public int getRow (){
        return row;
    }
    public int getCol (){
        return col;
    }
    public ItemType getType (){
        return type;
    }
}
