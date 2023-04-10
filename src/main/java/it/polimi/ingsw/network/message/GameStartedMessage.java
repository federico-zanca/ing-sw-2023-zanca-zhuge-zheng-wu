package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.gameboard.Square;

public class GameStartedMessage extends Message {
    private final Square[][] gameboard;

    public GameStartedMessage(String username, Square[][] gameboard) {
        super(username, MessageType.GAME_STARTED);
        this.gameboard = gameboard;
    }

    public Square[][] getGameboard() {
        return gameboard;
    }
}
