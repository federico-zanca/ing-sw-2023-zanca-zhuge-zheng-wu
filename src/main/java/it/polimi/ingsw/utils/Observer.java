package it.polimi.ingsw.utils;

import it.polimi.ingsw.network.message.Message;

public interface Observer {
    void update(Observable o, Message message);
}
