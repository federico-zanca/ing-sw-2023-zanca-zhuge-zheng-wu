package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commongoals.CommonGoalCard1;
import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.gameboard.Board;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void testEndTrigger() {
        // creo player e BookShelf, setBookshelf a player e chiamo endtrigger da player, verifica se corretto
        Player p = new Player("player_1");
        assertFalse(p.endTrigger());

        Bookshelf bookshelf = p.getBookshelf();
        ItemTile[][] itemmatrix;
        String[][] matrix ={
                {"G","P","T","C","G"},
                {"G","G","F","C","T"},
                {"G","G","T","B","F"},
                {"B","B","T","F","T"},
                {"G","P","F","G","G"},
                {"B","P","C","F","T"}
        };
        itemmatrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(itemmatrix);
        assertTrue(p.endTrigger());
    }

    @Test
    void testGetScore() {
        Player p = new Player("player_1");
        assertEquals(0,p.getScore());
    }

    @Test
    void testAddPoints() {
        Player p = new Player("player_1");
        p.addPoints(10);
        assertEquals(10, p.getScore());
    }

    @Test
    void testGetBookshelf() {
        Player p = new Player("player_1");
        Bookshelf bookshelf = p.getBookshelf();
        ItemTile[][] matrix = bookshelf.getShelfie();
        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 5; j++){
                assertTrue(matrix[i][j].isEmpty());
            }
        }
        matrix[5][3].setType(ItemType.CAT);
        matrix[4][3].setType(ItemType.CAT);
        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 5; j++){
                if ((i == 5 || i == 4) && j == 3){
                    assertEquals(ItemType.CAT,matrix[i][j].getType());
                }else {
                    assertTrue(matrix[i][j].isEmpty());
                }
            }
        }
    }

    @Test
    void testTakeFirstItem() {
        Player p = new Player("player_1");
        Board b = new Board();
        b.initBoard(4);
        ItemTile item = new ItemTile(ItemType.BOOK);
        b.placeItem(item,2,3);
        b.placeItem(item,2,4);
        ItemTile item1 = new ItemTile(ItemType.CAT);
        b.placeItem(item1,2,5);
        assertNull(p.takeFirstItem(b,4,5));
        assertEquals(ItemType.BOOK, p.takeFirstItem(b,2,3).getType());
    }

    @Test
    void testTakeOtherItem() {
        Player p = new Player("player_1");
        Board b = new Board();
        b.initBoard(4);
        ItemTile item = new ItemTile(ItemType.BOOK);
        b.placeItem(item,2,3);
        item = new ItemTile(ItemType.BOOK);
        b.placeItem(item,2,4);
        ItemTile item1 = new ItemTile(ItemType.CAT);
        b.placeItem(item1,2,5);

        ItemTile firstItem = p.takeFirstItem(b,2,3);
        assertNull(p.takeOtherItem(b,2,6));
        ItemTile otherItem = p.takeOtherItem(b,2,4);
        assertEquals(ItemType.BOOK, otherItem.getType());
    }

    @Test
    void testGetSetPersonalGoal() {
        Player p = new Player("player_1");
        assertNull(p.getPersonalGoal());
        PersonalGoalCard personalgoal = new PersonalGoalCard(4);
        p.setPersonalGoal(personalgoal);
        ItemType[][] obj = new ItemType[6][5];
        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 5; j++){
                if (i == 0 && j == 4){
                    obj[i][j] = ItemType.GAME;
                }else if (i == 2 && j == 0){
                    obj[i][j] = ItemType.TROPHY;
                }else if (i == 2 && j == 2){
                    obj[i][j] = ItemType.FRAME;
                }else if (i == 3 && j == 3){
                    obj[i][j] = ItemType.PLANT;
                }else if (i == 4 && j == 1){
                    obj[i][j] = ItemType.BOOK;
                }else if (i == 4 && j == 2){
                    obj[i][j] = ItemType.CAT;
                }else{
                    obj[i][j] = ItemType.EMPTY;
                }
            }
        }
        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 5; j++) {
                if (obj[i][j] != ItemType.EMPTY) {
                    assertEquals(obj[i][j], p.getPersonalGoal().getObjective(i, j));
                }
            }
        }
    }

    @Test
    void testGetUsername() {
        Player p = new Player("player_1");
        assertEquals("player_1",p.getUsername());
    }

    @Test
    void testCalculateScorePersonalGoal() {
        Player p = new Player("player_1");
        PersonalGoalCard personal_goal = new PersonalGoalCard(4);
        p.setPersonalGoal(personal_goal);
        Bookshelf bookshelf = p.getBookshelf();
        ItemTile[][] matrix = bookshelf.getShelfie();
        assertEquals(0,p.calculateScorePersonalGoal());
        matrix[4][1].setType(ItemType.BOOK);
        assertEquals(1,p.calculateScorePersonalGoal());
        matrix[4][2].setType(ItemType.CAT);
        assertEquals(2,p.calculateScorePersonalGoal());
        matrix[2][2].setType(ItemType.FRAME);
        assertEquals(4,p.calculateScorePersonalGoal());
        matrix[0][4].setType(ItemType.GAME);
        assertEquals(6,p.calculateScorePersonalGoal());

        matrix[2][0].setType(ItemType.TROPHY);
        assertEquals(9,p.calculateScorePersonalGoal());
        matrix[3][3].setType(ItemType.PLANT);
        assertEquals(12,p.calculateScorePersonalGoal());

        personal_goal = new PersonalGoalCard(1);
        p.setPersonalGoal(personal_goal);
        matrix[5][2].setType(ItemType.TROPHY);
        assertEquals(1,p.calculateScorePersonalGoal());
        matrix[2][3].setType(ItemType.BOOK);
        assertEquals(2,p.calculateScorePersonalGoal());
    }

    @Test
    void testCalculateAdjacentItemsPoints() {
        Player p = new Player("player_1");
        Bookshelf bookshelf = p.getBookshelf();
        ItemTile[][] matrix = bookshelf.getShelfie();
        for (int i = 0; i < 4; i++){
            matrix[1][i].setType(ItemType.PLANT);
        }
        matrix[0][0].setType(ItemType.PLANT);
        matrix[0][1].setType(ItemType.PLANT);
        matrix[1][4].setType(ItemType.TROPHY);

        matrix[2][0].setType(ItemType.FRAME);
        matrix[2][1].setType(ItemType.FRAME);
        matrix[2][2].setType(ItemType.PLANT);
        matrix[2][3].setType(ItemType.PLANT);
        matrix[2][4].setType(ItemType.BOOK);

        matrix[3][0].setType(ItemType.FRAME);
        matrix[3][1].setType(ItemType.GAME);
        matrix[3][2].setType(ItemType.GAME);
        matrix[3][3].setType(ItemType.GAME);
        matrix[3][4].setType(ItemType.BOOK);

        matrix[4][0].setType(ItemType.FRAME);
        matrix[4][1].setType(ItemType.TROPHY);
        matrix[4][2].setType(ItemType.CAT);
        matrix[4][3].setType(ItemType.CAT);
        matrix[4][4].setType(ItemType.CAT);

        matrix[5][0].setType(ItemType.TROPHY);
        matrix[5][1].setType(ItemType.TROPHY);
        matrix[5][2].setType(ItemType.TROPHY);
        matrix[5][3].setType(ItemType.CAT);
        matrix[5][4].setType(ItemType.CAT);

        assertEquals(21, p.calculateAdjacentItemsPoints());
    }
}