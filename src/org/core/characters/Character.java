package org.core.characters;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import org.core.demographics.Bloc;
import org.core.map.City;
import org.core.DateManager;
import org.core.demographics.Demographics;
import org.core.demographics.DemographicsManager;
import org.core.Engine;
import org.core.characters.Name;
import org.core.Repr;
import org.core.map.State;

import java.util.List;

public class Character implements Repr { 
    protected final static int MIN_AGE = 35;
    protected final static int MAX_AGE = 120;

    private Name name;
    private Demographics demographics;
    private String presentation; // string representation of gender presentation
    private City birthplaceCity; // city the character was born in
    private City currentLocationCity; // City the character is currently in / near
    private City residenceCity; // City the character currently lives in
    private Date birthday; // day of the year on which the character was born, also used to find age

    private CharacterModel appearance;

    static Bloc[] generateDemographics(City city){
        return null;
    }
    static Bloc[] generateDemographics(State state){
        return null;
    }

    public Character(){
        // Get demographics
        this.demographics = CharacterManager.generateDemographics();

        // Get origin
        generateOrigin();

        // Get birthday and age
        generateBirthDate();
    }
    public Character(String buildstring){
    }

    public Character(Name name, Demographics demographics, String presentation, City birthplaceCity, Date birthday){
        this.name = name;

        this.demographics = demographics;
        this.presentation = presentation;
        this.birthplaceCity = birthplaceCity;
        this.birthday = birthday;

        CharacterManager.addCharacter(this);
    }

    protected void generateBirthDate(){
        Integer year, day;
        long birthdate;
        // select a year to be the character's birth year
        year = Engine.weightedRandSelect(CharacterManager.getAgeDistribution(this.demographics));
        // select a day of the year to be the character's birthday
        birthdate = DateManager.dateFormatToOrdinal(Engine.weightedRandSelect(CharacterManager.getBirthdateDistribution())) * DateManager.dayDuration;
        // validate leapyears
        //System.out.printf("Selected year: %d, Selected date: %s.\n", year.intValue(), DateManager.ordinalToDateFormat((int) (birthdate / DateManager.dayDuration)));
        if(birthdate == 60 * DateManager.dayDuration && !DateManager.isLeapYear(year)){
            //System.out.println("Selected Feb 29 in a non-Leap Year. Reselecting.");
            generateBirthDate();
            return;
        }
        // set the birthday
        this.setBirthday(new Date(DateManager.yearToMillis(year) + birthdate));
    }

    protected void generateOrigin(){
        this.birthplaceCity = City.selectCity(this.demographics);
    }
    public City getBirthplaceCity(){
        return this.birthplaceCity;
    }
    public void setBirthplaceCity(City birthplace){
        this.birthplaceCity = birthplace;
    }
    public City getCurrentLocationCity(){
        return this.currentLocationCity;
    }
    public void setCurrentLocationCity(City currentLocation){
        this.currentLocationCity = currentLocation;
    }
    public City getResidenceCity(){
        return residenceCity;
    }
    public void setResidenceCity(City residence){
        this.residenceCity = residence;
    }

    protected void genName(){
        CharacterManager.generateName(this.demographics);
    }
    public Name getName(){
        return this.name;
    }
    protected void genDemographics(){
        CharacterManager.generateDemographics();
    }
    public Demographics getDemographics(){
        return this.demographics;
    }
    public void setDemographics(Demographics demographics){
        this.demographics = demographics;
    }
    protected void genAge(){
        this.genAge(Character.MIN_AGE, Character.MAX_AGE);
    }
    protected void genAge(int min){
        if(min < 0) throw new IllegalArgumentException(String.format("Min value of %d out of allowed range: age >= 0.%n", min));
        this.genAge(min, Character.MAX_AGE);
    }
    protected void genAge(int min, int max){
        if(max > 120) throw new IllegalArgumentException(String.format("Max value of %d out of allowed range: age < 120.%n", max));
        if(min < 0) throw new IllegalArgumentException(String.format("Min value of %d out of allowed range: age >= 0.%n", min));
        
        long age = 0;
        Integer[] range = IntStream.rangeClosed(1,10).boxed().toArray(Integer[]::new);
        while(age < MIN_AGE || age > MAX_AGE){
            age = Engine.weightedRandSelect(range, DemographicsManager.getPopulationPyramidPercent(DemographicsManager.EVERYONE));
        }
    }
    public void setAgeMillis(long age){ // this should almost never be used
        this.birthday = new Date(DateManager.currentGameDate.getTime() - age);
    }
    public long getAgeMillis(){
        return DateManager.currentGameDate.getTime() - this.birthday.getTime();
    }
    public double getAgeYears(){
        return DateManager.timeToYears(this.getAgeMillis());
    }
    protected void genPresentation(){
        CharacterManager.generatePresentation(this.demographics);
    }
    public String getPresentation(){ // should this be a string?
        return this.presentation;
    }
    public void setPresentation(String presentation){ // should this be a string?
        this.presentation = presentation;
    }
    protected void genOrigin(){
    }
    public City getCityOrigin(){
        return birthplaceCity;
    }
    public State getStateOrigin(){
        return birthplaceCity.getState();
    }
    public Date getBirthday(){
        return this.birthday;
    }
    public void setBirthday(Date birthday){
        this.birthday = birthday;
    }
    public void setBirthday(long milliseconds){
        this.birthday = new Date(milliseconds);
    }

    public void fromRepr(String repr){

    }

    public boolean equals(Character other){
        return this.toString().equals(other.toString());
    }
    public String toString(){
        return this.toRepr();
    }
    public String toRepr(){
        String repr = String.format(
            "%s:[name:\"%s\";presentation=%s;origin=;birthday=%d;];",
            this.getClass().toString().replace("class ", ""),
            this.name.toRepr(),
            this.presentation,
            this.birthday
        );
        return repr;
    }
}
