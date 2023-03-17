package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.ItemType;

public class PersonalGoalCard {
    final int Dimensions=9;
    private ItemType[][] objective;

    public PersonalGoalCard(){
        objective = new ItemType[Dimensions][Dimensions];
        for(int i=0; i<Dimensions; i++){
            for(int j=0; j<Dimensions; j++){
                objective[i][j] = ItemType.EMPTY;
            }
        }
    }


}
