package it.polimi.ingsw.model.gameboard;

public class Coordinates {
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
