import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Character
{
    public static List<Character> instances = new LinkedList<Character>();
  
    private String givenName; // first or given name, forename
    private String middleName; // middle name
    private String familyName; // last or family name, surname
    private int[] nameform; // representation of the order of the name
    private String fullName; // evaluated full name of the character
    private String[] demographics; // list of applicable demographic blocs
    private int age = 0; // age in years
    private String presentation; // string representation of gender presentation
    private State origin; // state in which the character lives
    private Date birthday; // day of the year on which the character was born

    public Character(){
    }
    public Character(String buildstring){
    }

    public Character(String givenName, String middleName, String familyName, int[] nameform, String[] demographics, int age, String presentation, State origin, Date birthday){
        this.givenName = givenName;
        this.middleName = middleName;
        this.familyName = familyName;
        this.nameform = nameform;
        this.evaluateName();

        this.demographics = demographics;
        this.age = age;
        this.presentation = presentation;
        this.origin = origin;
        this.birthday = birthday;

        instances.add(this);
    }
    public Character(String name, String[] demographics, int age, String presentation, State origin){
        this.setName(name);

        this.demographics = demographics;
        this.age = age;
        this.presentation = presentation;
        this.origin = origin;

        instances.add(this);
    }

    public boolean equals(Character other){
        return (
            this.fullName.equals(other.fullName) &&
            this.presentation.equals(other.presentation) &&
            this.origin.equals(other.origin)
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
    public String[] getDemographics(){
        return this.demographics;
    }
    public void setDemographics(String[] demographics){
        this.demographics = demographics;
    }
    protected void genAge(){
        this.genAge(0, 120);
    }
    protected void genAge(int min){
        this.genAge(min, 120);
    }
    protected void genAge(int min, int max){
        if(max > 120) throw new IllegalArgumentException(String.format("Max value of %d out of allowed range: age < 120.%n", max));
        if(min < 0) throw new IllegalArgumentException(String.format("Min value of %d out of allowed range: age >= 0.%n", min));
        this.age = Engine.randInt(min, max);

    }
    public void setAge(int age){
        this.age = age;
    }
    public int getAge(){
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
    public State getOrigin(){
        return origin;
    }
    public void setOrigin(State origin){
        this.origin = origin;
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
