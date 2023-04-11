package it.polimi.ingsw.model.commongoals;
import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;
public class CommonGoalCard12 extends CommonGoalCard {

    public CommonGoalCard12(int numPlayers){
        super(numPlayers);
    }
    public boolean check(Bookshelf bookshelf){
        ItemTile[][] matrix;
        matrix = bookshelf.getShelfie();
        int flag = 0;
        if(matrix[0][0].getType() != ItemType.EMPTY){
            for(int i=1;i<5;i++){
                if(matrix[i][i].getType() == ItemType.EMPTY || matrix[i-1][i].getType() != ItemType.EMPTY){
                    flag = 1;
                    break;
                }
            }
            if(flag == 0){
                return true;
            }
        }

        if(matrix[1][0].getType() != ItemType.EMPTY && flag != 1){
            for(int i=0;i<5;i++){
                if(matrix[i+1][i].getType() == ItemType.EMPTY || matrix[i][i].getType() != ItemType.EMPTY){
                    flag = 1;
                    break;
                }
            }
            if(flag == 0){
                return true;
            }
        }

        if(matrix[0][NCOL-1].getType() != ItemType.EMPTY && flag != 1){
            for(int i=3;i>=0;i--){
                if(matrix[4-i][i].getType() == ItemType.EMPTY || matrix[3-i][i].getType() != ItemType.EMPTY){
                    flag = 1;
                    break;
                }
            }
            if(flag == 0){
                return true;
            }
        }

        if(matrix[1][NCOL-1].getType() != ItemType.EMPTY && flag != 1){
            for(int i=3;i>=0;i--){
                if(matrix[5-i][i].getType() == ItemType.EMPTY || matrix[4-i][i].getType() != ItemType.EMPTY){
                    flag = 1;
                    break;
                }
            }
            if(flag == 0){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Cinque colonne di altezza crescente o decrescente: " +
                "a partire dalla prima colonna a sinistra o a destra, ogni colonna successiva deve essere formata da una tessera in più." +
                "Le tessere possono essere di qualsiasi tipo.";
    }
}
