package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.exceptions.FullColumnException;

public class Bookshelf {
    final int Rows=6;
    final int Columns=5;
    private ItemTile[][] shelfie;

    public Bookshelf(){
        shelfie = new ItemTile[Rows][Columns];
        emptyBookshelf();
    }

    public boolean isFull(){
        /*
        for(int i=0; i<Dimensions; i++)
            if(shelfie[0][i].getType()==ItemType.EMPTY) return false;
        return true;
         */
        return getNumEmptyCells()==0;
    }

    public void insertItem(ItemTile item, int column) throws FullColumnException {
        if(shelfie[0][Columns-1].getType()!= ItemType.EMPTY) throw new FullColumnException();
        for(int i=Rows-1; i>0; i--){
            if(shelfie[i][column].getType()==ItemType.EMPTY){
                shelfie[i][column] = item;
            }
        }
    }

    public int getNumEmptyCells(){
        int count = 0;
        for(int i=0; i<Rows; i++){
            for(int j=0; j<Columns; i++){
                if(shelfie[i][j].getType()==ItemType.EMPTY) count++;
            }
        }
        return count;
    }

    private void emptyBookshelf(){
        for(int i=0; i<Rows; i++){
            for(int j=0; j<Columns; j++){
                shelfie[i][j].setType(ItemType.EMPTY);
            }
        }
    }
}
