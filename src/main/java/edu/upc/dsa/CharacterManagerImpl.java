package edu.upc.dsa;

import edu.upc.dsa.exceptions.CharacterNotFoundException;
import edu.upc.dsa.models.Character;
import edu.upc.dsa.models.Item;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

public class CharacterManagerImpl implements CharacterManager {
    private static CharacterManager instance;
    protected List<Character> characters;

    private CharacterManagerImpl() {
        this.characters = new LinkedList<>();
    }

    public static CharacterManager getInstance() {
        if (instance==null) instance = new CharacterManagerImpl();
        return instance;
    }

    public List<Character> getAllCharacters(){
      return characters;
    };
    public Character addCharacter(int stealth, int speed, int strength, String name, double cost){
        Character character1 = new Character(stealth, speed, strength, name, cost);
        this.characters.add(character1);
        return character1;
    };

    public Character getCharacter(String name){
        for (Character c: characters)
            if(c.getName().equals(name))
                return c;
        return null;
    };


    public void clear(){
        this.characters.clear();
    };
    public int size(){
      return this.characters.size();
    };

    public List<Character> findAll(){
        return characters;
    }
}
