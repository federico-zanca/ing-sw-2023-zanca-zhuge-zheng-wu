package it.polimi.ingsw.controller;

import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.distributed.Lobby;
import it.polimi.ingsw.distributed.ServerImpl;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.TurnPhase;
import it.polimi.ingsw.model.exceptions.GameNotReadyException;
import it.polimi.ingsw.model.exceptions.InvalidUsernameException;
import it.polimi.ingsw.model.exceptions.LobbyNotFoundException;
import it.polimi.ingsw.model.gameboard.Coordinates;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.connectionmessage.CreateLobbyRequest;
import it.polimi.ingsw.network.message.connectionmessage.JoinLobbyRequest;
import it.polimi.ingsw.network.message.connectionmessage.LoginRequest;
import it.polimi.ingsw.network.message.gamemessage.DrawTilesMessage;
import it.polimi.ingsw.network.message.gamemessage.ExitGameRequest;
import it.polimi.ingsw.network.message.gamemessage.InsertTilesMessage;
import it.polimi.ingsw.network.message.lobbymessage.StartGameRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.model.enumerations.GamePhase;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    private Object lock = new Object();
    private ServerImpl server;
    private ClientImpl client;
    private ClientImpl client2;
    private Player player;
    private Player player2;
    private GameController controller;
    private Lobby lobby;
    @BeforeEach
    void setUp() throws RemoteException, LobbyNotFoundException, InterruptedException {
        server = new ServerImpl();
        client = new ClientImpl(server);
        client2 = new ClientImpl(server);
        server.getPreGameController().onConnectionMessage(client, new LoginRequest("c1"));
        server.getPreGameController().onConnectionMessage(client2, new LoginRequest("c2"));
        server.getPreGameController().onConnectionMessage(client, new CreateLobbyRequest("lobbyTest"));
        server.getPreGameController().onConnectionMessage(client2, new JoinLobbyRequest("lobbyTest"));
        lobby= server.getLobbyByName("lobbyTest")  ;
        server.getPreGameController().onLobbyMessage(client, new StartGameRequest());
        controller = lobby.getController();
        player = controller.getModel().getPlayerByUsername("c1");
        player2 = controller.getModel().getPlayerByUsername("c2");
    }
    @Nested
    public class AwardPhaseTest{
        @Test
        void awardPhaseStandard() {
            assertTrue(lobby.isGameStarted());
            controller.getModel().addPointsToPlayer(player, 10);
            controller.getModel().addPointsToPlayer(player2, 5);
            controller.awardPhase(null);
            assertEquals(controller.getModel().getLeaderboard().keySet().iterator().next(), "c1");
        }

        @Test
        void awardPhaseChampion() {
            assertTrue(lobby.isGameStarted());
            controller.getModel().addPointsToPlayer(player, 10);
            controller.getModel().addPointsToPlayer(player2, 5);
            controller.awardPhase(player2);
            assertEquals(controller.getModel().getLeaderboard().keySet().iterator().next(), "c2");

        }
    }

    @Test
    void onMessageReceived() throws InterruptedException {
        controller.getModel().setGamePhase(GamePhase.AWARDS);
        controller.onMessageReceived("c1", null);
        controller.getModel().setGamePhase(GamePhase.PLAY);
        controller.getModel().setTurnPhase(TurnPhase.DRAW);
        Square sq1 = new Square(new Coordinates(1,3), controller.getModel().getBoard().getGameboard()[1][3].getItem().getType() );
        Square sq2 = new Square(new Coordinates(1,4), controller.getModel().getBoard().getGameboard()[1][4].getItem().getType() );
        ArrayList<Square> squares = new ArrayList<>();
        squares.add(sq1);
        squares.add(sq2);
        assertDoesNotThrow(()->controller.onMessageReceived("c1", new DrawTilesMessage("c1", squares)));
        controller.getModel().setGamePhase(GamePhase.AWARDS);
        assertDoesNotThrow(()->controller.onMessageReceived("c1", new DrawTilesMessage("c1", squares)));
        controller.getModel().setGamePhase(GamePhase.ENDED);
        assertDoesNotThrow(()->controller.onMessageReceived("c1", new DrawTilesMessage("c1", squares)));



    }

    @Test
    void update() throws InterruptedException {
        controller.getModel().setGamePhase(GamePhase.PLAY);
        Square sq1 = new Square(new Coordinates(1,3), controller.getModel().getBoard().getGameboard()[1][3].getItem().getType() );
        Square sq2 = new Square(new Coordinates(1,4), controller.getModel().getBoard().getGameboard()[1][4].getItem().getType() );
        ArrayList<Square> squares = new ArrayList<>();
        squares.add(sq1);
        squares.add(sq2);
        assertDoesNotThrow(()->controller.update("c2", new DrawTilesMessage("c1", squares)));
        assertDoesNotThrow(()->controller.update("c1", new DrawTilesMessage("c1", squares)));
        controller.update("c2", new ExitGameRequest("c2"));
        sleep(100);
        assertTrue(controller.getTurnController().getPlayersToSkipUsernames().contains("c2"));
        controller.getModel().setTurnPhase(TurnPhase.INSERT);
        assertDoesNotThrow(()->controller.update("c1",
                                new InsertTilesMessage("c1", squares.stream().map(Square::getItem).collect(ArrayList::new, ArrayList::add, ArrayList::addAll),
                        0)));
        controller.getModel().setGamePhase(GamePhase.AWARDS);
        assertDoesNotThrow(()->controller.update("c1",
                         new InsertTilesMessage("c1", squares.stream().map(Square::getItem).collect(ArrayList::new, ArrayList::add, ArrayList::addAll),
                        0)));

    }
    @Test
    void startGame() throws RemoteException {
        //test case in which the game is not ready to start
        ClientImpl client3 = new ClientImpl(server);
        server.update(client3, new LoginRequest("c3"));
        server.update(client3, new CreateLobbyRequest("lobbyTest2"));
        assertThrows(GameNotReadyException.class, ()->server.getLobbyByName("lobbyTest2").getController().startGame());
    }

    @Test
    void addPlayer() {
        assertThrows(InvalidUsernameException.class, () -> controller.addPlayer(""));
        assertThrows(InvalidUsernameException.class, () -> controller.addPlayer("   "));
        assertThrows(InvalidUsernameException.class, () -> controller.addPlayer("-"));
        assertThrows(InvalidUsernameException.class, () -> controller.addPlayer("_"));
        assertThrows(InvalidUsernameException.class, () -> controller.addPlayer("\\!"));
    }

    @Test
    void getCurrentPlayerUsername() {
        assertEquals(controller.getTurnController().getPlayerQueue().get(0).getUsername(), controller.getCurrentPlayerUsername());
        assertNotEquals(controller.getTurnController().getPlayerQueueUsernames().get(1), controller.getCurrentPlayerUsername());
        controller.getTurnController().loadNextPlayer();
    }

    @Test
    void resetPlayers() throws InvalidUsernameException {
        controller.addPlayer("c3");
        assertTrue(controller.getModel().getPlayersUsernames().contains("c3"));
        controller.resetPlayers();
        assertTrue(controller.getModel().getPlayersUsernames().isEmpty());
        assertTrue(controller.getModel().getPlayers().isEmpty());
    }
}