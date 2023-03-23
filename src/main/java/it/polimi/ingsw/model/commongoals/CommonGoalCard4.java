package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

public class CommonGoalCard4 extends CommonGoalCard{

    public CommonGoalCard4(int numPlayers){
        super(numPlayers);
    }

    public boolean check(Bookshelf bookshelf){
        ItemTile[][] matrix;
        matrix = bookshelf.getShelfie();
        int k_init;
        // controllo le righe e le colonne tranne ultima colonna e riga
        for (int i = 0; i < NROW-1; i++) {
            for (int j = 0; j < NCOL-1; j++) {
                ItemTile topLeft1 = matrix[i][j];
                ItemTile topRight1 = matrix[i][j+1];
                ItemTile downLeft1 = matrix[i+1][j];
                ItemTile downRight1 = matrix[i+1][j+1];
                if (topLeft1.getType() != ItemType.EMPTY
                        && topLeft1.getType() == topRight1.getType()
                        && topRight1.getType() == downLeft1.getType()
                        && downLeft1.getType() == downRight1.getType()) {
                    matrix[i][j].setGroupId(1);
                    matrix[i][j+1].setGroupId(1);
                    matrix[i+1][j].setGroupId(1);
                    matrix[i+1][j+1].setGroupId(1);
                    // una volta trovato prima squadra 2x2 controllo per altre celle
                    for (int h = i; h < NROW-1; h++) {
                        if (h == i){
                            // se sono sulla stessa riga controllo da 2 collone succesive
                            k_init = j + 2;
                        }else{
                            // se non sono sulla stessa riga controllo da prima colonna
                            k_init = 0;
                        }
                        for (int k = k_init; k < NCOL-1; k++) {
                            ItemTile topLeft2 = matrix[h][k];
                            ItemTile topRight2 = matrix[h][k+1];
                            ItemTile downLeft2 = matrix[h+1][k];
                            ItemTile downRight2 = matrix[h+1][k+1];
                            if (downLeft2.getGroupId() != 0 ||
                                    topLeft2.getGroupId() != 0 ||
                                    downRight2.getGroupId() != 0 ||
                                    topRight2.getGroupId() != 0){
                                continue;
                            }
                            if (topLeft2.getType() != ItemType.EMPTY
                                    && topLeft2.getType() == topRight2.getType()
                                    && topRight2.getType() == downLeft2.getType()
                                    && downLeft2.getType() == downRight2.getType()) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

}
