package main.core.map;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.core.Engine;
import main.core.characters.Character;
import main.core.characters.PoliticalActor;

import java.util.HashMap;

public class State {
    static
    {
        Map<String, String> states = new HashMap<>();
        states.put("name", "Alabama");
        // this is where states.json will be read and the states objects created
    }

    private static List<State> instances = new ArrayList<>();

    private int FIPS;
    private String name;
    private int population;
    private String abbreviation;
    private String motto;
    private String nickname;
    private List<CongressionalDistrict> congressionalDistricts = new ArrayList<>();
    private List<County> counties = new ArrayList<>();
    private List<City> cities = new ArrayList<>();
    private List<String> universities = new ArrayList<>();
    private List<String> demographics = new ArrayList<>();
    private List<PoliticalActor> senators;
    private PoliticalActor governor;

    public State(int FIPS, String name, int population, String abbreviation, String motto, String nickname) {
        this.FIPS = FIPS;
        this.name = name;
        this.population = population;
        this.abbreviation = abbreviation;
        this.motto = motto;
        this.nickname = nickname;
    }

    public State(String name, int population, String abbreviation, List<String> demographics){
        this.name = name;
        this.population = population;
        this.abbreviation = abbreviation;
        this.demographics = demographics;

        instances.add(this);
    }

    public int getFIPS() {
        return FIPS;
    }
    public void setFIPS(int FIPS) {
        this.FIPS = FIPS;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }
    public void setPopulation(int population) {
        this.population = population;
    }
    public void addPopulation(int population) {
        this.population += population;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getMotto() {
        return motto;
    }
    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<CongressionalDistrict> getCongressionalDistricts() {
        return congressionalDistricts;
    }
    public void addCongressionalDistrict(CongressionalDistrict district) {
        congressionalDistricts.add(district);
    }
    public void removeCongressionalDistrict(CongressionalDistrict district) {
        congressionalDistricts.remove(district);
    }

    public List<County> getCounties() {
        return counties;
    }
    public void addCongressionalDistrict(County county) {
        counties.add(county);
    }
    public void removeCounty(County county) {
        counties.remove(county);
    }

    public List<City> getCities() {
        return cities;
    }
    public void addCity(City city) {
        cities.add(city);
    }
    public void removeCity(City city) {
        cities.remove(city);
    }

    public boolean hasSenator(PoliticalActor senator){
        return senators.contains(senator);
    }
    public void createSenators(){
        this.createSenators(2);
    }

    public void createSenators(int numberOfSenators){

    }
    public void removeSenator(PoliticalActor senator){
        senators.remove(senator);
    }

    public List<String> getDemographics(){
        return this.demographics;
    }

    public String toString(){
        return String.format("State("+
        "name=%s," +
        "population=%s," +
        "abbreviation=%s," +
        "universities=%s",
        this.name, this.population, this.abbreviation, this.universities);
    }
}