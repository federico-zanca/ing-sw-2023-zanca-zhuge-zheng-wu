package it.polimi.ingsw.model.commongoals;
import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

import java.util.Objects;

public class CommonGoalCardLimitTypes extends CommonGoalCard{
    private int num_target;
    private int min_type;
    private int max_type;
    private String rowOrCol;
    public CommonGoalCardLimitTypes(int numPlayers, int num_target, int min_type, int max_type, String rowOrCol){
        super(numPlayers);
        this.num_target = num_target;
        this.min_type = min_type;
        this.max_type = max_type;
        this.rowOrCol = rowOrCol;
    }

    public boolean check(Bookshelf bookshelf){
        ItemTile[][] matrix;
        matrix = bookshelf.getShelfie();
        int iter1;
        int iter2;
        if (rowOrCol == "row"){
            iter1 = NROW;
            iter2 = NCOL;
        }else{
            iter1 = NCOL;
            iter2 = NROW;
        }

        int count = 0;

        //itero per colonne della matrice
        for (int i = 0; i < iter1; i++) {
            //creo array per elementi presenti nella riga/colonna
            ItemTile[] items = new ItemTile[iter2];
            for (int j = 0; j < iter2; j++){
                if (rowOrCol == "row"){
                    items[j] = matrix[i][j];
                }else{
                    items[j] = matrix[j][i];
                }

            }
            int typeCount = 0;
            // per ogni riga/colonna controllo se riempita e conto il numero di tipi presenti, se <=3 incremento contatore count,
            // altrimenti vado alla colonna succesiva
            for (int j = 0; j < iter2; j++) {
                if (items[j].getType() != ItemType.EMPTY) {
                    boolean isUnique = true;
                    for (int k = 0; k < j; k++) {
                        if (items[j].getType() == items[k].getType()) {
                            isUnique = false;
                            break;
                        }
                    }
                    if (isUnique) {
                        typeCount++;
                        if (typeCount > max_type) {
                            break;
                        }
                    }
                }else{
                    typeCount = max_type + 1;
                    break;
                }
            }
            if (typeCount <= max_type && typeCount >= min_type) {
                count++;
            }
            if (count == num_target) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        if (num_target == 3 && min_type ==1 && max_type == 3 && rowOrCol == "col"){
            return "Tre colonne formate ciascuna da 6 tessere di uno, due o tre tipi differenti." +
                    "Colonne diverse possono avere combinazioni diverse di tipi di tessere.";
        }else if (num_target == 4 && min_type ==1 && max_type == 3 && rowOrCol == "row"){
            return "Quattro righe formate ciascuna da 5 tessere di uno, due o tre tipo differenti." +
                    "Righe diverse possono avere combinazioni diverse di tipi di tessere.";
        }else if (num_target == 2 && min_type ==6 && max_type == 6 && rowOrCol == "col"){
            return "Due colonne formate ciascuna da 6 diversi tipi di tessere.";
        }else{
            return "Due righe formate ciascuna da 5 diversi tipi di tessere.";
        }
    }
}
