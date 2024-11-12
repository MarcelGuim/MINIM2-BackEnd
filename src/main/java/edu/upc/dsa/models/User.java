package edu.upc.dsa.models;

import edu.upc.dsa.util.RandomUtils;

public class User {
    String id;
    String name;
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
        this.setName(user);
        this.setPassword(password);
    }

    public void setRandomId(){
        this.setId(RandomUtils.getId());
    }
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User [id="+id+", user=" + name + ", password=" + password +"]";
    }

}
