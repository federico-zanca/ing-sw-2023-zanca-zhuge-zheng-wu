package it.polimi.ingsw.network.message;

import it.polimi.ingsw.view.View;

public interface MessageToClient {
    void execute(View view);
}
