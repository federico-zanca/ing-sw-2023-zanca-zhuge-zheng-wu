package it.polimi.ingsw.distributed.local;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.network.message.Message;

public class ServerImpl implements Server {
    private Game model;
    private GameController controller;

    @Override
    public void registerClient(Client client) {
        this.model = Game.getInstance();
        //forse problemi per game.instance
        this.model.addObserver((o, arg) -> client.update(new GameView(), arg));
        this.controller = new GameController(client); //così creo un controller per ogni client, andrà cambiata questa cosa

    }

    @Override
    public void update(Client client, Message message) {
        this.controller.update(client, message);
    }

}
