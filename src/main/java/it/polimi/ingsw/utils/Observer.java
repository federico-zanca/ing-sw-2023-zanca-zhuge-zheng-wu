package it.polimi.ingsw.utils;

import it.polimi.ingsw.network.message.Message;

/**
 * Observer interface. It supports a generic method of update.
 */
public interface Observer {
    void update(Message message);
}
