package edu.upc.dsa;

import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;

import java.util.List;

public interface StoreManager {
    public List<Item> listAllItems();
    public List<User> listAllUsers();
    public void addUser(User user);
    public void addItem(Item item);
    public List<Item> BuyItemUser(String idItem, String nameUser);
    public void addAllUsers(List<User> u);
    public void addAllItems(List<Item> i);
    public void updateUser(User user);
    public void updateItem(Item item);

    public List<Item> getItemUser(String userName);
    public void clear();
}
