package it.polimi.ingsw.model.personalgoals;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

public class PersonalGoalCard10 extends PersonalGoalCard{
    public PersonalGoalCard10(){
        objective = new ItemType[Rows][Columns];
        for(int i=0; i<Rows; i++){
            for(int j=0; j<Columns; j++){
                objective[i][j] = ItemType.EMPTY;
            }
        }
        objective[0][4] = ItemType.TROPHY;
        objective[1][1] = ItemType.GAME;
        objective[2][0] = ItemType.BOOK;
        objective[3][3] = ItemType.CAT;
        objective[4][1] = ItemType.FRAME;
        objective[5][3] = ItemType.PLANT;
    }
    @Override
    public int calculateScore(ItemTile[][] matrix) {
        int score=0;

        if(matrix[0][4].getType() == objective[0][4]){ score = score + 1;}
        if(matrix[1][1].getType() == objective[1][1]){ score = score + 1;}
        if(matrix[2][0].getType() == objective[2][0]){ score = score + 1;}
        if(matrix[3][3].getType() == objective[3][3]){ score = score + 1;}
        if(matrix[4][1].getType() == objective[4][1]){ score = score + 1;}
        if(matrix[5][3].getType() == objective[5][3]){ score = score + 1;}

        if(score==1 || score==2) {return score;}
        else if (score==3) {return score+1;}
        else if (score==4) {return score+2;}
        else if (score==5) {return score+4;}
        else if (score==6) {return score*2;}
        else {return score;}
    }
}
