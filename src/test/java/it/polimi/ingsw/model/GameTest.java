package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.distributed.Lobby;
import it.polimi.ingsw.distributed.ServerImpl;
import it.polimi.ingsw.model.enumerations.GamePhase;
import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.enumerations.TurnPhase;
import it.polimi.ingsw.model.exceptions.GameNotReadyException;
import it.polimi.ingsw.model.exceptions.InvalidCommandException;
import it.polimi.ingsw.model.exceptions.InvalidUsernameException;
import it.polimi.ingsw.model.exceptions.LobbyNotFoundException;
import it.polimi.ingsw.model.gameboard.Coordinates;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.connectionmessage.CreateLobbyRequest;
import it.polimi.ingsw.network.message.connectionmessage.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game testGame;
    private Player testPlayer;
    private GameController gamecontroller;
    private Lobby lobby;
    private ServerImpl server;
    private ClientImpl client;

    @BeforeEach
    void setUp() throws RemoteException, LobbyNotFoundException, InterruptedException {
        server = new ServerImpl();
        client = new ClientImpl(server);
        server.register(client);
        server.update(client, new LoginRequest("c1"));
        server.update(client, new CreateLobbyRequest("testLobby"));
        sleep(100);
        lobby = server.getLobbyByName("testLobby");
        testGame = lobby.getController().getModel();
        gamecontroller = lobby.getController();
    }

    @Test
    void GameInitTest() {
        Player[] players = new Player[4];
        String[] names = new String[]{"Alpha", "Beta", "Gamma", "Delta"};
        ArrayList<Player> addedPlayers = new ArrayList<>();
        ArrayList<String> addedNames = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            players[i] = new Player(names[i]);
            testGame.addPlayer(players[i]);
            addedPlayers.add(players[i]);
            addedNames.add(names[i]);
            assertEquals(testGame.getPlayers(), addedPlayers);
            assertEquals(testGame.getPlayersUsernames(), addedNames);
            assertEquals(testGame.getCurrNumOfPlayers(), i + 1);
            assertEquals(testGame.getPlayerByUsername(names[i]), players[i]);

        }
        assertEquals(testGame.getGamePhase(), GamePhase.LOGIN);

    }

    @Test
    void TestGamePhase() {
        testGame.addPlayer(new Player("Alpha"));
        testGame.addPlayer(new Player("Beta"));
        testGame.addPlayer(new Player("Gamma"));
        testGame.startGame(null);
        assertTrue(testGame.isGameStarted());
        assertEquals(testGame.getGamePhase(), GamePhase.PLAY);
        assertEquals(testGame.getCurrNumOfPlayers(), 3);
        assertNotNull(testGame.getBoard());
        assertNotNull(testGame.getCommonGoals());
        testGame.setCurrentPlayer(new Player("Beta"));

        testGame.handleDrawPhase();
        assertEquals(testGame.getTurnPhase(), TurnPhase.DRAW);

        testGame.prepareForInsertPhase();
        assertEquals(testGame.getTurnPhase(), TurnPhase.INSERT);

        testGame.setTurnPhase(TurnPhase.CALCULATE);

        testGame.handleCalculatePhase();
        assertEquals(testGame.getTurnPhase(), TurnPhase.CALCULATE);

        testGame.prepareForRefillPhase();
        assertEquals(testGame.getTurnPhase(), TurnPhase.REFILL);

        testGame.setLastTurn(true);
        assertTrue(testGame.isLastTurn());


    }

    @Test
    void TestBoard() {
        testGame.addPlayer(new Player("Alpha"));
        testGame.addPlayer(new Player("Beta"));
        testGame.addPlayer(new Player("Gamma"));

        ArrayList<Square> squares = new ArrayList<>();
        squares.add(new Square(2, 3));
        squares.add(new Square(4, 5));
        testGame.startGame(null);
        testGame.setCurrentPlayer(new Player("Beta"));
        testGame.drawFromBoard(squares);
        assertEquals(testGame.getBoard().getSquare(new Coordinates(2, 3)).getItem().getType(), ItemType.EMPTY);
        assertEquals(testGame.getBoard().getSquare(new Coordinates(4, 5)).getItem().getType(), ItemType.EMPTY);
        System.out.println(testGame.getPlayers());
    }

    @Test
    void TestLeaderBoard() throws GameNotReadyException, InvalidUsernameException, InvalidCommandException {
        ArrayList<String> names = new ArrayList<>();
        names.add("Alpha");
        names.add("Beta");
        names.add("Gamma");
        testGame.addPlayer(new Player("Alpha"));
        testGame.addPlayer(new Player("Beta"));
        testGame.addPlayer(new Player("Gamma"));


        System.out.println(gamecontroller.getModel().getCurrNumOfPlayers());
        gamecontroller.getModel().setChosenNumOfPlayers(3);
        assertEquals(gamecontroller.getModel().getChosenPlayersNumber(), 3);
        gamecontroller.startGame();
        testGame.addPointsToPlayer(testGame.getPlayerByUsername("Alpha"), 3);
        testGame.addPointsToPlayer(testGame.getPlayerByUsername("Beta"), 2);
        testGame.addPointsToPlayer(testGame.getPlayerByUsername("Gamma"), 1);
        testGame.sortLeaderBoard(names);
        assertEquals(gamecontroller.getModel().getLeaderboard().get("Alpha"), 3);
        assertEquals(gamecontroller.getModel().getLeaderboard().get("Beta"), 2);
        assertEquals(gamecontroller.getModel().getLeaderboard().get("Gamma"), 1);

        testGame.addPointsToPlayer(testGame.getPlayerByUsername("Beta"), 2);
        testGame.updateLeaderBoard(names);
        assertEquals(gamecontroller.getModel().getLeaderboard().get("Beta"), 4);

    }

    @Test
    void TestPlayerRemovalAndPhases() {
        ArrayList<String> usernames = new ArrayList<>();
        testGame.addPlayer(new Player("Zeta"));
        testGame.addPlayer(new Player("Teta"));
        usernames.add("Zeta");
        usernames.add("Teta");
        assertEquals(testGame.getPlayersUsernames(), usernames);
        testGame.removePlayer(testGame.getPlayerByUsername("Teta"));
        usernames.remove("Teta");
        assertEquals(testGame.getPlayersUsernames(), usernames);

        testGame.resetPlayers();
        usernames.clear();
        assertEquals(testGame.getPlayers(), usernames);

        testGame.setGamePhase(GamePhase.LOGIN);
        assertEquals(testGame.getGamePhase(), GamePhase.LOGIN);
        testGame.nextGamePhase();
        assertEquals(testGame.getGamePhase(), GamePhase.INIT);
        testGame.nextGamePhase();
        assertEquals(testGame.getGamePhase(), GamePhase.PLAY);
        testGame.nextGamePhase();
        assertEquals(testGame.getGamePhase(), GamePhase.AWARDS);
        testGame.nextGamePhase();
        assertEquals(testGame.getGamePhase(), GamePhase.ENDED);
    }

    @Test
    void TestTile() {
        testPlayer = new Player("Player");
        ArrayList<ItemTile> tiles = new ArrayList<>();
        tiles.add(new ItemTile(ItemType.CAT));
        testGame.addPlayer(testPlayer);
        testGame.startGame(null);
        testGame.setCurrentPlayer(testPlayer);
        System.out.println(testGame.getCurrentPlayer());
        testGame.insertTiles(tiles, 2);
        testPlayer.getBookshelf().printBookshelf();

        assertEquals(testPlayer.getBookshelf().getSingleCell(5, 2).getType(), ItemType.CAT);
    }

    @Test
    public void testEndGameWinnerFirstInLeaderboard() {
        LinkedHashMap<String, Integer> expectedLeaderboard = new LinkedHashMap<>();
        Player winner = new Player("John");
        testGame.getLeaderboard().put("John", 0);
        testGame.getLeaderboard().put("Alice", 0);
        testGame.getLeaderboard().put("Bob", 0);
        testGame.addPointsToPlayer(winner, 100);

        ArrayList<String> playersQueue = new ArrayList<>();
        playersQueue.add("John");
        playersQueue.add("Alice");
        playersQueue.add("Bob");

        testGame.endGame(winner, playersQueue);

        // Assertions
        expectedLeaderboard.put("John", 100);
        expectedLeaderboard.put("Bob", 0); //added after Alice to the game, comes before Alice in the leaderboard
        expectedLeaderboard.put("Alice", 0);

        // Verify the leaderboard after the game ends
        assertEquals(expectedLeaderboard, testGame.getLeaderboard());
    }

    @Test
    public void testEndGameWinnerNotFirstInLeaderboard() {
        LinkedHashMap<String, Integer> expectedLeaderboard = new LinkedHashMap<>();
        testGame.getLeaderboard().put("John", 0);
        testGame.getLeaderboard().put("Alice", 0);
        testGame.getLeaderboard().put("Bob", 0);
        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");
        Player p3 = new Player("John");
        testGame.addPlayer(p1);
        testGame.addPlayer(p2);
        testGame.addPlayer(p3);
        Player winner = p2;
        testGame.addPointsToPlayer(p1, 150);

        ArrayList<String> playersQueue = new ArrayList<>();
        playersQueue.add("John");
        playersQueue.add("Alice");
        playersQueue.add("Bob");

        // Create test object and call the method
        testGame.endGame(winner, playersQueue);

        // Assertions
        expectedLeaderboard.put("Bob", 0);
        expectedLeaderboard.put("Alice", 150);
        expectedLeaderboard.put("John", 0);

        // Verify the leaderboard after the game ends
        assertEquals(expectedLeaderboard, testGame.getLeaderboard());
    }


    @Test
    public void testEndGameWithTiesInLeaderboard() {
        LinkedHashMap<String, Integer> expectedLeaderboard = new LinkedHashMap<>();
        testGame.getLeaderboard().put("John", 0);
        testGame.getLeaderboard().put("Alice", 0);
        testGame.getLeaderboard().put("Bob", 0);
        testGame.getLeaderboard().put("Charlie", 0);
        Player winner = new Player("John");
        testGame.addPointsToPlayer(winner, 100);

        ArrayList<String> playersQueue = new ArrayList<>();
        playersQueue.add("John");
        playersQueue.add("Alice");
        playersQueue.add("Bob");
        playersQueue.add("Charlie");

        // Create test object and call the method
        testGame.endGame(winner, playersQueue);

        // Assertions
        expectedLeaderboard.put("John", 100);
        expectedLeaderboard.put("Alice", 0);
        expectedLeaderboard.put("Bob", 0);
        expectedLeaderboard.put("Charlie", 0);

        // Verify the leaderboard after the game ends
        assertEquals(expectedLeaderboard, testGame.getLeaderboard());
    }

    @Test
    void handleCalculatePhase() {
        testGame.getLeaderboard().put("Adam", 0);
        testGame.getLeaderboard().put("Eve", 0);
        Player player = new Player("Adam");
        Player player1 = new Player("Eve");
        testGame.startGame(null);
        testGame.addPlayer(player);
        testGame.addPlayer(player1);
        testGame.setCurrentPlayer(player);
        for(int i=0; i<Bookshelf.Rows; i++){
            for(int j=0; j<Bookshelf.Columns; j++){
                player.getBookshelf().getShelfie()[i][j]=new ItemTile(ItemType.PLANT);
            }
        }
        testGame.handleCalculatePhase();
        assertTrue(player.getScore()>0);
    }
}