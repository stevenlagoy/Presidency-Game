package main.core.map;
import java.util.ArrayList;
import java.util.Collection;

import main.core.DateManager;
import main.core.characters.Character;
import main.core.demographics.Demographics;

public class City {

    public static City[] cities;

    public static City selectCity(Demographics demographics){
        return null;
    }
    
    private String name;
    private CongressionalDistrict district;
    private State state;
    private int population;
    private double area;
    private ArrayList<Character> charactersPresent = new ArrayList<Character>();
    private DateManager.TimeZone timeZone;

    public City(String name, int population, double area) {
        this.name = name;
        this.population = population;
        this.area = area;
    }

    public City(String name, String state, int population, double area) {
        this(name, MapManager.matchState(state), population, area);
    }

    public City(String name, State state, int population, double area) {
        this.name = name;
        this.state = state;
        this.population = population;
        this.area = area;
    }

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

    public String getName() {
        return name;
    }
    public String getNameWithState() {
        return name + ", " + state.getAbbreviation();
    }
    public void setName(String name) {
        this.name = name;
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

    public double getArea() {
        return area;
    }
    public void setArea(double area) {
        this.area = area;
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