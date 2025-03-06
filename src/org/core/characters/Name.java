package src.org.core.characters;

import src.org.core.Engine;
import src.org.core.Repr;

public class Name implements Repr
{
    enum Nameform {
        WESTERN,
        EASTERN
    }

    private Nameform nameform;
    private String forename;
    private String middlename;
    private String preferredname;
    private String[] nicknames;
    private String surname;
    private String familyname;
    private String generationname;
    private String givenname;
    private String honorific;
    private String ordination;
    private boolean abbreviateFirst;
    private boolean abbreviateMiddle;

    private String fullName;
    private String formalName;
    private String personalName;
    private String initials;

    public static String abbreviate(String name){
        String res = "";
        String[] names = name.split("[\\s]");
        for(int i = 0; i < names.length; i++){
            for(int j = 0; j < names[i].length(); j++){
                if(java.lang.Character.isLetter(names[i].charAt(j))){
                    res += java.lang.Character.toString(names[i].charAt(j)).toUpperCase();
                    res += ". ";
                    break;
                }
            }
        }
        res = res.trim();
        return res;
    }
    public static String initialize(String first, String middle, String last){
        String res = "";
        for(int i = 0; i < first.length(); i++){
            if(java.lang.Character.isAlphabetic(first.charAt(i))){
                res += first.charAt(i);
                break;
            }
        }
        for(int i = 0; i < middle.length(); i++){
            if(java.lang.Character.isAlphabetic(middle.charAt(i))){
                res += middle.charAt(i);
                break;
            }
        }
        for(int i = 0; i < last.length(); i++){
            if(java.lang.Character.isAlphabetic(last.charAt(i))){
                res += last.charAt(i);
                break;
            }
        }
        if(res.length() != 3){
            Engine.log("UNINITIALIZABLE NAME", String.format("At least one of (\"%s\", \"%s\", \"%s\") has no alphabetic characters and is uninitializable.", first, middle, last), new Exception());
            return null;
        }
        return res;
    }

    /**
     * @param nameform Either Eastern or Western, defines how the name will be ordered and rendered.
     * @param forename The first or given name.
     * @param middlename The middle or generational name.
     * @param surname The last or family name.
     */
    public Name(String nameform, String forename, String middlename, String surname){
        if(nameform.toUpperCase().equals("WESTERN")){
            this.nameform = Nameform.WESTERN;
            this.forename = forename;
            this.middlename = middlename;
            this.surname = surname;
        }
        else if(nameform.toUpperCase().equals("EASTERN")){
            this.nameform = Nameform.EASTERN;
            this.givenname = forename;
            this.generationname = middlename;
            this.familyname = surname;
        }
        else {
            Engine.log("NAME INVALID NAMEFORM: ", String.format("Invalid nameform supplied: %s", nameform), new Exception());
            return;
        }
    }
    public Name(String forename, String middlename, String surname){
        this("western", forename, middlename, surname);
    }
    public Name(String nameform, String honorific, String forename, String middlename, String surname){
        this(nameform, forename, middlename, surname);
        this.honorific = honorific;
    }
    public Name(String forename, String middlename, String surname, int ordination){
        this(forename, middlename, surname);
        if(ordination == 1) this.ordination = "Sr.";
        else if(ordination == 2) this.ordination = "Jr.";
        else for(int i = 0; i < ordination; i++) this.ordination += "I";
        // This assumes that everyone with an ordination of 1 uses Sr instead of I, and everyone with an ordination of 2 uses Jr instead of II. This can be overriden for a Name instance by directly accessing the ordination field.
    }
    public Name(String forename, boolean abbreviateForename, String middlename, boolean abbreviateMiddlename, String surname){
        this(forename, middlename, surname);
        this.abbreviateFirst = abbreviateForename;
        this.abbreviateMiddle = abbreviateMiddlename;
    }

    private void updateNames(){
        if(nameform == Nameform.WESTERN){
            fullName = String.format("%s %s %s \"%s\" %s %s", honorific, forename, middlename, nicknames[0], surname, ordination);
            formalName = String.format("%s %s %s %s %s", honorific, forename, middlename, surname, ordination);
            personalName = String.format("%s %s", nicknames[0], surname);
        }
        else if(nameform == Nameform.EASTERN){
            fullName = String.format("%s %s %s%s", honorific, familyname, generationname, givenname);
            formalName = fullName;
            personalName = String.format("%s %s%s", familyname, generationname, givenname);;
        }
    }

    public String toRepr(){
        String repr = String.format(
            "nameform:\"%s\";forename:\"%s\";middlename:\"%s\";nicknames:%s;surname:\"%s\";familyname:\"%s\";generationname:\"%s\";givenname:\"%s\";honorific:\"%s\";ordination:\"%s\";abbreviatefirst:\"%s\";abbreviatemiddle:\"%s\";",
            nameform.toString().toLowerCase(), forename, Engine.arrayToReprList(nicknames), middlename, surname, familyname, generationname, givenname, honorific, ordination, String.valueOf(abbreviateFirst), String.valueOf(abbreviateMiddle)
        );
        return repr;
    }
    public void fromRepr(String repr){
        
    }
}
