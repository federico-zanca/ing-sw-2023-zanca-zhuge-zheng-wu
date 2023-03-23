package it.polimi.ingsw.model.personalgoals;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

public class PersonalGoalCard9 extends PersonalGoalCard{
    public PersonalGoalCard9(){
        objective = new ItemType[Rows][Columns];
        for(int i=0; i<Rows; i++){
            for(int j=0; j<Columns; j++){
                objective[i][j] = ItemType.EMPTY;
            }
        }
        objective[0][2] = ItemType.GAME;
        objective[2][2] = ItemType.CAT;
        objective[3][4] = ItemType.BOOK;
        objective[4][1] = ItemType.TROPHY;
        objective[4][4] = ItemType.PLANT;
        objective[5][0] = ItemType.FRAME;
    }
    @Override
    public int calculateScore(ItemTile[][] matrix) {
        int score=0;

        if(matrix[0][2].getType() == objective[0][2]){ score = score + 1;}
        if(matrix[2][2].getType() == objective[2][2]){ score = score + 1;}
        if(matrix[3][4].getType() == objective[3][4]){ score = score + 1;}
        if(matrix[4][1].getType() == objective[4][1]){ score = score + 1;}
        if(matrix[4][4].getType() == objective[4][4]){ score = score + 1;}
        if(matrix[5][0].getType() == objective[5][0]){ score = score + 1;}

        if(score==1 || score==2) {return score;}
        else if (score==3) {return score+1;}
        else if (score==4) {return score+2;}
        else if (score==5) {return score+4;}
        else if (score==6) {return score*2;}
        else {return score;}
    }
}