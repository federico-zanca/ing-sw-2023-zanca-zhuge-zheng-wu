package it.polimi.ingsw.network.message.lobbymessage;

public class NotAdminMessage extends LobbyMessage{
    public NotAdminMessage() {
        super(LobbyMessageType.NOT_ADMIN);
    }
}
