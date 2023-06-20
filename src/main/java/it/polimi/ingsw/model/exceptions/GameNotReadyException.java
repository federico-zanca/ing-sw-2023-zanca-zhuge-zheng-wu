package it.polimi.ingsw.model.exceptions;
/**
 Exception thrown when trying to start a game that does not have enough players in the lobby.
 */
public class GameNotReadyException extends Throwable {
    public GameNotReadyException(String game_not_ready_to_start) {
        super(game_not_ready_to_start);
    }
}
