package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.exceptions.FullColumnException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a bookshelf with 6 rows and 5 columns to store item tiles.
 * Each tile represents a position on the bookshelf that can hold an item.
 */
public class Bookshelf implements Serializable {
    private static final long serialVersionUID = -4668097451983716980L;
    public static final int Rows=6;
    public static final int Columns=5;

    private ItemTile[][] shelfie;

    /**
     * Initializes a new instance of the Bookshelf class.
     * The bookshelf is initially empty, with all item tiles set to EMPTY.
     */
    public Bookshelf(){
        shelfie = new ItemTile[Rows][Columns];
        emptyBookshelf();
    }

    /**
     * Initializes a new instance of the Bookshelf class with the same contents as another bookshelf.
     * @param other The Bookshelf object from which to copy the contents.
     */
    public Bookshelf(Bookshelf other){
        this.shelfie = other.getShelfie();
    }

    /**
     * Setter for shelfie
     * @param shelfie matrix to set
     */
    public void setShelfie(ItemTile[][] shelfie) {
        this.shelfie = shelfie;
    }


    /**
     *
     * @return the maximum number of slots available in any column of the bookshelf
     */
    public int maxSlotsAvailable(){
        int max=0;
        int[] availableSlots=this.availableSlotsForEachColumn();
        for(int i=0; i<Columns; i++){
            if(availableSlots[i]>max){
                max=availableSlots[i];
            }
        }
        return max;
    }


    /**
     *
     * @return array of 5 integers, representing the number of available slots for the respective column
     */
    public int[] availableSlotsForEachColumn(){
        int[] availableSlots= new int[Columns];
        for(int i=0; i<Columns; i++){
            availableSlots[i]=0;
        }
        for(int i=0; i<Columns; i++){
            availableSlots[i]=availableSlotsForColumn(i);
        }
        return availableSlots;
    }

    /**
     * Calculates the number of available slots (empty item tiles) in a specific column of the bookshelf.
     * @param column The column index for which to count the available slots.
     * @return The number of available slots in the specified column.
     * @throws IndexOutOfBoundsException If the specified column is out of range.
     */
    public int availableSlotsForColumn(int column){
        int availableSlots=0;
        for(int i=0; i<Rows; i++){
            if(shelfie[i][column].getType()==ItemType.EMPTY){
                availableSlots++;
            }
        }
        return availableSlots;
    }

    /**
     * Creates an arraylist of integers that contains only columns that have enough space to contain the handsize.
     * @param handsize size of the players hand.
     * @return arraylist of columns.
     */
    public ArrayList<Integer> enableColumns(int handsize){
        ArrayList<Integer> cols = new ArrayList<>();
        int counter;
        for(int i=0;i<Columns;i++){
            counter = 0;
            for(int j=0;j<Rows;j++){
                if(shelfie[j][i].getType() == ItemType.EMPTY){
                    counter++;
                }
            }
            if(counter >= handsize){
                cols.add(i);
            }
        }
        return cols;
    }

    /**
     * Returns true if the Bookshelf is full
     * @return true if bookshelf is now full, false if it isn't
     */

    public boolean isFull(){
        return getNumEmptyCells()==0;
    }

