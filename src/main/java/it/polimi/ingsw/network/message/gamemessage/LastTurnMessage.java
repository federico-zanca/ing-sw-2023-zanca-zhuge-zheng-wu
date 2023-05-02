package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.gamemessage.GameMessageType;
import it.polimi.ingsw.view.View;

public class LastTurnMessage extends GameMessage implements MessageToClient {
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
