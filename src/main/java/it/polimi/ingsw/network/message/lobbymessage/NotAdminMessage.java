package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a message sent to the client indicating that the user is not the lobby administrator,
 * therefore do not have access to the action they are trying to perform.
 * Inherits from the {@link MessageToClient} class.
 */
public class NotAdminMessage extends MessageToClient {
    /**
     * Constructs a new NotAdminMessage.
     */
    public NotAdminMessage() {
        super(MessageType.LOBBY_MSG);
    }

    /**
     * Executes the message on the provided view, triggering the onNotAdminMessage callback.
     *
     * @param view The view on which to execute the message.
     */
    @Override
    public void execute(View view) {
        view.onNotAdminMessage(this);
    }
}
