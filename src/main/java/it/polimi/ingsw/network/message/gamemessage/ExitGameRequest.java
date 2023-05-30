package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.MessageType;

public class ExitGameRequest extends MessageToServer {
    private final String username;

    public ExitGameRequest(String username) {
        super(MessageType.GAME_MSG);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void execute(Client client, PreGameController preGameController) {
        System.err.println("Should not call this method: ExitGameRequest.execute()"); //TODO fai sparire quando è tempo di farlo
    }
}
