package it.polimi.ingsw.model;


import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 Represents a Bag that contains items and personal goal cards.
 */
public class Bag implements Serializable {
    private static final long serialVersionUID = -4755443721464701726L;
    public static final int INIT_ITEMS_NUM = 22;
    private ArrayList<PersonalGoalCard> personalGoals;
    //private ArrayList<ItemTile> itemTiles;
    private HashMap<ItemType, Integer> itemTiles;
    private ArrayList<ItemType> availableItems;

    /**
     Constructs a new Bag.
     Initializes the itemTiles HashMap with each ItemType and a count of INIT_ITEMS_NUM.
     Initializes the availableItems ArrayList with all ItemType values.
     */
    public Bag(){
        itemTiles = new HashMap<>();
        availableItems = ItemType.getValues();
        for(ItemType t: availableItems){
            itemTiles.put(t,INIT_ITEMS_NUM);
        }
        //TODO inserisci personal goal cards (non si sa come generarle)
    }

    /**
     Checks if the Bag is empty.
     @return true if all item types in the Bag have a count of 0, false otherwise.
     */
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
     Gets the itemTiles left in the bag.
     @return the HashMap containing the ItemType as keys and the corresponding count as values.
     */
    public HashMap<ItemType, Integer> getItemTiles() {
        return itemTiles;
    }

    /**
     Gets the availableItems ArrayList.
     @return the ArrayList containing all available ItemTypes.
     */
    public ArrayList<ItemType> getAvailableItems() {
        return availableItems;
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

        if (availableItems.isEmpty()) {
            return null;
        }
        else{
            //TODO replace with Random()
            int index = (int) (Math.random() * (availableItems.size()));
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
        int numLeft = getNumItemsLeftInBag();
        for(int i=0; i<Math.min(numItems,numLeft);i++){
            items.add(drawItem());
        }
        return items;
    }

    /**
     * Adds an ItemTile object to the bag
     * @param i the item which is wished to be added to the bag
     */
    public void putItem(ItemTile i){
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
        for (int count : itemTiles.values()) {
            sum += count;
        }
        return sum;
    }

    /**
     * Prints bag for testing.
     */
    public void printBag(){
        for(ItemType t:availableItems){
            System.out.println(t + ":" + itemTiles.get(t));
        }
    }
}
