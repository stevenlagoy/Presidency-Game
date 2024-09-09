import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class Party
{

    public static List<Party> instances = new ArrayList<>();

    private String name;
    private String adjective;
    private String abbreviation;
    //private ??? governingBody;
    private int colorCode;
    //private ??? symbol like the animal that represents the party
    private File iconFile;

    private Set<Character> members = new HashSet<>();
    private Set<Convention> conventions = new HashSet<>();
    // list for issues and stances as well

    public Party(String buildstring)
    {

    }

    public Party(String name, String adjective, String abbreviation, int colorCode, String iconFilePath) throws Exception
    {
        this.name = name;
        this.adjective = adjective;
        this.abbreviation = abbreviation;
        this.colorCode = colorCode;
        this.iconFile = new File(iconFilePath);
        if(!iconFile.exists()){
            throw(new Exception());
        }
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getAdjective(){
        return this.adjective;
    }
    public void setAdjective(String adjective){
        this.adjective = adjective;
    }
    public String getAbbr(){
        return this.abbreviation;
    }
    public void setAbbr(String abbreviation){
        this.abbreviation = abbreviation;
    }
    public int getColorCode(){
        return this.colorCode;
    }
    public void setColorCode(int colorCode){
        this.colorCode = colorCode;
    }
    public File getIconFile(){
        return this.iconFile;
    }
    public void setIconFile(String iconFilePath) throws Exception{
        this.iconFile = new File(iconFilePath);
        if(!iconFile.exists()){
            throw(new Exception());
        }
    }
    public Set<Character> getMembers(){
        return this.members;
    }
    public void addMember(Character c){
        this.members.add(c);
    }
    public void removeMember(Character c){
        this.members.remove(c);
    }
    public void resetMembers(){
        this.members.clear();
    }
    public Set<Convention> getConventions(){
        return this.conventions;
    }
    public void addConvention(Convention c){
        this.conventions.add(c);
    }
    public void removeConvention(Convention c){
        this.conventions.remove(c);
    }
    public void resetConventions(){
        this.conventions.clear();
    }



    public String toString(){
        return "";
    }
}