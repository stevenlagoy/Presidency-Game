import java.util.ArrayList;
import java.util.List;

public class Character
{
    public static List<Character> instances = new ArrayList<>();
  
    private String givenName;
    private String middleName;
    private String familyName;
    private int[] nameform;
    private String[] demographics;
    private int age = 0;
    private String presentation;
    protected State origin;

    public Character(){
    }
    public Character(String buildstring){
    }

    public Character(String givenName, String middleName, String familyName, int[] nameform){
        this.givenName = givenName;
        this.middleName = middleName;
        this.familyName = familyName;
        this.nameform = nameform;

        instances.add(this);
    }

    protected void genGivenName(){
    }
    public String getGivenName(){
        return this.givenName;
    }
    public void setGivenName(String name){
        this.givenName = name;
    }
    protected void genMiddleName(){
    }
    public void setMiddleName(String name){
        this.middleName = name;
    }
    public String getMiddleName(){
        return this.middleName;
    }
    protected void genFamilyName(){
    }
    public void setFamilyName(String name){
        this.familyName = name;
    }
    public String getFamilyName(){
        return this.familyName;
    }
    public void genOrigin(){
    }
    public void setAge(int age){
        this.age = age;
    }
    public int getAge(){
        return this.age;
    }
}
