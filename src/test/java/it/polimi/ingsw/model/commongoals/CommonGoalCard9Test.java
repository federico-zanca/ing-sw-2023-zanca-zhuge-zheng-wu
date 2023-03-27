package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.BookShelfTest;
import it.polimi.ingsw.model.ItemTile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCard9Test {
    @Test
    void checkEmpty() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard9 cg9 = new CommonGoalCard9(4);
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
        assertFalse(cg9.check(bookshelf));
    }

    @Test
    void check2columnsTrue() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard9 cg9 = new CommonGoalCard9(4);
        String[][] matrix ={
                {"G","P","E","E","G"},
                {"C","G","E","E","T"},
                {"F","G","T","G","F"},
                {"B","B","B","B","B"},
                {"T","P","P","G","P"},
                {"P","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg9.check(bookshelf));
    }

    @Test
    void check2columnsEmptyFalse() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard9 cg9 = new CommonGoalCard9(4);
        String[][] matrix ={
                {"G","E","E","E","E"},
                {"G","E","E","E","T"},
                {"G","G","E","E","F"},
                {"B","B","B","B","B"},
                {"G","P","P","G","G"},
                {"B","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg9.check(bookshelf));
    }

    @Test
    void check2columnsLessTypeFalse() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard9 cg9 = new CommonGoalCard9(4);
        String[][] matrix ={
                {"G","E","E","E","G"},
                {"G","G","E","E","T"},
                {"G","G","T","G","F"},
                {"B","P","C","T","B"},
                {"G","P","P","G","G"},
                {"B","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg9.check(bookshelf));
    }
}