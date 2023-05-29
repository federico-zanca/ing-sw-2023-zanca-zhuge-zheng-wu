package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

public class LastTurnMessage extends GameMessage implements MsgToClient {
    private final String currentPlayer;

    public LastTurnMessage(String username, String currentPlayer) {
        super(username, GameMessageType.LAST_TURN);
        this.currentPlayer = currentPlayer;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void execute(View view) {
        view.onLastTurnMessage(this);
    }
}
