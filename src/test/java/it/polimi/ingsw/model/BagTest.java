package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.ItemType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {

    @Test
    void testEmptyBag() {
        Bag b = new Bag();
        //Test when bag is full.
        assertFalse(b.emptyBag());
        b.drawItems(132);
        //test when bag is empty.
        assertTrue(b.emptyBag());
    }

    @Test
    void testDrawItem() {
        Bag b = new Bag();
        ItemTile item = b.drawItem();
        //after drawing an item, the bag(itemTiles hashmap) should have 1 less item of that type.
        assertEquals(21, b.getItemTiles().get(item.getType()));
        //and other Itemtypes should not have changed quantity
        for (ItemType t : b.getAvailableItems()) {
            if (t != item.getType()) {
                assertEquals(22, b.getItemTiles().get(t));
            }
        }
        //set the hashmap (ItemTiles) all keys at 0, and drawitem should draw null.
        for(ItemType t : ItemType.getValues()){
            b.getItemTiles().remove(t);
            b.getAvailableItems().remove(t);
            //b.getItemTiles().put(t, 0);
        }
        assertEquals(0,b.getAvailableItems().size());
        assertNull(b.drawItem());
    }
    @Test
    void testDrawItems() {
        Bag b = new Bag();
        ArrayList<ItemTile> items;
        //draws all items in the bag
        items = b.drawItems(132);
        //all the item drawn should be not null.
        for(ItemTile t : items){
            assertNotNull(t);
        }
        //for every Itemtype in the bag, the quantity should be zero.
        for(ItemType t : ItemType.getValues()){
           assertEquals(0,b.getItemTiles().get(t));
        }
        //no more availableitems.
        assertEquals(0,b.getAvailableItems().size());
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
        b.drawItems(5);
        assertEquals(127,b.getNumItemsLeftInBag());
    }
}