package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.test_commonGoals.BookShelfTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCard1Test {

    @Test
    void checkEmpty() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard1 cg1 = new CommonGoalCard1(4);
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
        assertFalse(cg1.check(bookshelf));
    }

    @Test
    void check6GroupsTrue() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard1 cg1 = new CommonGoalCard1(4);
        String[][] matrix ={
                {"G","P","E","E","G"},
                {"G","G","E","E","T"},
                {"G","G","T","E","F"},
                {"B","B","T","F","T"},
                {"G","P","F","G","G"},
                {"B","P","C","T","T"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg1.check(bookshelf));
    }

    @Test
    void check6GroupsFalse() {
        BookShelfTest bookshelf = new BookShelfTest();
        ItemTile[][] itemmatrix;
        CommonGoalCard1 cg1 = new CommonGoalCard1(4);
        String[][] matrix ={
                {"G","P","E","E","G"},
                {"G","G","E","E","T"},
                {"G","G","T","E","F"},
                {"B","B","T","F","T"},
                {"G","P","F","G","G"},
                {"B","P","C","F","T"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg1.check(bookshelf));
    }

}