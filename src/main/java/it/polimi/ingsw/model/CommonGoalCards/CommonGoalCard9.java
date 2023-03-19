package it.polimi.ingsw.model.CommonGoalCards;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;
import java.util.ArrayList;
public class CommonGoalCard9 extends CommonGoalCard{
    public CommonGoalCard9(int numPlayers){
        super(numPlayers);
    }

    public boolean check(ItemTile[][] matrix){

        ArrayList<ItemType> AllTypes = new ArrayList<ItemType>();
        ArrayList<ItemType> Column = new ArrayList<ItemType>();
        int col = 0;
        AllTypes.add(ItemType.CAT);
        AllTypes.add(ItemType.BOOK);
        AllTypes.add(ItemType.GAME);
        AllTypes.add(ItemType.FRAME);
        AllTypes.add(ItemType.TROPHY);
        AllTypes.add(ItemType.PLANT);

        for(int i=0;i<5;i++){
            if(matrix[0][i].getType() != ItemType.EMPTY){
                for(int j=0;j<6;j++){
                    Column.add(matrix[j][i].getType());
                }
                if(Column.containsAll(AllTypes)){
                    col = col + 1;
                }
                Column.clear();
            }
            if(col==2){
                return true;
            }
        }
        return false;
    }
}
