package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.Message;

public class ExitLobbyResponse extends LobbyMessage {
    private final boolean successful;
    private final String content;

    public ExitLobbyResponse(boolean successful, String content) {
        super(LobbyMessageType.EXIT_LOBBY_RESPONSE);
        this.successful = successful;
        this.content = content;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getContent() {
        return content;
    }
}
