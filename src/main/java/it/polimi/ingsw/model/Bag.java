package it.polimi.ingsw.model;


import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static java.lang.Math.min;

public class Bag {
    private ArrayList<PersonalGoalCard> personalGoals;
    //private ArrayList<ItemTile> itemTiles;
    private HashMap<ItemType, Integer> itemTiles;
    private ArrayList<ItemType> availableItems;
    public Bag(){
        itemTiles = new HashMap<>();
        itemTiles.put(ItemType.CAT, 22);
        itemTiles.put(ItemType.BOOK, 22);
        itemTiles.put(ItemType.FRAME, 22);
        itemTiles.put(ItemType.GAME, 22);
        itemTiles.put(ItemType.PLANT, 22);
        itemTiles.put(ItemType.TROPHY, 22);
        availableItems = new ArrayList<>(Arrays.asList(ItemType.CAT, ItemType.BOOK, ItemType.GAME, ItemType.TROPHY, ItemType.FRAME, ItemType.PLANT));

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
     * !!!!IMPORTANT!!! to add ItemTile to ArrayList of ItemTiles execute something like
     * ItemTile tile=Bag.drawItem();
     * if(tile!=null){
     * ArrayList<ItemTile>.add(tile);
     * }
     *
     * otherwise "null" will be added to the ArrayList of ItemTile when the bag is empty, and the ArrayList will not be empty!
     */
    public ItemTile drawItem () {
        ItemType k;
        //TODO define constants class

        if (availableItems.size() == 0) {
            return null;
        }
        else{
            int index = (int) (Math.random() * (availableItems.size()) + 1);
            k = availableItems.get(index);
            itemTiles.put(k, itemTiles.get(k) - 1);
            if (itemTiles.get(k) == 0) {
                availableItems.remove(k);
            }
            return new ItemTile(k);
        }
    }

    /**
     * Randomly extract @param numItems items from bag
     * @param numItems number of items to extract
     * @return items extracted
     */
    public ArrayList<ItemTile> drawItems(int numItems){
        ArrayList<ItemTile> items = new ArrayList<>();
        for(int i=0; i<min(numItems, getNumItemsLeftInBag()); i++){
            items.add(drawItem());
        }
        return items;
    }
//TODO sistemare gestione ItemType ItemTile incoerente

    /**
     * Adds an ItemTile object to the bag
     * @param i the item which is wished to be added to the bag
     */
    public void putItem(@NotNull ItemTile i){
        itemTiles.put(i.getType(), itemTiles.get(i.getType())+1);
        if(itemTiles.get(i.getType()) == 1){
            availableItems.add(i.getType());
        }
    }

    /**
     *
     * @return number of ItemTile objects left in the bag
     */
    public int getNumItemsLeftInBag(){
        int sum=0;
        for(ItemType i : availableItems){
            sum+=itemTiles.get(i);
        }
        return sum;
    }
}
