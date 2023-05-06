package it.polimi.ingsw.view;

import it.polimi.ingsw.utils.Observable;

public abstract class VirtualView extends Observable implements View{
    public abstract void run();
}
