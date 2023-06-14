package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.gameboard.Square;

import java.util.ArrayList;
import java.util.Collections;

public class InputValidator {
    public InputValidator() {
    }

    /**
     * Checks whether the given username is valid according to certain criteria.
     *
     * @param username the string representing the username to be validated.
     * @return true if the username meets all the validation criteria outlined below, false otherwise.
     * <p>
     * Criteria for a valid username:
     * 1. Does not contain any spaces.
     * 2. Does not start with a special character (-, _, or .).
     * 3. Does not end with - or .
     * 4. Does not contain any characters that are not letters or digits or one of the allowed special characters (-, _, or .).
     */
    public boolean isValidUsername(String username) {
        // Check for spaces in username
        if (username.contains(" ")) {
            return false;
        }

        // Check if username starts or ends with a special character or if it is solely composed of numbers
        char firstChar = username.charAt(0);
        if (firstChar == '-' || firstChar == '_' || firstChar == '.' || username.endsWith("-") || username.endsWith(".") || username.matches("[0-9]+")) {
            return false;
        }

        // Check for non-literal or non-numeric characters other than '-', '_' and '.'
        String pattern = "[^a-zA-Z0-9\\-_\\.]";
        if (username.matches(".*" + pattern + ".*")) {
            return false;
        }

        // All checks passed, username is valid
        return true;
    }

    /**
     * Checks if the current board configuration allows the player to draw more tiles
     *
     * @param hand  ArrayList of tiles taken by the player in this turn
     * @param board gameboard
     * @return true if there are other tiles the player can pick (based on what he's already picked and the board configuration)
     */
    public boolean isPossibleToDrawMore(ArrayList<Square> hand, Square[][] board) {
        ArrayList<Integer> rows = new ArrayList<Integer>();
        ArrayList<Integer> columns = new ArrayList<Integer>();
        for (Square sq : hand) {
            rows.add(sq.getRow());
            columns.add(sq.getColumn());
        }
        if (allCoordsAreEqual(rows)) {
            Collections.sort(columns);
            if (columns.get(0) != 0 && board[rows.get(0)][columns.get(0) - 1].isPickable()) return true;
            if (columns.get(columns.size() - 1) != board.length - 1 && board[rows.get(0)][columns.get(columns.size() - 1) + 1].isPickable())
                return true;
        }
        if (allCoordsAreEqual(columns)) {
            Collections.sort(rows);
            if (rows.get(0) != 0 && board[rows.get(0) - 1][columns.get(0)].isPickable()) return true;
            if (rows.get(rows.size() - 1) != board.length - 1 && board[rows.get(rows.size() - 1) + 1][columns.get(0)].isPickable())
                return true;
        }
        return false;
    }

    /**
     * This method splits the input string on commas and checks if there are exactly two parts. If not, it returns `true`.
     * If there are two parts, it attempts to parse them as integers using `Integer.parseInt()`.
     * If either of them cannot be parsed as an integer (due to a non-numeric character being present, for example),
     * the method returns `true`. Otherwise, it returns `false`.
     * Note that this implementation assumes the input string contains only ASCII digits, whitespace, and commas.
     * If other characters are allowed, the parsing logic would need to be adapted accordingly.
     *
     * @param input the string which format is to be evaluated
     * @return true if the String format is invalid
     */
    public static boolean invalidCoordFormat(String input) {
        String[] parts = input.split(",");

        // Check for two parts and trim any whitespace
        if (parts.length != 2) {
            return true;
        }

        try {
            // Attempt to parse integers from string parts : DON'T TOUCH!!!!!
            int first = Integer.parseInt(parts[0].trim());
            int second = Integer.parseInt(parts[1].trim());

            // Return false if we parsed two valid integers
            //System.err.println("Valid format");
            return false;

        } catch (NumberFormatException e) {
            // Catch any exception thrown by parseInt()
            //System.err.println("Invalid format");
            return true;
        }
    }

