package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.distributed.Lobby;
import it.polimi.ingsw.distributed.ServerImpl;
import it.polimi.ingsw.model.commongoals.CommonGoalCard;
import it.polimi.ingsw.model.enumerations.GamePhase;
import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.enumerations.TurnPhase;
import it.polimi.ingsw.model.exceptions.GameNotReadyException;
import it.polimi.ingsw.model.exceptions.InvalidCommandException;
import it.polimi.ingsw.model.exceptions.InvalidUsernameException;
import it.polimi.ingsw.model.exceptions.LobbyNotFoundException;
import it.polimi.ingsw.model.gameboard.Board;
import it.polimi.ingsw.model.gameboard.Coordinates;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import it.polimi.ingsw.network.message.connectionmessage.CreateLobbyRequest;
import it.polimi.ingsw.network.message.connectionmessage.JoinLobbyRequest;
import it.polimi.ingsw.network.message.connectionmessage.LoginRequest;
import it.polimi.ingsw.network.message.lobbymessage.StartGameRequest;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game testGame;
    private Player testPlayer;
    private GameController gamecontroller;
    private Lobby lobby;
    private ServerImpl server;
    private ClientImpl client;
    @BeforeEach
    void setUp() throws RemoteException, LobbyNotFoundException {
        server = new ServerImpl();
        client = new ClientImpl(server);
        server.register(client);
        server.update(client, new LoginRequest("c1"));
        server.update(client, new CreateLobbyRequest("testLobby"));
        lobby = server.getLobbyByName("testLobby");
        testGame = lobby.getController().getModel();
        gamecontroller=lobby.getController();

    }

    @Test
    void GameInitTest(){
        Player[] players=new Player[4];
        String[] names=new String[]{"Alpha", "Beta", "Gamma", "Delta"};
        ArrayList<Player> addedPlayers=new ArrayList<>();
        ArrayList<String> addedNames=new ArrayList<>();

        for(int i=0; i<4; i++){
            players[i]=new Player(names[i]);
            testGame.addPlayer(players[i]);
            addedPlayers.add(players[i]);
            addedNames.add(names[i]);
            assertEquals(testGame.getPlayers(), addedPlayers);
            assertEquals(testGame.getPlayersUsernames(), addedNames);
            assertEquals(testGame.getCurrNumOfPlayers(), i+1);
            assertEquals(testGame.getPlayerByUsername(names[i]), players[i]);

        }
        assertEquals(testGame.getGamePhase(), GamePhase.LOGIN);

    }

    @Test
    void TestGamePhase(){
        testGame.addPlayer(new Player("Alpha"));
        testGame.addPlayer(new Player("Beta"));
        testGame.addPlayer(new Player("Gamma"));
        testGame.startGame();
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
    void TestBoard(){
        testGame.addPlayer(new Player("Alpha"));
        testGame.addPlayer(new Player("Beta"));
        testGame.addPlayer(new Player("Gamma"));

        ArrayList<Square> squares=new ArrayList<>();
        squares.add(new Square(2,3));
        squares.add(new Square(4,5));
        testGame.startGame();
        testGame.setCurrentPlayer(new Player("Beta"));
        testGame.drawFromBoard(squares);
        assertEquals(testGame.getBoard().getSquare(new Coordinates(2,3)).getItem().getType(), ItemType.EMPTY);
        assertEquals(testGame.getBoard().getSquare(new Coordinates(4,5)).getItem().getType(), ItemType.EMPTY);
        System.out.println(testGame.getPlayers());
    }

    @Test
    void TestLeaderBoard() throws GameNotReadyException, InvalidUsernameException, InvalidCommandException {
        ArrayList<String> names=new ArrayList<>();
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
    void TestEndPhase(){
        gamecontroller.getModel().addPlayer(new Player("Alpha"));
        gamecontroller.getModel().startGame();
        gamecontroller.awardPhase(null);
    }




}