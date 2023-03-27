package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.test_commonGoals.BookShelfTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCard4Test {

    @Test
    void checkSquareTrue() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard4 cg4 = new CommonGoalCard4(4);
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
        assertTrue(cg4.check(bookshelf));
    }

    @Test
    void checkSquareFalse() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard4 cg4 = new CommonGoalCard4(4);
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
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard4 cg4 = new CommonGoalCard4(2);
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
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard4 cg4 = new CommonGoalCard4(3);
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
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard4 cg4 = new CommonGoalCard4(4);
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