package it.polimi.ingsw.view;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.connectionmessage.*;
import it.polimi.ingsw.network.message.gamemessage.*;
import it.polimi.ingsw.network.message.lobbymessage.*;

public interface View {

    void onConnectedServerMessage(ConnectedToServerMessage connectedToServerMessage);

    void onCreateLobbyResponse(CreateLobbyResponse createLobbyResponse);

    void onJoinLobbyResponse(JoinLobbyResponse joinLobbyResponse);

    void onLobbyListResponse(LobbyListResponse lobbyListResponse);

    void onLoginResponse(LoginResponse loginResponse);

    void onReconnectionMessage(ReconnectionMessage reconnectionMessage);

    void onUsernameResponse(UsernameResponse usernameResponse);

    void onAchievedCommonGoalMessage(AchievedCommonGoalMessage achievedCommonGoalMessage);

    void onAdjacentItemsPointsMessage(AdjacentItemsPointsMessage adjacentItemsPointsMessage);

    void onBoardMessage(BoardMessage boardMessage);

    void onBookshelfMessage(BookshelfMessage bookshelfMessage);

    void onDrawInfoMessage(DrawInfoMessage drawInfoMessage);

    void onEndGameMessage(EndGameMessage endGameMessage);

    void onExitGameResponse(ExitGameResponse exitGameResponse);

    void onGameStartedMessage(GameStartedMessage gameStartedMessage);

    void onInsertInfoMessage(InsertInfoMessage insertInfoMessage);

    void onLastTurnMessage(LastTurnMessage lastTurnMessage);

    void onLeaderBoardMessage(LeaderBoardMessage leaderBoardMessage);

    void onNewTurnMessage(NewTurnMessage newTurnMessage);

    void onNoCommonGoalMessage(NoCommonGoalMessage noCommonGoalMessage);

    void onPersonalGoalCardMessage(PersonalGoalCardMessage personalGoalCardMessage);

    void onPersonalGoalPointsMessage(PersonalGoalPointsMessage personalGoalPointsMessage);

    void onPlayerLeftMessage(PlayerLeftMessage playerLeftMessage);

    void onPlayerRejoinedMessage(PlayerRejoinedMessage playerRejoinedMessage);

    void onChangeNumOfPlayerResponse(ChangeNumOfPlayerResponse changeNumOfPlayerResponse);

    void onExitLobbyResponse(ExitLobbyResponse exitLobbyResponse);

    void onGameNotReadyMessage(GameNotReadyMessage gameNotReadyMessage);

    void onInvalidCommandMessage(InvalidCommandMessage invalidCommandMessage);

    void onNewAdminMessage(NewAdminMessage newAdminMessage);

    void onNotAdminMessage(NotAdminMessage notAdminMessage);

    void onPlayerListResponse(PlayerListResponse playerListResponse);

    void onPlayersInLobbyUpdate(PlayersInLobbyUpdate playersInLobbyUpdate);

    void update(Message message);
/*

    void askPlayerNumber();


    void showLoginResult(boolean valiUsername, String username);
    //TODO gestisci anche Login Failed se la connessione salta


    void showLobby();


    void showFirstPlayerThisGame();

*/
}
