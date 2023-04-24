package it.polimi.ingsw.model;
import it.polimi.ingsw.model.enumerations.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bag class tests")
public class BagTest {
    private Bag bag;

    @BeforeEach
    public void setUp() {
        bag = new Bag();
    }
    @Nested
    @DisplayName("Tests for emptyBag method")
    public class EmptyBagTests {
        @Test
        @DisplayName("Bag is initially not empty")
        public void testInitialBagNotEmpty() {
            assertFalse(bag.emptyBag());
        }

        @Test
        @DisplayName("Bag is empty after removing all items")
        public void testEmptyBag() {
            bag.drawItems(132);
            assertEquals(0,bag.getNumItemsLeftInBag());
        }
    }

    @Nested
    @DisplayName("Tests for drawItem and drawItems methods")
    public class DrawItemTests {
        @Test
        @DisplayName("Drawing all items from bag makes it empty")
        public void testDrawAllItems() {
            int numItems = bag.getNumItemsLeftInBag();
            ArrayList<ItemTile> items = bag.drawItems(numItems);
            assertEquals(numItems, items.size());
            assertTrue(bag.emptyBag());
            assertNull(bag.drawItem());
        }

        @Test
        @DisplayName("Drawing more items than in bag returns only remaining items")
        public void testDrawMoreItemsThanInBag() {
            int numItems = bag.getNumItemsLeftInBag();
            ArrayList<ItemTile> items = bag.drawItems(numItems + 1);
            assertEquals(numItems, items.size());
        }
    }

    @Nested
    @DisplayName("Tests for putItem method")
    public class PutItemTest {
        @Test
        @DisplayName("Adding an item updates itemTiles and availableItems")
        public void testAddItem() {
            ItemTile item = new ItemTile(ItemType.CAT);
            bag.putItem(item);

            HashMap<ItemType, Integer> itemTiles = bag.getItemTiles();
            assertEquals(23, itemTiles.get(ItemType.CAT).intValue());
        }
    }

    @Nested
    @DisplayName("Tests for getNumItemsLeftInBag method")
    public class NumItemsLeftInBagTest {
        @Test
        @DisplayName("getNumItemsLeftInBag returns correct number of items")
        public void testGetNumItemsLeftInBag() {
            int numItems = bag.getNumItemsLeftInBag();
            assertEquals(numItems, Bag.INIT_ITEMS_NUM * ItemType.getValues().size());

            ArrayList<ItemTile> items = bag.drawItems(5);
            numItems = bag.getNumItemsLeftInBag();
            assertEquals(numItems, Bag.INIT_ITEMS_NUM * ItemType.getValues().size() - 5);
        }
    }
}
