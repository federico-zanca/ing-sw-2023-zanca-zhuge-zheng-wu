package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.exceptions.FullColumnException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookshelfTest {

    @Test
    void maxSlotsAvailable() throws FullColumnException {
        Bookshelf B = new Bookshelf();
        ItemTile[][] itemmatrix;
        String[][] matrix ={
                {"G","P","E","E","G"},
                {"G","G","E","C","T"},
                {"G","G","E","C","F"},
                {"B","B","E","F","T"},
                {"G","P","E","G","G"},
                {"B","P","E","T","T"}
        };
        itemmatrix = B.stringToMat(matrix);
        B.setShelfie(itemmatrix);
       // B.printBookshelf();
        assertEquals(6,B.maxSlotsAvailable());
        B.insertItem(new ItemTile(ItemType.PLANT),3);
        assertEquals(5,B.maxSlotsAvailable());
    }

    @Test
    void isFull() {
        Bookshelf B = new Bookshelf();
        ItemTile[][] itemmatrix;
        String[][] matrix ={
                {"G","P","C","C","G"},
                {"G","G","C","C","T"},
                {"G","G","C","C","F"},
                {"B","B","C","F","T"},
                {"G","P","C","G","G"},
                {"B","P","C","T","T"}
        };
        itemmatrix = B.stringToMat(matrix);
        B.setShelfie(itemmatrix);
        //B.printBookshelf();
        assertTrue(B.isFull());
    }

    @Test
    void insertItem() throws FullColumnException {
        Bookshelf B = new Bookshelf();
        ItemTile dummyItem = new ItemTile(ItemType.PLANT);
        //B.printBookshelf();
        for(int i=1;i<17;i++){
            int n = i % 5;
            if(n==0){
                n=5;
            }
            B.insertItem(dummyItem,n);
            B.printBookshelf();
        }
        assertFalse(B.isFull());
        assertEquals(30-16,B.getNumEmptyCells());
        B.printBookshelf();
    }

    @Test
    void getNumEmptyCells() {
        Bookshelf B = new Bookshelf();
        ItemTile[][] itemmatrix;
        String[][] matrix ={
                {"G","P","E","E","G"},
                {"G","G","E","E","T"},
                {"G","G","E","C","F"},
                {"B","B","E","F","T"},
                {"G","P","E","G","G"},
                {"B","P","E","T","T"}
        };
        itemmatrix = B.stringToMat(matrix);
        B.setShelfie(itemmatrix);
        //B.printBookshelf();
        assertEquals(8,B.getNumEmptyCells());
    }
    @Test
    void getSingleCell() {
        Bookshelf B = new Bookshelf();
        ItemTile[][] itemmatrix;
        String[][] matrix ={
                {"G","P","E","E","G"},
                {"G","G","E","E","T"},
                {"G","G","E","C","F"},
                {"B","B","E","F","T"},
                {"G","P","E","G","G"},
                {"B","P","E","T","T"}
        };
        itemmatrix = B.stringToMat(matrix);
        B.setShelfie(itemmatrix);
        assertSame(itemmatrix[5][4],B.getSingleCell(5,4));
    }

    @Test
    void adjacentGroupsElaboration() {
        Bookshelf B = new Bookshelf();
        ItemTile[][] itemmatrix;
        String[][] matrix ={
                {"G","P","E","E","G"},
                {"G","G","E","E","T"},
                {"G","G","E","C","F"},
                {"B","B","E","F","T"},
                {"G","P","E","G","G"},
                {"B","P","E","T","T"}
        };
        itemmatrix = B.stringToMat(matrix);
        B.setShelfie(itemmatrix);
        assertEquals(14,B.adjacentGroupsElaboration());
    }

    @Test
    void findRegions(){
        Bookshelf B = new Bookshelf();
        ItemTile[][] itemmatrix;
        String[][] matrix ={
                {"G","G","G","G","G"},
                {"G","G","G","G","G"},
                {"G","G","G","G","G"},
                {"G","G","G","G","G"},
                {"G","G","G","G","G"},
                {"G","G","G","G","G"}
        };
        itemmatrix = B.stringToMat(matrix);
        B.setShelfie(itemmatrix);
    }

    @Test
    void countGIDoccurrencies() {
        Bookshelf B = new Bookshelf();
        ItemTile[][] itemmatrix;
        int num;
        String[][] matrix ={
                {"G","G","G","G","G"},
                {"G","G","C","G","G"},
                {"G","C","C","G","G"},
                {"G","C","G","G","G"},
                {"G","C","F","F","F"},
                {"G","G","C","G","G"}
        };
        itemmatrix = B.stringToMat(matrix);
        B.setShelfie(itemmatrix);
        num = B.adjacentGroupsElaboration();
        for(int i=0;i<num;i++){
            assertTrue(B.countGIDoccurrencies(i)>0);
            if(i!=0){
                assertTrue(30-B.countGIDoccurrencies(i-1)>=B.countGIDoccurrencies(i));
            }else{
                assertEquals(19,B.countGIDoccurrencies(0));
            }
        }
    }

    @Test
    void resetGroupIDs() {
        Bookshelf B = new Bookshelf();
        B.resetGroupIDs();
        //B.printGroupID();
        for(int i=0;i< B.Rows;i++){
            for(int j=0;j< B.Columns;j++){
                assertEquals(-1,B.getShelfie()[i][j].getGroupId());
            }
        }
    }
}