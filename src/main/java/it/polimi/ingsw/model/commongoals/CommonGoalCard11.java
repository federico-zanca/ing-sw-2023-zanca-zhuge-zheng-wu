
package it.polimi.ingsw.model.commongoals;
import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;
public class CommonGoalCard11 extends CommonGoalCard {
    public CommonGoalCard11(int numPlayers){
        super(numPlayers);
    }
    public boolean check(Bookshelf bookshelf){
        ItemTile[][] matrix;
        matrix = bookshelf.getShelfie();
        for(int i=1;i<NROW;i++){
            for(int j=1;j<NCOL-1;j++){
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
