package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SquareTest {

    @Test
    void testGetSetItem() {
        Square s = new Square(2,3);
        assertEquals(ItemType.EMPTY,s.getItem().getType());
        s.setItem(new ItemTile(ItemType.BOOK));
        assertEquals(ItemType.BOOK,s.getItem().getType());
    }

    @Test
    void getCoordinates() {
        Square s = new Square(2,3);
        assertEquals(2,s.getCoordinates().getRow());
        assertEquals(2,s.getRow());
        assertEquals(3,s.getCoordinates().getColumn());
        assertEquals(3,s.getColumn());
    }

    @Test
    void testPickable() {
        Square s = new Square(2,3);
        assertFalse(s.isPickable());
        s.setPickable(true);
        assertTrue(s.isPickable());
    }

}