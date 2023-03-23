package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commongoals.CommonGoalCard;
import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Bag {
    private ArrayList<CommonGoalCard> commonGoals;
    private ArrayList<PersonalGoalCard> personalGoals;
    //private ArrayList<ItemTile> itemTiles;
    private HashMap<ItemType, Integer> itemTiles;
    public Bag(){
        itemTiles = new HashMap<ItemType, Integer>();
        itemTiles.put(ItemType.CAT, 22);
        itemTiles.put(ItemType.BOOK, 22);
        itemTiles.put(ItemType.FRAME, 22);
        itemTiles.put(ItemType.GAME, 22);
        itemTiles.put(ItemType.PLANT, 22);
        itemTiles.put(ItemType.TROPHY, 22);

        //TODO inserisci 12 commongoal cards, una per tipo

        //TODO inserisci personal goal cards (non si sa come generarle)

    }

    public boolean emptyBag(){
        ItemType[] types = {ItemType.CAT, ItemType.BOOK, ItemType.GAME, ItemType.TROPHY, ItemType.FRAME, ItemType.PLANT};
        for (ItemType type : types){
            if (itemTiles.get(type)>0){
                return  false;
            }
        }
        return true;
    }

    /**
     * Returns a new ItemTile object of a randomly selected ItemType from those remaining in the bag
     * @return random ItemTile from those still present in the bag or null is the bag is empty
     *
     * !!!!IMPORTANT!!! to add ItemTile to ArrayList of ItemTiles execute something like
     * ItemTile tile=Bag.drawItem();
     * if(tile!=null){
     * ArrayList<ItemTile>.add(tile);
     * }
     *
     * otherwise "null" will be added to the ArrayList of ItemTile when the bag is empty, and the ArrayList will not be empty!
     */
    public ItemTile drawItem (){
        ItemType k;
        ItemType[] keys = {ItemType.CAT, ItemType.BOOK, ItemType.GAME, ItemType.TROPHY, ItemType.FRAME, ItemType.PLANT};

        if (!emptyBag()) {
            while (true) {
                int index = (int) (Math.random() * (itemTiles.size()) + 1);
                k = keys[index];
                if (itemTiles.get(k) > 0) {
                    itemTiles.put(k, itemTiles.get(k) - 1);
                    break;
                }
            }
            return new ItemTile(k);
        }
        return null;
    }
//TODO sistemare gestione ItemType ItemTile incoerente

    /**
     * Adds an ItemTile object to the bag
     * @param i the item which is wished to be added to the bag
     */
    public void putItem(@NotNull ItemTile i){
        itemTiles.put(i.getType(), itemTiles.get(i.getType())+1);
    }
}
