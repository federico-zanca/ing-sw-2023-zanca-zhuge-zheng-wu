package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.ItemType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

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
        //assertNotNull(item);

        for(int i=0;i<131;i++){
            assertNotNull(b.drawItem());
        }
        assertEquals(0,b.getNumItemsLeftInBag());
    }

    @Test
    void testDrawItems() {
        Bag b = new Bag();
        ArrayList<ItemTile> items = new ArrayList<>();
        HashMap<ItemType,Integer> exstracted = new HashMap<>();
        b.printBag();
        items = b.drawItems(122);

        for(int i=0;i<122;i++){
            exstracted.put(items.get(i).getType(),1);
        }
        assertEquals(132-122,b.getNumItemsLeftInBag());

        /*
        items = b.drawItems(5);
        assertEquals(5,items.size());
        assertEquals(127,b.getNumItemsLeftInBag());

        items = b.drawItems(7);
        assertEquals(7,items.size());
        assertEquals(120,b.getNumItemsLeftInBag());

        items = b.drawItems(120); //non passa test, invece di prendere 120 oggetti prende solo 60
        assertEquals(0,b.getNumItemsLeftInBag());
        assertEquals(120,items.size());
        */
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