package it.polimi.ingsw.distributed.local;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.view.ProtoCli;

public class ClientImpl implements Client, Runnable{

    ProtoCli view = new ProtoCli();

    public ClientImpl(Server server) {
        server.registerClient(this);
        view.addObserver((o, arg) -> server.update(this, arg));
    }

    @Override
    public void update(GameView o, Message message) {
        this.view.update(o, message);
    }

    @Override
    public void run() {
        this.view.run();
    }
}

