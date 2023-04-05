package it.polimi.ingsw.utils;

import it.polimi.ingsw.network.message.Message;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    private final List<Observer> observers = new ArrayList<>();


    /**
     * Adds an observer.
     *
     * @param obs the observer to be added.
     */
    public synchronized void addObserver(Observer obs) {
        observers.add(obs);
    }

    /**
     * Removes an observer.
     *
     * @param obs the observer to be removed.
     */
    public synchronized void removeObserver(Observer obs) {
        observers.remove(obs);
    }

    /**
     * Notifies all the current observers through the update method and passes to them a {@link Message}.
     *
     * @param message the message to be passed to the observers.
     */
    public void notifyObservers(Message message) {
        for (Observer observer : observers) {
            observer.update(message, this);
        }
    }
}
