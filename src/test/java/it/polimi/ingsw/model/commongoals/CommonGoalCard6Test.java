package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        System.out.println(cg6);
        assertFalse(cg6.check(bookshelf));
    }

    @Test
    void check8SameTypeTrue() {
        Player p = new Player("Player_1");
        Bookshelf bookshelf = p.getBookshelf();
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
        assertEquals(8,cg6.peek());
        cg6.takePoints(p);
        assertEquals(8,p.getScore());
        assertTrue(cg6.achievedBy(p));
        assertTrue(cg6.getThoseWhoAchieved().contains(p));
        assertEquals(6,cg6.peek());
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