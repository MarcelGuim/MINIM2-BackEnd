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

    @Before
    public void setUp() {
        this.sm = StoreManagerImpl.getInstance();
        User user1 = new User("Manolo", "0003");
        User user2 = new User("Andreu", "0002");
        User user3 = new User("Pep", "0001");
        Item item1 = new Item("s123","pelacables2000" );
        Item item2 = new Item("s124","pelacables3000" );
        Item item3 = new Item("s125","pelacables4000" );
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
    public void listofItems{

    }
    @Test
    public void listofUsers{

    }
    @Test
    public void ItemUser{

    }
}
