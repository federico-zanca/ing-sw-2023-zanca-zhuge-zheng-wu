package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.Message;

public class DrawInfoMessage extends GameMessage {
    private final int maxNumItems;
    private final GameView model;

    public DrawInfoMessage(String username, GameView model, int maxNumTiles){
        super(username, GameMessageType.DRAW_INFO);
        this.model = model;
        this.maxNumItems = maxNumTiles;
    }

    public ItemTile[][] getBookshelf() {
        return bookshelf;
    }

    public int getMaxNumItems() {
        return maxNumItems;
    }

    public GameView getModel() {
        return model;
    }
}
