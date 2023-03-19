package it.polimi.ingsw.model.commongoals;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;
public class CommonGoalCard12 extends CommonGoalCard {

    public CommonGoalCard12(int numPlayers){
        super(numPlayers);
    }
    public boolean check(ItemTile[][] matrix){
        int flag = 0;
        if(matrix[0][0].getType() != ItemType.EMPTY){
            for(int i=1;i<5;i++){
                if(matrix[i][i].getType() == ItemType.EMPTY || matrix[i-1][i].getType() != ItemType.EMPTY){
                    flag = 1;
                    break;
                }
            }
            if(flag == 0){
                return true;
            }
        }

        if(matrix[1][0].getType() != ItemType.EMPTY && flag != 1){
            for(int i=0;i<5;i++){
                if(matrix[i+1][i].getType() == ItemType.EMPTY || matrix[i][i].getType() != ItemType.EMPTY){
                    flag = 1;
                    break;
                }
            }
            if(flag == 0){
                return true;
            }
        }

        if(matrix[0][4].getType() != ItemType.EMPTY && flag != 1){
            for(int i=3;i>=0;i--){
                if(matrix[4-i][i].getType() == ItemType.EMPTY || matrix[3-i][i].getType() != ItemType.EMPTY){
                    flag = 1;
                    break;
                }
            }
            if(flag == 0){
                return true;
            }
        }

        if(matrix[1][4].getType() != ItemType.EMPTY && flag != 1){
            for(int i=3;i>=0;i--){
                if(matrix[5-i][i].getType() == ItemType.EMPTY || matrix[4-i][i].getType() != ItemType.EMPTY){
                    flag = 1;
                    break;
                }
            }
            if(flag == 0){
                return true;
            }
        }
        return false;
    }
}
