package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCard11Test {
    @Test
    void checkEmpty() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard11 cg11 = new CommonGoalCard11(4);
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
        assertFalse(cg11.check(bookshelf));
    }

    @Test
    void checkExactXTrue() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard11 cg11 = new CommonGoalCard11(4);
        String[][] matrix ={
                {"G","P","E","E","E"},
                {"C","G","E","E","E"},
                {"F","G","T","E","E"},
                {"B","G","B","E","E"},
                {"T","B","P","G","P"},
                {"B","P","B","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg11.check(bookshelf));
    }

    @Test
    void checkXTrue() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard11 cg11 = new CommonGoalCard11(4);
        String[][] matrix ={
                {"G","P","E","E","E"},
                {"C","G","E","E","E"},
                {"F","G","T","E","E"},
                {"B","G","B","B","E"},
                {"T","B","P","G","P"},
                {"B","P","B","B","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg11.check(bookshelf));
    }
    @Test
    void checkXFalse() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard11 cg11 = new CommonGoalCard11(4);
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
        assertFalse(cg11.check(bookshelf));
    }
}