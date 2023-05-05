package it.polimi.ingsw.model.commongoals;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShapeCoordinates {
        private final int row;
        private final int column;

        public ShapeCoordinates(@JsonProperty("row")int row, @JsonProperty("col")int column){
            this.row = row;
            this.column = column;
        }
        public int getRow(){
            return row;
        }
        public int getColumn() {return column;}
}
