package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

public class DrawInfoMessage extends MessageToClient {
    private final int maxNumItems;
    private final GameView model;
    private final String username;

    public DrawInfoMessage(String username, GameView model, int maxNumTiles){
        super(MessageType.GAME_MSG);
        this.username = username;
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

    public String getUsername() {
        return username;
    }
}
