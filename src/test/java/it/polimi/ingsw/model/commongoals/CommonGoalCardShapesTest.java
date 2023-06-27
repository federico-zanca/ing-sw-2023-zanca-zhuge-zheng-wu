package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.ItemType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCardShapesTest {

    @Test
    void test4Corners() {
        Player p = new Player("player_1");
        Bookshelf bookshelf = p.getBookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardShapes cg2 = new CommonGoalCardShapes(4,"4Corners");
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
        bookshelf.getShelfie()[0][4].setType(ItemType.EMPTY);
        assertFalse(cg2.check(bookshelf));
    }
    @Test
    void testDiagonal() {
        Player p = new Player("player_1");
        Bookshelf bookshelf = p.getBookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardShapes cg = new CommonGoalCardShapes(4,"Diagonal");
        String[][] matrix1 ={
                {"G","P","E","E","G"},
                {"C","G","E","E","T"},
                {"G","G","G","E","T"},
                {"B","P","T","G","T"},
                {"G","P","T","T","G"},
                {"B","P","G","T","G"}
        };
        itemmatrix = bookshelf.stringToMat(matrix1);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg.check(bookshelf));
        bookshelf.getShelfie()[1][1].setType(ItemType.CAT);
        assertFalse(cg.check(bookshelf));
        String[][] matrix2 = {
                {"G","P","E","E","G"},
                {"C","G","E","E","T"},
                {"G","G","G","T","T"},
                {"B","P","T","B","T"},
                {"G","T","P","P","G"},
                {"T","P","G","P","G"}
        };
        itemmatrix = bookshelf.stringToMat(matrix2);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg.check(bookshelf));
        bookshelf.getShelfie()[1][4].setType(ItemType.GAME);
        assertFalse(cg.check(bookshelf));
    }
    @Test
    void testX() {
        Player p = new Player("player_1");
        Bookshelf bookshelf = p.getBookshelf();
        ItemTile[][] itemmatrix;
        CommonGoalCardShapes cg = new CommonGoalCardShapes(4,"X");
        String[][] matrix1 ={
                {"G","P","E","E","E"},
                {"C","G","E","E","E"},
                {"F","G","T","E","E"},
                {"B","G","B","E","E"},
                {"T","B","P","G","P"},
                {"B","P","B","C","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix1);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg.check(bookshelf));
        bookshelf.getShelfie()[4][1].setType(ItemType.CAT);
        assertFalse(cg.check(bookshelf));
        String[][] matrix2 = {
                {"G","P","E","E","E"},
                {"C","G","E","E","E"},
                {"F","G","T","E","E"},
                {"B","G","B","B","E"},
                {"T","B","P","G","P"},
                {"B","P","B","B","C"}
        };
        itemmatrix = bookshelf.stringToMat(matrix2);
        bookshelf.setShelfie(itemmatrix);
        bookshelf.printBookshelf();
        assertTrue(cg.check(bookshelf));
        bookshelf.getShelfie()[3][0].setType(ItemType.GAME);
        assertFalse(cg.check(bookshelf));
    }

    @Test
    void TestPrint(){

        CommonGoalCardShapes cg1 = new CommonGoalCardShapes(4,"X");
        CommonGoalCardShapes cg2 = new CommonGoalCardShapes(4,"4Corners");
        CommonGoalCardShapes cg3 = new CommonGoalCardShapes(4,"Diagonal");

        System.out.println(cg1);
        System.out.println(cg2);
        System.out.println(cg3);

    }

}