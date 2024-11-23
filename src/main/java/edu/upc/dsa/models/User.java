package edu.upc.dsa.models;

import edu.upc.dsa.util.RandomUtils;

public class User {
    String id;
    String name;
    String password;
    String correo;
    double money;
    double cobre;
    String Hash;
    static int lastId;

    public User() {
        this.setId(RandomUtils.getId());
    }
    public User(String user, String password, String correo) {
        this(null, user, password, correo);
    }

    public User(String id, String user, String password, String correo) {
        this();
        if (id != null) this.setId(id);
        this.setName(user);
        this.setPassword(password);
        this.setCorreo(correo);
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
    public double getMoney() {
        return money;
    }
    public void setMoney(double money) {
        this.money = money;
    }

    public String getHash() {
        return Hash;
    }

    public void setHash(String hash) {
        Hash = hash;
    }

    public double getCobre() {
        return cobre;
    }

    public void setCobre(double cobre) {
        this.cobre = cobre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return "User [id="+id+", user=" + name + ", password=" + password +"]";
    }

}
