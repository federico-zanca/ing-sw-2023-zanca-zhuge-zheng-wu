package it.polimi.ingsw.view;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.connectionmessage.*;
import it.polimi.ingsw.network.message.gamemessage.*;
import it.polimi.ingsw.network.message.lobbymessage.*;
/**
 * Represents a generic view to be implemented by each view type (TUI, GUI in JavaFX)
 */
public interface View {
    /**
     * Called when a connected server message is received.
     * Notify that the client is connected to the server.
     * @param connectedToServerMessage The connected server message.
     */
    void onConnectedServerMessage(ConnectedToServerMessage connectedToServerMessage);

    /**
     * Called when a create_lobby_response is received.
     * Indicating whether it was successful or not.
     * @param createLobbyResponse The create_lobby_response.
     */
    void onCreateLobbyResponse(CreateLobbyResponse createLobbyResponse);

    /**
     * Called when a join lobby response is received.
     * Handles the join type of the client.
     * @param joinLobbyResponse The join lobby response.
     */
    void onJoinLobbyResponse(JoinLobbyResponse joinLobbyResponse);

    /**
     * Called when a lobby list response is received.
     * Displays the lobby list.
     * @param lobbyListResponse The lobby list response.
     */
    void onLobbyListResponse(LobbyListResponse lobbyListResponse);

    /**
     * Called when a login response is received.
     * Indicating whether it was successful or not
     * @param loginResponse The login response.
     */
    void onLoginResponse(LoginResponse loginResponse);

    /**
     * Called when a reconnection message is received.
     * Handles reconnection of client to game.
     * @param reconnectionMessage The reconnection message.
     */
    void onReconnectionMessage(ReconnectionMessage reconnectionMessage);

    /**
     * Called when a username response is received.
     * Indicating whether it was successful or not.
     * @param usernameResponse The username response.
     */
    void onUsernameResponse(UsernameResponse usernameResponse);

    /**
     * Called when an achieved common goal message is received.
     * Indicating a player achieved cg in the last turn
     * @param achievedCommonGoalMessage The achieved common goal message.
     */
    void onAchievedCommonGoalMessage(AchievedCommonGoalMessage achievedCommonGoalMessage);

    /**
     * Called when an adjacent items points message is received.
     * @param adjacentItemsPointsMessage The adjacent items points message.
     */
    void onAdjacentItemsPointsMessage(AdjacentItemsPointsMessage adjacentItemsPointsMessage);

    /**
     * Called when a board message is received.
     *
     * @param boardMessage The board message.
     */
    void onBoardMessage(BoardMessage boardMessage);

    /**
     * Called when a bookshelf message is received.
     *
     * @param bookshelfMessage The bookshelf message.
     */
    void onBookshelfMessage(BookshelfMessage bookshelfMessage);

    /**
     * Called when a draw info message is received.
     *
     * @param drawInfoMessage The draw info message.
     */
    void onDrawInfoMessage(DrawInfoMessage drawInfoMessage);

    /**
     * Called when an end game message is received.
     *
     * @param endGameMessage The end game message.
     */
    void onEndGameMessage(EndGameMessage endGameMessage);

    /**
     * Called when an exit game response is received.
     *
     * @param exitGameResponse The exit game response.
     */
    void onExitGameResponse(ExitGameResponse exitGameResponse);

    /**
     * Called when a game started message is received.
     *
     * @param gameStartedMessage The game started message.
     */
    void onGameStartedMessage(GameStartedMessage gameStartedMessage);

    /**
     * Called when an insert info message is received.
     *
     * @param insertInfoMessage The insert info message.
     */
    void onInsertInfoMessage(InsertInfoMessage insertInfoMessage);

    /**
     * Called when a last Turn message is received.
     *
     * @param lastTurnMessage The last turn message.
     */
    void onLastTurnMessage(LastTurnMessage lastTurnMessage);

    /**
     * Called when a Leader Board Message is received.
     *
     * @param leaderBoardMessage The leader Board message.
     */
    void onLeaderBoardMessage(LeaderBoardMessage leaderBoardMessage);

    /**
     * Called when a new Turn Message is received.
     *
     * @param newTurnMessage The new Turn Message.
     */
    void onNewTurnMessage(NewTurnMessage newTurnMessage);

    /**
     * Called when a no CommonGoal Message is received.
     *
     * @param noCommonGoalMessage The no CommonGoal Message.
     */
    void onNoCommonGoalMessage(NoCommonGoalMessage noCommonGoalMessage);

    /**
     * Called when a personalGoal Card Message is received.
     *
     * @param personalGoalCardMessage The personalGoal Card Message.
     */
    void onPersonalGoalCardMessage(PersonalGoalCardMessage personalGoalCardMessage);

    /**
     * Called when a personalGoal Points Message is received.
     *
     * @param personalGoalPointsMessage The personalGoal Points Message.
     */
    void onPersonalGoalPointsMessage(PersonalGoalPointsMessage personalGoalPointsMessage);

    /**
     * Called when a player Left Message is received.
     *
     * @param playerLeftMessage The player Left Message.
     */
    void onPlayerLeftMessage(PlayerLeftMessage playerLeftMessage);

    /**
     * Called when a player Rejoined Message is received.
     *
     * @param playerRejoinedMessage The player Rejoined Message.
     */
    void onPlayerRejoinedMessage(PlayerRejoinedMessage playerRejoinedMessage);

    /**
     * Called when a change Num Of Player Response is received.
     *
     * @param changeNumOfPlayerResponse The change_Num_Of_Player Response.
     */
    void onChangeNumOfPlayerResponse(ChangeNumOfPlayerResponse changeNumOfPlayerResponse);

    /**
     * Called when an exit Lobby Response is received.
     *
     * @param exitLobbyResponse The exit Lobby Response.
     */
    void onExitLobbyResponse(ExitLobbyResponse exitLobbyResponse);

    /**
     * Called when a game Not Ready Message is received.
     *
     * @param gameNotReadyMessage The game Not Ready Message.
     */
    void onGameNotReadyMessage(GameNotReadyMessage gameNotReadyMessage);

    /**
     * Called when an invalid Command Message is received.
     *
     * @param invalidCommandMessage The invalid Command Message.
     */
    void onInvalidCommandMessage(InvalidCommandMessage invalidCommandMessage);

    /**
     * Called when a new Admin Message is received.
     *
     * @param newAdminMessage The new Admin Message.
     */
    void onNewAdminMessage(NewAdminMessage newAdminMessage);

    /**
     * Called when a not Admin Message is received.
     *
     * @param notAdminMessage The not Admin Message.
     */
    void onNotAdminMessage(NotAdminMessage notAdminMessage);

    /**
     * Called when a player List Response is received.
     *
     * @param playerListResponse The player List Response.
     */
    void onPlayerListResponse(PlayerListResponse playerListResponse);

    /**
     * Called when a players In Lobby Update message is received.
     *
     * @param playersInLobbyUpdate The players_in_Lobby_Update message.
     */
    void onPlayersInLobbyUpdate(PlayersInLobbyUpdate playersInLobbyUpdate);

    /**
     Updates the view based on the received message.
     @param message the message received
     */
    void update(Message message);
  /*

    void askPlayerNumber();


    void showLoginResult(boolean valiUsername, String username);


    void showLobby();


    void showFirstPlayerThisGame();

*/
}
