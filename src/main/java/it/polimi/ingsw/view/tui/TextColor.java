package it.polimi.ingsw.view.tui;
/**
 * Represents different text colors and formatting options for console output.
 * Each enum value contains the corresponding ANSI escape sequence for the color or formatting style.
 */
public enum TextColor {
    //pickable colors
    RED("\033[51;38;5;255;48;5;160m"),      // RED
    FUCSIA("\033[51;38;5;255;48;5;199m"),      // FUCSIA
    GREEN("\033[51;38;5;255;48;5;34m"),    // GREEN  "\u001B[32m"
    BLUE("\033[51;38;5;255;48;5;21m"),     // BLUE

    // Bold
    YELLOW_BOLD("\033[51;38;5;255;48;5;11m"), // YELLOW
    CYAN_BOLD("\033[51;38;5;255;48;5;45m"),   // CYAN

    WHITE("\033[51;38;5;0;48;5;254m"), //PURPLE

    //non pickable colors

    WRED("\033[38;5;255;48;5;160m"),      // RED
    WFUCSIA("\033[38;5;255;48;5;199m"),      // FUCSIA
    WGREEN("\033[38;5;255;48;5;34m"),    // GREEN  "\u001B[32m"
    WBLUE("\033[38;5;255;48;5;27m"),     // BLUE

    // Bold
    WYELLOW_BOLD("\033[38;5;255;48;5;11m"), // YELLOW
    WCYAN_BOLD("\033[38;5;255;48;5;45m"),   // CYAN

    WWHITE("\033[38;5;255;48;5;254m"), //PURPLE
    NO_COLOR("\033[0m"), //NO_COLOR

    CYANTEXT("\033[38;5;45m"),

    MAGENTATEXT("\033[38;5;199m"),
    BRIGHT_PURPLE_TEXT("\033[38;5;201m"),

    GOLD_TEXT("\033[38;5;220m"),

    BRIGHT_RED_TEXT("\033[38;5;196m");

    private final String color;

    TextColor(String color){
        this.color=color;
    }

    @Override
    public String toString() {
        return color;
    }
}
