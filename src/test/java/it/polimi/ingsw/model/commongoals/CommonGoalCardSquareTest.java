package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCardSquareTest {

    @Test
    void checkSquareTrue() {
        Player p = new Player("Player_1");
        Bookshelf bookshelf = p.getBookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardSquare cg4 = new CommonGoalCardSquare(4);
        String[][] matrix ={
                {"G","G","P","E","E"},
                {"G","P","P","E","E"},
                {"C","C","B","E","E"},
                {"C","C","G","B","C"},
                {"P","P","C","C","B"},
                {"G","G","C","C","B"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        System.out.println(cg4);
        assertTrue(cg4.check(bookshelf));
        assertEquals(8,cg4.peek());
        cg4.takePoints(p);
        assertEquals(8,p.getScore());
        assertTrue(cg4.achievedBy(p));
        assertTrue(cg4.getThoseWhoAchieved().contains(p));
        assertEquals(6,cg4.peek());
    }

    @Test
    void checkSquareFalse() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardSquare cg4 = new CommonGoalCardSquare(4);
        String[][] matrix ={
                {"G","G","P","E","E"},
                {"G","C","P","E","E"},
                {"C","C","B","E","E"},
                {"C","C","B","B","C"},
                {"P","P","C","C","B"},
                {"G","G","C","C","B"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg4.check(bookshelf));
    }

    @Test
    void checkSquareEmpty() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardSquare cg4 = new CommonGoalCardSquare(2);
        String[][] matrix ={
                {"E","E","E","E","E"},
                {"E","E","E","E","E"},
                {"E","E","E","E","E"},
                {"E","E","E","E","E"},
                {"E","E","E","E","E"},
                {"E","E","E","E","E"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg4.check(bookshelf));
    }

    @Test
    void checkSquareAdjacentSame() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardSquare cg4 = new CommonGoalCardSquare(3);
        String[][] matrix ={
                {"G","G","P","E","E"},
                {"G","C","P","E","E"},
                {"B","B","B","E","E"},
                {"B","G","B","B","C"},
                {"C","C","C","C","B"},
                {"C","C","C","C","B"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg4.check(bookshelf));
    }

    @Test
    void checkSquareAdjacentDifferent() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardSquare cg4 = new CommonGoalCardSquare(4);
        String[][] matrix ={
                {"G","G","P","E","E"},
                {"G","C","P","E","E"},
                {"C","C","B","E","E"},
                {"C","B","B","B","C"},
                {"G","G","C","C","B"},
                {"G","G","C","C","B"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg4.check(bookshelf));
    }
}