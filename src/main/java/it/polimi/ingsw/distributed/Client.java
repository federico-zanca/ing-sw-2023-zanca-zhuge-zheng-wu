package it.polimi.ingsw.distributed;

import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.network.message.Message;

public interface Client {
    void update(GameView o, Message message);
}
