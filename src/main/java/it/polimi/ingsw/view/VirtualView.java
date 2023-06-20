package it.polimi.ingsw.view;

import it.polimi.ingsw.utils.Observable;
/**
 * Abstract class representing a virtual view that is runnable.
 * Subclasses of VirtualView are responsible for implementing the run method (e.g. TextualUI).
 */
public abstract class VirtualView extends Observable implements View{
    /**
     * Runs the virtual view.
     */
    public abstract void run();
}
