package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.BookShelfTest;
import it.polimi.ingsw.model.ItemTile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCard10Test {
    @Test
    void checkEmpty() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard10 cg10 = new CommonGoalCard10(4);
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
        assertFalse(cg10.check(bookshelf));
    }

    @Test
    void check2rowsTrue() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard10 cg10 = new CommonGoalCard10(4);
        String[][] matrix ={
                {"G","P","E","E","G"},
                {"C","G","E","E","T"},
                {"F","G","T","P","C"},
                {"B","B","B","B","B"},
                {"T","P","B","G","C"},
                {"P","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg10.check(bookshelf));
    }

    @Test
    void check2rowsEmptyFalse() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard10 cg10 = new CommonGoalCard10(4);
        String[][] matrix ={
                {"G","E","E","E","E"},
                {"G","E","E","E","T"},
                {"G","G","E","E","F"},
                {"B","B","E","E","B"},
                {"G","P","E","E","G"},
                {"B","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg10.check(bookshelf));
    }

    @Test
    void check2rowsLessTypeFalse() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard10 cg10 = new CommonGoalCard10(4);
        String[][] matrix ={
                {"G","E","E","E","G"},
                {"G","G","E","E","T"},
                {"G","G","T","G","F"},
                {"B","P","T","T","B"},
                {"G","P","P","G","G"},
                {"B","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg10.check(bookshelf));
    }
}