package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.test_commonGoals.BookShelfTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCard5Test {

    @Test
    void checkEmpty() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard5 cg5 = new CommonGoalCard5(4);
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
        assertFalse(cg5.check(bookshelf));
    }

    @Test
    void check3ColumnsTrue() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard5 cg5 = new CommonGoalCard5(4);
        String[][] matrix ={
                {"G","P","E","E","G"},
                {"C","P","E","E","T"},
                {"G","P","E","E","T"},
                {"B","P","E","E","T"},
                {"B","P","E","E","G"},
                {"B","P","E","E","G"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg5.check(bookshelf));
    }
    @Test
    void checkNot3ColumnsFull() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard5 cg5 = new CommonGoalCard5(4);
        String[][] matrix ={
                {"G","P","E","E","E"},
                {"C","P","E","E","T"},
                {"G","P","E","E","T"},
                {"B","P","E","E","T"},
                {"B","P","E","E","G"},
                {"B","P","E","E","G"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg5.check(bookshelf));
    }
    @Test
    void checkColumnsFullMoreType() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard5 cg5 = new CommonGoalCard5(4);
        String[][] matrix ={
                {"G","P","E","E","G"},
                {"C","P","E","E","P"},
                {"G","P","E","E","T"},
                {"B","P","E","E","T"},
                {"B","P","E","E","C"},
                {"B","P","E","E","G"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg5.check(bookshelf));
    }
}