    /**
     * Checks if the input format is one exact number which fits in the bookshelf's dimension. If not, it returns 'true'.
     *
     * @param input chosen column
     * @return valid column
     */
    public boolean invalidColumnFormat(String input) {
        try {
            int col = Integer.parseInt(input.trim());
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    /**
     * Checks if the input provided is a valid number of players format.
     *
     * @param input the input string to validate as number of players
     * @return {@code true} if the input is not a valid number of players format, {@code false} otherwise
     */
    public boolean invalidNumOfPlayersFormat(String input) {
        try {
            int chosenNum = Integer.parseInt(input.trim());
            return chosenNum < GameController.MIN_PLAYERS || chosenNum > GameController.MAX_PLAYERS;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    /**
     * Checks if the given array of Integers contains only equals elements
     *
     * @param x ArrayList of integers
     * @return true if the elements of the passed ArrayList are all equal
     */
    public boolean allCoordsAreEqual(ArrayList<Integer> x) {
        for (int i = 0; i < x.size() - 1; i++) {
            if (x.get(i) != x.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the integers int the given arraylist are consecutive (in whatever order they are presented)
     *
     * @param x ArrayList of integers
     * @return true if the elements in the given arraylist are consecutive (in whatever order they are presented)
     */
    public boolean allCoordsAreAdjacent(ArrayList<Integer> x) {
        Collections.sort(x);
        for (int i = 0; i < x.size() - 1; i++) {
            if (x.get(i) != x.get(i + 1) - 1) return false;
        }
        return true;
    }

    /**
     * Checks if the passed coordinates are those of a Square which is in a straight line (horizontal or vertical) and adjacent to the ones in the player's hand
     *
     * @param row    row number of the Square to check
     * @param column column number of the Square to check
     * @param hand   List of Squares (tiles) already picked by the player during this turn
     * @return true if the passed coordinates are those of a Square which is in a straight line (horizontal or vertical) and adjacent to the ones in the player's hand
     */
    public boolean inLineTile(int row, int column, ArrayList<Square> hand) {
        ArrayList<Integer> rows = new ArrayList<Integer>();
        ArrayList<Integer> columns = new ArrayList<Integer>();
        rows.add(row);
        columns.add(column);
        for (Square sq : hand) {
            rows.add(sq.getRow());
            columns.add(sq.getColumn());
        }
        return (allCoordsAreAdjacent(rows) && allCoordsAreEqual(columns)) || (allCoordsAreAdjacent(columns) && allCoordsAreEqual(rows));
    }

    /**
     * Checks if the tile has already been picked by the player this turn
     *
     * @param row    row number of the Square to check
     * @param column column number of the Square to check
     * @param hand   List of Squares (tiles) already picked by the player during this turn
     * @return true if the passed coordinates are those of a tile which is already in the player's hand
     */
    public boolean isTileAlreadyOnHand(int row, int column, ArrayList<Square> hand) {
        if (hand.size() == 0) return false;
        for (Square sq : hand) {
            if (sq.getCoordinates().getRow() == row && sq.getCoordinates().getColumn() == column) return true;
        }
        return false;
    }

    /**
     * @param input String to check
     * @return true if the string passed is equal to N/No/n/no
     */
    public static boolean isNo(String input) {
        input = input.toLowerCase();
        return input.equals("n") || input.equals("no");
    }

    /**
     * @param input String to check
     * @return true if the string passed is equal to N/No/n/no/Y/y/Yes/yes
     */
    public static boolean isYesOrNo(String input) {
        input = input.toLowerCase();
        return input.equals("y") || input.equals("yes") || isNo(input);
    }

    /**
     * Checks if the column chosen by the player has enough space to insert the players hand. If not it returns 'true'.
     *
     * @param column  column that the player chose
     * @param columns arraylist of available columns
     * @return valid chosen column
     */
    public boolean columnHasLessSpace(int column, ArrayList<Integer> columns) {
        return !columns.contains(column);
    }

    /**
     * Checks if the given input string represents a positive answer, i.e., "yes" or "y" (case insensitive).
     *
     * @param input the input string to check
     * @return true if the input is "yes" or "y", false otherwise
     */
    public boolean isYes(String input) {
        input = input.toLowerCase();
        return input.equals("y") || input.equals("yes");
    }

    /**
     * Same as invalidCoordsFormat but is generic
     *
     * @param input cli input
     * @param n number of numbers separated by the coma that you have in the input string
     * @return true if the input represents an invalid order format, false otherwise.
     */
    public static boolean invalidOrderFormat(String input, int n) {
        String[] parts = input.split(",");

        // Check for two parts and trim any whitespace
        if (parts.length != n) {
            return true;
        }

        try {
            // Attempt to parse integers from string parts : DON'T TOUCH!!!!!
            for (int i = 0; i < n; i++) {
                int num = Integer.parseInt(parts[i].trim());
            }

            // Return false if we parsed two valid integers
            //System.err.println("Valid format");
            return false;

        } catch (NumberFormatException e) {
            // Catch any exception thrown by parseInt()
            //System.err.println("Invalid format");
            return true;
        }
    }
}