package it.polimi.ingsw.distributed;

import it.polimi.ingsw.network.message.Message;

public interface Server {
    void registerClient(Client client);
    void update(Client client, Message message);
}
