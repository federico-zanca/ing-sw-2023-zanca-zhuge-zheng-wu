package it.polimi.ingsw.model.personalgoals;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

public class PersonalGoalCard7 extends PersonalGoalCard{

    public PersonalGoalCard7(){
        objective = new ItemType[Rows][Columns];
        for(int i=0; i<Rows; i++){
            for(int j=0; j<Columns; j++){
                objective[i][j] = ItemType.EMPTY;
            }
        }
        objective[0][0] = ItemType.CAT;
        objective[1][3] = ItemType.FRAME;
        objective[2][1] = ItemType.PLANT;
        objective[3][0] = ItemType.TROPHY;
        objective[4][4] = ItemType.GAME;
        objective[5][2] = ItemType.BOOK;
    }
    @Override
    public int calculateScore(ItemTile[][] matrix) {
        int score=0;

        if(matrix[0][0].getType() == objective[0][0]){ score = score + 1;}
        if(matrix[1][3].getType() == objective[1][3]){ score = score + 1;}
        if(matrix[2][1].getType() == objective[2][1]){ score = score + 1;}
        if(matrix[3][0].getType() == objective[3][0]){ score = score + 1;}
        if(matrix[4][4].getType() == objective[4][4]){ score = score + 1;}
        if(matrix[5][2].getType() == objective[5][2]){ score = score + 1;}

        if(score==1 || score==2) {return score;}
        else if (score==3) {return score+1;}
        else if (score==4) {return score+2;}
        else if (score==5) {return score+3;}
        else if (score==6) {return score*2;}
        else {return score;}
    }
}
