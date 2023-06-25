package it.polimi.ingsw.controller;

import it.polimi.ingsw.distributed.*;
import it.polimi.ingsw.model.exceptions.ClientAlreadyInLobbyException;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.InvalidLobbyNameException;
import it.polimi.ingsw.model.exceptions.LobbyNotFoundException;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.connectionmessage.*;
import it.polimi.ingsw.network.message.lobbymessage.*;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class PreGameControllerTest {
    private PreGameController controller;
    private ServerImpl server;
    private Client client;
    @BeforeEach
    public void setUp() throws RemoteException {
        server = new ServerImpl();
        client = new ClientImpl(server);
        controller = new PreGameController(server);
    }
    @Test
    public void onConnectionMessage() {
        MessageToServer message = new LoginRequest("test");
        controller.onConnectionMessage(client, message);
        assertNotNull(controller);
    }

    @Test
    void onLobbyMessage() {
        MessageToServer message = new LoginRequest("test");
        controller.onConnectionMessage(client, message);
        assertNotNull(controller);
    }

    @Nested
    public class onCreateLobbyRequest {
        @Test
        void CreateLobbyRequestValid () {
            CreateLobbyRequest message = new CreateLobbyRequest("test");
            controller.onCreateLobbyRequest(client, message);
            assertDoesNotThrow(() -> server.getLobbyByName("test"));
        }

        @Test
        void CreateLobbyRequestInvalid () {
            CreateLobbyRequest message = new CreateLobbyRequest("test");
            controller.onCreateLobbyRequest(client, message);
            assertThrows(LobbyNotFoundException.class, () -> server.getLobbyByName("test2"));
        }

        @Test
        void CreateLobbyRequestNull () {
            CreateLobbyRequest message = new CreateLobbyRequest(null);
            controller.onCreateLobbyRequest(client, message);
            assertTrue(server.getLobbies().isEmpty());
            assertTrue(()->{
                for(Client c : server.getConnectedClients().keySet()){
                    if(server.getConnectedClientInfo(c).getLobby()!=null){
                        return false;
                    }
                }
                return true;
            });
        }

        @Test
        void CreateLobbyRequestEmpty () {
            CreateLobbyRequest message = new CreateLobbyRequest("");
            controller.onCreateLobbyRequest(client, message);
            assertTrue(server.getLobbies().isEmpty());
            assertTrue(()->{
                for(Client c : server.getConnectedClients().keySet()){
                    if(server.getConnectedClientInfo(c).getLobby()!=null){
                        return false;
                    }
                }
                return true;
            });
        }

        @Test
        void CreateLobbyRequestSpaces () {
            CreateLobbyRequest message = new CreateLobbyRequest("   ");
            controller.onCreateLobbyRequest(client, message);
            assertTrue(server.getLobbies().isEmpty());
            assertTrue(()->{
                for(Client c : server.getConnectedClients().keySet()){
                    if(server.getConnectedClientInfo(c).getLobby()!=null){
                        return false;
                    }
                }
                return true;
            });
        }
    }
    @Nested
    public class onJoinLobbyRequest {
        @Test
        void onJoinLobbyRequestTest() throws RemoteException {
            JoinLobbyRequest message;
            message = new JoinLobbyRequest("test");
            controller.onJoinLobbyRequest(client, message);
            assertTrue(server.getLobbies().isEmpty());
            assertNull(server.getConnectedClientInfo(client).getLobby());

            Lobby lobby = null;
            try {
                lobby = new Lobby(server, client, "test");
                server.addLobby(lobby);
            } catch (FullLobbyException | ClientAlreadyInLobbyException e) {
                fail();
            }
            controller.onJoinLobbyRequest(client, message);
            assertDoesNotThrow(() -> server.getLobbyByName("test"));
            assertTrue(lobby.getInLobbyClients().contains(client));
            assertEquals(1, lobby.getInLobbyClients().size());
            assertEquals(server.getConnectedClientInfo(client).getLobby(), lobby);

            assertTrue(server.getLobbyOfClient(client).getInLobbyClients().contains(client));
            assertEquals(1, server.getLobbyOfClient(client).getInLobbyClients().size());
            assertEquals(1, lobby.getInLobbyClients().size());

            Client client2 = new ClientImpl(server);
            JoinLobbyRequest message2 = new JoinLobbyRequest("test");
            controller.onJoinLobbyRequest(client2, message2);

            Client client3 = new ClientImpl(server);
            JoinLobbyRequest message3 = new JoinLobbyRequest("test");
            controller.onJoinLobbyRequest(client3, message3);
            try {
                assertFalse(server.getLobbyByName("test").getInLobbyClients().contains(client3));
                assertNull(server.getConnectedClientInfo(client3).getLobby());
                assertSame(server.getConnectedClientInfo(client3).getClientState(), ClientState.IN_SERVER);
                assertTrue(server.getLobbyByName("test").getInLobbyClients().contains(client2));
                assertTrue(server.getLobbyByName("test").getInLobbyClients().contains(client));
                assertEquals(2, server.getLobbyByName("test").getInLobbyClients().size());
            } catch (LobbyNotFoundException e) {
                fail();
            }

        }
        @Test
        void rejoinRequestTest() throws RemoteException, LobbyNotFoundException, InterruptedException {
            Client client2 = new ClientImpl(server);
            Client client3 = new ClientImpl(server);
            server.register(client2);
            server.register(client3);
            server.register(client);
            server.update(client, new LoginRequest("c1"));
            server.update(client2, new LoginRequest("c2"));
            server.update(client3, new LoginRequest("c3"));
            server.update(client, new CreateLobbyRequest("test"));
            server.update(client2, new JoinLobbyRequest("test"));
            server.update(client, new StartGameRequest());

            server.disconnect(client2);

            Lobby lobby = server.getLobbyByName("test");
            assertTrue(lobby.getController().getTurnController().getPlayersToSkipUsernames().contains("c2"));
            assertFalse(server.getConnectedClientInfo(client2).isConnected());
            assertSame(server.getConnectedClientInfo(client2).getClientState(), ClientState.IN_GAME);
            assertTrue(lobby.isGameStarted());

            Client client4 = new ClientImpl(server);
            server.register(client4);
            server.update(client4, new LoginRequest("c2"));
            server.update(client4, new JoinLobbyRequest("test"));

            assertTrue(server.getConnectedClientInfo(client4).isConnected());
            assertFalse(server.getConnectedClients().containsKey(client2));
            assertTrue(lobby.getInLobbyClients().contains(client4));
            assertSame(server.getConnectedClientInfo(client4).getClientState(), ClientState.IN_GAME);
        }
    }

    @Test
    void onLobbyListRequest() {
        server.update(client, new CreateLobbyRequest("test"));
        server.update(client, new LobbyListRequest());
        assertEquals(1, server.getLobbies().size());
        assertEquals("test", server.getLobbies().get(0).getName());
    }

    @Nested
    public class LoginRequestTests {
        @Test
        void onLoginRequest() {
            LoginRequest message = new LoginRequest("test");
            controller.onLoginRequest(client, message);
            assertEquals("test", server.getConnectedClientInfo(client).getClientID());
            assertEquals(ClientState.IN_SERVER, server.getConnectedClientInfo(client).getClientState());
            assertTrue(server.getConnectedClientInfo(client).isConnected());
            assertNull(server.getConnectedClientInfo(client).getLobby());

        }

        @Test
        void onLoginRequestEmpty() {
            LoginRequest message = new LoginRequest("");
            controller.onLoginRequest(client, message);
            assertEquals(server.getConnectedClientInfo(client).getClientID(), client.toString());
            assertEquals(ClientState.IN_SERVER, server.getConnectedClientInfo(client).getClientState());
            assertTrue(server.getConnectedClientInfo(client).isConnected());
            assertNull(server.getConnectedClientInfo(client).getLobby());
        }

        @Test
        void onLoginRequestSpaces() {
            LoginRequest message = new LoginRequest("   ");
            controller.onLoginRequest(client, message);
            assertEquals(server.getConnectedClientInfo(client).getClientID(), client.toString());
            assertEquals(ClientState.IN_SERVER, server.getConnectedClientInfo(client).getClientState());
            assertTrue(server.getConnectedClientInfo(client).isConnected());
            assertNull(server.getConnectedClientInfo(client).getLobby());
        }

        @Test
        void onLoginRequestNull() {
            LoginRequest message = new LoginRequest(null);
            controller.onLoginRequest(client, message);
            assertEquals(server.getConnectedClientInfo(client).getClientID(), client.toString());
            assertEquals(ClientState.IN_SERVER, server.getConnectedClientInfo(client).getClientState());
            assertTrue(server.getConnectedClientInfo(client).isConnected());
            assertNull(server.getConnectedClientInfo(client).getLobby());
        }

        @Test
        void onLoginRequestTaken() throws RemoteException {
            LoginRequest message = new LoginRequest("test");
            controller.onLoginRequest(client, message);
            assertEquals("test", server.getConnectedClientInfo(client).getClientID());
            assertEquals(ClientState.IN_SERVER, server.getConnectedClientInfo(client).getClientState());
            assertEquals(server.getConnectedClients().get(client).getClientID(), "test");
            assertTrue(server.getConnectedClientInfo(client).isConnected());
            assertNull(server.getConnectedClientInfo(client).getLobby());

            Client client2 = new ClientImpl(server);
            controller.onLoginRequest(client2, message);
            assertEquals(server.getConnectedClientInfo(client2).getClientID(), client2.toString());
            assertEquals(ClientState.IN_SERVER, server.getConnectedClientInfo(client2).getClientState());
            assertTrue(server.getConnectedClientInfo(client2).isConnected());
            assertNull(server.getConnectedClientInfo(client2).getLobby());

            server.disconnect(client);
            controller.onLoginRequest(client2, message);
            assertEquals(server.getConnectedClientInfo(client2).getClientID(), message.getUsername());
            assertEquals(ClientState.IN_SERVER, server.getConnectedClientInfo(client2).getClientState());
            assertTrue(server.getConnectedClientInfo(client2).isConnected());

        }
    }

    @Test
    void onUsernameRequest() {
        server.update(client, new LoginRequest("test"));
        UsernameRequest message = new UsernameRequest("test1");
        controller.onUsernameRequest(client, message);
        assertEquals("test1", server.getConnectedClientInfo(client).getClientID());
        assertEquals(ClientState.IN_SERVER, server.getConnectedClientInfo(client).getClientState());
        assertTrue(server.getConnectedClientInfo(client).isConnected());
        assertNull(server.getConnectedClientInfo(client).getLobby());
    }

    @Test
    void onChangeNumOfPlayersRequest() throws LobbyNotFoundException, RemoteException {
        server.update(client, new LoginRequest("test"));
        server.update(client, new CreateLobbyRequest("testLobby"));
        server.update(client, new ChangeNumOfPlayersRequest(3));
        assertEquals(3, server.getLobbyByName("testLobby").getController().getModel().getChosenPlayersNumber());
        Client client2 = new ClientImpl(server);
        Client client3 = new ClientImpl(server);
        server.update(client2, new LoginRequest("client2"));
        server.update(client2, new JoinLobbyRequest("testLobby"));
        server.update(client3, new LoginRequest("client3"));
        server.update(client3, new JoinLobbyRequest("testLobby"));

        server.update(client, new ChangeNumOfPlayersRequest(2));
        assertEquals(3, server.getLobbyByName("testLobby").getController().getModel().getChosenPlayersNumber());
        server.update(client, new ChangeNumOfPlayersRequest(5));
        assertEquals(3, server.getLobbyByName("testLobby").getController().getModel().getChosenPlayersNumber());
        //non admin user tries to change the number of players
        server.update(client2, new ChangeNumOfPlayersRequest(4));
        assertEquals(3, server.getLobbyByName("testLobby").getController().getModel().getChosenPlayersNumber());

        server.update(client3, new ExitLobbyRequest());
        server.update(client2, new ExitLobbyRequest());
        server.update(client, new ChangeNumOfPlayersRequest(1));
        assertEquals(3, server.getLobbyByName("testLobby").getController().getModel().getChosenPlayersNumber());
    }

    @Test
    void onExitLobbyRequest() {
        server.update(client, new LoginRequest("test"));
        server.update(client, new CreateLobbyRequest("testLobby"));
        server.update(client, new ExitLobbyRequest());
        assertNull(server.getConnectedClientInfo(client).getLobby());
        assertEquals(ClientState.IN_SERVER, server.getConnectedClientInfo(client).getClientState());
        assertTrue(server.getConnectedClientInfo(client).isConnected());
        assertTrue(server.getLobbies().isEmpty());
    }

    @Test
    void onPlayerListRequest() throws RemoteException {
        server.update(client, new LoginRequest("test"));
        server.update(client, new CreateLobbyRequest("testLobby"));
        server.update(client, new PlayerListRequest());
        assertEquals("test", server.getLobbyOfClient(client).getClientsUsernames().get(0));
        assertEquals(1, server.getLobbyOfClient(client).getClientsUsernames().size());
        Client client2 = new ClientImpl(server);
        server.update(client2, new LoginRequest("client2"));
        server.update(client2, new JoinLobbyRequest("testLobby"));
        server.update(client, new PlayerListRequest());
        assertEquals(2, server.getLobbyOfClient(client).getClientsUsernames().size());
        assertEquals("client2", server.getLobbyOfClient(client).getClientsUsernames().get(1));

        server.update(client2, new ExitLobbyRequest());
        server.update(client, new PlayerListRequest());
        assertEquals(1, server.getLobbyOfClient(client).getClientsUsernames().size());
        assertEquals("test", server.getLobbyOfClient(client).getClientsUsernames().get(0));
    }

    @Test
    void onStartGameRequest() throws LobbyNotFoundException {
        server.update(client, new LoginRequest("test"));
        server.update(client, new CreateLobbyRequest("testLobby"));
        server.update(client, new StartGameRequest());
        assertEquals(ClientState.IN_A_LOBBY, server.getConnectedClientInfo(client).getClientState());
        assertEquals(1, server.getLobbies().size());
        assertFalse(server.getLobbyOfClient(client).isGameStarted());
    }
}