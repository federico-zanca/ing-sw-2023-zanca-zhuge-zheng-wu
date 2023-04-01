package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {

    @Test
    void testEmptyBag() {
        Bag b = new Bag();
        assertFalse(b.emptyBag());
        for (int k = 0; k < 132; k++){
            b.drawItem();
        }
        assertTrue(b.emptyBag());
    }

    @Test
    void testDrawItem() {
        Bag b = new Bag();
        ItemTile item = b.drawItem();
        assertNotNull(item);
        assertEquals(131,b.getNumItemsLeftInBag());
        item = b.drawItem();
        assertNotNull(item);
        assertEquals(130,b.getNumItemsLeftInBag());
    }

    @Test
    void testDrawItems() {
        Bag b = new Bag();
        ArrayList<ItemTile> items = new ArrayList<>();

        items = b.drawItems(5);
        assertEquals(5,items.size());
        assertEquals(127,b.getNumItemsLeftInBag());

        items = b.drawItems(7);
        assertEquals(7,items.size());
        assertEquals(120,b.getNumItemsLeftInBag());

        //items = b.drawItems(66);
        //assertEquals(66,items.size());
        //assertEquals(66,b.getNumItemsLeftInBag());

        //items = b.drawItems(33);
        //assertEquals(33,items.size());
        //assertEquals(33,b.getNumItemsLeftInBag());

        //items = b.drawItems(17);
        //assertEquals(17,items.size());
        //assertEquals(16,b.getNumItemsLeftInBag());

        //items = b.drawItems(9);
        //assertEquals(9,items.size());
        //assertEquals(7,b.getNumItemsLeftInBag());
        //items = b.drawItems(60);
        //assertEquals(60,b.getNumItemsLeftInBag());
        //assertEquals(60,items.size());

        //items = b.drawItems(59);
        //assertEquals(1,b.getNumItemsLeftInBag());
        //assertEquals(59,items.size());

        items = b.drawItems(120); //non passa test, invece di prendere 120 oggetti prende solo 60
        assertEquals(0,b.getNumItemsLeftInBag());
        assertEquals(120,items.size());
    }

    @Test
    void testPutItem() {
        Bag b = new Bag();
        ItemTile item = b.drawItem();
        assertEquals(131,b.getNumItemsLeftInBag());
        b.putItem(item);
        assertEquals(132,b.getNumItemsLeftInBag());
    }

    @Test
    void testGetNumItemsLeftInBag() {
        Bag b = new Bag();
        assertEquals(132,b.getNumItemsLeftInBag());
        ArrayList<ItemTile> items = new ArrayList<>();
        items = b.drawItems(5);
        assertEquals(127,b.getNumItemsLeftInBag());
    }
}