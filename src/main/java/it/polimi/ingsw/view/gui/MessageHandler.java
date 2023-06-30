package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.JoinType;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import it.polimi.ingsw.network.message.ChatMessage;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.connectionmessage.*;
import it.polimi.ingsw.network.message.gamemessage.*;
import it.polimi.ingsw.network.message.lobbymessage.*;
import it.polimi.ingsw.view.gui.sceneControllers.GuiPhase;
import it.polimi.ingsw.view.tui.ActionType;
import it.polimi.ingsw.view.tui.LobbyDisplayInfo;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.view.tui.PlayerState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MessageHandler extends VirtualView implements View {
    private final GUI gui;
    ArrayList<LobbyDisplayInfo> lobbies;
    private String myLobby = "";
    private String myUsername = "";
    private PersonalGoalCard personalGoalCard;
    private MessageToClient lastMessage;
    private HashMap<String,EndGameScores> scores;
    private ChatBox chatBox = new ChatBox();
    /**
     * Handles the event when the client successfully connects to the server.
     * This method updates the GUI phase to LOGIN, sets the current scene to the menu scene,
     * and changes the scene to the updated one.
     *
     * @param connectedToServerMessage The ConnectedToServerMessage indicating a successful connection to the server.
     */
    @Override
    public void onConnectedServerMessage(ConnectedToServerMessage connectedToServerMessage) {
        gui.setPhase(GuiPhase.LOGIN);
        gui.setCurrentScene(gui.getScene(GameFxml.MENU_SCENE.s));
        gui.changeScene();
    }
    /**
     * Handles the response when creating a lobby.
     * If the response indicates a successful lobby creation, this method updates the GUI phase to IN_LOBBY,
     * sets the current scene to the in-lobby scene, changes the scene to the updated one, sets the admin name,
     * and initializes a new chat box.
     *
     * @param createLobbyResponse The CreateLobbyResponse indicating the result of the lobby creation.
     */
    @Override
        public void onCreateLobbyResponse(CreateLobbyResponse createLobbyResponse) {
            if(createLobbyResponse.isSuccessful()){
                gui.setPhase(GuiPhase.IN_LOBBY);
                gui.setCurrentScene(gui.getScene(GameFxml.IN_LOBBY_SCENE.s));
                gui.changeScene();
                gui.setAdminName(myUsername);
                this.chatBox = new ChatBox();
            }
        }
    /**
     * Handles the response when joining a lobby.
     * If the response indicates that the join request was refused, it sets the error message in the GUI.
     * If the response indicates that the player successfully joined the lobby, it updates the GUI phase to IN_LOBBY,
     * sets the current scene to the in-lobby scene, changes the scene to the updated one, sets the player names,
     * disables the spinner, and initializes a new chat box.
     * If the response indicates that the player has reconnected to a game, it updates the GUI phase to GAME,
     * sets the current scene to the game scene, and changes the scene to the updated one.
     *
     * @param joinLobbyResponse The JoinLobbyResponse indicating the result of the lobby join request.
     */
    @Override
    public void onJoinLobbyResponse(JoinLobbyResponse joinLobbyResponse) {
        if(joinLobbyResponse.getJoinType() == JoinType.REFUSED){
            gui.setError(joinLobbyResponse.getContent());
        }else if(joinLobbyResponse.getJoinType() == JoinType.JOINED){
            gui.setPhase(GuiPhase.IN_LOBBY);
            gui.setCurrentScene(gui.getScene(GameFxml.IN_LOBBY_SCENE.s));
            gui.changeScene();
            gui.setAllPlayersNames(joinLobbyResponse.getUsernames());
            gui.setSpinnerDisable();
            this.chatBox = new ChatBox();
        }else if(joinLobbyResponse.getJoinType() == JoinType.REJOINED){
            gui.setPhase(GuiPhase.GAME);
            gui.setCurrentScene(gui.getScene(GameFxml.GAME_SCENE.s));
            gui.changeScene();
        }
    }
    /**
     * Handles the response containing the list of available lobbies.
     * It updates the lobbies list in the class and notifies the GUI to display the updated list.
     *
     * @param lobbyListResponse The LobbyListResponse containing the list of available lobbies.
     */
    @Override
    public void onLobbyListResponse(LobbyListResponse lobbyListResponse) {
        this.lobbies = lobbyListResponse.getLobbies();
        gui.setLobbies(lobbies);
    }
    /**
     * Handles the response received after attempting to log in to the server.
     * If the login is successful, it updates the username and transitions to the server scene,
     * displaying the list of available lobbies. If the login fails, it displays an error message
     * indicating that the username is already in use.
     *
     * @param loginResponse The LoginResponse containing the login result.
     */
    @Override
    public void onLoginResponse(LoginResponse loginResponse) {
        if(loginResponse.isSuccessful()) {
            this.myUsername = loginResponse.getUsername();
            gui.setPhase(GuiPhase.SERVER);
            gui.setCurrentScene(gui.getScene(GameFxml.SERVER_SCENE.s));
            gui.changeScene();
            gui.lobbyList();
        }else{
            gui.setError("Username is already in use!");
        }
    }
    /**
     * Handles the reconnection message received after a successful reconnection to the game.
     * It updates the personal goal card, sets the phase to "GAME", and transitions to the game scene.
     * The player state is set to "WATCHING" and the game view is updated with the reconnected player's information.
     * A welcome notification is displayed, and the chat messages are updated if available.
     *
     * @param reconnectionMessage The ReconnectionMessage containing the reconnection information.
     */
    @Override
    public void onReconnectionMessage(ReconnectionMessage reconnectionMessage) {
        Player reconnectedPlayer = null;
        setPersonalGoalCard(reconnectionMessage.getPersonalGoal());
        for(Player p : reconnectionMessage.getModel().getPlayers()){
            if(Objects.equals(p.getUsername(), myUsername)){
                reconnectedPlayer = p;
                break;
            }
        }
        if(reconnectedPlayer != null){
            gui.setPhase(GuiPhase.GAME);
            gui.setCurrentScene(gui.getScene(GameFxml.GAME_SCENE.s));
            gui.changeScene();
            gui.setPlayerState(PlayerState.WATCHING);
            gui.setReconnectedGameScene(reconnectionMessage.getModel(),reconnectedPlayer);
            gui.setGameNotification("Ben tornato!");
            //gui.setChatMessages(reconnectionMessage.getChat());
        }
    }
    /**
     * Handles the response received after requesting a username change.
     * If the username change is successful, the player's username is updated, and an alert is displayed.
     * If the username change is unsuccessful, an error message is displayed.
     *
     * @param usernameResponse The UsernameResponse containing the result of the username change request.
     */
    @Override
    public void onUsernameResponse(UsernameResponse usernameResponse) {
        if(usernameResponse.isSuccessful()){
            setMyUsername(usernameResponse.getUsername());
            gui.setAlert();
        }else{
            gui.setError("Name change unsuccessful");
        }
    }
    /**
     * Handles the message received when a player achieves a common goal.
     * Updates the icons of the player who achieved the goal.
     *
     * @param achievedCommonGoalMessage The AchievedCommonGoalMessage containing the details of the achieved common goal.
     */
    @Override
    public void onAchievedCommonGoalMessage(AchievedCommonGoalMessage achievedCommonGoalMessage) {
        String str = achievedCommonGoalMessage.getContent();
        int index = str.trim().indexOf(" ");
        if (index >= 0) {
            str = str.substring(0, index).trim();
        }
        gui.setIcons(str,achievedCommonGoalMessage.getCommonGoal(),achievedCommonGoalMessage.getCg());
    }
    //TODO metodo vuoto! da implementare per la leaderboard scene
    @Override
    public void onAdjacentItemsPointsMessage(AdjacentItemsPointsMessage adjacentItemsPointsMessage) {
        gui.addAdjacentPoints(adjacentItemsPointsMessage.getPlayerUsername(),adjacentItemsPointsMessage.getPoints());
    }
    /**
     * Handles the message received when the game board is updated.
     *
     * @param boardMessage The BoardMessage containing the updated game board.
     */
    @Override
    public void onBoardMessage(BoardMessage boardMessage) {
        gui.setGameBoard(boardMessage.getBoard(), 0);
    }
    /**
     * Handles the message received when the bookshelf is updated.
     *
     * @param bookshelfMessage The BookshelfMessage containing the updated bookshelf.
     */
    @Override
    public void onBookshelfMessage(BookshelfMessage bookshelfMessage) {
        gui.setBookshelf(bookshelfMessage.getBookshelf());
    }
    /**
     * Handles the message received when the player receives information about drawing tiles.
     *
     * @param drawInfoMessage The DrawInfoMessage containing the relevant information.
     */
    @Override
    public void onDrawInfoMessage(DrawInfoMessage drawInfoMessage) {
        lastMessage = drawInfoMessage;
        gui.setPlayerState(PlayerState.ACTIVE);
        gui.setActionType(ActionType.DRAW_TILES);
        gui.setGameNotification("Puoi pescare al massimo "+Math.min(3,drawInfoMessage.getMaxNumItems())+" tessere");
        gui.setGameBoard(drawInfoMessage.getModel().getBoard().getGameboard(),drawInfoMessage.getMaxNumItems());
        //gui.setPlayers(drawInfoMessage.getModel().getPlayers());
        gui.setDrawn(false);
    }
    /**
     * Handles the message received when the game ends.
     *
     * @param endGameMessage The EndGameMessage containing the end game information.
     */
    @Override
    public void onEndGameMessage(EndGameMessage endGameMessage) {
        lastMessage = endGameMessage;
        setScores(gui.getScores());
        gui.setPhase(GuiPhase.END_GAME);
        gui.setCurrentScene(gui.getScene(GameFxml.END_SCENE.s));
        gui.changeScene();
        gui.setLeaderBoard(endGameMessage.getRanking());
        gui.setScoreBoard(scores);
    }
    /**
     * Handles the response received when exiting the game.
     *
     * @param exitGameResponse The ExitGameResponse containing the exit game information.
     */
    @Override
    public void onExitGameResponse(ExitGameResponse exitGameResponse) {
        //TODO ALERT FOR CONTENT OF EXITGAMERESPONSE
        gui.setPhase(GuiPhase.SERVER);
        gui.setCurrentScene(gui.getScene(GameFxml.SERVER_SCENE.s));
        gui.changeScene();
    }
    /**
     * Handles the message received when the game has started.
     *
     * @param gameStartedMessage The GameStartedMessage containing the game information.
     *
     * Updates the GUI phase to the GAME phase and changes the current scene to the game scene.
     */
    @Override
    public void onGameStartedMessage(GameStartedMessage gameStartedMessage) {
        lastMessage = gameStartedMessage;
        scores = new HashMap<>();
        gui.setPhase(GuiPhase.GAME);
        gui.setCurrentScene(gui.getScene(GameFxml.GAME_SCENE.s));
        gui.changeScene();
        gui.setGameScene(gameStartedMessage.getGameView());
        gui.setPlayers(gameStartedMessage.getGameView().getPlayerQueue());
    }
    /**
     * Handles the message received when the player needs to insert tiles from their hand.
     *
     * @param insertInfoMessage The InsertInfoMessage containing the hand tiles to be inserted.
     * <p>
     * Updates the GUI player state to ACTIVE and sets the action type to INSERT_HAND.
     * Updates the GUI hand with the tiles from the insertInfoMessage.
     */
    @Override
    public void onInsertInfoMessage(InsertInfoMessage insertInfoMessage) {
        lastMessage = insertInfoMessage;
        gui.setPlayerState(PlayerState.ACTIVE);
        gui.setActionType(ActionType.INSERT_HAND);
        gui.setHand(insertInfoMessage.getHand());
    }
    /**
     * Handles the message received when it is the last turn of the game.
     *
     * @param lastTurnMessage The LastTurnMessage indicating that it is the last turn.
     *
     * Updates the GUI game notification to indicate that it is the last turn.
     */
    @Override
    public void onLastTurnMessage(LastTurnMessage lastTurnMessage) {
        lastMessage = lastTurnMessage;
        gui.setGameNotification("It's the last turn!");
        gui.addLastTurnScore(lastTurnMessage.getCurrentPlayer());
        gui.setLastTurnIcon(lastTurnMessage.getCurrentPlayer());
    }
    //TODO vuoto! da implementare per la leaderboard scene
    @Override
    public void onLeaderBoardMessage(LeaderBoardMessage leaderBoardMessage) {

    }
    /**
     * Handles the message received at the start of a new turn.
     *
     * @param newTurnMessage The NewTurnMessage containing the information about the new turn.
     * <p>
     * This method updates the GUI with the necessary information for the new turn.
     * It clears the tiles, sets the action type to "NONE", displays a notification message, and updates the turn indicator.
     * If the current player is the same as the user's username, it displays a notification indicating that it's their turn.
     * Otherwise, it displays the notification received in the message.
     */
    @Override
    public void onNewTurnMessage(NewTurnMessage newTurnMessage) {
        lastMessage = newTurnMessage;
        gui.clearTiles();
        gui.setActionType(ActionType.NONE);
        if(myUsername.equals(newTurnMessage.getCurrentPlayer())){
            gui.setGameNotification("è il tuo turno");
        }else{
            gui.setGameNotification(newTurnMessage.getContent());
        }

        //gui.setTurnIndicator(newTurnMessage.getCurrentPlayer());
    }
    /**
     * Handles the message indicating that no common goal is available.
     *
     * @param noCommonGoalMessage The NoCommonGoalMessage indicating the absence of a common goal.
     *
     * This method currently does not perform any actions in the GUI.
     */
    @Override
    public void onNoCommonGoalMessage(NoCommonGoalMessage noCommonGoalMessage) {

    }
    /**
     * Handles the message containing the personal goal card for the player.
     *
     * @param personalGoalCardMessage The PersonalGoalCardMessage containing the player's personal goal card.
     *
     * This method updates the personal goal card in the GUI with the provided card from the message.
     */
    @Override
    public void onPersonalGoalCardMessage(PersonalGoalCardMessage personalGoalCardMessage) {
        lastMessage = personalGoalCardMessage;
        setPersonalGoalCard(personalGoalCardMessage.getPersonalGoalCard());
    }
    /**
     * Handles the message containing the points scored for a personal goal.
     *
     * @param personalGoalPointsMessage The PersonalGoalPointsMessage containing the points scored for a personal goal.
     * <p>
     * This method can be used to update the GUI with the points scored for a personal goal by a player.
     * However, the current implementation does not perform any specific actions.
     */
    @Override
    public void onPersonalGoalPointsMessage(PersonalGoalPointsMessage personalGoalPointsMessage) {
        gui.addPersonalScore(personalGoalPointsMessage.getPlayerUsername(),personalGoalPointsMessage.getPoints());

    }
    /**
     * Handles the message indicating that a player has left the game.
     *
     * @param playerLeftMessage The PlayerLeftMessage indicating that a player has left the game.
     * <p>
     * This method can be used to update the GUI with a notification about the player who left the game.
     * The provided content in the message can be displayed as a notification to inform other players about the event.
     */
    @Override
    public void onPlayerLeftMessage(PlayerLeftMessage playerLeftMessage) {
        gui.setGameNotification(playerLeftMessage.getContent());
    }
    /**
     * Handles the message indicating that a player has rejoined the game.
     *
     * @param playerRejoinedMessage The PlayerRejoinedMessage indicating that a player has rejoined the game.
     * <p>
     * This method can be used to update the GUI with a notification about the player who rejoined the game.
     * The provided player information in the message can be displayed as a notification to inform other players about the event.
     */
    @Override
    public void onPlayerRejoinedMessage(PlayerRejoinedMessage playerRejoinedMessage) {
        gui.setGameNotification("Il giocatore "+playerRejoinedMessage.getPlayer()+" si è riconnesso alla partita!");
    }
    /**
     * Handles the response message indicating the result of a request to change the number of players in the game.
     *
     * @param changeNumOfPlayerResponse The ChangeNumOfPlayerResponse indicating the result of the request.
     * <p>
     * This method can be used to update the GUI based on the response from the server.
     * If the request was successful, the chosen number of players can be displayed in the spinner control.
     * If the request failed, an error message can be displayed to inform the user about the failure.
     */
    @Override
    public void onChangeNumOfPlayerResponse(ChangeNumOfPlayerResponse changeNumOfPlayerResponse) {
        if(changeNumOfPlayerResponse.isSuccessful()){
            gui.setSpinnerValue(changeNumOfPlayerResponse.getChosenNum());
        }else{
            gui.setError(changeNumOfPlayerResponse.getContent());
        }
    }
    /**
     * Handles the response message indicating the result of a request to exit the lobby.
     *
     * @param exitLobbyResponse The ExitLobbyResponse indicating the result of the request.
     * <p>
     * This method can be used to update the GUI based on the response from the server.
     * If the request was successful, the GUI phase can be set to the server phase, the scene can be changed to the server scene,
     * and the chat box can be cleared.
     */
    @Override
    public void onExitLobbyResponse(ExitLobbyResponse exitLobbyResponse) {
        if(exitLobbyResponse.isSuccessful()){
            gui.setPhase(GuiPhase.SERVER);
            gui.setCurrentScene(gui.getScene(GameFxml.SERVER_SCENE.s));
            gui.changeScene();
            this.chatBox = null;
        }
    }
    /**
     * Handles the message indicating that the game is not ready to start.
     *
     * @param gameNotReadyMessage The GameNotReadyMessage indicating the reason why the game is not ready.
     * <p>
     * This method can be used to display an error message on the GUI indicating that the game cannot start
     * due to certain conditions not being met.
     */
    @Override
    public void onGameNotReadyMessage(GameNotReadyMessage gameNotReadyMessage) {
        gui.setError("Non ci sono le condizioni per iniziare la partita!");
    }
    //TODO empty method
    @Override
    public void onInvalidCommandMessage(InvalidCommandMessage invalidCommandMessage) {

    }
    //TODO empty method
    @Override
    public void onNewAdminMessage(NewAdminMessage newAdminMessage) {

    }
    /**
     * Handles the message indicating that the player is not the admin of the lobby.
     *
     * @param notAdminMessage The NotAdminMessage containing information about the admin status.
     * <p>
     * This method can be used to display an error message on the GUI when a non-admin player tries to use a command
     * that is restricted to the lobby admin.
     */
    @Override
    public void onNotAdminMessage(NotAdminMessage notAdminMessage) {
        gui.setError("Non sei l'admin di questa lobby! Solo l'admin può usare questo comando.");
    }
    //TODO empty method
    @Override
    public void onPlayerListResponse(PlayerListResponse playerListResponse) {
    }
    /**
     * Handles the update message indicating the updated list of players in the lobby.
     *
     * @param playersInLobbyUpdate The PlayersInLobbyUpdate message containing the updated player list.
     *
     * This method can be used to update the player list displayed on the GUI when there are changes in the lobby's player roster.
     */
    @Override
    public void onPlayersInLobbyUpdate(PlayersInLobbyUpdate playersInLobbyUpdate) {
        gui.setAllPlayersNames(playersInLobbyUpdate.getAllPlayersUsernames());
    }
    /**
     * Updates the GUI based on the received message from the server.
     *
     * @param message The message received from the server.
     * <p>
     * This method is responsible for handling different types of messages and updating the GUI accordingly.
     * It checks the type of the message and calls the corresponding handler method to process the message.
     * If the message type is not recognized, it prints an error message indicating that the message is ignored.
     */
    @Override
    public void update(Message message) {
        if (message.getType()== MessageType.GAME_MSG) {
            onGameMessage((MessageToClient) message);
        }else if(message.getType()==MessageType.CONNECTION_MSG){
            onConnectionMessage((MessageToClient) message);
        } else if (message.getType()==MessageType.LOBBY_MSG) {
            onLobbyMessage((MessageToClient) message);
        } else if (message instanceof ChatMessage) {
            onChatMessage((ChatMessage) message);
        } else
        //if(!(message instanceof HeartBeatMessage))
        {
            System.err.println("Ignoring message from server");
        }
    }
    /**
     * Handles the ChatMessage received from the server.
     *
     * @param message The ChatMessage received from the server.
     *
     * This method adds the ChatMessage to the chatBox and updates the GUI to display the new message.
     */
    private void onChatMessage(ChatMessage message) {
        chatBox.addMessage(message);
        gui.setChatMessage(message);
    }
    /**
     * Handles the LobbyMessage received from the server.
     *
     * @param message The LobbyMessage received from the server.
     * <p>
     * This method executes the appropriate action based on the type of LobbyMessage.
     * The execution is delegated to the corresponding method in the message object.
     */
    private void onLobbyMessage(MessageToClient message) {
        message.execute(this);
    }
    /**
     * Handles the ConnectionMessage received from the server.
     *
     * @param message The ConnectionMessage received from the server.
     * <p>
     * This method executes the appropriate action based on the type of ConnectionMessage.
     * The execution is delegated to the corresponding method in the message object.
     */
    private void onConnectionMessage(MessageToClient message) {
        message.execute(this);
    }
    /**
     * Handles the GameMessage received from the server.
     *
     * @param message The GameMessage received from the server.
     * <p>
     * This method executes the appropriate action based on the type of GameMessage.
     * The execution is delegated to the corresponding method in the message object.
     */
    private void onGameMessage(MessageToClient message) {
        message.execute(this);
    }
    //TODO empty method
    @Override
    public void run() {
    }
    /**
     * Constructs a new MessageHandler object.
     *
     * @param gui The GUI object to interact with the user interface.
     */
    public MessageHandler(GUI gui){
        this.gui = gui;
    }
    /**
     * Returns the lobby name associated with the current user.
     *
     * @return The lobby name.
     */
    public String getMyLobby() {
        return myLobby;
    }

    public ArrayList<LobbyDisplayInfo> getLobbies() {
        return lobbies;
    }

    /**
     * Sets the lobby name the user is connected to
     * @param myLobby
     */
    public void setMyLobby(String myLobby) {
        this.myLobby = myLobby;
    }

    /**
     * Returns the username of the current user
     * @return the username of the user
     */
    public String getMyUsername() {
        return myUsername;
    }

    /**
     * Sets the usernema of the current user
     * @param myUsername the username to be set
     */
    public void setMyUsername(String myUsername) {
        this.myUsername = myUsername;
    }

    /**
     * @return the last Message received by the client
     */
    public MessageToClient getLastMessage() {
        return lastMessage;
    }

    /**
     * Sets the {@link PersonalGoalCard} of the user, received from the server
     * @param personalGoalCard the {@link PersonalGoalCard} of the user
     */
    public void setPersonalGoalCard(PersonalGoalCard personalGoalCard) {
        this.personalGoalCard = personalGoalCard;
    }

    /**
     * Returns the {@link PersonalGoalCard} of the user
     * @return the {@link PersonalGoalCard} of the user
     */
    public PersonalGoalCard getPersonalGoalCard() {
        return personalGoalCard;
    }

    /**
     * @return the list of {@link ChatMessage} contained in the chatLog
     */
    public ArrayList<ChatMessage> getChatLog(){
        return chatBox.getChatLog();
    }
    public void setScores(HashMap<String,EndGameScores> scores){
        this.scores = scores;
    }
}
