package main.core.map;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import main.core.DateManager;
import main.core.characters.Character;
import main.core.demographics.Demographics;
import main.core.demographics.Bloc;

public class Municipality {

    public static enum TypeClass {
        CITY,
        TOWN,
        VILLAGE,
        FIRST_CLASS,
        SECOND_CLASS,
        THIRD_CLASS,
        HOME_RULE;

        public static final TypeClass defaultTypeClass = TypeClass.CITY;
        public static TypeClass matchTypeClass(String typeClass) {
            String target = typeClass.toUpperCase().trim().replaceAll("\\s+","_");
            for (TypeClass tc : TypeClass.values()) {
                if (tc.toString().equals(target)) return tc;
            }
            return null;
        }
    }

    public static Municipality[] cities;

    public static Municipality selectMunicipality(Demographics demographics){
        return null;
    }
    
    private String FIPS;
    private String name;
    private State state;
    private List<County> counties;
    private TypeClass typeClass;
    private int population;
    private double area;
    private List<Character> charactersPresent;
    private Map<Bloc, Float> demographics; // Percentages of the municipality which belong to each Bloc
    private TimeZone timeZone;

    public Municipality() {
        FIPS = "";
        name = "";
        state = null;
        counties = new ArrayList<>();
        typeClass = TypeClass.defaultTypeClass;
        population = 0;
        area = 0.0;
        charactersPresent = new ArrayList<>();
        demographics = new HashMap<>();
        timeZone = null;
    }

    public Municipality(String FIPS, String name, String state, String county, String typeClass, int population, double area, Map<Bloc, Float> demographics, TimeZone timeZone) {
       this(FIPS, name, state, List.of(county), typeClass, population, area, demographics, timeZone);
    }

    public Municipality(String FIPS, String name, String state, List<String> counties, String typeClass, int population, double area, Map<Bloc, Float> demographcis, TimeZone timeZone) {
        this(FIPS, name, MapManager.matchState(state), MapManager.matchCounties(counties, MapManager.matchState(state)), TypeClass.matchTypeClass(typeClass), population, area, demographcis, timeZone);
    }

    public Municipality(String FIPS, String name, State state, County county, TypeClass typeClass, int population, double area, Map<Bloc, Float> demographics, TimeZone timeZone) {
        this(FIPS, name, state, List.of(county), typeClass, population, area, demographics, timeZone);
    }

    public Municipality(String FIPS, String name, State state, List<County> counties, TypeClass typeClass, int population, double area, Map<Bloc, Float> demographics, TimeZone timeZone) {
        this.FIPS = FIPS;
        this.name = name;
        this.state = state;
        this.counties = counties;
        this.typeClass = typeClass;
        this.population = population;
        this.area = area;
        this.demographics = demographics != null ? demographics : MapManager.getDefaultDemographics();
        this.timeZone = timeZone;
        MapManager.municipalities.add(this);
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

    public List<County> getCounties() {
        return counties;
    }
    public void setCounties(List<County> counties) {
        this.counties = counties;
    }
    public void SetCounties(County county) {
        this.counties = List.of(county);
    }
    public void addCounty(County county) {
        this.counties.add(county);
    }
    public void removeCounty(County county) {
        this.counties.remove(county);
    }

    public TypeClass getTypeClass() {
        return typeClass;
    }
    public void setTypeClass(TypeClass typeClass) {
        this.typeClass = typeClass;
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

    public Map<Bloc, Float> getDemographics() {
        return this.demographics;
    }
    public void setDemographics(Map<Bloc, Float> demographics) {
        this.demographics = demographics;
    }
    public Float getDemographicPercentage(Bloc bloc) {
        return this.demographics.get(bloc);
    }

    public boolean equals(Municipality other){
        return this.toString().equals(other.toString());
    }
}