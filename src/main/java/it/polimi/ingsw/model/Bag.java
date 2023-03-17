package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Bag {
    private ArrayList<CommonGoalCard> commonGoals;
    private ArrayList<PersonalGoalCard> personalGoals;
    private ArrayList<ItemTile> itemTiles;

    public Bag(){

    }

    public ItemTile drawItem (){
        ItemTile tmp;
        int index = (int)(Math.random()*(itemTiles.size()) + 1);
        tmp = itemTiles.get(index);
        itemTiles.remove(index);
        return tmp;
    }

    public void putItem(ItemTile i){
        itemTiles.add(i);
    }
}
