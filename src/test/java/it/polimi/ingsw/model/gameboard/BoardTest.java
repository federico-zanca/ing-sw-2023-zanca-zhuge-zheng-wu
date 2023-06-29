package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.exceptions.IllegalDrawException;
import org.junit.Before;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
class BoardTest {
    private Bag bag;

    private Board board;

    public Board getBoard() {
        return board;
    }

    @Before
    public void setUp() {
        board = null;
        bag = null;
    }

    @Test
        //3p and 4p can be repeated here.
    void init2pBoardHasCorrectNumberOfEmptyCells() {
        int counter = 0;
        board = new Board();
        board.initBoard(2);
        for (int i = 0; i < Board.DIMENSIONS; i++) {
            for (int j = 0; j < Board.DIMENSIONS; j++) {
                if (board.getGameboard()[i][j].getItem().getType() == ItemType.EMPTY) {
                    counter++;
                } else if (board.getGameboard()[i][j].getItem().getType() != ItemType.FORBIDDEN) {
                    fail();
                }
            }
        }
        //3p should have expected 8 more, 4p 16 more.
        assertEquals(29, counter);
    }

    @Test
    void refillBoardWhenEmpty() {
        board = new Board();
        bag = new Bag();
        board.initBoard(2);
        ArrayList<ItemTile> items = bag.drawItems(29);
        board.refillBoardWithItems(items);
        int n = board.numCellsToRefill();
        assertEquals(n, 0);
    }
    @Test
    void refillBoardWhenNotEmpty() {
        board = new Board();
        bag = new Bag();

        board.initBoard(2);
        ArrayList<ItemTile> items = bag.drawItems(29);
        board.refillBoardWithItems(items);

        Board board1 = new Board();
        board1.initBoard(2);
        board1 = getBoard();
        items = bag.drawItems(29);
        board.refillBoardWithItems(items);
        for (int i = 0; i < Board.DIMENSIONS; i++) {
            for (int j = 0; j < Board.DIMENSIONS; j++) {
                assertEquals(board.getGameboard()[i][j].getItem().getType(), board1.getGameboard()[i][j].getItem().getType());
            }
        }
    }
    @Test
    void numCellsToRefill() {
        board = new Board();
        board.initBoard(2);
        assertEquals(29,board.numCellsToRefill());
    }

    @Test
    void emptyBoardNeedsRefill() {
        board = new Board();
        board.initBoard(2);
        assertTrue(board.needsRefill());
    }
    @Test
    void singleItemsWithNoAdjacentsBoardNeedsRefill() {
        board = new Board();
        board.initBoard(2);
        ItemTile item = new ItemTile(ItemType.PLANT);
        board.placeItem(item,1,3);
        board.placeItem(item,3,3);
        board.placeItem(item,2,4);
        assertTrue(board.needsRefill());
    }
    @Test
    void filledBoardNeedsRefill() {
        board = new Board();
        bag = new Bag();
        board.initBoard(2);
        board.refillBoardWithItems(bag.drawItems(29));
        assertFalse(board.needsRefill());
    }

    @Test
    void placeItemWhenCellIsEmpty(){
        board = new Board();
        board.initBoard(2);
        ItemTile item = new ItemTile(ItemType.PLANT);
        board.placeItem(item, 1, 3);
    }

    @Test()
    void pickItem() throws IllegalDrawException {
        board = new Board();

        for(int i=2; i<=5; i++){
            board.initBoard(i);
            //Placing items on a square and on adjacent squares.
            ItemTile item = new ItemTile(ItemType.BOOK);
            board.placeItem(item,1,3);
            item = new ItemTile(ItemType.FRAME);
            board.placeItem(item,1,4);
            item = board.pickItem(1,3);
            assertEquals(ItemType.BOOK,item.getType());
            assertNotNull(item);
        }

    }

    @Disabled
    void pickItems() {
    }
    @Test
    void testDoesSquareHaveFreeSide() throws IllegalDrawException {
        board = new Board();
        bag = new Bag();
        Square[][] gameboard=board.getGameboard();
        board.initBoard(4);
        ArrayList<Square>sq=new ArrayList<>();
        sq.add(gameboard[3][3]);
        board.refillBoardWithItems(bag.drawItems(board.numCellsToRefill()));
        board.pickItems(sq);
        //After picking one item, the squares adjacent should have a free side.
        assertTrue(board.doesSquareHaveFreeSide(3,2));
        assertTrue(board.doesSquareHaveFreeSide(3,4));
        assertTrue(board.doesSquareHaveFreeSide(2,3));
        assertTrue(board.doesSquareHaveFreeSide(4,3));
        //the squares on the board edge all have free sides.(manually repeated test on all squares)
        assertTrue(board.doesSquareHaveFreeSide(1,3));
        for(int i= 1; i<8; i++){
            for(int j=1; j<8; j++){
                if ((!gameboard[i+1][j].getItem().hasSomething() || !gameboard[i-1][j].getItem().hasSomething() || !gameboard[i][j-1].getItem().hasSomething() || !gameboard[i][j+1].getItem().hasSomething()) && gameboard[i][j].getItem().hasSomething()){
                    assertTrue(board.doesSquareHaveFreeSide(i, j));
                }

            }

        }
    }

    @Test
    void disableAllSquares() {
    board = new Board();
    board.initBoard(2);
    board.enableSquaresWithFreeSide();
    board.disableAllSquares();
        for(int i=0; i<Board.DIMENSIONS; i++){
            for(int j=0; j<Board.DIMENSIONS; j++){
                assertFalse(board.getGameboard()[i][j].isPickable());
            }
        }
    }

    @Test
    void testPickItems() throws IllegalDrawException {
        Board board = new Board();
        board.initBoard(2);
        ArrayList<Square> items = new ArrayList<>();
        Square firstSquare = board.getSquare(new Coordinates(1,3));
        Square secondSquare = board.getSquare(new Coordinates(1,4));
        ItemTile firstItem = firstSquare.getItem();
        ItemTile secondItem = secondSquare.getItem();
        items.add(firstSquare);
        items.add(secondSquare);
        ArrayList<ItemTile> hand = board.pickItems(items);
        assertEquals(firstItem.getType(),hand.get(0).getType());
        assertEquals(secondItem.getType(),hand.get(1).getType());
    }

}