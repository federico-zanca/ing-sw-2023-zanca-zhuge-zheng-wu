package it.polimi.ingsw.model.commongoals;

import org.junit.jupiter.api.Test;
import it.polimi.ingsw.model.test_commonGoals.BookShelfTest;
import it.polimi.ingsw.model.ItemTile;


import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCard3Test {
    @Test
    void checkEmpty() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard6 cg6 = new CommonGoalCard6(3);
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
        assertFalse(cg6.check(bookshelf));
    }
    @Test
    void check4GroupTrue() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard3 cg3 = new CommonGoalCard3(2);
        String[][] matrix ={
                {"C","P","P","E","E"},
                {"C","P","P","E","E"},
                {"C","G","B","E","E"},
                {"C","G","G","B","C"},
                {"C","C","G","B","C"},
                {"C","C","B","B","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg3.check(bookshelf));
    }

    @Test
    void check4GroupFalse() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard3 cg3 = new CommonGoalCard3(4);
        String[][] matrix ={
                {"C","P","P","E","E"},
                {"C","P","P","E","E"},
                {"C","G","B","E","E"},
                {"C","G","G","B","C"},
                {"C","C","G","B","C"},
                {"C","C","B","G","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg3.check(bookshelf));
    }
}