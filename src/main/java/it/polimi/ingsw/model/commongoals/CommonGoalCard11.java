<<<<<<<< HEAD:src/main/java/it/polimi/ingsw/model/commongoals/CommonGoalCard11.java
package it.polimi.ingsw.model.commongoals;
========
package it.polimi.ingsw.model.CommonGoalCards;
>>>>>>>> c7ffb5150689a86c88a1956d9b79d75593ea373e:src/main/java/it/polimi/ingsw/model/CommonGoalCards/CommonGoalCard11.java
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;
public class CommonGoalCard11 extends CommonGoalCard {
    public CommonGoalCard11(int numPlayers){
        super(numPlayers);
    }
    public boolean check(ItemTile[][] matrix){
        for(int i=1;i<5;i++){
            for(int j=1;j<4;j++){
                if(matrix[i][j].getType() != ItemType.EMPTY){
                    if(matrix[i][j].getType()==matrix[i-1][j-1].getType()
                            && matrix[i][j].getType()==matrix[i+1][j-1].getType()
                            && matrix[i][j].getType()==matrix[i-1][j+1].getType()
                            && matrix[i][j].getType()==matrix[i+1][j+1].getType()){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
