package main.core.map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.JSONObject;
import main.core.Engine;
import main.core.Jsonic;
import main.core.Repr;
import main.core.characters.PoliticalActor;
import main.core.demographics.Demographics;

public class State implements Repr<State>, Jsonic<State> {
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
    private List<Municipality> municipalities = new ArrayList<>();
    private List<University> universities = new ArrayList<>();
    private Map<Demographics, Float> demographics = new HashMap<>();
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

    public State(String name, int population, String abbreviation, Map<Demographics, Float> demographics) {
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

    public List<Municipality> getMunicipalities() {
        return municipalities;
    }
    public void addMunicipality(Municipality municipality) {
        municipalities.add(municipality);
    }
    public void removeMunicipality(Municipality municipality) {
        municipalities.remove(municipality);
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

    public Map<Demographics, Float> getDemographics(){
        return this.demographics;
    }

    public String toRepr() {
        String [] congressionalDistrictsStrings = new String[congressionalDistricts.size()];
        for (int i = 0; i < congressionalDistricts.size(); i++) {
            congressionalDistrictsStrings[i] = congressionalDistricts.get(i).getOfficeID();
        }
        String congressionalDistrictsRepr = Repr.arrayToReprList(congressionalDistrictsStrings);
        String [] countiesStrings = new String[counties.size()];
        for (int i = 0; i < counties.size(); i++) {
            countiesStrings[i] = counties.get(i).getName() + ", " + counties.get(i).getState().getAbbreviation();
        }
        String countiesRepr = Repr.arrayToReprList(countiesStrings);
        String [] citiesStrings = new String[municipalities.size()];
        for (int i = 0; i < municipalities.size(); i++) {
            citiesStrings[i] = municipalities.get(i).getName() + ", " + municipalities.get(i).getState().getAbbreviation();
        }
        String citiesRepr = Repr.arrayToReprList(citiesStrings);
        String [] universitiesStrings = new String[universities.size()];
        for (int i = 0; i < universities.size(); i++) {
            universitiesStrings[i] = universities.get(i).getName();
        }
        String universitiesRepr = Repr.arrayToReprList(universitiesStrings);
        String [] demographicsStrings = new String[demographics.size()];
        for (int i = 0; i < universities.size(); i++) {
            demographicsStrings[i] = "PLACEHOLDER : 0.0";
        }
        String demographicsRepr = Repr.arrayToReprList(demographicsStrings);
        String [] senatorsStrings = new String[senators.size()];
        for (int i = 0; i < senators.size(); i++) {
            senatorsStrings[i] = senators.get(i).getName() + ", " + senators.get(i).getName().getLegalName();
        }
        String senatorsRepr = Repr.arrayToReprList(senatorsStrings);
        String repr = String.format(
            "%s:[FIPS:%s;name=%s;population=%d;abbreviation=%s;motto=%s;nickname=%s;congressionalDistricts=[%s];counties=[%s];cities=[%s];universities=[%s];demographics=[%s];senators=[%s];governor=%s;];",
            this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1],
            this.FIPS,
            this.name,
            this.population,
            this.abbreviation,
            this.motto,
            this.nickname,
            congressionalDistrictsRepr,
            countiesRepr,
            citiesRepr,
            universitiesRepr,
            demographicsRepr,
            senatorsRepr,
            this.governor.getName().getLegalName()
        );
        return repr;
    }

    public State fromRepr(String repr) {
        return this;
    }

    @Override
    public JSONObject toJson() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJson'");
    }

    @Override
    public State fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }
}