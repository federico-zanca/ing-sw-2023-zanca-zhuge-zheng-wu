package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.exceptions.FullColumnException;

import java.util.ArrayList;
import java.util.Objects;

public class Bookshelf {
    protected final int Rows=6;
    protected final int Columns=5;

    private ItemTile[][] shelfie;

    public Bookshelf(){
        shelfie = new ItemTile[Rows][Columns];
        emptyBookshelf();
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
        int[] availableSlots=this.availableSlots();
        for(int i=0; i<Columns; i++){
            if(availableSlots[i]>max){
                max=availableSlots[i];
            }
        }
        return max;
    }

    /**
     *
     * @return ArrayList of Integers representing the index of the columns with at least 2 available slots
     */

    public ArrayList<Integer> availableColumns(){
        ArrayList<Integer> availableCol=new ArrayList<Integer>();
        int[] availableSlots=this.availableSlots();

        for (int i=0; i<availableSlots.length; i++){
            if (availableSlots[i]>=2){
                availableCol.add(i);
            }
        }

        return availableCol;
    }

    /**
     *
     * @return array of 5 integers, representing the number of available slots for the respective column
     */
    public int[] availableSlots(){
        int[] availableSlots= new int[Columns];
        for(int i=0; i<Columns; i++){
            availableSlots[i]=0;
        }
        for(int i=0; i<Columns; i++){
            for(int j=0; j<Rows; j++){
                if(shelfie[j][i].getType()!=ItemType.EMPTY){
                    availableSlots[i]++;
                }
            }
        }

        return availableSlots;
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
        if(r>5 || r<0 || c>4 || c<0){throw new IndexOutOfBoundsException();}
        return shelfie[r][c];
    }

    /**
     * Recursively hecks if cells surrounding the one given share the same ItemType of the given cell.
     * If so, sets GroupId accordingly
     * @param type ItemType in examination
     * @param gid GroupId to check and set
     * @param row row number of the cell
     * @param column column number of the cell
     */
    private void findRegions(ItemType type, int gid, int row, int column){
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
                    shelfie[i][j].setGroupId(currentGId++);
                    findRegions(shelfie[i][j].getType(), shelfie[i][j].getGroupId(), i, j);
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

}
