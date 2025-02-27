import java.util.ArrayList;
import java.util.Collection;

public class City {

    public static City[] cities;
    public static City selectCity(Demographics demographics){
        return null;
    }
    
    private CongressionalDistrict district;
    private State state;
    private int population;
    private ArrayList<Character> charactersPresent = new ArrayList<Character>();
    private DateManager.TimeZone timeZone;

    public City(CongressionalDistrict district, int population, DateManager.TimeZone timeZone){
        this.district = district;
        this.population = population;
        this.timeZone = timeZone;
    }
    public City(CongressionalDistrict district, int population, String timeZone){
        this.district = district;
        this.population = population;
        this.timeZone = DateManager.matchTimeZone(timeZone);
    }

    public CongressionalDistrict getDistrict(){
        return this.district;
    }
    public void setDistrict(CongressionalDistrict district){
        this.district = district;
    }
    public State getState(){
        return this.state;
    }
    public void setState(State state){
        this.state = state;
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

    public boolean equals(City other){
        return this.toString().equals(other.toString());
    }
}