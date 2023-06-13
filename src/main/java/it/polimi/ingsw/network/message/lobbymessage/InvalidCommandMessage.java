package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a message sent to the client indicating an invalid command.
 * Inherits from the {@link MessageToClient} class.
 */
public class InvalidCommandMessage extends MessageToClient {
    /**
     * Constructs a new InvalidCommandMessage.
     */
    public InvalidCommandMessage(){
        super(MessageType.LOBBY_MSG);
    }
    /**
     * Executes the message on the provided view, triggering the onInvalidCommandMessage callback.
     *
     * @param view The view on which to execute the message.
     */
    @Override
    public void execute(View view) {
        view.onInvalidCommandMessage(this);
    }
}
