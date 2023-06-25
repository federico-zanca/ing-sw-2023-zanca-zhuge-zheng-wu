package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BookshelfTest {
    private Bookshelf testShelf;
    private ItemTile testTile;
    @BeforeEach
    public void setup(){
        testShelf = new Bookshelf();
        testTile = new ItemTile(ItemType.PLANT);
    }
    @Nested
    @DisplayName("Tests for maxSlotsAvailable")
    public class maxSlotsAvailableTest {
        @Test
        public void emptyShelfTest(){
           assertEquals(6,testShelf.maxSlotsAvailable());
        }
        @Test
        public void maxFiveSlotsTest(){
            for(int i = 0; i<Bookshelf.Columns; i++) testShelf.insertItem(testTile,i);
            assertEquals(5,testShelf.maxSlotsAvailable());
        }
        @Test
        public void maxTwoSlotsTest(){
            for(int i = Bookshelf.Rows-1;i>=0;i--){
                for(int j = 0;j<Bookshelf.Columns;j++){
                    if(j==0){
                        if(i<4){
                            testShelf.insertItem(testTile,j);// stops inserting after the 4th item in the first column
                        }
                    }else{
                       testShelf.insertItem(testTile,j); // other columns should be full
                    }
                }
            }
            assertEquals(2,testShelf.maxSlotsAvailable());
        }

    }
    @Nested
    @DisplayName("isFullTests")
    public class isFullTests{
        @Test
        public void emptyShelf(){
            assertFalse(testShelf.isFull());
        }
        @Test
        public void fullShelf(){
            for(int i=0;i<Bookshelf.Rows;i++){
                for(int j=0;j<Bookshelf.Columns;j++){
                    testShelf.insertItem(testTile,j);
                }
            }
            assertTrue(testShelf.isFull());
        }
    }
    @Nested
    @DisplayName("insertItem test")
    public class insertItem{
        @Test
        public void normalInsertTest(){
            testShelf.insertItem(testTile,0);
            assertNotNull(testShelf.getShelfie()[5][0]);
            assertEquals(ItemType.PLANT,testShelf.getShelfie()[5][0].getType());
        }
        @Test
        public void insertItemsTest(){
            ArrayList<ItemTile> testsItems = new ArrayList<>();
            testsItems.add(testTile);
            testsItems.add(testTile);
            testsItems.add(testTile);
            testShelf.insertItems(testsItems,0);
            for(int i=5;i>3;i--){
                assertNotNull(testShelf.getShelfie()[i][0]);
                assertEquals(ItemType.PLANT,testShelf.getShelfie()[i][0].getType());
            }
        }
    }
    @Nested
    @DisplayName("getNumEmptyCells tests")
    public class getNumEmptyCells{
        @Test
        public void emptyShelf(){
         assertEquals(30,testShelf.getNumEmptyCells());
        }
        @Test
        public void fullShelf(){
            for(int i = Bookshelf.Rows-1;i>=0;i--){
                for(int j = 0;j<Bookshelf.Columns;j++){
                        testShelf.insertItem(testTile,j); // other columns should be full
                }
            }
            assertEquals(0,testShelf.getNumEmptyCells());
        }
    }
    @Nested
    @DisplayName("getSingleCell tests")
    public class getSingleCell{
        @Test
        public void emptyCell(){
            assertNotNull(testShelf.getSingleCell(0,0).getType());
            assertEquals(ItemType.EMPTY,testShelf.getSingleCell(0,0).getType());
        }
        @Test
        public void normalCell(){
            testShelf.insertItem(testTile,0);
            assertNotNull(testShelf.getSingleCell(0,0).getType());
            assertEquals(ItemType.PLANT,testShelf.getSingleCell(5,0).getType());
        }
    }
    @Nested
    @DisplayName("adjacentGroupsElaboration tests (findRegions included)")
    public class adjacentGroupsElaboration {
       @Test
        public void noGroups(){
           assertEquals(0,testShelf.adjacentGroupsElaboration());
       }
       @Test
        public void twoGroups(){
           for(int i = Bookshelf.Rows-1;i>=0;i--){
               for(int j = 0;j<Bookshelf.Columns;j++){
                   if(i>3)testShelf.insertItem(testTile,j); // other columns should be full
                   else if(i>1){testShelf.insertItem(new ItemTile(ItemType.FRAME),j);}
               }
           }
           testShelf.printBookshelf();
           assertEquals(2,testShelf.adjacentGroupsElaboration());
       }
       @Test
        public void trickyGroups(){
           ItemTile[][] itemmatrix;
           Bookshelf testshelf2;
           String[][] matrix ={
                   {"P","G","G","P","C"},
                   {"P","P","P","P","C"},
                   {"F","F","P","C","C"},
                   {"T","F","P","C","G"},
                   {"T","F","P","C","C"},
                   {"F","T","C","C","C"}
           };
           itemmatrix = testShelf.stringToMat(matrix);
           testShelf.setShelfie(itemmatrix);
           assertEquals(8,testShelf.adjacentGroupsElaboration());
           testshelf2=new Bookshelf(testShelf);
           assertEquals(testshelf2.getShelfie(),testShelf.getShelfie());
       }
    }
    @Test
    void countGIDoccurrencies() {
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
        itemmatrix = testShelf.stringToMat(matrix);
        testShelf.setShelfie(itemmatrix);
        num = testShelf.adjacentGroupsElaboration();
        for(int i=0;i<num;i++){
            assertTrue(testShelf.countGIDoccurrencies(i)>0);
            if(i!=0){
                assertTrue(30-testShelf.countGIDoccurrencies(i-1)>=testShelf.countGIDoccurrencies(i));
            }else{
                assertEquals(19,testShelf.countGIDoccurrencies(0));
            }
        }
    }
    @Test
    void enableColumns(){
        ItemTile[][] itemmatrix;
        String[][] matrix ={
                {"E","E","E","E","E"},
                {"G","E","E","G","G"},
                {"G","E","C","G","G"},
                {"G","C","G","G","G"},
                {"G","C","F","F","F"},
                {"G","G","C","G","G"}
        };
        itemmatrix = testShelf.stringToMat(matrix);
        testShelf.setShelfie(itemmatrix);
        ArrayList<Integer> testArray = new ArrayList<>();
        testArray.add(1);
        assertEquals(testArray,testShelf.enableColumns(3));
    }
}