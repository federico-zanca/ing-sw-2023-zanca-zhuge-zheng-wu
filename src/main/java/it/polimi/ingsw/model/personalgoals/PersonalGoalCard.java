package it.polimi.ingsw.model.personalgoals;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

public abstract class PersonalGoalCard {
    final int Rows=6;
    final int Columns=5;
    protected ItemType[][] objective;

    /**
     * Default constructor
     * Initializes the PersonalGoal to a completely empty bookshelf
     */
    public PersonalGoalCard(){
        objective = new ItemType[Rows][Columns];
        for(int i=0; i<Rows; i++){
            for(int j=0; j<Columns; j++){
                objective[i][j] = ItemType.EMPTY;
            }
        }
    }

    public abstract int calculateScore(ItemTile[][] matrix);


}
