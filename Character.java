import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.List;

public class Character implements Repr
{ 
    protected final static int MIN_AGE = 35;
    protected final static int MAX_AGE = 120;

    private String givenName; // first or given name, forename
    private String middleName; // middle name
    private String familyName; // last or family name, surname
    private int[] nameform; // representation of the order of the name
    private String fullName; // evaluated full name of the character
    private Bloc[] demographics; // list of applicable demographic blocs
    private double age = 0; // age in years
    private String presentation; // string representation of gender presentation
    private City birthPlace; // city the character was born in
    private City currentLocation; // City the character is currently in / near
    private City residencePlace; // City the character currently lives in
    private Date birthday; // day of the year on which the character was born

    static City generateOrigin(){
        return City.selectCity();
    }
    static Bloc[] generateDemographics(City city){
        return null;
    }
    static Bloc[] generateDemographics(State state){
        return null;
    }
    protected void generateBirthDate(){
        Date birthdate;
        Integer[] range = IntStream.rangeClosed(1,10).boxed().toArray(Integer[]::new);
        birthdate = new Date(Engine.weighedRandSelect(range, CharacterManager.getBirthdateDistribution()) * DateManager.dayDuration);
        // check leapyear validity
        if(birthdate.getTime() == 60 * DateManager.dayDuration && !DateManager.isLeapYear(DateManager.calculateYear(this.getAge()))){
            generateBirthDate(); // retry
            return;
        }
        this.setBirthday(birthdate);
        this.setAge(this.getBirthday().getTime() / DateManager.yearDuration);
    }

    public Character(){
        // Get origin
        this.birthPlace = generateOrigin();

        // Get demographics
        this.demographics = generateDemographics(birthPlace);

        // Get birthday and age
        generateBirthDate();
        this.age = birthday.getTime();

    }
    public Character(String buildstring){
    }

    public Character(String givenName, String middleName, String familyName, int[] nameform, Bloc[] demographics, int age, String presentation, City origin, Date birthday){
        this.givenName = givenName;
        this.middleName = middleName;
        this.familyName = familyName;
        this.nameform = nameform;
        this.evaluateName();

        this.demographics = demographics;
        this.age = age;
        this.presentation = presentation;
        this.birthPlace = origin;
        this.birthday = birthday;

        CharacterManager.addCharacter(this);
    }
    public Character(String name, Bloc[] demographics, int age, String presentation, City origin){
        this.setName(name);

        this.demographics = demographics;
        this.age = age;
        this.presentation = presentation;
        this.birthPlace = origin;

        CharacterManager.addCharacter(this);
    }

    public boolean equals(Character other){
        return (
            this.fullName.equals(other.fullName) &&
            this.presentation.equals(other.presentation) &&
            this.birthPlace.equals(other.birthPlace)
        );
    }

    public String toString(){
        String repr = String.format(
            "%S:givenName=%s,middleName=%s,familyName=%s,nameform=,age=%d,presentation=%s,origin=,birthday=%d,",
            "CHARACTER",
            this.givenName,
            this.middleName,
            this.familyName,
            this.nameform.toString(),
            this.age,
            this.presentation,
            this.birthday
        );
        return repr;
    }

    protected void genGivenName(){
        this.evaluateName();
    }
    public String getGivenName(){
        return this.givenName;
    }
    public void setGivenName(String name){
        this.givenName = name;
        this.evaluateName();
    }
    protected void genMiddleName(){
        this.evaluateName();
    }
    public void setMiddleName(String name){
        this.middleName = name;
        this.evaluateName();
    }
    public String getMiddleName(){
        return this.middleName;
    }
    protected void genFamilyName(){
        this.evaluateName();
    }
    public void setFamilyName(String name){
        this.familyName = name;
        this.evaluateName();
    }
    public String getFamilyName(){
        return this.familyName;
    }
    private void evaluateName(){
        fullName = "";
        for(int name : nameform){
            if(name == 0) fullName += givenName;
            if(name == 1) fullName += middleName;
            if(name == 2) fullName += familyName;
        }
    }
    public String getName(){
        return fullName;
    }
    public void setName(String name){

    }
    public void setName(String givenName, String middleName, String familyName, int[] nameform){
        this.givenName = givenName;
        this.middleName = middleName;
        this.familyName = familyName;
        this.nameform = nameform;

        this.evaluateName();
    }
    protected void genDemographics(){
    }
    public Bloc[] getDemographics(){
        return this.demographics;
    }
    public void setDemographics(Bloc[] demographics){
        this.demographics = demographics;
    }
    protected void genAge(){
        this.genAge(MIN_AGE, MAX_AGE);
    }
    protected void genAge(int min){
        this.genAge(min, MAX_AGE);
    }
    protected void genAge(int min, int max){
        if(max > 120) throw new IllegalArgumentException(String.format("Max value of %d out of allowed range: age < 120.%n", max));
        if(min < 0) throw new IllegalArgumentException(String.format("Min value of %d out of allowed range: age >= 0.%n", min));
        
        int age = 0;
        Integer[] range = IntStream.rangeClosed(1,10).boxed().toArray(Integer[]::new);
        while(age < MIN_AGE || age > MAX_AGE){
            age = Engine.weighedRandSelect(range, DemographicsManager.getPopulationPyramidPercent(DemographicsManager.EVERYONE));
        }
        this.setAge(age);
    }
    public void setAge(double age){
        this.age = age;
    }
    public double getAge(){
        return this.age;
    }
    protected void genPresentation(){
    }
    public String getPresentation(){
        return this.presentation;
    }
    public void setPresentation(String presentation){
        this.presentation = presentation;
    }
    protected void genOrigin(){
    }
    public City getCityOrigin(){
        return birthPlace;
    }
    public State getStateOrigin(){
        return birthPlace.getState();
    }
    public void setOrigin(City origin){
        this.birthPlace = origin;
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
}
