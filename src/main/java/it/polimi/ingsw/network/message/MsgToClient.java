package it.polimi.ingsw.network.message;

import it.polimi.ingsw.view.View;
/**
 * The interface representing a message sent to client.
 */
public interface MsgToClient {
    void execute(View view);
}
