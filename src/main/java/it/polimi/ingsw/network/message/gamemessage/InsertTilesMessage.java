package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.MessageType;

import java.util.ArrayList;
/**
 * Represents a message for inserting tiles into the bookshelf.
 * Inherits from the {@link MessageToServer} class.
 */
public class InsertTilesMessage extends MessageToServer {
    private final ArrayList<ItemTile> items;


    private final int column;
    private final String username;
    /**
     * Constructs a new InsertTilesMessage.
     * @param username The username of the player.
     * @param items The list of item tiles to be inserted.
     * @param column The column in the bookshelf where the tiles should be inserted.
     */
    public InsertTilesMessage(String username, ArrayList<ItemTile> items, int column){
        super(MessageType.GAME_MSG);
        this.username = username;
        this.items = items;
        this.column = column;
    }
    /**
     * Gets the list of item tiles to be inserted.
     * @return The list of item tiles.
     */
    public ArrayList<ItemTile> getItems() {
        return items;
    }
    /**
     * Gets the column in the bookshelf where the tiles should be inserted.
     * @return The column index.
     */
    public int getColumn() {
        return column;
    }
    /**
     * Gets the username of the player.
     * @return The username.
     */
    public String getUsername() {
        return username;
    }
    /**
     * Executes the message on the client's view.
     * @param client The client that sends the message.
     * @param preGameController The pre-game controller that handles the message.
     */
    @Override
    public void execute(Client client, PreGameController preGameController) {
        System.err.println("Should not call this method: InsertTilesMessage.execute()"); //TODO fai sparire quando è tempo di farlo
    }
}
