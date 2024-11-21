package edu.upc.dsa.models;

import edu.upc.dsa.util.RandomUtils;

public class Character {
    String id;
    String name;
    int strength;
    int speed;
    int stealth;
    double cost;

    public Character(){}

    public Character(int stealth, int speed, int strength, String name, double cost) {
        this.setId(RandomUtils.getId());
        this.setStealth(stealth);
        this.setSpeed(speed);
        this.setStrength(strength);
        this.setName(name);
        this.setCost(cost);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getStealth() {
        return stealth;
    }

    public void setStealth(int stealth) {
        this.stealth = stealth;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
