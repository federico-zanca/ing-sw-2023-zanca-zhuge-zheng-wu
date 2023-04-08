package it.polimi.ingsw.model.gameboard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CoordinatesTest {

    @Test
    void getRowCol() {
        Coordinates coord = new Coordinates(1,4);
        assertEquals(1, coord.getRow());
        assertEquals(4, coord.getColumn());
    }
}