package it.polimi.ingsw.model.CommonGoalCards;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;
import java.util.ArrayList;
public class CommonGoalCard8 extends CommonGoalCard{

    public CommonGoalCard8(int numPlayers){
        super(numPlayers);
    }

    public boolean check(ItemTile[][] matrix){
        int rows = 0;
        ArrayList<ItemType> SixType = new ArrayList<ItemType>();
        for(int i=0;i<6;i++){
            for(int j=0;j<5;j++){
                if(matrix[i][j].getType() != ItemType.EMPTY && SixType.size()<3){
                    if(SixType.size()==0){
                        SixType.add(matrix[i][j].getType());
                    }else{
                        if(!(SixType.contains(matrix[i][j].getType()))){
                            SixType.add(matrix[i][j].getType());
                        }
                    }
                }else{
                    if(matrix[i][j].getType() == ItemType.EMPTY){
                        return false;
                    }
                    break;
                }
            }
            SixType.clear();
            rows = rows + 1;
            if(rows==4){
                return true;
            }
        }
        return false;
    }
}
