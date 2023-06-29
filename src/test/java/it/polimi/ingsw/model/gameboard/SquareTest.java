package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SquareTest {

    private Square s;
    @BeforeEach
    public void setup(){
        s = new Square(2,3);
    }
    @Test
    void testSquareConstructor1() {
        Square sq=new Square(s);
        assertEquals(sq.getItem(), s.getItem());
        assertEquals(sq.getCoordinates(), s.getCoordinates());
    }

    @Test
    void testSquareConstructor2() {
        Coordinates coord = new Coordinates(1,2);
        Square sq=new Square(coord, ItemType.CAT);

        assertEquals(sq.getCoordinates(), coord);
        assertEquals(sq.getItem().getType(), ItemType.CAT);
    }

    @Test
    void testGetSetItem() {
        assertEquals(ItemType.EMPTY,s.getItem().getType());
        s.setItem(new ItemTile(ItemType.BOOK));
        assertEquals(ItemType.BOOK,s.getItem().getType());
    }

    @Test
    void getCoordinates() {
        assertEquals(2,s.getCoordinates().getRow());
        assertEquals(2,s.getRow());
        assertEquals(3,s.getCoordinates().getColumn());
        assertEquals(3,s.getColumn());
    }

    @Test
    void testPickable() {
        assertFalse(s.isPickable());
        s.setPickable(true);
        assertTrue(s.isPickable());
    }

}