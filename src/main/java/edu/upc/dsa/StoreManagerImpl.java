package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;
import edu.upc.dsa.models.Character;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreManagerImpl implements StoreManager {
    private static StoreManager instance;
    protected HashMap<String,List<Item>> itemsOfUsers; //Key=user name
    protected HashMap<String,List<User>> usersOfItems; //Key=item id
    protected HashMap<String,List<Character>> charactersOfUsers; //Key=user name
    protected HashMap<String,List<User>> usersOfCharacter; //Key=character name
    protected List<Character> characters;
    protected List<User> users;
    protected List<Item> items;
    final static Logger logger = Logger.getLogger(StoreManagerImpl.class);
    private UserManager um;
    private ItemManager im;
    private CharacterManager cm;

    private StoreManagerImpl() {
        this.usersOfItems = new HashMap<>();
        this.itemsOfUsers = new HashMap<>();
        this.charactersOfUsers = new HashMap<>();
        this.usersOfCharacter = new HashMap<>();
        this.characters = new ArrayList<>();
        this.users = new ArrayList<>();
        this.items = new ArrayList<>();
        this.im = ItemManagerImpl.getInstance();
        this.um = UserManagerImpl.getInstance();
        this.cm = CharacterManagerImpl.getInstance();
    }

    public static StoreManager getInstance() {
        if (instance==null) instance = new StoreManagerImpl();
        return instance;
    }
    public void addAllUsers(List<User> u)
    {
        users = u;
        for(User us: u){
            itemsOfUsers.computeIfAbsent(us.getName(), k -> new ArrayList<>());
            charactersOfUsers.computeIfAbsent(us.getName(), k -> new ArrayList<>());
        }
    }
    public void addAllItems(List<Item> i)
    {
        items = i;
        for(Item it : i){
            usersOfItems.computeIfAbsent(it.getId(), k -> new ArrayList<>());
        }
    }
    public void addAllCharacters(List<Character> c){
        characters = c;
        for(Character ca : c){
            usersOfCharacter.computeIfAbsent(ca.getName(), k -> new ArrayList<>());
        }
    }

    public void addUser(User user){
        users.add(user);
        itemsOfUsers.put(user.getName(),new ArrayList<>());
    };
    public void addItem(Item item){
        items.add(item);
        usersOfItems.put(item.getId(),new ArrayList<>());
    };
    public List<Item> listAllItems(){
        return items;
    };
    public List<User> listAllUsers(){
        return users;

    };

    public List<Item> BuyItemUser(String idItem, String nameUser) throws UserNotFoundException, ItemNotFoundException, NotEnoughMoneyException {
        User u = um.getUserFromUsername(nameUser);
        if (u==null) throw new UserNotFoundException();
        Item i = im.getItem(idItem);
        if (i == null) throw new ItemNotFoundException();
        if(u.getMoney()>=i.getCost()){
            itemsOfUsers.get(u.getName()).add(i);
            usersOfItems.get(idItem).add(u);
            u.setMoney(u.getMoney()-i.getCost());
            return itemsOfUsers.get(u.getName());
        }
        else throw new NotEnoughMoneyException();
    };

    public List<Character> BuyCharacter(String nameUser, String nameCharacter) throws UserNotFoundException, CharacterNotFoundException, NotEnoughMoneyException {
        Character c = cm.getCharacter(nameCharacter);
        if (c== null) throw new CharacterNotFoundException();
        User u = um.getUserFromUsername(nameUser);
        if (u == null) throw new UserNotFoundException();
        if(u.getMoney()>=c.getCost()){
            charactersOfUsers.get(u.getName()).add(c);
            usersOfCharacter.get(c.getName()).add(u);
            u.setMoney(u.getMoney()-c.getCost());
            return charactersOfUsers.get(u.getName());
        }
        else throw new NotEnoughMoneyException();
    }
    public void updateUser(User user){
        for(User u:users)
        {
            if(u.getName().equals(user.getName()))
                u=user;
        }
    };
    public void updateItem(Item item){
        for(Item i: items) {
            if (i.getId().equals(item.getId()))
                i = item;
        }
    };

    public List<Item> getItemUser(String userName) throws UserNotFoundException, UserHasNoItemsException{
        if(itemsOfUsers.get(userName) ==null) throw new UserNotFoundException();
        if(itemsOfUsers.get(userName).size() == 0) throw new UserHasNoItemsException();
        return itemsOfUsers.get(userName);
    };

    public List<Character> getCharacterUser(String userName) throws UserNotFoundException, UserHasNoCharacterException{
        if(charactersOfUsers.get(userName) ==null) throw new UserNotFoundException();
        if(charactersOfUsers.get(userName).size() == 0) throw new UserHasNoCharacterException();
        return charactersOfUsers.get(userName);
    }

}
