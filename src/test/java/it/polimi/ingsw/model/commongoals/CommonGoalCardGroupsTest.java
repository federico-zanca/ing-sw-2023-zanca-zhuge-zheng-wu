package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CommonGoalCardGroupsTest {

    @Test
    void checkEmpty() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardGroups cg1 = new CommonGoalCardGroups(4,6,2);
        CommonGoalCardGroups cg3 = new CommonGoalCardGroups(4,4,4);
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
        System.out.println(cg1);
        assertFalse(cg1.check(bookshelf));
        System.out.println(cg3);
        assertFalse(cg3.check(bookshelf));
    }

    @Test
    void check6GroupsTrue() {
        Player p = new Player("Player_1");
        Bookshelf bookshelf = p.getBookshelf();
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
        assertEquals(8,cg1.peek());
        cg1.takePoints(p);
        assertEquals(8,p.getScore());
        assertTrue(cg1.achievedBy(p));
        assertTrue(cg1.getThoseWhoAchieved().contains(p));
        assertEquals(6,cg1.peek());
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
        Player p = new Player("Player_1");
        Bookshelf bookshelf = p.getBookshelf();
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
        assertEquals(8,cg3.peek());
        cg3.takePoints(p);
        assertEquals(8,p.getScore());
        assertTrue(cg3.achievedBy(p));
        assertTrue(cg3.getThoseWhoAchieved().contains(p));
        assertEquals(4,cg3.peek());
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