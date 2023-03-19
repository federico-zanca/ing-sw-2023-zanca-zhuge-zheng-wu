package it.polimi.ingsw.model.CommonGoalCards;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

public class CommonGoalCard1 extends CommonGoalCard {

    public CommonGoalCard1(int numPlayers){
        super(numPlayers);
    }
    public boolean check(ItemTile[][] matrix){
        int counter = 0;
        for(int i=0;i<6;i++){
            for(int j=0;j<5;j++){
                if(matrix[i][j].getGroupId() == 0 && !(matrix[i][j].getType() == ItemType.EMPTY)){

                    if(matrix[i][j].getType().equals(matrix[i+1][j].getType()) && i != 5){
                        counter = counter + 1;
                        matrix[i][j].setGroupId(counter);
                        matrix[i+1][j].setGroupId(counter);
                    }else if(matrix[i][j].getType().equals(matrix[i][j+1].getType()) && j != 4){
                        counter = counter + 1;
                        matrix[i][j].setGroupId(counter);
                        matrix[i][j+1].setGroupId(counter);
                    }
                }
                if(counter == 6){
                    return true;
                }
            }
        }
        return false;
    }
}