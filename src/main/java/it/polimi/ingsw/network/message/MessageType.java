package it.polimi.ingsw.network.message;

public enum MessageType {
    ERROR,
    LOGINREQUEST,
    DRAW_TILES,
    INSERT_TILES,

    BOARD, //il messaggio contiene la board aggiornata da dare alla view
    DRAW_INFO, BOOKSHELF
}
