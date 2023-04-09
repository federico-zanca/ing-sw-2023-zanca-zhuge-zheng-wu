
package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

import java.util.ArrayList;
public class CommonGoalCard10 extends CommonGoalCard {

    public CommonGoalCard10(int numPlayers){
        super(numPlayers);
    }
    public boolean check(Bookshelf bookshelf){
        ItemTile[][] matrix;
        matrix = bookshelf.getShelfie();
        ArrayList<ItemType> Column = new ArrayList<ItemType>();
        int row = 0;
        for(int i=0;i<6;i++){
            Column.clear();
            for(int j=0;j<5;j++){
                if(matrix[i][j].getType() == ItemType.EMPTY){
                    break;
                }
                if(!(Column.contains(matrix[i][j].getType()))){
                    Column.add(matrix[i][j].getType());
                }
            }
            if(Column.size() == 5){
                row = row + 1;
            }
            if(row == 2){
                return true;
            }
        }
        return false;
    }
}
