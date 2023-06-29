package it.polimi.ingsw.view.gui;

public class EndGameScores {
    private int AdjacentPoints;
    private int personalPoints;
    private int fullShelfPoints;
    private int totalPoints;
    public EndGameScores(){
        AdjacentPoints = 0;
        personalPoints = 0;
        fullShelfPoints = 0;
        totalPoints = 0;
    }

    public int getAdjacentPoints() {
        return AdjacentPoints;
    }

    public void setAdjacentPoints(int adjacentPoints) {
        AdjacentPoints = adjacentPoints;
    }

    public int getPersonalPoints() {
        return personalPoints;
    }

    public void setPersonalPoints(int personalPoints) {
        this.personalPoints = personalPoints;
    }

    public int getFullShelfPoints() {
        return fullShelfPoints;
    }

    public void setFullShelfPoints(int fullShelfPoints) {
        this.fullShelfPoints = fullShelfPoints;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

}
