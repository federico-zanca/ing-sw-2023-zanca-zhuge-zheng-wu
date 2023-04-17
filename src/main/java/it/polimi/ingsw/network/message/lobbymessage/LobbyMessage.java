package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.Message;

public abstract class LobbyMessage extends Message {
    private final LobbyMessageType type;

    public LobbyMessage(LobbyMessageType type) {
        this.type = type;
    }

    public LobbyMessageType getType() {
        return type;
    }
}
