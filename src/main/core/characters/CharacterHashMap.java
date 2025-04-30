package main.core.characters;

import main.core.Engine;

public class CharacterHashMap
{
    private class CharacterNode
    {
        int key;
        Character value;
        CharacterNode next;
        public CharacterNode(Character value){
            this.key = 0; // unused
            this.value = value;
            this.next = null; // unused
        }
    }
    
    static final int DEFAULT_CAPACITY = 101; // must be prime
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int ACCEPTABLE_NUM_TRIES = 20;
    
    private CharacterNode[] characters;
    private int size;
    
    public CharacterHashMap(){
        this(DEFAULT_CAPACITY);
    }
    public CharacterHashMap(int initialCapacity){
        characters = new CharacterNode[initialCapacity];
        size = 0;
    }
    private float getLoadFactor(){
        return size / characters.length;
    }
    private void rehash(){
        Engine.log("Rehashing");
        Character[] temp_Characters = new Character[size];
        
        for(int i = 0; i < size; i++){
            temp_Characters[i] = characters[i].value;
        }
        this.increaseCapacity(); // also clears the list
        for(Character character : temp_Characters){
            this.insert(character);
        }
    }
    public void clear(){
        // empties the hashmap but does not change the size of the array
        characters = new CharacterNode[characters.length];
        size = 0;
    }
    private void increaseCapacity(){
        // clears the list and increases the size of the array s.t. load factor < 0.5
        int newCap = characters.length;
        while(size * 1.0 / newCap > 0.5) newCap++;
        this.clear();
        characters = new CharacterNode[Engine.nextPrime(newCap)];
    }
    public void insert(Character character){
        if(this.getLoadFactor() > DEFAULT_LOAD_FACTOR) this.rehash();
        int key = hash1(character);
        int jump = hash2(character);
        for(int tries = 1; tries < ACCEPTABLE_NUM_TRIES; tries++){
            if (characters[key*jump*tries % characters.length].value.equals(character)) return;
            if (characters[key*jump*tries % characters.length] != null){
                characters[key*jump*tries % characters.length] = new CharacterNode(character);
                break;
            }
        }
        return;
    }
    public void remove(Character character){
        int key = hash1(character);
        int jump = hash2(character);
        for (int tries = 1; tries < ACCEPTABLE_NUM_TRIES; tries++) {
            if (characters[key*jump*tries % characters.length] == null) continue;
            if (characters[key*jump*tries % characters.length].value.equals(character)) {
                characters[key*jump*tries % characters.length] = null;
                break;
            }
        }
        return;
    }
    private int hash1(Character character) {
        return character.hashCode();
    }

    private int hash2(Character character) {
        return 1 + character.hashCode() % (characters.length - 1);
    }
    
    
}
