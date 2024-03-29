package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;


public class CommonGoalCard8SameTiles extends CommonGoalCard{
    public CommonGoalCard8SameTiles(int numPlayers){
        super(numPlayers);
    }

    public boolean check(Bookshelf bookshelf){
        ItemTile[][] matrix;
        matrix = bookshelf.getShelfie();
        int[] freq = new int[6];
        for (int i = 0; i < NROW; i++) {
            for (int j = 0; j < NCOL; j++) {
                switch (matrix[i][j].getType()) {
                    case CAT:
                        freq[0]++;
                        break;
                    case BOOK:
                        freq[1]++;
                        break;
                    case GAME:
                        freq[2]++;
                        break;
                    case FRAME:
                        freq[3]++;
                        break;
                    case TROPHY:
                        freq[4]++;
                        break;
                    case PLANT:
                        freq[5]++;
                        break;
                    default:
                        break;
                }
            }
        }
        for (int count : freq) {
            if (count == 8) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Otto tessere dello stesso tipo. " +
                "Non ci sono restrizioni sulla posizione di queste tessere.";
    }
}
