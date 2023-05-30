package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

public class GameNotReadyMessage extends MessageToClient {
    public GameNotReadyMessage(String game_not_ready_to_start) {
        super(MessageType.LOBBY_MSG);
    }

    @Override
    public void execute(View view) {
        view.onGameNotReadyMessage(this);
    }
}
