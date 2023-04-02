package it.polimi.ingsw.model.enumerations;

import java.util.ArrayList;
import java.util.Arrays;

public enum ItemType {
        CAT,
        BOOK,
        GAME,
        FRAME,
        TROPHY,
        PLANT,
        EMPTY,
        FORBIDDEN;

        public String toString(){
                switch (this) {
                        case CAT:
                                return "C";
                        case BOOK:
                                return "B";
                        case GAME:
                                return "G";
                        case FRAME:
                                return "F";
                        case TROPHY:
                                return "T";
                        case PLANT:
                                return "P";
                        case EMPTY:
                                return "E";
                        default:
                                return " ";
                }
        }
        public static ArrayList<ItemType> getValues(){
                ArrayList<ItemType> tmp = new ArrayList<>(Arrays.asList(ItemType.values()));
                tmp.remove(ItemType.FORBIDDEN);
                tmp.remove(ItemType.EMPTY);
                return tmp;
        }
}