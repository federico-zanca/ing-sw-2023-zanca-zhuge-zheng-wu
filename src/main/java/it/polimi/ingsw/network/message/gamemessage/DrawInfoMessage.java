package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

public class DrawInfoMessage extends GameMessage implements MsgToClient {
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
