package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

public class GameStartedMessage extends MessageToClient {
    private final GameView gameView;
    private final String username;

    public GameStartedMessage(String username, GameView gameView) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.gameView = gameView;
    }

    public GameView getGameView() {
        return gameView;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void execute(View view) {
        view.onGameStartedMessage(this);
    }
}
