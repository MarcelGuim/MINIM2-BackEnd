package edu.upc.dsa;

import edu.upc.dsa.exceptions.UserNotFoundException;
import edu.upc.dsa.exceptions.UserRepeatedException;
import edu.upc.dsa.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

public class UserManagerImpl implements UserManager {
    private static UserManager instance;
    protected HashMap<String, User> users;
    final static Logger logger = Logger.getLogger(UserManagerImpl.class);

    private UserManagerImpl() {
        this.users = new HashMap<>();
    }

    public static UserManager getInstance() {
        if (instance==null) instance = new UserManagerImpl();
        return instance;
    }

    public int size() {
        int ret = this.users.size();
        logger.info("size " + ret);

        return ret;
    }

    public User addUser(User u) throws UserRepeatedException {
        logger.info("new User " + u);
        if(users.get(u.getName()) == null)
        {
            this.users.put(u.getName(),u);
            logger.info("new User added");
            return u;
        }
        else{
            logger.warn("User already exists with that name");
            throw new UserRepeatedException();
        }
    }

    public User addUser(String user, String password) throws UserRepeatedException{
        return this.addUser(null, user, password);
    }

    public User addUser(String id, String user, String password) throws UserRepeatedException {
        return this.addUser(new User(id, user,password));
    }

    public User getUser(String id) {
        logger.info("getUser("+id+")");

        for (User u: this.users.values()) {
            if (u.getId().equals(id)) {
                logger.info("getUser("+id+"): "+u);

                return u;
            }
        }
        logger.warn("not found " + id);
        return null;
    }

    public User getUser2(String id) throws UserNotFoundException {
        User u = getUser(id);
        if (u == null) throw new UserNotFoundException();
        return u;
    }

    public User getUserFromUsername(String _username) {
        logger.info("getUser("+_username+")");

        for (User u: this.users.values()) {
            if (u.getName().equals(_username)) {
                logger.info("getUser("+_username+"): "+u);

                return u;
            }
        }
        logger.warn("not found " + _username);
        return null;
    }


    public List<User> findAll() {
        List<User> resupuesta = new ArrayList<>(this.users.values());
        return resupuesta;
    }

    @Override
    public void deleteUser(String id) {

        User u = this.getUser(id);
        if (u==null) {
            logger.warn("not found " + u);
        }
        else logger.info(u+" deleted ");

        this.users.remove(u);

    }

    @Override
    public User updateUser(User u) {
        User t = this.getUser(u.getId());
        if (t!=null) {
            logger.info(u+" rebut!!!! ");
            t.setName(u.getName());
            t.setPassword(u.getPassword());
            t.setMoney(u.getMoney());
            logger.info(t+" updated ");
        }
        else {
            logger.warn("not found "+u);
        }
        return t;
    }
    public void clear() {
        this.users.clear();
    }
}