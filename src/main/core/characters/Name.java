package main.core.characters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.core.Engine;
import main.core.Repr;

public class Name implements Repr
{
    public static enum NameForm {
        WESTERN,
        EASTERN,
        HISPANIC,
        NATIVE_AMERICAN
    }

    private static enum DisplayOption {
        INCL_HONORIFICS, USE_INITIALS, NICKNAME_AS_GIVEN, FORMAL, INFORMAL
    }

    /**
     * Returns a number of initials from the passed name(s).
     * @param name The name(s) to be initialized. Each will be split by whitespace before being initialized.
     * @param limit The maximum number of initial letters to be returned.
     * @return At most limit number of capitalized letters as the initials of the name(s).
     */
    public static String initialize(int limit, String... names) {
        List<String> splitNames = new ArrayList<String>();
        for (String name : names){
            splitNames.addAll(Arrays.asList(name.split("[\\s]")));
        }
        String res = "";
        for (int i = 0; i < splitNames.size() && limit == -1 ? true : i < limit; i++) {
            for(int j = 0; j < splitNames.get(i).length(); j++){
                if (java.lang.Character.isAlphabetic(names[i].charAt(j))){
                    res += java.lang.Character.toUpperCase(names[i].charAt(j));
                    break;
                }
            }
        }
        return res;
    }
    /**
     * Returns the first letter initial of the passed name(s).
     * @param names The name(s) to be initialized.
     * @return The first valid letter of the name(s), capitalized.
     */
    public static String initialize(String... names){
        return initialize(-1, names);
    }

    private NameForm nameForm;

    // Western Name Elements
    private String givenName;
    private boolean abbrFirst;
    private boolean atomicFirst;
    private String middleName;
    private boolean abbrMiddle;
    private boolean atomicMiddle;
    private String familyName;
    private String birthSurname;
    private String paternalName;
    private String maternalName;

    // Nicknames
    private List<String> nicknames;
    private boolean includeNickname;

    // Other Names
    private String religiousName;
    private String legalName;
    private String informalName;

    // Additional Name Parts
    private String honorific;
    private String ordinal;
    private List<String> suffixes;

    private List<DisplayOption> displayOptions;

    /**
     * Create a Name object.
     * @param nameform "WESTERN" or "EASTERN"
     * @param name1 If Western, first name. If Eastern, family name. If Hispanic, given name.
     * @param name2 If Western, middle name. If Eastern, generation name. If Hispanic, paternal name.
     * @param name3 If Western, last name. If Eastern, given name. If Hispanic, maternal name.
     */
    public Name(String nameform, String givenName, String middleName, String familyName){
        this(NameForm.valueOf(nameform.toUpperCase()), givenName, middleName, familyName);
    }

    public Name(NameForm nameform, String givenName, String middleName, String familyName){
        if (nameform == null) {
            Engine.log("NAME INVALID NAMEFORM: ", String.format("Invalid nameform supplied: %s", nameform), new Exception());
            return;
        }
        this.nameForm = nameform;
        switch (nameform) {
            case WESTERN :
                this.givenName = givenName;
                this.middleName = middleName;
                this.familyName = familyName;
                this.birthSurname = familyName;
                break;
            case EASTERN :
                this.familyName = familyName;
                this.middleName = middleName;
                this.givenName = givenName;
                break;
            case HISPANIC :
                this.givenName = givenName;
                this.middleName = middleName;
                this.paternalName = familyName.split("\\s+")[0];
                this.maternalName = familyName.split("\\s+")[1];
                break;
            case NATIVE_AMERICAN :
                this.givenName = givenName;
                this.familyName = familyName;
                break;
        }
    }

    public Name(String firstName, String middleName, String lastName){
        this(NameForm.WESTERN, firstName, middleName, lastName);
    }

    public Name(String firstName, boolean abbrFirst, String middleName, boolean abbrMiddle, String lastName) {
        this(firstName, middleName, lastName);
        this.abbrFirst = abbrFirst;
        this.abbrMiddle = abbrMiddle;
    }

    private String abbreviate(String name) {
        return abbreviate(name, false);
    }

    private String abbreviate(String name, boolean atomic) {
        if (name != null && !name.isEmpty()) {
            if (atomic) {
                // For names like JeanLuc, return J. instead of J.L.
                for (int i = 0; i < name.length(); i++) {
                    if (java.lang.Character.isAlphabetic(name.charAt(i)))
                        return String.valueOf(name.charAt(i)) + ".";
                }
            }
            else {
                String res = "";
                String[] names = name.split("[\\s]");
                for (String n : names) {
                    for (int i = 0; i < n.length(); i++) {
                        if (java.lang.Character.isAlphabetic(n.charAt(i))) {
                            res += String.valueOf(n.charAt(0)) + ".";
                            break;
                        }
                    }
                }
                return res;
            }
        }
        return "";
    }

