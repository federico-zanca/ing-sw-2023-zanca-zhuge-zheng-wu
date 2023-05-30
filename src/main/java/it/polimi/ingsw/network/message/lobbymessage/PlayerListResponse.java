package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public class PlayerListResponse extends MessageToClient {
    private final ArrayList<String> clients;

    public PlayerListResponse(ArrayList<String> clients) {
        super(MessageType.LOBBY_MSG);
        this.clients = clients;
    }

    public ArrayList<String> getClients() {
        return clients;
    }

    @Override
    public void execute(View view) {
        view.onPlayerListResponse(this);
    }
}
