package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.gamemessage.GameMessageType;

import java.util.ArrayList;
public class InsertTilesMessage extends MessageToServer {
    private final ArrayList<ItemTile> items;


    private final int column;
    private final String username;

    public InsertTilesMessage(String username, ArrayList<ItemTile> items, int column){
        super(MessageType.GAME_MSG);
        this.username = username;
        this.items = items;
        this.column = column;
    }

    public ArrayList<ItemTile> getItems() {
        return items;
    }
    public int getColumn() {
        return column;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void execute(Client client, PreGameController preGameController) {
        System.err.println("Should not call this method: InsertTilesMessage.execute()"); //TODO fai sparire quando è tempo di farlo
    }
}
