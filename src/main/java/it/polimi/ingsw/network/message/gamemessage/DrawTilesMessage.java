package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.MessageType;

import java.util.ArrayList;

public class DrawTilesMessage extends MessageToServer {
    private final ArrayList<Square> squares;
    private final String username;

    public DrawTilesMessage(String username, ArrayList<Square> squares) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.squares = squares;
    }

    public ArrayList<Square> getSquares() {
        return squares;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void execute(Client client, PreGameController preGameController) {
        System.err.println("Should not call this method: DrawTilesMessage.execute()"); //TODO fai sparire quando è tempo di farlo
    }
}
