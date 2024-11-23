package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Character;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;

import java.util.List;

public interface StoreManager {
    public List<Item> listAllItems();
    public List<User> listAllUsers();
    public void addUser(User user);
    public void updateUser(User user);
    public void updateItem(Item item);
    public void addItem(Item item);
    public List<Item> BuyItemUser(String idItem, String nameUser) throws UserNotFoundException, ItemNotFoundException, NotEnoughMoneyException;
    public List<Item> getItemUser(String userName) throws UserNotFoundException, UserHasNoItemsException;
    public void addAllUsers(List<User> u);
    public void addAllItems(List<Item> i);
    public void addAllCharacters(List<Character> c);
    public List<Character> BuyCharacter(String nameUser, String nameCharacter) throws UserNotFoundException, CharacterNotFoundException, NotEnoughMoneyException;
    public List<Character> getCharacterUser(String userName) throws UserNotFoundException, UserHasNoCharacterException;
    public void clear();
    public List<Item> getItemsUserCanBuy(User u) throws  NotEnoughMoneyException;
    public List<Character> getCharacterUserCanBuy(User u) throws  NotEnoughMoneyException;
}
