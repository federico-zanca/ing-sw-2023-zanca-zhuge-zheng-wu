package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.exceptions.FullColumnException;

import java.awt.print.Book;

public class Bookshelf {
    protected final int Rows=6;
    protected final int Columns=5;

    private ItemTile[][] shelfie;

    public Bookshelf(){
        shelfie = new ItemTile[Rows][Columns];
        emptyBookshelf();
    }

    public void setShelfie(ItemTile[][] shelfie) {
        this.shelfie = shelfie;
    }


    /**
     * Returns true if the Bookshelf is full
     * @return true if bookshelf is now full, false if it isn't
     */
    public boolean isFull(){
        /*
        for(int i=0; i<Dimensions; i++)
            if(shelfie[0][i].getType()==ItemType.EMPTY) return false;
        return true;
         */
        return getNumEmptyCells()==0;
    }

    /**
     * Places an item in the lowest cell of a specified column.
     * @param item the ItemTile to be inserted
     * @param column the target column of the insertion
     * @throws FullColumnException if the target column is already full
     */
    public void insertItem(ItemTile item, int column) throws FullColumnException {
        if(shelfie[0][Columns-1].getType()!= ItemType.EMPTY) throw new FullColumnException();
        for(int i=Rows-1; i>0; i--){
            if(shelfie[i][column].getType()==ItemType.EMPTY){
                shelfie[i][column] = item;
            }
        }
    }

    /**
     * Returns the number of empty cells in the bookshelf
     * @return number of empty cells in bookshelf
     */
    public int getNumEmptyCells(){
        int count = 0;
        for(int i=0; i<Rows; i++){
            for(int j=0; j<Columns; i++){
                if(shelfie[i][j].getType()==ItemType.EMPTY) count++;
            }
        }
        return count;
    }

    /**
     * Sets every cell of bookshelf to an ItemTile having ItemType = ItemType.EMPTY
     */
    private void emptyBookshelf(){
        for(int i=0; i<Rows; i++){
            for(int j=0; j<Columns; j++){
                shelfie[i][j] = new ItemTile(ItemType.EMPTY);
            }
        }
    }

    /**
     * Returns the bookshelf
     * @return
     */
    public ItemTile[][] getShelfie() {
        return shelfie;
    }

    /**
     * Returns the single selected cell
     * @param r
     * @param c
     * @return
     * @throws IndexOutOfBoundsException
     */
    public ItemTile getSingleCell(int r,int c) throws IndexOutOfBoundsException{
        if(r>5 || r<0 || c>4 || c<0){throw new IndexOutOfBoundsException();}
        return shelfie[r][c];
    }
}
