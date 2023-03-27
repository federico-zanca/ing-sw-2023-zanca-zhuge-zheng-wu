package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.test_commonGoals.BookShelfTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCard2Test {
    @Test
    void checkEmpty() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard2 cg2 = new CommonGoalCard2(4);
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
        assertFalse(cg2.check(bookshelf));
    }

    @Test
    void check4CornersTrue() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard2 cg2 = new CommonGoalCard2(4);
        String[][] matrix ={
                {"G","P","E","E","G"},
                {"G","G","E","E","T"},
                {"G","G","T","E","F"},
                {"B","B","T","F","T"},
                {"F","P","F","G","F"},
                {"G","P","C","T","G"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg2.check(bookshelf));
    }

    @Test
    void check4CornersEmptyFalse() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard2 cg2 = new CommonGoalCard2(4);
        String[][] matrix ={
                {"G","P","E","E","E"},
                {"G","G","E","E","T"},
                {"G","G","T","E","F"},
                {"B","B","T","F","T"},
                {"G","P","F","G","G"},
                {"G","P","C","F","G"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg2.check(bookshelf));
    }
    @Test
    void check4CornersTypeDifferentFalse() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard2 cg2 = new CommonGoalCard2(4);
        String[][] matrix ={
                {"G","P","E","E","F"},
                {"G","G","E","E","T"},
                {"G","G","T","E","F"},
                {"B","B","T","F","T"},
                {"G","P","F","G","G"},
                {"G","P","C","F","G"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg2.check(bookshelf));

    }
}