package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.enumerations.JoinType;
import it.polimi.ingsw.network.message.ChatMessage;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.connectionmessage.*;
import it.polimi.ingsw.network.message.gamemessage.*;
import it.polimi.ingsw.network.message.lobbymessage.*;
import it.polimi.ingsw.view.gui.sceneControllers.GuiPhase;
import it.polimi.ingsw.view.tui.LobbyDisplayInfo;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;

public class MessageHandler extends VirtualView implements View {
    private final GUI gui;
    ArrayList<LobbyDisplayInfo> lobbies;
    private String myLobby = "";
    private String myUsername = "";
    private GameMessage lastMessage;

    public MessageHandler(GUI gui){
        this.gui = gui;
    }

    @Override
    public void onConnectedServerMessage(ConnectedToServerMessage connectedToServerMessage) {
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

    @Override
    public void onCreateLobbyResponse(CreateLobbyResponse createLobbyResponse) {
        if(createLobbyResponse.isSuccessful()){
            gui.setPhase(GuiPhase.IN_LOBBY);
            gui.setCurrentScene(gui.getScene(GameFxml.IN_LOBBY_SCENE.s));
            gui.changeScene();
            gui.setAdminName(myUsername);
        }
        //TODO settare error in caso di fallimento
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
        }
    }

    @Override
    public void onLobbyListResponse(LobbyListResponse lobbyListResponse) {
        this.lobbies = lobbyListResponse.getLobbies();
    }

    @Override
    public void onLoginResponse(LoginResponse loginResponse) {
        if(loginResponse.isSuccessful()) {
            //this.myUsername = loginResponse.getUsername();
            gui.setPhase(GuiPhase.SERVER);
            gui.setCurrentScene(gui.getScene(GameFxml.SERVER_SCENE.s));
            gui.changeScene();
        }else{
            gui.setError("Username is already in use!");
        }
    }

    @Override
    public void onReconnectionMessage(ReconnectionMessage reconnectionMessage) {

    }

    @Override
    public void onUsernameResponse(UsernameResponse usernameResponse) {

    }

    @Override
    public void onAchievedCommonGoalMessage(AchievedCommonGoalMessage achievedCommonGoalMessage) {

    }

    @Override
    public void onAdjacentItemsPointsMessage(AdjacentItemsPointsMessage adjacentItemsPointsMessage) {

    }

    @Override
    public void onBoardMessage(BoardMessage boardMessage) {

    }

    @Override
    public void onBookshelfMessage(BookshelfMessage bookshelfMessage) {

    }

    @Override
    public void onDrawInfoMessage(DrawInfoMessage drawInfoMessage) {

    }

    @Override
    public void onEndGameMessage(EndGameMessage endGameMessage) {

    }

    @Override
    public void onExitGameResponse(ExitGameResponse exitGameResponse) {
    }

    @Override
    public void onGameStartedMessage(GameStartedMessage gameStartedMessage) {
        lastMessage = gameStartedMessage;
        gui.setPhase(GuiPhase.GAME);
        gui.setCurrentScene(gui.getScene(GameFxml.GAME_SCENE.s));
        gui.changeScene();
    }

    @Override
    public void onInsertInfoMessage(InsertInfoMessage insertInfoMessage) {

    }

    @Override
    public void onLastTurnMessage(LastTurnMessage lastTurnMessage) {

    }

    @Override
    public void onLeaderBoardMessage(LeaderBoardMessage leaderBoardMessage) {

    }

    @Override
    public void onNewTurnMessage(NewTurnMessage newTurnMessage) {

    }

    @Override
    public void onNoCommonGoalMessage(NoCommonGoalMessage noCommonGoalMessage) {

    }

    @Override
    public void onPersonalGoalCardMessage(PersonalGoalCardMessage personalGoalCardMessage) {

    }

    @Override
    public void onPersonalGoalPointsMessage(PersonalGoalPointsMessage personalGoalPointsMessage) {

    }

    @Override
    public void onPlayerLeftMessage(PlayerLeftMessage playerLeftMessage) {

    }

    @Override
    public void onPlayerRejoinedMessage(PlayerRejoinedMessage playerRejoinedMessage) {

    }

    @Override
    public void onChangeNumOfPlayerResponse(ChangeNumOfPlayerResponse changeNumOfPlayerResponse) {

    }

    @Override
    public void onExitLobbyResponse(ExitLobbyResponse exitLobbyResponse) {
        if(exitLobbyResponse.isSuccessful()){
            gui.setPhase(GuiPhase.SERVER);
            gui.setCurrentScene(gui.getScene(GameFxml.SERVER_SCENE.s));
            gui.changeScene();
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
        if (message instanceof GameMessage) {
            onGameMessage((GameMessage) message);
        }else if(message instanceof ConnectionMessage){
            onConnectionMessage((ConnectionMessage) message);
        } else if (message instanceof LobbyMessage) {
            onLobbyMessage((LobbyMessage) message);
        } else if (message instanceof ChatMessage) {
            onChatMessage((ChatMessage) message);
        } else
        //if(!(message instanceof HeartBeatMessage))
        {
            System.err.println("Ignoring message from server");
        }
    }
    private void onChatMessage(ChatMessage message) {
    }
    private void onLobbyMessage(LobbyMessage message) {
        message.execute(this);
    }
    private void onConnectionMessage(ConnectionMessage message) {
        message.execute(this);
    }
    private void onGameMessage(GameMessage message) {
        message.execute(this);
    }
    @Override
    public void run() {
    }

    public void setMyUsername(String myUsername) {
        this.myUsername = myUsername;
    }
}
