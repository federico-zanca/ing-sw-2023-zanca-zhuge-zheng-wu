package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

import java.util.LinkedHashMap;

public class EndGameMessage extends MessageToClient {
    private final LinkedHashMap<String, Integer> ranking;
    private final String username;

    public EndGameMessage(String username, LinkedHashMap<String, Integer> ranking) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.ranking = ranking;
    }

    public LinkedHashMap<String, Integer> getRanking() {
        return ranking;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void execute(View view) {
        view.onEndGameMessage(this);
    }
}
