package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

public class CommonGoalCardSquare extends CommonGoalCard{

    public CommonGoalCardSquare(int numPlayers){
        super(numPlayers);
    }

    public boolean check(Bookshelf bookshelf){
        ItemTile[][] matrix;
        matrix = bookshelf.getShelfie();
        int k_init;
        // controllo le righe e le colonne tranne ultima colonna e riga
        for (int i = 0; i < Bookshelf.Rows-1; i++) {
            for (int j = 0; j < Bookshelf.Columns-1; j++) {
                if (matrix[i][j].getType() != ItemType.EMPTY
                        && matrix[i][j].getType() == matrix[i][j+1].getType()
                        && matrix[i][j+1].getType() == matrix[i+1][j].getType()
                        && matrix[i+1][j].getType() == matrix[i+1][j+1].getType()
                        && checkAdjDifferentType (matrix, i, j, "topLeft")
                        && checkAdjDifferentType (matrix, i, j+1, "topRight")
                        && checkAdjDifferentType (matrix, i+1, j, "downLeft")
                        && checkAdjDifferentType (matrix, i+1, j+1, "downRight")) {

                    // una volta trovato prima squadra 2x2 controllo per le celle rimanenti
                    for (int h = i; h < Bookshelf.Rows-1; h++) {
                        if (h == i || h == i+1){
                            // se sono sulla stesse righe controllo da 2 collone succesive
                            k_init = j + 2;
                        }else{
                            // se non sono sulla stessa riga controllo da prima colonna
                            k_init = 0;
                        }
                        for (int k = k_init; k < Bookshelf.Columns-1; k++) {
                            if (matrix[h][k].getType() != ItemType.EMPTY
                                    && matrix[h][k].getType() == matrix[h][k+1].getType()
                                    && matrix[h][k+1].getType() == matrix[h+1][k].getType()
                                    && matrix[h+1][k].getType() == matrix[h+1][k+1].getType()
                                    && checkAdjDifferentType (matrix, h, k, "topLeft")
                                    && checkAdjDifferentType (matrix, h, k+1, "topRight")
                                    && checkAdjDifferentType (matrix, h+1, k, "downLeft")
                                    && checkAdjDifferentType (matrix, h+1, k+1, "downRight")) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean checkAdjDifferentType (ItemTile[][] matrix, int i, int j, String position){
        if (position.equals("topLeft")){
            if (( i == 0 || matrix[i-1][j].getType() !=  matrix[i][j].getType())
                    && (j == 0 || matrix[i][j-1].getType() !=  matrix[i][j].getType())){
                return true;
            }
        }else if (position.equals("topRight")){
            if (( i == 0 || matrix[i-1][j].getType() !=  matrix[i][j].getType())
                    && (j == Bookshelf.Columns-1 || matrix[i][j+1].getType() !=  matrix[i][j].getType())){
                return true;
            }
        }else if (position.equals("downLeft")){
            if (( i == Bookshelf.Rows-1 || matrix[i+1][j].getType() !=  matrix[i][j].getType())
                    && (j == 0 || matrix[i][j-1].getType() !=  matrix[i][j].getType())){
                return true;
            }
        }else{
            if (( i == Bookshelf.Rows-1 || matrix[i+1][j].getType() !=  matrix[i][j].getType())
                    && (j == Bookshelf.Columns-1 || matrix[i][j+1].getType() !=  matrix[i][j].getType())){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Due gruppi separati di 4 tessere dello stesso tipo che formano un quadrato 2x2." +
                "Le tessere dei due gruppi devono essere dello stesso tipo.";
    }
}


