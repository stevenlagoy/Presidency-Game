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
    private byte age = 0;
    private String presentation;

    public Character(String buildstring){
    }

    public Character(String givenName, String middleName, String familyName, int[] nameform){
        this.givenName = givenName;
        this.middleName = middleName;
        this.familyName = familyName;
        this.nameform = nameform;

        instances.add(this);
    }

    public void setGivenName(String name){
        this.givenName = name;
    }
    public String getGivenName(){
        return this.givenName;
    }
    public void setMiddleName(String name){
        this.middleName = name;
    }
    public String getMiddleName(){
        return this.middleName;
    }
    public void setFamilyName(String name){
        this.familyName = name;
    }
    public String getFamilyName(){
        return this.familyName;
    }
    public void setAge(byte age){
        this.age = age;
    }
    public byte getAge(){
        return this.age;
    }
}
