package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.connectionmessage.*;
import it.polimi.ingsw.network.message.gamemessage.*;
import it.polimi.ingsw.network.message.lobbymessage.*;
import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.view.View;

public class Gui extends Observable implements View {

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

    }
}
