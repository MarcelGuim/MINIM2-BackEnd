package edu.upc.dsa.models;

import edu.upc.dsa.util.RandomUtils;

public class User {
    String id;
    String user;
    String password;
    static int lastId;

    public User() {
        this.setId(RandomUtils.getId());
    }
    public User(String user, String password) {
        this(null, user, password);
    }

    public User(String id, String user, String password) {
        this();
        if (id != null) this.setId(id);
        this.setUser(user);
        this.setPassword(password);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id=id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User [id="+id+", user=" + user + ", password=" + password +"]";
    }

}
