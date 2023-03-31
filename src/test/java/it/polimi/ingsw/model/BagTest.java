package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {

    @Test
    void emptyBagTest() {
        Bag b = new Bag();
        assertFalse(b.emptyBag());
        for (int k = 0; k < 132; k++){
            b.drawItem();
        }
        assertTrue(b.emptyBag());
    }

    @Test
    void drawItemTest() {
        Bag b = new Bag();
        ItemTile item = b.drawItem();
        assertNotNull(item);
        assertEquals(131,b.getNumItemsLeftInBag());
        item = b.drawItem();
        assertEquals(130,b.getNumItemsLeftInBag());
    }

    @Test
    void drawItemsTest() {
        Bag b = new Bag();
        ArrayList<ItemTile> items = new ArrayList<>();

        items = b.drawItems(5);
        assertEquals(5,items.size());
        assertEquals(127,b.getNumItemsLeftInBag());

        items = b.drawItems(7); //non passa drawitem per indexOutOfBoundException
        assertEquals(7,items.size());
        assertEquals(120,b.getNumItemsLeftInBag());

        items = b.drawItems(122);
        assertEquals(120,items.size());
    }

    @Test
    void putItemTest() {
        Bag b = new Bag();
        ItemTile item = b.drawItem();
        assertEquals(131,b.getNumItemsLeftInBag());
        b.putItem(item);
        assertEquals(132,b.getNumItemsLeftInBag());
    }

    @Test
    void getNumItemsLeftInBagTest() {
        Bag b = new Bag();
        assertEquals(132,b.getNumItemsLeftInBag());
        ArrayList<ItemTile> items = new ArrayList<>();
        items = b.drawItems(5);
        assertEquals(127,b.getNumItemsLeftInBag());
    }
}