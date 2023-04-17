package it.polimi.ingsw.network.message.gamemessage;

public enum GameMessageType {
    ERROR,
    LOGINREQUEST,
    DRAW_TILES,
    INSERT_TILES,

    BOARD, //il messaggio contiene la board aggiornata da dare alla view
    DRAW_INFO,
    LOGIN_REPLY,
    INSERT_INFO,
    ACHIEVED_COMMON_GOAL,
    NO_COMMON_GOAL,
    NEW_TURN,
    GAME_STARTED,
    END_GAME,
    LEADERBOARD,
    LAST_TURN,
    PERSONAL_GOAL_POINTS,
    ADJACENT_ITEMS_POINTS,
    BOOKSHELF,
    COMMONGOALCARD
}
