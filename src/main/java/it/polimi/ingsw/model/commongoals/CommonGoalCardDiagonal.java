package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

public class CommonGoalCardDiagonal extends CommonGoalCard{
    public CommonGoalCardDiagonal(int numPlayers){
        super(numPlayers);
    }
    public boolean check(Bookshelf bookshelf){
        ItemTile[][] matrix;
        matrix = bookshelf.getShelfie();
        for (int i = 0; i < NROW - 4; i++) {
            for (int j = 0; j < NCOL - 4; j++) {
                if (matrix[i][j].getType() != ItemType.EMPTY &&
                        matrix[i][j].getType() == matrix[i + 1][j + 1].getType() &&
                        matrix[i][j].getType() == matrix[i + 2][j + 2].getType() &&
                        matrix[i][j].getType() == matrix[i + 3][j + 3].getType() &&
                        matrix[i][j].getType() == matrix[i + 4][j + 4].getType()) {
                    return true;
                }
                // direzione opposta
                if (matrix[i][j + 4].getType() != ItemType.EMPTY&&
                        matrix[i][j + 4].getType() == matrix[i + 1][j + 3].getType() &&
                        matrix[i][j + 4].getType() == matrix[i + 2][j + 2].getType() &&
                        matrix[i][j + 4].getType() == matrix[i + 3][j + 1].getType() &&
                        matrix[i][j + 4].getType() == matrix[i + 4][j].getType()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Cinque tessere dello stesso tipo che formano una diagonale.";
    }
}
