package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.User;

import java.util.List;

public interface UserManager {

    public User addUser(String id, String user, String password,  String mail) throws UserRepeatedException;
    public User addUser(String user, String password, String mail) throws UserRepeatedException;
    public User addUser(User u) throws UserRepeatedException;
    public User getUser(String id);
    public User getUser2(String id) throws UserNotFoundException;
    public User getUserFromUsername(String _username) throws  UserNotFoundException;
    public List<User> findAll();
    public void deleteUser(String id);
    public User updateUser(User u);
    public void updateCobre(double cobre, User user);
    public double updateMoney(User user) throws UserHasNoCobreException, UserHasNoMultiplicadorException;
    public double damePrecioCobre(User user);
    public void clear();
    public int size();
    public void changePassword(User user, String pswd);
    public void RecoverPassword(User user) throws Exception;
}
