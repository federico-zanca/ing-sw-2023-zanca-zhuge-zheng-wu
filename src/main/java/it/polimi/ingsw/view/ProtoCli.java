package it.polimi.ingsw.view;

import it.polimi.ingsw.model.gameboard.Coordinates;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.utils.Observer;

import java.util.Scanner;

public class ProtoCli extends Observable implements Observer, Runnable {

    @Override
    public void run(){
        Coordinates choice = askPlayer();
    }

    public Coordinates askPlayer() {
        Scanner s = new Scanner(System.in);
        System.out.println("Inserisci le coordinate della tessera da prendere: ");
        return new Coordinates(0, 0);
    }

    public void update(Message receivedMessage, Observable o){
        return;
    }
}
