package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonGoalCard3Test {
    @Test
    void checkEmpty() {
        Bookshelf bookshelf = new Bookshelf();
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
        Bookshelf bookshelf = new Bookshelf();
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
        Bookshelf bookshelf = new Bookshelf();
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