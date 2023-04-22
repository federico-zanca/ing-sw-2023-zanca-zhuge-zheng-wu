package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

import java.util.ArrayList;
import java.util.List;

public class CommonGoalCardGroups extends CommonGoalCard {

        private int num_group;
        private int num_item;

        public CommonGoalCardGroups(int numPlayers, int num_group, int num_item){
            super(numPlayers);
            this.num_group = num_group;
            this.num_item = num_item;
        }
        public boolean check(Bookshelf bookshelf){
            ItemTile[][] matrix;
            matrix = bookshelf.getShelfie();
            int groupIDcount = 0;
            int groupCount = 0;
            List<Integer> itemCount = new ArrayList<Integer>();
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    if (matrix[i][j].getType() != ItemType.EMPTY) {
                        if (matrix[i][j].getGroupId() == 0) {
                            //"creo" nuovo gruppo
                            groupIDcount++;
                            matrix[i][j].setGroupId(groupIDcount);
                            itemCount.add(groupIDcount-1, 1);
                            findAdjacentTiles(matrix, i, j, groupIDcount);
                        } else if (matrix[i][j].getGroupId() > 0) {
                            //incremento contatore del gruppo a cui elemento corrente appartiene
                            int correntGroup = matrix[i][j].getGroupId();
                            itemCount.set(correntGroup-1, itemCount.get(correntGroup-1) + 1);
                        }
                    }
                }
            }
            for (int i = 0; i < matrix.length; i++){
                for (int j = 0; j < matrix[0].length; j++){
                    matrix[i][j].setGroupId(0);
                }
            }
            for (int h = 0; h < groupIDcount; h++) {
                if (itemCount.get(h) >= num_item) {
                    groupCount ++;
                    if(groupCount == num_group){
                        return true;
                    }
                }
            }
            return false;
        }

        private void findAdjacentTiles(ItemTile[][] matrix, int i, int j, int group) {
            // controllo cella sotto
            if (i + 1 < matrix.length && matrix[i + 1][j].getType() ==  matrix[i][j].getType() && matrix[i + 1][j].getGroupId() == 0) {
                matrix[i+1][j].setGroupId(group);
                findAdjacentTiles(matrix, i + 1, j, group);
            }
            // controllo cella destra
            if (j + 1 < matrix[0].length && matrix[i][j + 1].getType() ==  matrix[i][j].getType() && matrix[i][j+1].getGroupId()==0) {
                matrix[i][j+1].setGroupId(group);
                findAdjacentTiles(matrix, i, j + 1, group);
            }
            // controllo cella sopra
            if (i - 1 >= 0 && matrix[i - 1][j].getType() ==  matrix[i][j].getType() && matrix[i - 1][j].getGroupId() == 0) {
                matrix[i-1][j].setGroupId(group);
                findAdjacentTiles(matrix, i - 1, j, group);
            }
            // controllo cella sinistra
            if (j - 1 >= 0 && matrix[i][j - 1].getType() ==  matrix[i][j].getType() && matrix[i][j - 1].getGroupId() == 0) {
                matrix[i][j-1].setGroupId(group);
                findAdjacentTiles(matrix, i, j - 1, group);
            }
            return;
        }

        @Override
        public String toString() {
            return num_group + " gruppi separati formati ciascuno da " + num_item + "due tessere adiacenti dello stesso tipo." +
                    "Le tessere di un Gruppo possono essere diverse da quelle di un altro gruppo.";
        }
 }

