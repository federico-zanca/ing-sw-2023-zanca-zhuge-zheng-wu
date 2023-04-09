package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.enumerations.ItemType.*;
import static org.junit.jupiter.api.Assertions.*;

class ItemTileTest {

    @Test
    void testSetType() {
        ItemTile item = new ItemTile(EMPTY);
        item.setType(BOOK);
        assertEquals(BOOK,item.getType());
    }

    @Test
    void testSetGroupId() {
        ItemTile item = new ItemTile(FRAME);
        item.setGroupId(3);
        assertEquals(3,item.getGroupId());
        item.setGroupId(1);
        assertEquals(1,item.getGroupId());
        item.setGroupId(6);
        assertEquals(6,item.getGroupId());
    }

    @Test
    void testGetGroupId() {
        ItemTile item = new ItemTile(FRAME);
        assertEquals(0,item.getGroupId());
    }

    @Test
    void testGetType() {
        ItemTile item = new ItemTile(FRAME);
        assertEquals(FRAME,item.getType());
    }

    @Test
    void testIsEmpty() {
        ItemTile item = new ItemTile(EMPTY);
        assertTrue(item.isEmpty());
        item.setType(BOOK);
        assertFalse(item.isEmpty());
    }

    @Test
    void testIsForbidden() {
        ItemTile item = new ItemTile(FORBIDDEN);
        assertTrue(item.isForbidden());
        item.setType(EMPTY);
        assertFalse(item.isForbidden());
    }

    @Test
    void testHasSomething() {
        ItemTile item = new ItemTile(FORBIDDEN);
        assertFalse(item.hasSomething());
        item.setType(EMPTY);
        assertFalse(item.hasSomething());
        item.setType(BOOK);
        assertTrue(item.hasSomething());
    }
}