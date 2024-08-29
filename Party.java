import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Party {

    public static List<Party> instances = new ArrayList<>();

    private String name;
    private String adjective;
    private String abbreviation;
    //private ??? governingBody;
    private int colorCode;
    //private ??? symbol like the animal that represents the party
    private File iconFile;

    private List<Character> members = new ArrayList<>();
    private List<Convention> conventions = new ArrayList<>();
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

    public String toString(){
        return "";
    }
}