package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.view.View;

public class DrawInfoMessage extends GameMessage implements MessageToClient {
    private final int maxNumItems;
    private final GameView model;

    public DrawInfoMessage(String username, GameView model, int maxNumTiles){
        super(username, GameMessageType.DRAW_INFO);
        this.model = model;
        this.maxNumItems = maxNumTiles;
    }

    public int getMaxNumItems() {
        return maxNumItems;
    }

    public GameView getModel() {
        return model;
    }

    @Override
    public void execute(View view) {
        view.onDrawInfoMessage(this);
    }
}
