package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void testInitBoard() {
        Board b = new Board();

        b.initBoard(2);
        for (int i = 0; i < 9; i++){
            assertTrue(b.getGameboard()[0][i].getItem().isForbidden());
            assertTrue(b.getGameboard()[8][i].getItem().isForbidden());
        }
        assertTrue(b.getGameboard()[1][3].getItem().isEmpty());
        assertTrue(b.getGameboard()[1][4].getItem().isEmpty());
        assertTrue(b.getGameboard()[2][3].getItem().isEmpty());
        assertTrue(b.getGameboard()[2][4].getItem().isEmpty());
        assertTrue(b.getGameboard()[2][5].getItem().isEmpty());
        for (int i = 0; i < 6; i++){
            assertTrue(b.getGameboard()[3][2+i].getItem().isEmpty());
            assertTrue(b.getGameboard()[5][1+i].getItem().isEmpty());
            assertTrue(b.getGameboard()[4][1+i].getItem().isEmpty());
        }
        assertTrue(b.getGameboard()[4][7].getItem().isEmpty());
        for (int i = 0; i < 3; i++){
            assertTrue(b.getGameboard()[0][i].getItem().isForbidden());
            assertTrue(b.getGameboard()[0][8 - i].getItem().isForbidden());
            assertTrue(b.getGameboard()[1][i].getItem().isForbidden());
            assertTrue(b.getGameboard()[1][8 - i].getItem().isForbidden());
        }

        b.initBoard(3);
        assertTrue(b.getGameboard()[0][3].getItem().isEmpty());
        assertTrue(b.getGameboard()[2][2].getItem().isEmpty());
        assertTrue(b.getGameboard()[2][6].getItem().isEmpty());
        assertTrue(b.getGameboard()[3][8].getItem().isEmpty());
        assertTrue(b.getGameboard()[5][0].getItem().isEmpty());
        assertTrue(b.getGameboard()[6][2].getItem().isEmpty());
        assertTrue(b.getGameboard()[6][6].getItem().isEmpty());
        assertTrue(b.getGameboard()[8][5].getItem().isEmpty());

        b.initBoard(4);
        assertTrue(b.getGameboard()[0][4].getItem().isEmpty());
        assertTrue(b.getGameboard()[1][5].getItem().isEmpty());
        assertTrue(b.getGameboard()[3][1].getItem().isEmpty());
        assertTrue(b.getGameboard()[4][0].getItem().isEmpty());
        assertTrue(b.getGameboard()[4][8].getItem().isEmpty());
        assertTrue(b.getGameboard()[5][7].getItem().isEmpty());
        assertTrue(b.getGameboard()[7][3].getItem().isEmpty());
        assertTrue(b.getGameboard()[8][4].getItem().isEmpty());
    }

    @Test
    void testRefillBoard() {
        Board b = new Board();
        b.initBoard(4);

        ItemTile item = new ItemTile(ItemType.BOOK);
        b.placeItem(item,2,3);
        ItemTile item1 = new ItemTile(ItemType.CAT);
        b.placeItem(item1,2,5);
        ItemTile item2 = new ItemTile(ItemType.FRAME);
        b.placeItem(item2,1,4);
        b.placeItem(item2,3,4);
        assertEquals(41,b.numCellsToRefill());
        ArrayList<ItemTile> items = new ArrayList<>();
        for (int i = 0; i < 41; i++){
            items.add(new ItemTile(ItemType.BOOK));
        }
        b.refillBoard(items);
        assertEquals(0,b.numCellsToRefill());
    }

    @Test
    void testNumCellsToRefill() {
        Board b = new Board();
        b.initBoard(2);
        assertEquals(29,b.numCellsToRefill());
        ItemTile item = new ItemTile(ItemType.BOOK);
        b.placeItem(item,2,3);
        assertEquals(28,b.numCellsToRefill());

        b = new Board();
        b.initBoard(3);
        assertEquals(37,b.numCellsToRefill());


        b.initBoard(4);
        assertEquals(45,b.numCellsToRefill());


    }

    @Test
    void testNeedsRefill() {
        Board b = new Board();
        b.initBoard(2);
        assertTrue(b.needsRefill());

        ItemTile item = new ItemTile(ItemType.BOOK);
        b.placeItem(item,3,3);
        ItemTile item1 = new ItemTile(ItemType.CAT);
        b.placeItem(item1,3,4);
        assertFalse(b.needsRefill());

        ItemTile item2 = new ItemTile(ItemType.BOOK);
        b.placeItem(item2,1,4);
        ItemTile item3 = new ItemTile(ItemType.CAT);
        b.placeItem(item3,2,4);
        assertFalse(b.needsRefill());
    }

    @Test
    void testPlaceItem() {
        Board b = new Board();
        b.initBoard(3);

        ItemTile item = new ItemTile(ItemType.BOOK);
        b.placeItem(item,1,1);
        assertTrue(b.getGameboard()[1][1].getItem().isForbidden());
        b.placeItem(item,1,3);
        assertEquals(ItemType.BOOK, b.getGameboard()[1][3].getItem().getType());
        ItemTile item1 = new ItemTile(ItemType.CAT);
        b.placeItem(item1,3,3);
        assertEquals(ItemType.CAT, b.getGameboard()[3][3].getItem().getType());
    }

    @Test
    void testPickItem() {
        Board b = new Board();
        b.initBoard(2);

        ItemTile item = new ItemTile(ItemType.BOOK);
        b.placeItem(item,1,3);
        item = new ItemTile(ItemType.CAT);
        b.placeItem(item,3,3);
        item = b.pickItem(1,3);
        assertTrue(b.getGameboard()[1][3].getItem().isEmpty());
        assertEquals(ItemType.BOOK,item.getType());
    }

    @Test
    void testEnableSquaresWithFreeSide() {
        Board b = new Board();
        b.initBoard(4);

        ItemTile item = new ItemTile(ItemType.BOOK);
        b.placeItem(item,2,3);
        b.placeItem(item,2,4);
        ItemTile item1 = new ItemTile(ItemType.CAT);
        b.placeItem(item1,2,5);
        b.enableSquaresWithFreeSide();

        assertTrue(b.getGameboard()[2][3].isPickable());
        assertTrue(b.getGameboard()[2][4].isPickable());
        assertTrue(b.getGameboard()[2][5].isPickable());

        ItemTile item2 = new ItemTile(ItemType.FRAME);
        b.placeItem(item2,1,4);
        b.placeItem(item2,3,4);
        b.enableSquaresWithFreeSide();

        assertTrue(b.getGameboard()[2][3].isPickable());
        assertTrue(b.getGameboard()[2][5].isPickable());
        assertFalse(b.getGameboard()[2][4].isPickable());

        b.placeItem(item1,1,5);
        b.placeItem(item1,3,5);
        b.enableSquaresWithFreeSide();
        assertTrue(b.getGameboard()[2][3].isPickable());
        assertFalse(b.getGameboard()[2][4].isPickable());
        assertTrue(b.getGameboard()[2][5].isPickable());

        b.placeItem(item1,2,6);
        b.enableSquaresWithFreeSide();
        assertFalse(b.getGameboard()[2][5].isPickable());
    }

    @Test
    void testPickableFirstItems() {
        Board b = new Board();
        b.initBoard(4);

        ItemTile item = new ItemTile(ItemType.BOOK);
        b.placeItem(item,2,3);
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
        b.placeItem(item2,3,4);
        pickables = b.pickableFirstItems();
        exp = new int[] {1, 4, 2, 3, 2, 5, 3, 4};
        for (int i = 0; i < exp.length; i++){
            assertEquals(exp[i], pickables.get(i));
        }

        b.placeItem(item1,1,5);
        b.placeItem(item1,3,5);
        pickables = b.pickableFirstItems();
        exp = new int[] {1, 4, 1, 5, 2, 3, 2, 5, 3, 4, 3, 5};
        for (int i = 0; i < exp.length; i++){
            assertEquals(exp[i], pickables.get(i));
        }
    }

    @Test
    void testPickableItems() {
        Board b = new Board();
        b.initBoard(4);

        ItemTile item = new ItemTile(ItemType.BOOK);
        b.placeItem(item,2,3);
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
        b.placeItem(item2,3,4);
        pickables = b.pickableItems(1,4);
        exp = new int[] {2, 4, 3, 4};
        for (int i = 0; i < exp.length; i++){
            assertEquals(exp[i], pickables.get(i));
        }

        b.placeItem(item1,1,5);
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
        b.initBoard(4);

        ItemTile item = new ItemTile(ItemType.BOOK);
        b.placeItem(item,2,3);
        b.placeItem(item,2,4);
        ItemTile item1 = new ItemTile(ItemType.CAT);
        b.placeItem(item1,2,5);
        assertTrue(b.doesSquareHaveFreeSide(2,3));
        assertTrue(b.doesSquareHaveFreeSide(2,4));
        assertTrue(b.doesSquareHaveFreeSide(2,5));

        ItemTile item2 = new ItemTile(ItemType.FRAME);
        b.placeItem(item2,1,4);
        b.placeItem(item2,3,4);
        assertTrue(b.doesSquareHaveFreeSide(2,3));
        assertFalse(b.doesSquareHaveFreeSide(2,4));
        assertTrue(b.doesSquareHaveFreeSide(2,5));
        assertTrue(b.doesSquareHaveFreeSide(1,4));

    }
}