    package it.polimi.ingsw.model.gameboard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CoordinatesTest {

    @Test
    void getRowCol() {
        Coordinates coord = new Coordinates(1,4);
        Coordinates empty=new Coordinates();
        assertEquals(1, coord.getRow());
        assertEquals(4, coord.getColumn());
        coord.setRow(2);
        coord.setColumn(3);
        assertEquals(2, coord.getRow());
        assertEquals(3, coord.getColumn());

    }
}