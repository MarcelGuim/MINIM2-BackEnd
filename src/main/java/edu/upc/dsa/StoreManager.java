package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.GameCharacter;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;

import java.util.List;

public interface StoreManager {
    public void addUser(User user);
    public void addItem(Item item);
    public List<Item> BuyItemUser(String ItemName, String nameUser) throws UserNotFoundException, ItemNotFoundException, NotEnoughMoneyException;
    public List<Item> getItemUser(String userName) throws UserNotFoundException, UserHasNoItemsException;
    public void addAllUsers(List<User> u);
    public void addAllItems(List<Item> i);
    public void addAllCharacters(List<GameCharacter> c);
    public List<GameCharacter> BuyCharacter(String nameUser, String nameCharacter) throws UserNotFoundException, CharacterNotFoundException, NotEnoughMoneyException;
    public List<GameCharacter> getCharacterUser(String userName) throws UserNotFoundException, UserHasNoCharacterException;
    public void clear();
    public List<Item> getItemsUserCanBuy(User u) throws  NotEnoughMoneyException;
    public List<GameCharacter> getCharacterUserCanBuy(User u) throws  NotEnoughMoneyException;
}
