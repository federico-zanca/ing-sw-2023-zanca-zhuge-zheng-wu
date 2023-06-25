package it.polimi.ingsw.controller;

import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.distributed.Lobby;
import it.polimi.ingsw.distributed.ServerImpl;
import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.GamePhase;
import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.exceptions.LobbyNotFoundException;
import it.polimi.ingsw.model.gameboard.Coordinates;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.connectionmessage.CreateLobbyRequest;
import it.polimi.ingsw.network.message.connectionmessage.JoinLobbyRequest;
import it.polimi.ingsw.network.message.connectionmessage.LoginRequest;
import it.polimi.ingsw.network.message.gamemessage.DrawInfoMessage;
import it.polimi.ingsw.network.message.gamemessage.DrawTilesMessage;
import it.polimi.ingsw.network.message.gamemessage.ExitGameRequest;
import it.polimi.ingsw.network.message.gamemessage.InsertTilesMessage;
import it.polimi.ingsw.network.message.lobbymessage.ExitLobbyRequest;
import it.polimi.ingsw.network.message.lobbymessage.StartGameRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class TurnControllerTest {
    private ClientImpl client;
    private ServerImpl server;
    private ClientImpl client2;
    private Player player;
    private Player player2;
    private GameController controller;
    private Lobby lobby;
    @BeforeEach
    void setUp() throws RemoteException, LobbyNotFoundException {
        server = new ServerImpl();
        client = new ClientImpl(server);
        client2 = new ClientImpl(server);
        server.update(client, new LoginRequest("c1"));
        server.update(client2, new LoginRequest("c2"));
        server.update(client, new CreateLobbyRequest("lobbyTest"));
        server.update(client2, new JoinLobbyRequest("lobbyTest"));
        lobby= server.getLobbyByName("lobbyTest")  ;
        server.update(client, new StartGameRequest());
        controller = lobby.getController();
        player = controller.getModel().getPlayerByUsername("c1");
        player2 = controller.getModel().getPlayerByUsername("c2");
    }

    @Test
    void drawPhase() {
        assertDoesNotThrow(()-> controller.getTurnController().drawPhase("c1", new ExitGameRequest("c1")));
        Square sq1 = new Square(new Coordinates(1,3), controller.getModel().getBoard().getGameboard()[1][3].getItem().getType() );
        Square sq2 = new Square(new Coordinates(1,8), controller.getModel().getBoard().getGameboard()[1][4].getItem().getType() );
        Square sq3 = new Square(new Coordinates(1,4), controller.getModel().getBoard().getGameboard()[1][4].getItem().getType() );
        Square sq4 = new Square(new Coordinates(1,5), controller.getModel().getBoard().getGameboard()[1][4].getItem().getType() );
        Square sq5 = new Square(new Coordinates(1,6), controller.getModel().getBoard().getGameboard()[1][4].getItem().getType() );
        ArrayList<Square> squares = new ArrayList<>();
        squares.add(sq1);
        squares.add(sq2);
        controller.getTurnController().drawPhase("c1", new DrawTilesMessage("c1", squares));
        assertNotSame(controller.getModel().getBoard().getGameboard()[1][3].getItem().getType(), ItemType.EMPTY);

        squares.remove(sq2);
        squares.add(sq3);
        squares.add(sq4);
        squares.add(sq5);

        controller.getTurnController().drawPhase("c1", new DrawTilesMessage("c1", squares));
        assertNotSame(controller.getModel().getBoard().getGameboard()[1][3].getItem().getType(), ItemType.EMPTY);
        assertNotSame(controller.getModel().getBoard().getGameboard()[1][4].getItem().getType(), ItemType.EMPTY);
        assertNotSame(controller.getModel().getBoard().getGameboard()[1][5].getItem().getType(), ItemType.EMPTY);

        squares.remove(sq5);
        squares.remove(sq4);
        Square sq6 = new Square(new Coordinates(2,3), controller.getModel().getBoard().getGameboard()[2][3].getItem().getType() );
        squares.add(sq6);
        controller.getTurnController().drawPhase("c1", new DrawTilesMessage("c1", squares));
        assertNotSame(controller.getModel().getBoard().getGameboard()[1][3].getItem().getType(), ItemType.EMPTY);
        assertNotSame(controller.getModel().getBoard().getGameboard()[1][4].getItem().getType(), ItemType.EMPTY);
        assertNotSame(controller.getModel().getBoard().getGameboard()[2][3].getItem().getType(), ItemType.EMPTY);

        squares.remove(sq6);
        Square sq7 = new Square(new Coordinates(2,5), ItemType.GAME );
        squares.add(sq7);
        controller.getTurnController().drawPhase("c1", new DrawTilesMessage("c1", squares));
        assertNotSame(controller.getModel().getBoard().getGameboard()[1][3].getItem().getType(), ItemType.EMPTY);
        assertNotSame(controller.getModel().getBoard().getGameboard()[1][4].getItem().getType(), ItemType.EMPTY);
        assertNotSame(controller.getModel().getBoard().getGameboard()[2][5].getItem().getType(), ItemType.EMPTY);
    }

    @Test
    void loadNextPlayer() {
        String firstUsername = controller.getTurnController().getPlayerQueue().get(0).getUsername();
        String secondUsername = controller.getTurnController().getPlayerQueue().get(1).getUsername();
        assertEquals(firstUsername, controller.getModel().getCurrentPlayer().getUsername());
        controller.getTurnController().loadNextPlayer();
        assertEquals(secondUsername, controller.getModel().getCurrentPlayer().getUsername());
        ItemTile[][] shelfie = controller.getModel().getPlayerByUsername("c1").getBookshelf().getShelfie();
        for(int i=0; i<shelfie.length; i++)
            for(int j=0; j<shelfie[0].length; j++)
                shelfie[i][j] = new ItemTile(ItemType.GAME);
        ItemTile[][] shelfie2 = controller.getModel().getPlayerByUsername("c2").getBookshelf().getShelfie();
        for(int i=0; i<shelfie2.length; i++)
            for(int j=0; j<shelfie2[0].length; j++)
                shelfie2[i][j] = new ItemTile(ItemType.GAME);
        shelfie[0][0] = new ItemTile(ItemType.EMPTY);
        ArrayList<ItemTile> array = new ArrayList<>();
        array.add(new ItemTile(ItemType.GAME));
        player.setHand(array);
        InsertTilesMessage message = new InsertTilesMessage("c1", array, 0);
        controller.getTurnController().insertPhase("c1", message);
        controller.getTurnController().loadNextPlayer();
        assertTrue(controller.getModel().isLastTurn());
        controller.getTurnController().loadNextPlayer();

        assertDoesNotThrow(()->controller.getTurnController().loadNextPlayer());

    }

    @Test
    void triggerLastTurn() {

    }

    @Test
    void insertPhase() {
        assertDoesNotThrow(()-> controller.getTurnController().insertPhase("c1", new ExitGameRequest("c1")));
        ArrayList<ItemTile> array = new ArrayList<>();
        array.add(new ItemTile(ItemType.GAME));
        InsertTilesMessage message = new InsertTilesMessage("c1", array, 0);
        Bookshelf bookshelf = controller.getModel().getPlayerByUsername("c1").getBookshelf();
        controller.getTurnController().insertPhase("c1", message);
        controller.getTurnController().loadNextPlayer();
        assertEquals(controller.getModel().getPlayerByUsername("c1").getBookshelf(), bookshelf);

    }

    @Test
    void addPlayerToSkip() throws InterruptedException {
        controller.disconnectPlayer("c1");
        int i = controller.getTurnController().TIMEOUT_LASTPLAYER + 100;
        sleep(i);
        assertSame(controller.getModel().getGamePhase(), GamePhase.ENDED);
    }
}