package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

public class NewTurnMessage extends MessageToClient {
    private final String currentPlayer;
    private final String username;

    public NewTurnMessage(String username, String currentPlayer) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.currentPlayer = currentPlayer;
    }

    public String getContent(){
        return "Turno di " + getCurrentPlayer();
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void execute(View view) {
        view.onNewTurnMessage(this);
    }
}
