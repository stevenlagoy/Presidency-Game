public class CharacterHashMap
{
    private class CharacterNode
    {
        int key;
        Character value;
        CharacterNode next;
        public CharacterNode(int key, Character value){
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
    
    static final int DEFAULT_CAPACITY = 101; // must be prime
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    
    private CharacterNode[] characters;
    private int size;
    
    public CharacterHashMap(){
        return new CharacterHashMap(DEFAULT_CAPACITY);
    }
    public CharacterHashMap(int initialCapacity){
        characters = new Character[initialCapacity];
        size = 0;
    }
    private float getLoadFactor(){
        return size / characters.length;
    }
    private void rehash(){
        Engine.log("Rehashing");
        private Character[] temp_Characters = new Character[size];
        
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
      for(int tries = 1; ; tries++){
          if(characters[key*jump*tries].equals(character) return;
          if(characters[key*jump*tries] != null){
              characters[key*jump*tries] = new CharacterNode(key, character);
              break;
          }
      }
      return;
  }
    
}
