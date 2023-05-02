package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.view.View;

public class GameNotReadyMessage extends LobbyMessage implements MessageToClient {
    public GameNotReadyMessage(String game_not_ready_to_start) {
        super(LobbyMessageType.GAME_NOT_READY);
    }

    @Override
    public void execute(View view) {
        view.onGameNotReadyMessage(this);
    }
}
