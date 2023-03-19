package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.ItemType;
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

    public ItemTile drawItem (){
        ItemType k;
        ItemType[] keys = {ItemType.CAT, ItemType.BOOK, ItemType.GAME, ItemType.TROPHY, ItemType.FRAME, ItemType.PLANT};
        while(true) {
            int index = (int) (Math.random() * (itemTiles.size()) + 1);
            k = keys[index];
            if(itemTiles.get(k) > 0){
                itemTiles.put(k, itemTiles.get(k) - 1);
                break;
            }
        }
        return new ItemTile(k);
    }
//TODO sistemare gestione ItemType ItemTile incoerente
    public void putItem(@NotNull ItemTile i){
        itemTiles.put(i.getType(), itemTiles.get(i.getType())+1);
    }
}
