package it.polimi.ingsw.model.commongoals;
import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;
public class CommonGoalCard2 extends CommonGoalCard {

    public CommonGoalCard2(int numPlayers){
        super(numPlayers);
    }
    public boolean check(Bookshelf bookshelf){
        ItemTile[][] matrix;
        matrix = bookshelf.getShelfie();
        if(matrix[0][0].getType() != ItemType.EMPTY
                && matrix[0][0].getType() == matrix[0][Bookshelf.Columns-1].getType()
                && matrix[0][0].getType() == matrix[Bookshelf.Rows-1][Bookshelf.Columns-1].getType()
                && matrix[0][0].getType() == matrix[Bookshelf.Rows-1][0].getType()){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Quattro tessere dello stesso tipo ai quattro angoli della Libreria.";
    }
}