    /**
     * Places an item in the lowest cell of a specified column.
     * @param item the ItemTile to be inserted
     * @param column the target column of the insertion
     * @throws FullColumnException if the target column is already full
     */
    public void insertItem(ItemTile item, int column){
        for(int i=Rows-1; i>=0; i--){
            if(shelfie[i][column].getType()==ItemType.EMPTY){
                shelfie[i][column] = item;
                break;
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
            for(int j=0; j<Columns; j++){
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
        if(r>Rows || r<0 || c>Columns || c<0){throw new IndexOutOfBoundsException();}
        return shelfie[r][c];
    }

    /**
     * Recursively checks if cells surrounding the one given share the same ItemType of the given cell.
     * If so, sets GroupId accordingly
     * @param type ItemType in examination
     * @param gid GroupId to check and set
     * @param row row number of the cell
     * @param column column number of the cell
     */
    public void findRegions(ItemType type, int gid, int row, int column){
        if(row<0 || column<0 || row>=Rows || column>=Columns) return;
        if(shelfie[row][column].getGroupId()==gid) return;
        if(shelfie[row][column].getType()==type){
            shelfie[row][column].setGroupId(gid);
            findRegions(type, gid, row-1, column);
            findRegions(type, gid, row+1, column);
            findRegions(type, gid, row, column-1);
            findRegions(type, gid, row, column+1);
        }
    }

    /**
     * Assigns a GroupID to every item in the bookshelf
     * @return the number of Groups of adjacent items found in the Bookshelf
     */
    public int adjacentGroupsElaboration(){
        resetGroupIDs();
        int currentGId = 0;
        for(int i=0; i<Rows; i++){
            for(int j=0; j<Columns; j++){
                if(shelfie[i][j].getGroupId()==-1 && shelfie[i][j].hasSomething()){
                    //shelfie[i][j].setGroupId(currentGId);
                    findRegions(shelfie[i][j].getType(), currentGId, i, j);
                    currentGId++;
                }
            }
        }
        return currentGId;
    }

    /**
     * Counts the number of Items in the bookshelf belonging to a given GroupID
     * @param gidToCheck GroupID in examination
     * @return number of ItemTiles in bookshelf having the specified GroupId
     */
    public int countGIDoccurrencies(int gidToCheck) {
        int count=0;
        for(int i=0; i<Rows; i++){
            for(int j=0; j<Columns; j++){
                if(shelfie[i][j].getGroupId()==gidToCheck) count++;
            }
        }
        return count;
    }

    /**
     * Sets every item's groupid to -1
     */
    public void resetGroupIDs() {
        for(int i=0; i< Rows; i++){
            for(int j=0; j<Columns; j++){
                shelfie[i][j].setGroupId(-1);
            }
        }
    }
    /**
     * convert a string matrix to an Itemtile matrix
     * @param mat string matrix
     * @return matrix of ItemTile
     */
    public ItemTile[][] stringToMat(String[][] mat){
        ItemTile[][] B = new ItemTile[Rows][Columns];

        for(int i=0;i<Rows;i++){
            for(int j=0;j<Columns;j++){
                if(Objects.equals(mat[i][j], "G")){B[i][j] = new ItemTile(ItemType.GAME);}
                if(Objects.equals(mat[i][j], "B")){B[i][j] = new ItemTile(ItemType.BOOK);}
                if(Objects.equals(mat[i][j], "C")){B[i][j] = new ItemTile(ItemType.CAT);}
                if(Objects.equals(mat[i][j], "P")){B[i][j] = new ItemTile(ItemType.PLANT);}
                if(Objects.equals(mat[i][j], "F")){B[i][j] = new ItemTile(ItemType.FRAME);}
                if(Objects.equals(mat[i][j], "T")){B[i][j] = new ItemTile(ItemType.TROPHY);}
                if(Objects.equals(mat[i][j], "E")){B[i][j] = new ItemTile(ItemType.EMPTY);}
            }
        }
        return B;
    }
    /**
     * print the bookshelf
     */
    public void printBookshelf(){
        for(int i=0; i<Rows; i++){
            for(int j=0; j<Columns; j++){
                if(getSingleCell(i,j).getType() == ItemType.GAME){System.out.print("G\t");}
                if(getSingleCell(i,j).getType() == ItemType.BOOK){System.out.print("B\t");}
                if(getSingleCell(i,j).getType() == ItemType.CAT){System.out.print("C\t");}
                if(getSingleCell(i,j).getType() == ItemType.PLANT){System.out.print("P\t");}
                if(getSingleCell(i,j).getType() == ItemType.FRAME){System.out.print("F\t");}
                if(getSingleCell(i,j).getType() == ItemType.TROPHY){System.out.print("T\t");}
                if(getSingleCell(i,j).getType() == ItemType.EMPTY){System.out.print("E\t");}
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Inserts a list of item tiles into a specific column of the bookshelf.
     * The items are inserted one by one using the insertItem() method.
     * @param items  The list of item tiles to be inserted.
     * @param column The column index where the items should be inserted.
     * @throws IndexOutOfBoundsException If the specified column is out of range.
     */
    public void insertItems(ArrayList<ItemTile> items, int column) {
        for(ItemTile item : items){
            insertItem(item, column);
        }
    }
}
