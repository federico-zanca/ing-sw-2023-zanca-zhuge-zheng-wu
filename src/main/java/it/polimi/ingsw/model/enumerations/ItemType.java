package it.polimi.ingsw.model.enumerations;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This enum contains all possible Types/Colors of an Item tile.
 */
public enum ItemType {
        CAT("C"),
        BOOK("B"),
        GAME("G"),
        FRAME("F"),
        TROPHY("T"),
        PLANT("P"),
        EMPTY(" "),
        FORBIDDEN(" ");

        private final String letter;
        ItemType(String letter){
                this.letter = letter;
        }
        @Override
        public String toString() {
                return letter;
        }

        public static ArrayList<ItemType> getValues(){
                ArrayList<ItemType> tmp = new ArrayList<>(Arrays.asList(ItemType.values()));
                tmp.remove(ItemType.FORBIDDEN);
                tmp.remove(ItemType.EMPTY);
                return tmp;
        }
}