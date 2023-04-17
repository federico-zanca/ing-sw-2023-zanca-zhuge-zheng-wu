package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.Message;

import java.util.LinkedHashMap;

public class EndGameMessage extends GameMessage {
    private final LinkedHashMap<String, Integer> ranking;
    public EndGameMessage(String username, LinkedHashMap<String, Integer> ranking) {
        super(username, GameMessageType.END_GAME);
        this.ranking = ranking;
    }

    public LinkedHashMap<String, Integer> getRanking() {
        return ranking;
    }

}
