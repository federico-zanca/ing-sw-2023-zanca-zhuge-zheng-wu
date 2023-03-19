<<<<<<<< HEAD:src/main/java/it/polimi/ingsw/model/commongoals/CommonGoalCard2.java
package it.polimi.ingsw.model.commongoals;
========
package it.polimi.ingsw.model.CommonGoalCards;
>>>>>>>> c7ffb5150689a86c88a1956d9b79d75593ea373e:src/main/java/it/polimi/ingsw/model/CommonGoalCards/CommonGoalCard2.java
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;
public class CommonGoalCard2 extends CommonGoalCard {

    public CommonGoalCard2(int numPlayers){
        super(numPlayers);
    }
    public boolean check(ItemTile[][] matrix){
        if(matrix[0][0].getType() != ItemType.EMPTY
                && matrix[0][0].getType() == matrix[0][4].getType()
                && matrix[0][0].getType() == matrix[5][4].getType()
                && matrix[0][0].getType() == matrix[5][0].getType()){
            return true;
        }
        return false;
    }
}
