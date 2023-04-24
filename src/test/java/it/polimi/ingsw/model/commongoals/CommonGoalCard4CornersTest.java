package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCard4CornersTest {
    @Test
    void checkEmpty() {
       Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard4Corners cg2 = new CommonGoalCard4Corners(4);
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
        System.out.println(cg2);
        assertFalse(cg2.check(bookshelf));
    }

    @Test
    void check4CornersTrue() {
        Player p = new Player("player_1");
        Bookshelf bookshelf = p.getBookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard4Corners cg2 = new CommonGoalCard4Corners(4);
        String[][] matrix ={
                {"G","P","E","E","G"},
                {"G","G","E","E","T"},
                {"G","G","T","E","F"},
                {"B","B","T","F","T"},
                {"F","P","F","G","F"},
                {"G","P","C","T","G"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg2.check(bookshelf));
        assertEquals(8,cg2.peek());
        cg2.takePoints(p);
        assertEquals(8,p.getScore());
        assertTrue(cg2.achievedBy(p));
        assertTrue(cg2.getThoseWhoAchieved().contains(p));
        assertEquals(6,cg2.peek());
    }

    @Test
    void check4CornersEmptyFalse() {
       Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard4Corners cg2 = new CommonGoalCard4Corners(4);
        String[][] matrix ={
                {"G","P","E","E","E"},
                {"G","G","E","E","T"},
                {"G","G","T","E","F"},
                {"B","B","T","F","T"},
                {"G","P","F","G","G"},
                {"G","P","C","F","G"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg2.check(bookshelf));
    }
    @Test
    void check4CornersTypeDifferentFalse() {
       Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCard4Corners cg2 = new CommonGoalCard4Corners(4);
        String[][] matrix ={
                {"G","P","E","E","F"},
                {"G","G","E","E","T"},
                {"G","G","T","E","F"},
                {"B","B","T","F","T"},
                {"G","P","F","G","G"},
                {"G","P","C","F","G"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertFalse(cg2.check(bookshelf));

    }
}