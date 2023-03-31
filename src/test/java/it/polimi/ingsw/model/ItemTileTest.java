package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.ItemType;
import org.junit.jupiter.api.Test;

import java.awt.print.Book;

import static it.polimi.ingsw.model.enumerations.ItemType.*;
import static org.junit.jupiter.api.Assertions.*;

class ItemTileTest {

    @Test
    void setType() {
        ItemTile item = new ItemTile(EMPTY);
        item.setType(BOOK);
        assertEquals(BOOK,item.getType());
    }

    @Test
    void setGroupId() {
        ItemTile item = new ItemTile(FRAME);
        item.setGroupId(3);
        assertEquals(3,item.getGroupId());
        item.setGroupId(1);
        assertEquals(1,item.getGroupId());
        item.setGroupId(6);
        assertEquals(6,item.getGroupId());
    }

    @Test
    void getGroupId() {
        ItemTile item = new ItemTile(FRAME);
        assertEquals(0,item.getGroupId());
    }

    @Test
    void getType() {
        ItemTile item = new ItemTile(FRAME);
        assertEquals(FRAME,item.getType());
    }

    @Test
    void isEmpty() {
        ItemTile item = new ItemTile(EMPTY);
        assertTrue(item.isEmpty());
        item.setType(BOOK);
        assertFalse(item.isEmpty());
    }

    @Test
    void isForbidden() {
        ItemTile item = new ItemTile(FORBIDDEN);
        assertTrue(item.isForbidden());
        item.setType(EMPTY);
        assertFalse(item.isForbidden());
    }

    @Test
    void hasSomething() {
        ItemTile item = new ItemTile(FORBIDDEN);
        assertFalse(item.hasSomething());
        item.setType(EMPTY);
        assertFalse(item.hasSomething());
        item.setType(BOOK);
        assertTrue(item.hasSomething());
    }
}