package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Bookshelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCard4Test {

    @Test
    void checkSquareTrue() {
        Bookshelf bookshelf = new Bookshelf();
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
        Bookshelf bookshelf = new Bookshelf();
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
        Bookshelf bookshelf = new Bookshelf();
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
        Bookshelf bookshelf = new Bookshelf();
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
        Bookshelf bookshelf = new Bookshelf();
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