package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCardLimitTypesTest {

    @Test
    void checkEmpty() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardLimitTypes cg5 = new CommonGoalCardLimitTypes(4,3,1,3,"col");
        CommonGoalCardLimitTypes cg8 = new CommonGoalCardLimitTypes(4,4,1,3,"row");
        CommonGoalCardLimitTypes cg9 = new CommonGoalCardLimitTypes(4,2,6,6,"col");
        CommonGoalCardLimitTypes cg10 = new CommonGoalCardLimitTypes(4,2,5,5,"row");
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
        System.out.println(cg5);
        assertFalse(cg5.check(bookshelf));
        System.out.println(cg8);
        assertFalse(cg8.check(bookshelf));
        System.out.println(cg9);
        assertFalse(cg9.check(bookshelf));
        System.out.println(cg10);
        assertFalse(cg10.check(bookshelf));
    }

    @Test
    void check3ColumnsTrue() {
        Player p = new Player("Player_1");
        Bookshelf bookshelf = p.getBookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardLimitTypes cg5 = new CommonGoalCardLimitTypes(4,3,1,3,"col");
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
        assertTrue(cg5.check(p.getBookshelf()));
        assertEquals(8,cg5.peek());
        cg5.takePoints(p);
        assertEquals(8,p.getScore());
        assertTrue(cg5.achievedBy(p));
        assertTrue(cg5.getThoseWhoAchieved().contains(p));
        assertEquals(6,cg5.peek());
    }
    @Test
    void checkNot3ColumnsFull() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardLimitTypes cg5 = new CommonGoalCardLimitTypes(4,3,1,3,"col");
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
    void check3ColumnsFullMoreType() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardLimitTypes cg5 = new CommonGoalCardLimitTypes(4,3,1,3,"col");
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

    @Test
    void check4rowsTrue() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardLimitTypes cg8 = new CommonGoalCardLimitTypes(4,4,1,3,"row");
        String[][] matrix ={
                {"G","P","E","E","G"},
                {"G","G","E","E","T"},
                {"G","G","T","G","F"},
                {"B","B","B","B","B"},
                {"G","P","P","G","G"},
                {"B","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg8.check(bookshelf));
    }

    @Test
    void check4rowsEmptyFalse() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardLimitTypes cg8 = new CommonGoalCardLimitTypes(4,4,1,3,"row");
        String[][] matrix ={
                {"G","P","E","E","G"},
                {"G","G","E","E","T"},
                {"G","G","E","E","F"},
                {"B","B","B","B","B"},
                {"G","P","P","G","G"},
                {"B","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg8.check(bookshelf));
    }

    @Test
    void check4rowsMoreTypeFalse() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardLimitTypes cg8 = new CommonGoalCardLimitTypes(4,4,1,3,"row");
        String[][] matrix ={
                {"G","E","E","E","G"},
                {"G","G","E","E","T"},
                {"G","G","T","G","F"},
                {"B","P","C","T","B"},
                {"G","P","P","G","G"},
                {"B","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg8.check(bookshelf));
    }

    @Test
    void check2columnsTrue() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardLimitTypes cg9 = new CommonGoalCardLimitTypes(4,2,6,6,"col");
        String[][] matrix ={
                {"G","P","E","E","G"},
                {"C","G","E","E","T"},
                {"F","G","T","G","F"},
                {"B","B","B","B","B"},
                {"T","P","P","G","P"},
                {"P","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg9.check(bookshelf));
    }

    @Test
    void check2columnsEmptyFalse() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardLimitTypes cg9 = new CommonGoalCardLimitTypes(4,2,6,6,"col");
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
        assertFalse(cg9.check(bookshelf));
    }

    @Test
    void check2columnsLessTypeFalse() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardLimitTypes cg9 = new CommonGoalCardLimitTypes(4,2,6,6,"col");
        String[][] matrix ={
                {"G","E","E","E","G"},
                {"G","G","E","E","T"},
                {"G","G","T","G","F"},
                {"B","P","C","T","B"},
                {"G","P","P","G","G"},
                {"B","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg9.check(bookshelf));
    }

    @Test
    void check2rowsTrue() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardLimitTypes cg10 = new CommonGoalCardLimitTypes(4,2,5,5,"row");
        String[][] matrix ={
                {"G","P","E","E","G"},
                {"C","G","E","E","T"},
                {"F","G","T","P","C"},
                {"B","B","B","B","B"},
                {"T","P","B","G","C"},
                {"P","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg10.check(bookshelf));
    }

    @Test
    void check2rowsEmptyFalse() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardLimitTypes cg10 = new CommonGoalCardLimitTypes(4,2,5,5,"row");
        String[][] matrix ={
                {"G","E","E","E","E"},
                {"G","E","E","E","T"},
                {"G","G","E","E","F"},
                {"B","B","E","E","B"},
                {"G","P","E","E","G"},
                {"B","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg10.check(bookshelf));
    }

    @Test
    void check2rowsLessTypeFalse() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardLimitTypes cg10 = new CommonGoalCardLimitTypes(4,2,5,5,"row");
        String[][] matrix ={
                {"G","E","E","E","G"},
                {"G","G","E","E","T"},
                {"G","G","T","G","F"},
                {"B","P","T","T","B"},
                {"G","P","P","G","G"},
                {"B","P","C","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg10.check(bookshelf));
    }
}