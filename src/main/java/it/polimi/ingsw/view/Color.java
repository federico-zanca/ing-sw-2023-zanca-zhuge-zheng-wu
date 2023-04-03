package it.polimi.ingsw.view;

public enum Color {
    //pickable colors
    RED("\033[38;5;0;48;5;160m"),      // RED
    FUCSIA("\033[38;5;0;48;5;199m"),      // FUCSIA
    GREEN("\033[38;5;0;48;5;34m"),    // GREEN  "\u001B[32m"
    BLUE("\033[38;5;254;48;5;21m"),     // BLUE

    // Bold
    YELLOW_BOLD("\033[38;5;0;48;5;11m"), // YELLOW
    CYAN_BOLD("\033[38;5;0;48;5;45m"),   // CYAN

    WHITE("\033[38;5;0;48;5;254m"), //PURPLE

    //non pickable colors

    WRED("\033[38;5;160m"),      // RED
    WFUCSIA("\033[38;5;199m"),      // FUCSIA
    WGREEN("\033[38;5;34m"),    // GREEN  "\u001B[32m"
    WBLUE("\033[38;5;27m"),     // BLUE

    // Bold
    WYELLOW_BOLD("\033[38;5;11m"), // YELLOW
    WCYAN_BOLD("\033[38;5;45m"),   // CYAN

    WWHITE("\033[38;5;254m"), //PURPLE
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
