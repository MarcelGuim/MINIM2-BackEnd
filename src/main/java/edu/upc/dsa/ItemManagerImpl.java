package edu.upc.dsa;

import edu.upc.dsa.exceptions.ItemNotFoundException;
import edu.upc.dsa.models.Item;

import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

public class ItemManagerImpl implements ItemManager {
    private static ItemManager instance;
    protected List<Item> items;
    final static Logger logger = Logger.getLogger(ItemManagerImpl.class);

    private ItemManagerImpl() {
        this.items = new LinkedList<>();
    }

    public static ItemManager getInstance() {
        if (instance==null) instance = new ItemManagerImpl();
        return instance;
    }

    public int size() {
        int ret = this.items.size();
        logger.info("size " + ret);

        return ret;
    }

    public Item addItem(Item i) {
        logger.info("new Item " + i);

        this.items.add (i);
        logger.info("new Item added");
        return i;
    }

    public Item addItem(String name){
        return this.addItem(null,name);
    }

    public Item addItem(String id, String name) {
        return this.addItem(new Item(id, name));
    }

    public Item getItem(String id) {
        logger.info("getItem("+id+")");

        for (Item i: this.items) {
            if (i.getId().equals(id)) {
                logger.info("getItem("+id+"): "+i);

                return i;
            }
        }
        logger.warn("not found " + id);
        return null;
    }

    public Item getItem2(String id) throws ItemNotFoundException {
        Item i = getItem(id);
        if (i == null) throw new ItemNotFoundException();
        return i;
    }


    public List<Item> findAll() {
        return this.items;
    }

    @Override
    public void deleteItem(String id) {

        Item i = this.getItem(id);
        if (i==null) {
            logger.warn("not found " + i);
        }
        else logger.info(i+" deleted ");

        this.items.remove(i);

    }

    @Override
    public Item updateItem(Item i) {
        Item t = this.getItem(i.getId());

        if (t!=null) {
            logger.info(i+" rebut!!!! ");

            t.setName(i.getName());
            t.setCost(i.getCost());
            logger.info(t+" updated ");
        }
        else {
            logger.warn("not found "+i);
        }

        return t;
    }

    public void clear() {
        this.items.clear();
    }
}