package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonGoalCard5Test {

    @Test
    void checkEmpty() {
        Bookshelf bookshelf = new Bookshelf();
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
        Bookshelf bookshelf = new Bookshelf();
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
        Bookshelf bookshelf = new Bookshelf();
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
        Bookshelf bookshelf = new Bookshelf();
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