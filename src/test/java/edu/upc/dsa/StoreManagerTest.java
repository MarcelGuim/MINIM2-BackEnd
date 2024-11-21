package edu.upc.dsa;

import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class StoreManagerTest {
    StoreManager sm;
    ItemManager im;
    UserManager um;

    @Before
    public void setUp() {
        this.sm = StoreManagerImpl.getInstance();
        this.im = ItemManagerImpl.getInstance();
        this.um = UserManagerImpl.getInstance();
        User user1 = new User("Manolo", "0003");
        User user2 = new User("Andreu", "0002");
        User user3 = new User("Pep", "0001");
        Item item1 = new Item("s123","pelacables2000" );
        Item item2 = new Item("s124","pelacables3000" );
        Item item3 = new Item("s125","pelacables4000" );
        this.um.addUser(user1);
        this.um.addUser(user2);
        this.um.addUser(user3);
        this.im.addItem(item1);
        this.im.addItem(item2);
        this.im.addItem(item3);
        this.sm.addUser(user1);
        this.sm.addUser(user2);
        this.sm.addUser(user3);
        this.sm.addItem(item1);
        this.sm.addItem(item2);
        this.sm.addItem(item3);


    }
    @After
    public void tearDown() {
        // És un Singleton
        this.sm.clear();
    }
    @Test
    public void listofItems(){
        List<Item> items  = sm.listAllItems();
        Assert.assertEquals(3, items.size());
        Assert.assertEquals("s123", items.get(0).getId());
        Assert.assertEquals("s124", items.get(1).getId());
        Assert.assertEquals("s125", items.get(2).getId());


    }
    @Test
    public void listofUsers(){
        List<User> users  = sm.listAllUsers();
        Assert.assertEquals(3, users.size());
        Assert.assertEquals("Manolo", users.get(0).getName());
        Assert.assertEquals("Andreu", users.get(1).getName());
        Assert.assertEquals("Pep", users.get(2).getName());

    }
    @Test
    public void ItemUser(){
        List<Item> itemsUser;
        List<Item> itemsUser2;
        itemsUser = this.sm.BuyItemUser("s123", "Andreu");

        Assert.assertEquals(1, itemsUser.size());
        Assert.assertEquals("s123", itemsUser.get(0).getId());
        itemsUser2 = this.sm.BuyItemUser("s124", "Andreu");
        Assert.assertEquals(2, itemsUser2.size());
        Assert.assertEquals("s124", itemsUser2.get(1).getId());
    }
}
