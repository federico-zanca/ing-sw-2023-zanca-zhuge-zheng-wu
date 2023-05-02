package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.view.View;

import java.util.LinkedHashMap;

public class EndGameMessage extends GameMessage implements MessageToClient {
    private final LinkedHashMap<String, Integer> ranking;

    public EndGameMessage(String username, LinkedHashMap<String, Integer> ranking) {
        super(username, GameMessageType.END_GAME);
        this.ranking = ranking;
    }

    public LinkedHashMap<String, Integer> getRanking() {
        return ranking;
    }

    @Override
    public void execute(View view) {
        view.onEndGameMessage(this);
    }
}
