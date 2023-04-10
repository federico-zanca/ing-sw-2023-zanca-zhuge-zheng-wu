package it.polimi.ingsw.network.message;

import java.util.LinkedHashMap;

public class EndGameMessage extends Message {
    private final LinkedHashMap<String, Integer> ranking;
    public EndGameMessage(String username, LinkedHashMap<String, Integer> ranking) {
        super(username, MessageType.END_GAME);
        this.ranking = ranking;
    }

    public LinkedHashMap<String, Integer> getRanking() {
        return ranking;
    }

}
