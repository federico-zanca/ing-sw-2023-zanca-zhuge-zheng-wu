package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.distributed.ClientState;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.ChatMessage;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.connectionmessage.*;
import it.polimi.ingsw.network.message.gamemessage.*;
import it.polimi.ingsw.network.message.lobbymessage.*;
import it.polimi.ingsw.view.tui.ActionType;
import it.polimi.ingsw.view.tui.LobbyDisplayInfo;
import it.polimi.ingsw.view.tui.PlayerState;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;
import javafx.application.Application;

import java.util.ArrayList;

public class Gui extends VirtualView implements View {
    private ClientState clientState;
    private ActionType actionType = ActionType.LOGIN;
    private GameMessage lastMessage;
    private String myUsername;
    private PlayerState playerState = PlayerState.WATCHING;
    private final Object lock = new Object();
    ArrayList<Square> tilesToDraw;
    ArrayList<ItemTile> tilesToInsert;

    ArrayList<LobbyDisplayInfo> lobbies;

    public ArrayList<LobbyDisplayInfo> getLobbies() {
        return lobbies;
    }
    private ArrayList<ChatMessage> chat;
    private boolean isChatting=false;

    public Gui(){
        chat = new ArrayList<>();
        tilesToDraw = new ArrayList<>();
        tilesToInsert = new ArrayList<>();
    }

    @Override
    public void onConnectedServerMessage(ConnectedToServerMessage connectedToServerMessage) {

    }

    @Override
    public void onCreateLobbyResponse(CreateLobbyResponse createLobbyResponse) {

    }

    @Override
    public void onJoinLobbyResponse(JoinLobbyResponse joinLobbyResponse) {

    }

    @Override
    public void onLobbyListResponse(LobbyListResponse lobbyListResponse) {
        this.lobbies = lobbyListResponse.getLobbies();
    }

    @Override
    public void onLoginResponse(LoginResponse loginResponse) {

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

    }

    @Override
    public void onGameNotReadyMessage(GameNotReadyMessage gameNotReadyMessage) {

    }

    @Override
    public void onInvalidCommandMessage(InvalidCommandMessage invalidCommandMessage) {

    }

    @Override
    public void onNewAdminMessage(NewAdminMessage newAdminMessage) {

    }

    @Override
    public void onNotAdminMessage(NotAdminMessage notAdminMessage) {

    }

    @Override
    public void onPlayerListResponse(PlayerListResponse playerListResponse) {

    }

    @Override
    public void onPlayersInLobbyUpdate(PlayersInLobbyUpdate playersInLobbyUpdate) {

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
}
