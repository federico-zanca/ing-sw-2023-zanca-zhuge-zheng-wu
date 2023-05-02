package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.view.View;

public class GameStartedMessage extends GameMessage implements MessageToClient {
    private final GameView gameView;

    public GameStartedMessage(String username, GameView gameView) {
        super(username, GameMessageType.GAME_STARTED);
        this.gameView = gameView;
    }

    public GameView getGameView() {
        return gameView;
    }

    @Override
    public void execute(View view) {
        view.onGameStartedMessage(this);
    }
}
