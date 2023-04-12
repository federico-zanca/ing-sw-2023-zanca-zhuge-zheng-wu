package it.polimi.ingsw.model.gameboard;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private static final long serialVersionUID = 3852798630854592187L;
    private final int row;

    private final int column;

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
}
