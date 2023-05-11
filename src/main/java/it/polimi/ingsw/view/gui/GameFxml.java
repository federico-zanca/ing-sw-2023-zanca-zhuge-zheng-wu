package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.tui.ActionType;

public enum GameFxml {
    MENU_SCENE("menu_scene.fxml");

    private ActionType actionType;

    public final String s;
    static{
        MENU_SCENE.actionType = ActionType.LOGIN;
    }
    GameFxml(String s) {
        this.s=s;
    }
    public ActionType getActionType() {
        return actionType;
    }

}
