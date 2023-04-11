package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

import java.util.ArrayList;
public class CommonGoalCard8 extends CommonGoalCard {

    public CommonGoalCard8(int numPlayers){
        super(numPlayers);
    }

    public boolean check(Bookshelf bookshelf){
        ItemTile[][] matrix;
        matrix = bookshelf.getShelfie();
        int rows = 0;
        int flag;
        ArrayList<ItemType> SixType = new ArrayList<ItemType>();
        for(int i=0;i<6;i++){
            flag = 0;
            SixType.clear();
            for(int j=0;j<5;j++){
                if(matrix[i][j].getType() != ItemType.EMPTY && SixType.size()<4){
                    if(!(SixType.contains(matrix[i][j].getType()))){
                        SixType.add(matrix[i][j].getType());
                    }
                }else if(matrix[i][j].getType() == ItemType.EMPTY || SixType.size()>3){
                    flag = 1;
                    break;
                }
            }
            if(flag == 0){
                rows = rows + 1;
            }
            if(rows==4){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Quattro righe formate ciascuna da 5 tessere di uno, due o tre tipo differenti." +
                "Righe diverse possono avere combinazioni diverse di tipi di tessere.";
    }
}
