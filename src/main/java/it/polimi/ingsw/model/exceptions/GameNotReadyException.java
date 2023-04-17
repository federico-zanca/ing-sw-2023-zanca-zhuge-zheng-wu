package it.polimi.ingsw.model.exceptions;

public class GameNotReadyException extends Throwable {
    public GameNotReadyException(String game_not_ready_to_start) {
        super(game_not_ready_to_start);
    }
}
