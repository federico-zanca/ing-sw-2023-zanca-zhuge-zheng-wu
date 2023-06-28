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
import java.util.Objects;

public class MessageHandler extends VirtualView implements View {
    private final GUI gui;
    ArrayList<LobbyDisplayInfo> lobbies;
    private String myLobby = "";
    private String myUsername = "";
    private PersonalGoalCard personalGoalCard;
    private MessageToClient lastMessage;
    private ChatBox chatBox = new ChatBox();
    @Override
    public void onConnectedServerMessage(ConnectedToServerMessage connectedToServerMessage) {
        gui.setPhase(GuiPhase.LOGIN);
        gui.setCurrentScene(gui.getScene(GameFxml.MENU_SCENE.s));
        gui.changeScene();
    }
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
    @Override
    public void onLobbyListResponse(LobbyListResponse lobbyListResponse) {
        this.lobbies = lobbyListResponse.getLobbies();
        gui.setLobbies(lobbies);
    }

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

    @Override
    public void onUsernameResponse(UsernameResponse usernameResponse) {
        if(usernameResponse.isSuccessful()){
            setMyUsername(usernameResponse.getUsername());
            gui.setAlert();
        }else{
            gui.setError("Name change unsuccessful");
        }
    }

    @Override
    public void onAchievedCommonGoalMessage(AchievedCommonGoalMessage achievedCommonGoalMessage) {
        String str = achievedCommonGoalMessage.getContent();
        int index = str.trim().indexOf(" ");
        if (index >= 0) {
            str = str.substring(0, index).trim();
        }
        gui.setIcons(str,achievedCommonGoalMessage.getCommonGoal(),achievedCommonGoalMessage.getCg());
    }

    @Override
    public void onAdjacentItemsPointsMessage(AdjacentItemsPointsMessage adjacentItemsPointsMessage) {

    }

    @Override
    public void onBoardMessage(BoardMessage boardMessage) {
        gui.setGameBoard(boardMessage.getBoard(), 0);
    }

    @Override
    public void onBookshelfMessage(BookshelfMessage bookshelfMessage) {
        gui.setBookshelf(bookshelfMessage.getBookshelf());
    }

    @Override
    public void onDrawInfoMessage(DrawInfoMessage drawInfoMessage) {
        lastMessage = drawInfoMessage;
        gui.setPlayerState(PlayerState.ACTIVE);
        gui.setActionType(ActionType.DRAW_TILES);
        gui.setGameNotification("Puoi pescare al massimo "+Math.min(3,drawInfoMessage.getMaxNumItems())+" tessere");
        gui.setGameBoard(drawInfoMessage.getModel().getBoard().getGameboard(),drawInfoMessage.getMaxNumItems());
        gui.setPlayers(drawInfoMessage.getModel().getPlayers());
    }

    @Override
    public void onEndGameMessage(EndGameMessage endGameMessage) {
        lastMessage = endGameMessage;
        gui.setPhase(GuiPhase.END_GAME);
        gui.setCurrentScene(gui.getScene(GameFxml.END_SCENE.s));
        gui.changeScene();
        gui.setLeaderBoard(endGameMessage.getRanking());
    }

    @Override
    public void onExitGameResponse(ExitGameResponse exitGameResponse) {
        //TODO ALERT FOR CONTENT OF EXITGAMERESPONSE
        gui.setPhase(GuiPhase.SERVER);
        gui.setCurrentScene(gui.getScene(GameFxml.SERVER_SCENE.s));
        gui.changeScene();
    }

    @Override
    public void onGameStartedMessage(GameStartedMessage gameStartedMessage) {
        lastMessage = gameStartedMessage;
        gui.setPhase(GuiPhase.GAME);
        gui.setCurrentScene(gui.getScene(GameFxml.GAME_SCENE.s));
        gui.changeScene();
        gui.setGameScene(gameStartedMessage.getGameView());
    }

    @Override
    public void onInsertInfoMessage(InsertInfoMessage insertInfoMessage) {
        lastMessage = insertInfoMessage;
        gui.setPlayerState(PlayerState.ACTIVE);
        gui.setActionType(ActionType.INSERT_HAND);
        gui.setHand(insertInfoMessage.getHand());
    }

