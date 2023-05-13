package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player;
    private Bookshelf testBookshelf;
    private ItemTile[] testTiles;

    @BeforeEach
    void setUp() {
        player = new Player("TestPlayer");
        testBookshelf = player.getBookshelf();
        testTiles = new ItemTile[]{
                new ItemTile(ItemType.TROPHY),
                new ItemTile(ItemType.PLANT),
                new ItemTile(ItemType.FRAME)
        };
    }

    // Tests for addPoints()
    @Test
    void testAddPoints() {
        player.addPoints(5);
        assertEquals(5, player.getScore());
    }

    @Test
    void testAddPointsWithNegativeValue() {
        player.addPoints(-5);
        assertEquals(-5, player.getScore());
    }

    // Tests for calculateScorePersonalGoal()
    @Test
    void testCalculateScorePersonalGoalWithNoMatches() {
        PersonalGoalCard noMatchGoal = new PersonalGoalCard(3);
        testBookshelf.insertItem(testTiles[0],0);
        testBookshelf.insertItem(testTiles[1],4);
        testBookshelf.insertItem(testTiles[2],3);
        assertEquals(0, player.calculateScorePersonalGoal(noMatchGoal));
    }

    @Test
    void testCalculateScorePersonalGoalWithSomeMatches() {
        PersonalGoalCard someMatchGoal = new PersonalGoalCard(1);
        testBookshelf.insertItem(testTiles[0], 2);
        testBookshelf.insertItem(testTiles[1], 0);
        testBookshelf.insertItem(testTiles[2], 1);
        assertEquals(1, player.calculateScorePersonalGoal(someMatchGoal));
    }

    // Tests for calculateAdjacentItemsPoints()
    @Test
    void testCalculateAdjacentItemsPointsWithNoGroups() {
        assertEquals(0, player.calculateAdjacentItemsPoints());
    }

    @Test
    void testCalculateAdjacentItemsPointsWithSomeGroups() {
        testBookshelf.insertItem(testTiles[0],  1);
        testBookshelf.insertItem(testTiles[0],  1);
        testBookshelf.insertItem(testTiles[0],  1);
        testBookshelf.insertItem(testTiles[0],  1);
        assertEquals(3, player.calculateAdjacentItemsPoints());
    }

    // Tests for setHand() and getHand()
    @Test
    void testSetAndGetHand() {
        ArrayList<ItemTile> testHand = new ArrayList<>();
        testHand.add(testTiles[0]);
        testHand.add(testTiles[1]);
        player.setHand(testHand);
        assertEquals(testHand, player.getHand());
    }

    // Tests for toString()
    @Test
    void testToString() {
        assertEquals("Player : TestPlayer", player.toString());
    }

    // Integration tests
    @Test
    void testEndTriggerWhenBookshelfIsNotFull() {
        assertFalse(player.endTrigger());
    }

    @Test
    void testEndTriggerWhenBookshelfIsFull() {
        for (int i = 0; i <= 5; i++) {
            for(int j=0;j<=4;j++)
                testBookshelf.insertItem(testTiles[0],j);
        }
        assertTrue(player.endTrigger());
    }

    @Test
    void testGetScore() {
        // Test new player score
        assertEquals(0, player.getScore());

        // Test adding points and getting score
        player.addPoints(5);
        assertEquals(5, player.getScore());
    }

    @Test
    void testGetBookshelf() {
        assertEquals(testBookshelf, player.getBookshelf());
    }

    @Test
    void testGetUsername() {
        assertEquals("TestPlayer", player.getUsername());
    }
}
