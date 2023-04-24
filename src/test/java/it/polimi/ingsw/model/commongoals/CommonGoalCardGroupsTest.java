package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonGoalCardGroupsTest {

    @Test
    void checkEmpty() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardGroups cg1 = new CommonGoalCardGroups(4,6,2);
        CommonGoalCardGroups cg2 = new CommonGoalCardGroups(4,4,4);
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
        assertFalse(cg2.check(bookshelf));
    }

    @Test
    void check6GroupsTrue() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardGroups cg1 = new CommonGoalCardGroups(4,6,2);
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
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardGroups cg1 = new CommonGoalCardGroups(4,6,2);
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
    @Test
    void check4GroupTrue() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardGroups cg3 = new CommonGoalCardGroups(2,4,4);
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
        CommonGoalCardGroups cg3 = new CommonGoalCardGroups(2,4,4);
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