package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.GameView;

public class GameStartedMessage extends GameMessage {
    private final GameView gameView;

    public GameStartedMessage(String username, GameView gameView) {
        super(username, GameMessageType.GAME_STARTED);
        this.gameView = gameView;
    }

    public GameView getGameView() {
        return gameView;
    }
}
