package it.polimi.ingsw.network.message.lobbymessage;

public enum LobbyMessageType {
    START_GAME_REQUEST,
    EXIT_LOBBY_RESPONSE,
    GAME_NOT_READY,
    NEW_ADMIN,
    NOT_ADMIN,
    CHANGE_NUM_OF_PLAYERS_REQUEST,
    INVALID_COMMAND,
    CHANGE_NUM_OF_PLAYER_RESPONSE,
    PLAYER_LIST_REQUEST,
    PLAYER_LIST_RESPONSE,
    NEW_PLAYER_IN_LOBBY,
    EXIT_LOBBY_REQUEST

}
