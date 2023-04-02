package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Disabled
    void testInitBoard() {
        Board b = new Board();
        /*tested also case 3, case 4, and case others.*/
        b.initBoard(2);
        b.printBoard();
    }

    @Test
    void testBoardMethods() {
        Board b = new Board();
        Bag bag = new Bag();
        //initialize the board for 4 p
        b.initBoard(4);
        //Board needs refill because initBoard instantiates an empty board.
        assertTrue(b.needsRefill());
        b.refillBoard(bag.drawItems(b.numCellsToRefill()));
        //After refilling the board, it has to be full and needsRefill must be false.
        assertEquals(0,b.numCellsToRefill());
        assertFalse(b.needsRefill());
        //Picking all items.
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                b.pickItem(i,j);
            }
        }
        //Placing two items that are separated.
        b.placeItem(new ItemTile(ItemType.PLANT),0,3);
        b.placeItem(new ItemTile(ItemType.PLANT),4,5);
        //The board needs refill for the only two items are not adjacent.
        assertEquals(45-2,b.numCellsToRefill());
        assertTrue(b.needsRefill());
        //Refills.
        b.refillBoard(bag.drawItems(b.numCellsToRefill()));
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                b.pickItem(i,j);
            }
        }
        //This time we place two items that are adjacent, so the board doesn't need refill.
        b.placeItem(new ItemTile(ItemType.PLANT),4,4);
        b.placeItem(new ItemTile(ItemType.PLANT),4,5);
        //Method refillBoard shouldn't refill.
        b.refillBoard(bag.drawItems(b.numCellsToRefill()));
        assertFalse(b.needsRefill());
    }

    @Test
    void testPlaceItem() {
        Board b = new Board();
        b.initBoard(3);
        //Placing items on a forbidden square.
        ItemTile item = new ItemTile(ItemType.BOOK);
        b.placeItem(item,1,1);
        assertTrue(b.getGameboard()[1][1].getItem().isForbidden());
        //Placing items on a valid square, the item should be there.
        item = new ItemTile(ItemType.BOOK);
        b.placeItem(item,1,3);
        assertEquals(ItemType.BOOK, b.getGameboard()[1][3].getItem().getType());
        //Repeats above.
        ItemTile item1 = new ItemTile(ItemType.CAT);
        b.placeItem(item1,3,3);
        assertEquals(ItemType.CAT, b.getGameboard()[3][3].getItem().getType());
    }

    @Test
    void testPickItem() {
        Board b = new Board();
        b.initBoard(2);
        //Placing items on a square and on adjacent squares.
        ItemTile item = new ItemTile(ItemType.BOOK);
        b.placeItem(item,1,3);
        item = new ItemTile(ItemType.FRAME);
        b.placeItem(item,1,4);
        item = new ItemTile(ItemType.CAT);
        b.placeItem(item,2,3);
        //Picking one item, the square should be empty after picking and did not pick other item.
        item = b.pickItem(1,3);
        assertTrue(b.getGameboard()[1][3].getItem().isEmpty());
        assertEquals(ItemType.BOOK,item.getType());
    }

    @Disabled
    void testEnableSquaresWithFreeSide() {
        Board b = new Board();
        Bag bag = new Bag();
        b.initBoard(4);
        b.refillBoard(bag.drawItems(b.numCellsToRefill()));
        b.enableSquaresWithFreeSide();
    }

    @Disabled
    void testPickableFirstItems() {
        Board b = new Board();
        b.initBoard(4);

        ItemTile item = new ItemTile(ItemType.BOOK);
        b.placeItem(item,2,3);
        item = new ItemTile(ItemType.BOOK);
        b.placeItem(item,2,4);
        ItemTile item1 = new ItemTile(ItemType.CAT);
        b.placeItem(item1,2,5);
        ArrayList<Integer> pickables = b.pickableFirstItems();
        int[] exp = new int[] {2, 3, 2, 4, 2, 5};
        for (int i = 0; i < exp.length; i++){
            assertEquals(exp[i], pickables.get(i));
        }

        ItemTile item2 = new ItemTile(ItemType.FRAME);
        b.placeItem(item2,1,4);
        item2 = new ItemTile(ItemType.FRAME);
        b.placeItem(item2,3,4);
        pickables = b.pickableFirstItems();
        exp = new int[] {1, 4, 2, 3, 2, 5, 3, 4};
        for (int i = 0; i < exp.length; i++){
            assertEquals(exp[i], pickables.get(i));
        }

        item1 = new ItemTile(ItemType.CAT);
        b.placeItem(item1,1,5);
        item1 = new ItemTile(ItemType.CAT);
        b.placeItem(item1,3,5);
        pickables = b.pickableFirstItems();
        exp = new int[] {1, 4, 1, 5, 2, 3, 2, 5, 3, 4, 3, 5};
        for (int i = 0; i < exp.length; i++){
            assertEquals(exp[i], pickables.get(i));
        }
    }

    @Disabled
    void testPickableItems() {
        Board b = new Board();
        b.initBoard(4);

        ItemTile item = new ItemTile(ItemType.BOOK);
        b.placeItem(item,2,3);
        item = new ItemTile(ItemType.BOOK);
        b.placeItem(item,2,4);
        ItemTile item1 = new ItemTile(ItemType.CAT);
        b.placeItem(item1,2,5);
        ArrayList<Integer> pickables = b.pickableItems(2,3);
        int[] exp = new int[] {2, 4, 2, 5};
        for (int i = 0; i < exp.length; i++){
            assertEquals(exp[i], pickables.get(i));
        }

        ItemTile item2 = new ItemTile(ItemType.FRAME);
        b.placeItem(item2,1,4);
        item2 = new ItemTile(ItemType.FRAME);
        b.placeItem(item2,3,4);
        pickables = b.pickableItems(1,4);
        exp = new int[] {2, 4, 3, 4};
        for (int i = 0; i < exp.length; i++){
            assertEquals(exp[i], pickables.get(i));
        }

        item1 = new ItemTile(ItemType.CAT);
        b.placeItem(item1,1,5);
        item1 = new ItemTile(ItemType.CAT);
        b.placeItem(item1,3,5);
        pickables = b.pickableItems(1,5);
        exp = new int[] {2, 5, 3, 5, 1, 4};
        for (int i = 0; i < exp.length; i++){
            assertEquals(exp[i], pickables.get(i));
        }
    }

    @Test
    void testDoesSquareHaveFreeSide() {
        Board b = new Board();
        Bag bag = new Bag();
        b.initBoard(4);
        b.refillBoard(bag.drawItems(b.numCellsToRefill()));
        b.pickItem(3,3);
        //After picking one item, the squares adjacent should have a free side.
        assertTrue(b.doesSquareHaveFreeSide(3,2));
        assertTrue(b.doesSquareHaveFreeSide(3,4));
        assertTrue(b.doesSquareHaveFreeSide(2,3));
        assertTrue(b.doesSquareHaveFreeSide(4,3));
        //the squares on the board edge all have free sides.(manually repeated test on all squares)
        assertTrue(b.doesSquareHaveFreeSide(1,3));
    }
}