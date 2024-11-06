import java.util.ArrayList;
import java.util.Collection;

public class City {
    
    private CongressionalDistrict district;
    private int population;
    private ArrayList<Character> charactersPresent = new ArrayList<Character>();

    public City(CongressionalDistrict district, int population){
        this.district = district;
        this.population = population;
    }

    public CongressionalDistrict getDistrict(){
        return this.district;
    }
    public void setDistrict(CongressionalDistrict district){
        this.district = district;
    }
    public int getPopulation(){
        return this.population;
    }
    public void setPopulation(int population){
        this.population = population;
    }
    public void addPopulation(int population){
        this.population += population;
    }
    public void addCharacter(Character character){
        this.charactersPresent.add(character);
    }
    public void removeCharacter(Character character){
        this.charactersPresent.remove(character);
    }
    public void addCharacters(Collection<? extends Character> characters){
        this.charactersPresent.addAll(characters);
    }
    public void removeCharacters(Collection<? extends Character> characters){
        this.charactersPresent.removeAll(characters);
    }
}