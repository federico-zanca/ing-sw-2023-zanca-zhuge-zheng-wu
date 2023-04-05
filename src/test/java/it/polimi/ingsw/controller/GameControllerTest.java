package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.DrawTilesMessage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    @Test
    void setupGameController() {
    }

    @Test
    void play() {
    }

    @Test
    void getGame() {
    }

    @Test
    void endGame() {
    }

    @Test
    void assignPersonalGoalPoints() {
    }

    @Test
    void update() {
        GameController g = new GameController(null);
        Game game = g.getGame();
        game.addPlayer(new Player("amuro"));
        System.out.println(game.getPlayersUsernames());
        game.setChosenNumOfPlayers(4);
        game.startGame();
        game.getBoard().enableSquaresWithFreeSide();

        ArrayList<Square> hand = new ArrayList<>();
        hand.add(new Square(0,3));
        hand.add(new Square(1,3));
        //hand.add(new Square())
        DrawTilesMessage message = new DrawTilesMessage("amuro", hand);
        assert(g.isDrawPhaseValid(message));

        hand=new ArrayList<>();
        hand.add(new Square(0,3));
        hand.add(new Square(1,4));
        //hand.add(new Square())
        message = new DrawTilesMessage("amuro", hand);
        assertFalse(g.isDrawPhaseValid(message));

        hand=new ArrayList<>();
        hand.add(new Square(0,3));
        hand.add(new Square(1,4));
        //hand.add(new Square())
        message = new DrawTilesMessage("amuro", hand);
        assertFalse(g.isDrawPhaseValid(message));
    }
}