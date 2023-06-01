package it.polimi.ingsw.model.gameboard;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private static final long serialVersionUID = 3852798630854592187L;

    @JsonProperty("row")
    private int row;

    @JsonProperty("column")
    private int column;

    public Coordinates(){}
    public Coordinates(int row, int column){
        this.row = row;
        this.column = column;
    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }

    public int setRow(int row){
        this.row = row;
        return row;
    }

    public int setColumn(int column){
        this.column = column;
        return column;
    }
}
