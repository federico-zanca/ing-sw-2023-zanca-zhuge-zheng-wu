package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Bookshelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCard6Test {

    @Test
    void checkEmpty() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard6 cg6 = new CommonGoalCard6(4);
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
    void check8SameTypeTrue() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard6 cg6 = new CommonGoalCard6(4);
        String[][] matrix ={
                {"G","P","E","E","G"},
                {"C","P","E","E","T"},
                {"G","G","E","E","T"},
                {"B","P","E","E","T"},
                {"G","P","E","E","G"},
                {"B","P","G","F","G"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg6.check(bookshelf));
    }
    @Test
    void check8SameTypeFalse() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard6 cg6 = new CommonGoalCard6(4);
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
        assertFalse(cg6.check(bookshelf));
    }

}