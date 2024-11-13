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
            int k = 12;
            itemsOfUsers.get(u.getName()).add(i);
            k = 1;
            usersOfItems.get(idItem).add(u);
            k = 8;
            return itemsOfUsers.get(u.getName());
        }
        return null;
    };
}
