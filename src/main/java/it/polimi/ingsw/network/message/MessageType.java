package it.polimi.ingsw.network.message;

public enum MessageType {
    ERROR,
    LOGINREQUEST,
    DRAW_TILES,
    INSERT_TILES,

    BOARD, //il messaggio contiene la board aggiornata da dare alla view
    DRAW_INFO, LOGIN_REPLY, CHANGE_PHASE, INSERT_INFO, ACHIEVED_COMMON_GOAL, NO_COMMON_GOAL, NEW_TURN, GAME_STARTED, END_GAME, LEADERBOARD, LAST_TURN, BOOKSHELF
}
