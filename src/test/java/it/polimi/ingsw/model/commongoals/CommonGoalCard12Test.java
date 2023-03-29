package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCard12Test {
    @Test
    void checkEmpty() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard12 cg12 = new CommonGoalCard12(4);
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
        assertFalse(cg12.check(bookshelf));
    }

    @Test
    void checkStairDecFromRightUpTrue() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard12 cg12 = new CommonGoalCard12(4);
        String[][] matrix ={
                {"E","E","E","E","T"},
                {"E","E","E","T","T"},
                {"E","E","T","G","F"},
                {"E","T","B","B","B"},
                {"T","P","P","G","P"},
                {"P","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg12.check(bookshelf));
    }
    @Test
    void checkStairDecFromRightDownTrue() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard12 cg12 = new CommonGoalCard12(4);
        String[][] matrix ={
                {"E","E","E","E","E"},
                {"E","E","E","E","T"},
                {"E","E","E","G","F"},
                {"E","E","B","B","B"},
                {"E","P","P","G","P"},
                {"P","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg12.check(bookshelf));
    }
    @Test
    void checkStairDecFromLeftUpTrue() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard12 cg12 = new CommonGoalCard12(4);
        String[][] matrix ={
                {"G","E","E","E","E"},
                {"P","F","E","E","E"},
                {"P","G","B","E","E"},
                {"T","B","B","G","E"},
                {"T","P","P","G","P"},
                {"P","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg12.check(bookshelf));
    }
    @Test
    void checkStairDecFromLeftDownTrue() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard12 cg12 = new CommonGoalCard12(4);
        String[][] matrix ={
                {"E","E","E","E","E"},
                {"P","E","E","E","E"},
                {"P","G","E","E","E"},
                {"T","B","B","E","E"},
                {"T","P","P","G","E"},
                {"P","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg12.check(bookshelf));
    }
    @Test
    void checkStairNot5ColFalse() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard12 cg12 = new CommonGoalCard12(4);
        String[][] matrix ={
                {"E","E","E","E","E"},
                {"E","E","E","E","E"},
                {"E","E","E","E","F"},
                {"E","E","E","B","B"},
                {"E","E","P","G","P"},
                {"E","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg12.check(bookshelf));
    }

    @Test
    void checkStairEmptyColLeftFalse() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard12 cg12 = new CommonGoalCard12(4);
        String[][] matrix ={
                {"G","E","E","E","E"},
                {"P","F","E","E","E"},
                {"P","G","E","E","E"},
                {"T","B","E","G","E"},
                {"T","P","P","G","P"},
                {"P","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg12.check(bookshelf));
    }

    @Test
    void checkStairOverColLeftFalse() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard12 cg12 = new CommonGoalCard12(4);
        String[][] matrix ={
                {"E","E","E","E","E"},
                {"P","E","E","E","E"},
                {"P","G","P","E","E"},
                {"T","B","P","E","E"},
                {"T","P","P","G","E"},
                {"P","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg12.check(bookshelf));
    }
    @Test
    void checkStairOverColRightFalse() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard12 cg12 = new CommonGoalCard12(4);
        String[][] matrix ={
                {"E","E","E","E","T"},
                {"E","E","C","T","T"},
                {"E","E","T","G","F"},
                {"E","T","B","B","B"},
                {"T","P","P","G","P"},
                {"P","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg12.check(bookshelf));
    }
    @Test
    void checkStairEmptyColRightFalse() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard12 cg12 = new CommonGoalCard12(4);
        String[][] matrix ={
                {"E","E","E","E","E"},
                {"E","E","E","E","T"},
                {"E","E","E","G","F"},
                {"E","E","E","B","B"},
                {"E","P","P","G","P"},
                {"P","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg12.check(bookshelf));
    }

}