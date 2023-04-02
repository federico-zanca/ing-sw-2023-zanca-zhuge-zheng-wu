package it.polimi.ingsw.view;

public enum Color {
    // Regular Colors. Normal color, no bold, background color etc.
    RED("\033[0;31m"),      // RED
    GREEN("\033[38;5;28m"),    // GREEN  "\u001B[32m"
    BLUE("\033[0;34m"),     // BLUE

    // Bold
    YELLOW_BOLD("\033[1;33m"), // YELLOW
    CYAN_BOLD("\033[1;36m"),   // CYAN

    PURPLE("\033[38;5;56m"), //PURPLE

    NO_COLOR("\033[0m"); //NO_COLOR

    private final String color;

    Color(String color){
        this.color=color;
    }

    @Override
    public String toString() {
        return color;
    }
}
