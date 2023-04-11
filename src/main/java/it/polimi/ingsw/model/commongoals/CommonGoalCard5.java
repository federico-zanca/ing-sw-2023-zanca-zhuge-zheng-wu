package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

public class CommonGoalCard5 extends CommonGoalCard{
    public CommonGoalCard5(int numPlayers){
        super(numPlayers);
    }

    public boolean check(Bookshelf bookshelf){
        ItemTile[][] matrix;
        matrix = bookshelf.getShelfie();
        int columnCount = 0;
        //itero per colonne della matrice
        for (int i = 0; i < NCOL; i++) {
            //creo array per elementi presenti nella colonna
            ItemTile[] column = new ItemTile[NROW];
            for (int j = 0; j < NROW; j++){
                column[j] = matrix[j][i];
            }
            int typeCount = 0;
            // per ogni colonna controllo se riempita e conto il numero di tipi presenti, se <=3 incremento contatore columnCount,
            // altrimenti vado alla colonna succesiva
            for (int j = 0; j < NROW; j++) {
                if (column[j].getType() != ItemType.EMPTY) {
                    boolean isUnique = true;
                    for (int k = 0; k < j; k++) {
                        if (column[j].getType() == column[k].getType()) {
                            isUnique = false;
                            break;
                        }
                    }
                    if (isUnique) {
                        typeCount++;
                        if (typeCount > 3) {
                            break;
                        }
                    }
                }else{
                    typeCount = 4;
                    break;
                }
            }
            if (typeCount <= 3) {
                columnCount++;
            }
            if (columnCount == 3) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Tre colonne formate ciascuna da 6 tessere di uno, due o tre tipi differenti." +
                "Colonne diverse possono avere combinazioni diverse di tipi di tessere.";
    }
}
