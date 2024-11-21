package edu.upc.dsa;

import edu.upc.dsa.exceptions.HashMissingException;
import edu.upc.dsa.exceptions.UserNotFoundException;
import edu.upc.dsa.exceptions.UserRepeatedException;
import edu.upc.dsa.exceptions.WrongPasswordException;
import edu.upc.dsa.models.User;
import edu.upc.dsa.util.RandomUtils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
//Libreries de Hash:
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


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

    public User getUserFromUsername(String _username) throws UserNotFoundException{
        logger.info("getUser("+_username+")");

        for (User u: this.users.values()) {
            if (u.getName().equals(_username)) {
                logger.info("getUser("+_username+"): "+u);

                return u;
            }
        }
        logger.warn("not found " + _username);
        throw new UserNotFoundException();
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

    public boolean LoginUser(User UserEnviat, User UserServidor) throws WrongPasswordException, HashMissingException {
        if(UserServidor.getHash() == null){
            UserServidor.setHash(RandomUtils.getHash());
            throw new HashMissingException();
        }
        try{
            String concatenat = UserServidor.getPassword() + UserServidor.getHash();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(concatenat.getBytes());
            StringBuilder hashHex = new StringBuilder();
            for (byte b : hash) {
                hashHex.append(String.format("%02x", b));
            }
            if(UserEnviat.getPassword().equals(hashHex.toString()))
                return true;
            else
                throw new WrongPasswordException();
        }
        catch(NoSuchAlgorithmException ex){
            return false;
        }
    };

}