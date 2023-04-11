package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.view.ProtoCli;
//import it.polimi.ingsw.view.VirtualView;

/**
 * Hello world!
 *
 */
public class Server
{
    public static void main(String[] args){
        Game model = Game.getInstance();
        GameView modelView = new GameView(model);
        ProtoCli view = new ProtoCli();
        GameController controller = new GameController(view);

        modelView.addObserver(view);
        view.addObserver(controller);

        view.run();
    }
}
