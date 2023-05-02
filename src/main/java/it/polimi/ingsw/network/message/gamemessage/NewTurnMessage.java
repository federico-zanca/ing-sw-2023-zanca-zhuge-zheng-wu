package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.gamemessage.GameMessageType;
import it.polimi.ingsw.view.View;

public class NewTurnMessage extends GameMessage implements MessageToClient {
    private final String currentPlayer;
    public NewTurnMessage(String username, String currentPlayer) {
        super(username, GameMessageType.NEW_TURN);
        this.currentPlayer = currentPlayer;
    }

    public String getContent(){
        return "Turno di " + getCurrentPlayer();
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void execute(View view) {
        view.onNewTurnMessage(this);
    }
}
