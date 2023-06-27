package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

class GameViewTest {

    @Test
    public void testGameViewConstructorWithoutPlayers() {
        // Create a sample Game model without players
        Game gameModel = new Game();
        GameView gameView = new GameView(gameModel);
        // Verify that the players list is an empty list
        assertTrue(gameView.getPlayers().isEmpty());
    }

    @Test
    public void testGameViewConstructorWithPlayers() {
        // Create a sample Game model with players
        Game gameModel = new Game();
        gameModel.getPlayers().add(new Player("John"));
        gameModel.getPlayers().add(new Player("Alice"));


        // Create a GameView object using the constructor
        GameView gameView = new GameView(gameModel);

        // Verify that the players list is correctly initialized in the GameView object
        assertEquals(gameModel.getPlayers(), gameView.getPlayers());
    }

    @Test
    public void testGameViewConstructorWithLeaderboard() {
        // Create a sample Game model with a leaderboard
        Game gameModel = new Game();
        gameModel.getLeaderboard().put("John", 100);
        gameModel.getLeaderboard().put("Alice", 80);

        GameView gameView = new GameView(gameModel);

        // Verify that the leaderboard is correctly initialized in the GameView object
        assertEquals(gameModel.getLeaderboard(), gameView.getLeaderboard());
    }

    @Test
    public void testGameViewConstructorWithoutLeaderboard() {
        // Create a sample Game model without a leaderboard
        Game gameModel = new Game();
        GameView gameView = new GameView(gameModel);

        // Verify that the leaderboard is an empty LinkedHashMap in the GameView object
        assertTrue(gameView.getLeaderboard().isEmpty());
    }

}