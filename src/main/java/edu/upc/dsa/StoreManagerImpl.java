package edu.upc.dsa;

import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;
import org.apache.log4j.Logger;
import org.reflections.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class StoreManagerImpl implements StoreManager {
    private static StoreManager instance;
    protected HashMap<String,List<Item>> itemsOfUsers; //Key=user name
    protected HashMap<String,List<User>> usersOfItems; //Key=item id
    protected List<User> users;
    protected List<Item> items;
    final static Logger logger = Logger.getLogger(StoreManagerImpl.class);
    private UserManager um;
    private ItemManager im;

    private StoreManagerImpl() {
        this.usersOfItems = new HashMap<>();
        this.itemsOfUsers = new HashMap<>();
        this.users = new ArrayList<>();
        this.items = new ArrayList<>();
        this.im = ItemManagerImpl.getInstance();
        this.um = UserManagerImpl.getInstance();
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
        }
    }
    public void addAllItems(List<Item> i)
    {
        items = i;
        for(Item it : i){
            usersOfItems.computeIfAbsent(it.getId(), k -> new ArrayList<>());
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

    public List<Item> BuyItemUser(String idItem, String nameUser){
        User u = um.getUserFromUsername(nameUser);
        Item i = im.getItem(idItem);
        if(u.getMoney()>=i.getCost()){
            itemsOfUsers.get(u.getName()).add(i);
            usersOfItems.get(idItem).add(u);
            u.setMoney(u.getMoney()-i.getCost());
            return itemsOfUsers.get(u.getName());
        }
        return null;
    };
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

    public List<Item> getItemUser(String userName){
        return itemsOfUsers.get(userName);
    };
}
