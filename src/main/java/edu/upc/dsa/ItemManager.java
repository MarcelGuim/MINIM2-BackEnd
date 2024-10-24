package edu.upc.dsa;

import edu.upc.dsa.models.Item;
import edu.upc.dsa.exceptions.ItemNotFoundException;

import java.util.List;

public interface ItemManager {

    public Item addItem(String id, String name);
    public Item addItem(String name);
    public Item addItem(Item i);
    public Item getItem(String id);
    public Item getItem2(String id) throws ItemNotFoundException;

    public List<Item> findAll();
    public void deleteItem(String id);
    public Item updateItem(Item i);

    public void clear();
    public int size();
}