    @Override
    public void onLastTurnMessage(LastTurnMessage lastTurnMessage) {
        lastMessage = lastTurnMessage;
        gui.setGameNotification("It's the last turn!");
    }

    @Override
    public void onLeaderBoardMessage(LeaderBoardMessage leaderBoardMessage) {

    }

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
        gui.setTurnIndicator(newTurnMessage.getCurrentPlayer());
    }

    @Override
    public void onNoCommonGoalMessage(NoCommonGoalMessage noCommonGoalMessage) {

    }

    @Override
    public void onPersonalGoalCardMessage(PersonalGoalCardMessage personalGoalCardMessage) {
        lastMessage = personalGoalCardMessage;
        setPersonalGoalCard(personalGoalCardMessage.getPersonalGoalCard());
    }

    @Override
    public void onPersonalGoalPointsMessage(PersonalGoalPointsMessage personalGoalPointsMessage) {

    }

    @Override
    public void onPlayerLeftMessage(PlayerLeftMessage playerLeftMessage) {
        gui.setGameNotification(playerLeftMessage.getContent());
    }

    @Override
    public void onPlayerRejoinedMessage(PlayerRejoinedMessage playerRejoinedMessage) {
        gui.setGameNotification("Il giocatore "+playerRejoinedMessage.getPlayer()+" si è riconnesso alla partita!");
    }

    @Override
    public void onChangeNumOfPlayerResponse(ChangeNumOfPlayerResponse changeNumOfPlayerResponse) {
        if(changeNumOfPlayerResponse.isSuccessful()){
            gui.setSpinnerValue(changeNumOfPlayerResponse.getChosenNum());
        }else{
            gui.setError(changeNumOfPlayerResponse.getContent());
        }
    }

    @Override
    public void onExitLobbyResponse(ExitLobbyResponse exitLobbyResponse) {
        if(exitLobbyResponse.isSuccessful()){
            gui.setPhase(GuiPhase.SERVER);
            gui.setCurrentScene(gui.getScene(GameFxml.SERVER_SCENE.s));
            gui.changeScene();
            this.chatBox = null;
        }
    }

    @Override
    public void onGameNotReadyMessage(GameNotReadyMessage gameNotReadyMessage) {
        gui.setError("Non ci sono le condizioni per iniziare la partita!");
    }

    @Override
    public void onInvalidCommandMessage(InvalidCommandMessage invalidCommandMessage) {

    }

    @Override
    public void onNewAdminMessage(NewAdminMessage newAdminMessage) {

    }

    @Override
    public void onNotAdminMessage(NotAdminMessage notAdminMessage) {
        gui.setError("Non sei l'admin di questa lobby! Solo l'admin può usare questo comando.");
    }

    @Override
    public void onPlayerListResponse(PlayerListResponse playerListResponse) {
    }

    @Override
    public void onPlayersInLobbyUpdate(PlayersInLobbyUpdate playersInLobbyUpdate) {
        gui.setAllPlayersNames(playersInLobbyUpdate.getAllPlayersUsernames());
    }

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

    private void onChatMessage(ChatMessage message) {
        chatBox.addMessage(message);
        gui.setChatMessage(message);
    }

    private void onLobbyMessage(MessageToClient message) {
        message.execute(this);
    }

    private void onConnectionMessage(MessageToClient message) {
        message.execute(this);
    }
    private void onGameMessage(MessageToClient message) {
        message.execute(this);
    }
    @Override
    public void run() {
    }
    public MessageHandler(GUI gui){
        this.gui = gui;
    }
    public String getMyLobby() {
        return myLobby;
    }

    public ArrayList<LobbyDisplayInfo> getLobbies() {
        return lobbies;
    }

    public void setMyLobby(String myLobby) {
        this.myLobby = myLobby;
    }
    public String getMyUsername() {
        return myUsername;
    }
    public void setMyUsername(String myUsername) {
        this.myUsername = myUsername;
    }
    public MessageToClient getLastMessage() {
        return lastMessage;
    }
    public void setPersonalGoalCard(PersonalGoalCard personalGoalCard) {
        this.personalGoalCard = personalGoalCard;
    }
    public PersonalGoalCard getPersonalGoalCard() {
        return personalGoalCard;
    }
    public ArrayList<ChatMessage> getChatLog(){
        return chatBox.getChatLog();
    }
}