    private String formatProfessionalSuffixes() {
        if (suffixes.isEmpty()) {
            return "";
        }
        return ", " + String.join(", ", suffixes);
    }

    @Override
    public String toString() {
        // Check if specific display options are set
        boolean showHonorific = displayOptions.contains(DisplayOption.INCL_HONORIFICS);
        boolean useInitials = displayOptions.contains(DisplayOption.USE_INITIALS);
        boolean useNicknameAsGiven = displayOptions.contains(DisplayOption.NICKNAME_AS_GIVEN);
        boolean useFormal = displayOptions.contains(DisplayOption.FORMAL);
        boolean useInformal = displayOptions.contains(DisplayOption.INFORMAL);
        
        // If no display options are set, use default behavior
        if (displayOptions.isEmpty()) {
            showHonorific = true;
            useInitials = abbrFirst || abbrMiddle;
            useNicknameAsGiven = includeNickname;
        }
        
        StringBuilder nameString = new StringBuilder();
        
        // Handle religious name format
        if (religiousName != null) {
            if (useFormal) {
                if (showHonorific && honorific != null && !honorific.isEmpty()) {
                    nameString.append(honorific).append(" ");
                }
                nameString.append(religiousName);
                if (legalName != null && !legalName.isEmpty()) {
                    nameString.append(" (").append(legalName).append(")");
                }
            } else {
                nameString.append(religiousName);
            }
            nameString.append(formatProfessionalSuffixes());
            return nameString.toString().trim();
        }
        
        // Handle informal name format
        if (useInformal && informalName != null && !informalName.isEmpty()) {
            nameString.append(informalName);
            return nameString.toString().trim();
        }
        
        // Handle honorific
        if (showHonorific && honorific != null && !honorific.isEmpty()) {
            nameString.append(honorific).append(" ");
        }
        
        // Handle different name forms
        if (nameForm == NameForm.NATIVE_AMERICAN) {
            nameString.append(givenName);
        }
        else if (nameForm == NameForm.EASTERN) {
            String fullChineseName = familyName + " " + (middleName != null ? middleName + " " : "") + givenName;
            if (givenName != null) {
                String first;
                if (useNicknameAsGiven && !nicknames.isEmpty()) {
                    first = nicknames.get(0);
                } else {
                    first = useInitials || abbrFirst ? abbreviate(givenName, atomicFirst) : givenName;
                }
                
                String middle = (middleName != null && !middleName.isEmpty()) ? 
                    (useInitials || abbrMiddle ? abbreviate(middleName, atomicMiddle) : middleName) : "";
                
                String westernName = first + (middle.isEmpty() ? "" : " " + middle) + " " + familyName;
                nameString.append(westernName).append(" / ").append(fullChineseName);
            } else {
                nameString.append(fullChineseName);
            }
        } else {
            // Western and other name forms
            String first;
            if (useNicknameAsGiven && !nicknames.isEmpty()) {
                first = nicknames.get(0);
            } else {
                first = useInitials || abbrFirst ? abbreviate(givenName, atomicFirst) : givenName;
            }
            
            String middle = (middleName != null && !middleName.isEmpty()) ? 
                (useInitials || abbrMiddle ? abbreviate(middleName, atomicMiddle) : middleName) : "";
            
            nameString.append(first);
            if (!middle.isEmpty()) {
                nameString.append(" ").append(middle);
            }
            nameString.append(" ").append(familyName);
            
            // Add birth surname if applicable
            if (birthSurname != null && !birthSurname.isEmpty() && useFormal) {
                nameString.append(" (née ").append(birthSurname).append(")");
            }
        }
        
        // Add nickname if not used as given name
        if (includeNickname && !nicknames.isEmpty() && !useNicknameAsGiven) {
            nameString.append(" \"").append(nicknames.get(0)).append("\"");
        }
        
        // Add ordinal if present
        if (ordinal != null && !ordinal.isEmpty()) {
            nameString.append(" ").append(ordinal);
        }
        
        // Add professional suffixes
        nameString.append(formatProfessionalSuffixes());
        
        return nameString.toString().trim();
    }

    public String toRepr(){
        String repr = String.format(
            "nameform:\"%s\";forename:\"%s\";middlename:\"%s\";nicknames:%s;surname:\"%s\";familyname:\"%s\";generationname:\"%s\";givenname:\"%s\";honorific:\"%s\";ordination:\"%s\";abbreviatefirst:\"%s\";abbreviatemiddle:\"%s\";",
            nameForm.toString().toLowerCase(), givenName, Engine.arrayToReprList(nicknames), middleName, familyName, familyName, middleName, givenName, honorific, ordinal, String.valueOf(abbrFirst), String.valueOf(abbrMiddle)
        );
        return repr;
    }
    public void fromRepr(String repr){
        
    }
}