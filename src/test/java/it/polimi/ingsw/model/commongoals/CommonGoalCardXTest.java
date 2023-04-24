package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CommonGoalCardXTest {
    @Test
    void checkEmpty() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardX cg11 = new CommonGoalCardX(4);
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
        System.out.println(cg11);
        assertFalse(cg11.check(bookshelf));
    }

    @Test
    void checkExactXTrue() {
        Player p = new Player("Player_1");
        Bookshelf bookshelf = p.getBookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardX cg11 = new CommonGoalCardX(4);
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
        assertEquals(8,cg11.peek());
        cg11.takePoints(p);
        assertEquals(8,p.getScore());
        assertTrue(cg11.achievedBy(p));
        assertTrue(cg11.getThoseWhoAchieved().contains(p));
        assertEquals(6,cg11.peek());    }

    @Test
    void checkXTrue() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardX cg11 = new CommonGoalCardX(4);
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
        CommonGoalCardX cg11 = new CommonGoalCardX(4);
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