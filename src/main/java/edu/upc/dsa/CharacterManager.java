package edu.upc.dsa;
import edu.upc.dsa.exceptions.CharacterNotFoundException;
import edu.upc.dsa.models.Character;
import edu.upc.dsa.models.User;

import java.util.List;

public interface CharacterManager {

    public Character addCharacter(int stealth, int speed, int strength, String name, double cost);
    public void clear();
    public int size();
    public Character getCharacter(String name);
    public List<Character> findAll();

}
