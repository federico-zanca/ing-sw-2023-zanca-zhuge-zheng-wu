package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.view.View;

public class ExitLobbyResponse extends LobbyMessage implements MessageToClient {
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

    @Override
    public void execute(View view) {
        view.onExitLobbyResponse(this);
    }
}
