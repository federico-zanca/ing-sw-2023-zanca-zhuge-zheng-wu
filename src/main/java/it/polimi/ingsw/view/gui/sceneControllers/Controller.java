package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.JavaFXApp;

public interface Controller {
    void setGui(Gui gui);
    void setApp(JavaFXApp app);
    void initialize();
}
