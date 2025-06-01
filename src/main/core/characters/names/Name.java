/*
 * Name.java
 * Steven LaGoy
 * Created: 23 March 2025 at 1:27 AM
 * Modified: 31 May 2025
 */

package main.core.characters.names;

// IMPORTS ----------------------------------------------------------------------------------------

// Standard Library Imports
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Internal Imports
import core.JSONObject;
import main.core.Engine;
import main.core.Jsonic;
import main.core.Repr;

/** Models the Personal Name of a Character, with options for several Name Forms, Patterns, and Display Options.
 * <p>
 * Contains fields and methods for basic parts of a person's name, data about the way those parts are used, and various forms of displaying that name.
 */
public final class Name implements Repr<Name>, Jsonic<Name> {
    
    private static enum NamePart {
        HONORIFIC,
        GIVEN_NAME,
        MIDDLE_NAME,
        NICKNAME,
        FAMILY_NAME,
        MATERNAL_NAME,
        PATERNAL_NAME,
        ORDINAL,
        SUFFIXES
    }

    private static enum NameStyle {
        LEGAL,
        FORMAL,
        BIOGRAPHICAL,
        COMMON,
        INFORMAL,
        NICKNAME
    }

    private static final Map<NameStyle, List<NamePart>> namePatterns = Map.of(
        NameStyle.LEGAL,        List.of(NamePart.GIVEN_NAME, NamePart.MIDDLE_NAME, NamePart.FAMILY_NAME, NamePart.ORDINAL),
        NameStyle.FORMAL,       List.of(),
        NameStyle.BIOGRAPHICAL, List.of(NamePart.HONORIFIC, NamePart.GIVEN_NAME, NamePart.MIDDLE_NAME, NamePart.NICKNAME, NamePart.FAMILY_NAME, NamePart.ORDINAL, NamePart.SUFFIXES),
        NameStyle.COMMON,       List.of(NamePart.GIVEN_NAME, NamePart.MIDDLE_NAME, NamePart.FAMILY_NAME),
        NameStyle.INFORMAL,     List.of(NamePart.GIVEN_NAME, NamePart.FAMILY_NAME),
        NameStyle.NICKNAME,     List.of(NamePart.NICKNAME, NamePart.FAMILY_NAME)
    );

    public static enum NameForm {
        WESTERN,
        EASTERN,
        HISPANIC,
        NATIVE_AMERICAN;

        public static NameForm defaultForm = NameForm.WESTERN;
    }

    private static enum DisplayOption {
        WESTERN_FIRST,
        EASTERN_FIRST,
        NATIVE_FIRST,
        PATERNAL_FIRST,
        MATERNAL_FIRST,
        INCLUDE_ORDINAL,
        INCLUDE_NICKNAME;
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
            splitNames.addAll(Arrays.asList(name.split("[\s]")));
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

    // First or Given Name
    private String givenName;
    private boolean abbrFirst;
    private boolean atomicFirst;

    // Middle or Generational Name
    private String middleName;
    private boolean abbrMiddle;
    private boolean atomicMiddle;
    
    // Last or Family Name
    private String familyName;
    private String birthSurname;
    private String paternalName;
    private String maternalName;

    // Nicknames
    private List<String> nicknames;
    private boolean includeNickname;

    // Other Names
    private String religiousName;
    private String westernName;
    private String legalName;
    private String informalName;

    // Additional Name Parts
    private String honorific;
    private String ordinal;
    private List<String> suffixes;

    private Set<DisplayOption> displayOptions;

    public Name() {
        this.nameForm = NameForm.defaultForm;
        this.givenName = "";
        this.abbrFirst = false;
        this.atomicFirst = false;
        this.middleName = "";
        this.abbrMiddle = false;
        this.atomicMiddle = false;
        this.familyName = "";
        this.birthSurname = "";
        this.paternalName = "";
        this.maternalName = "";
        this.nicknames = new ArrayList<>();
        this.includeNickname = false;
        this.religiousName = "";
        this.legalName = "";
        this.informalName = "";
        this.honorific = "";
        this.ordinal = "";
        this.suffixes = new ArrayList<>();
        this.displayOptions = new HashSet<>();
    }

    public Name(String buildstring) {
        if (buildstring == null || buildstring.isBlank()) {
            throw new IllegalArgumentException("The given buildstring was null, and a Name object could not be created.");
        }
        fromRepr(buildstring);
    }

    public Name(JSONObject json) {
        if (json == null) {
            throw new IllegalArgumentException("The passed JSON Object was null, and a Character object could not be created.");
        }
        fromJson(json);
    }

    public Name(Name other) {
        this.nameForm = other.getNameForm();
        this.givenName = other.getGivenName();
        this.abbrFirst = other.isAbbrFirst();
        this.atomicFirst = other.isAtomicFirst();
        this.middleName = other.getMiddleName();
        this.abbrMiddle = other.isAbbrMiddle();
        this.atomicMiddle = other.isAtomicMiddle();
        this.familyName = other.getFamilyName();
        this.birthSurname = other.getBirthSurname();
        this.paternalName = other.getPaternalName();
        this.maternalName = other.getMaternalName();
        for (String nickname : other.getNicknames())
            this.nicknames.add(nickname);
        this.includeNickname = other.isIncludeNickname();
        this.religiousName = other.religiousName;
        this.legalName = other.legalName;
        this.informalName = other.informalName;
        this.honorific = other.honorific;
        this.ordinal = other.ordinal;
        for (String suffix : other.getSuffixes())
            this.suffixes.add(suffix);
        for (DisplayOption option : other.getDisplayOptions())
            this.displayOptions.add(option);
    }

    public Name(String firstName, String middleName, String lastName){
        this(NameForm.defaultForm, firstName, middleName, lastName);
    }

    public Name(NameForm nameform, String givenName, String middleName, String familyName){
        this();
        if (nameform == null) {
            Engine.log("INVALID NAMEFORM: ", String.format("Invalid nameform supplied: %s", nameform), new Exception());
            throw new IllegalArgumentException(String.format("Invalid nameform supplied: %s", nameform));
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
                this.paternalName = familyName.split("\s+")[0];
                this.maternalName = familyName.split("\s+")[1];
                break;
            case NATIVE_AMERICAN :
                this.givenName = givenName;
                this.familyName = familyName;
                break;
        }
        this.nicknames = new ArrayList<>();
        this.suffixes = new ArrayList<>();
    }

    public Name(
        NameForm nameForm,
        String givenName,
        boolean abbrFirst,
        boolean atomicFirst,
        String middleName,
        boolean abbrMiddle,
        boolean atomicMiddle,
        String familyName,
        String birthSurname,
        String paternalName,
        String maternalName,
        List<String> nicknames,
        boolean includeNickname,
        String religiousName,
        String legalName,
        String informalName,
        String honorific,
        String ordinal,
        List<String> suffixes
    ) {
        this.nameForm = nameForm;
        this.givenName = givenName;
        this.abbrFirst = abbrFirst;
        this.atomicFirst = atomicFirst;
        this.middleName = middleName;
        this.abbrMiddle = abbrMiddle;
        this.atomicMiddle = atomicMiddle;
        this.familyName = familyName;
        this.birthSurname = birthSurname;
        this.paternalName = paternalName;
        this.maternalName = maternalName;
        this.nicknames = nicknames;
        this.includeNickname = includeNickname;
        this.religiousName = religiousName;
        this.legalName = legalName;
        this.informalName = informalName;
        this.honorific = honorific;
        this.ordinal = ordinal;
        this.suffixes = suffixes;
    }

    public String abbreviate(String name) {
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
                String[] names = name.split("[\s]");
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
        if (suffixes == null || suffixes.isEmpty()) {
            return "";
        }
        return ", " + String.join(", ", suffixes);
    }

    @Override
    public String toString() {
        // Check if specific display options are set
        boolean showHonorific = false, useInitials = false, nicknameAsGiven = false, useFormal = false, useInformal = false;
        if (displayOptions != null) {
            showHonorific = displayOptions.contains(DisplayOption.INCL_HONORIFICS);
            useInitials = displayOptions.contains(DisplayOption.USE_INITIALS);
            nicknameAsGiven = displayOptions.contains(DisplayOption.NICKNAME_AS_GIVEN);
            useFormal = displayOptions.contains(DisplayOption.FORMAL);
            useInformal = displayOptions.contains(DisplayOption.INFORMAL);
        }
        
        // If no display options are set, use default behavior
        if (displayOptions != null && displayOptions.isEmpty()) {
            showHonorific = true;
            useInitials = abbrFirst || abbrMiddle;
            nicknameAsGiven = includeNickname;
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
            String fullChineseName = familyName + " " + (middleName != null ? middleName + givenName.toLowerCase() : givenName);
            nameString.append(fullChineseName);
        } else {
            // Western and other name forms
            String first;
            if (nicknameAsGiven && !nicknames.isEmpty()) {
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
            
            // Add nickname if not used as given name
            if (includeNickname && !nicknames.isEmpty() && !nicknameAsGiven) {
                nameString.append(" \"").append(nicknames.get(0)).append("\"");
            }

            nameString.append(" ").append(familyName);
            
            // Add birth surname if applicable
            if (birthSurname != null && !birthSurname.isEmpty() && useFormal) {
                nameString.append(" (née ").append(birthSurname).append(")");
            }
        }
        
        // Add ordinal if present
        if (ordinal != null && !ordinal.isEmpty()) {
            nameString.append(" ").append(ordinal);
        }
        
        // Add professional suffixes
        nameString.append(formatProfessionalSuffixes());
        
        return nameString.toString().trim();
    }

    public String getFullName() {
        StringBuilder nameString = new StringBuilder();
        if (nameForm == NameForm.WESTERN) {
            if (givenName != null) nameString.append(givenName).append(" ");
            if (middleName != null) nameString.append(middleName).append(" ");
            if (familyName != null) nameString.append(familyName);
        }
        else if (nameForm == NameForm.EASTERN) {
            if (familyName != null) nameString.append(familyName).append(" ");
            if (middleName != null) nameString.append(middleName);
            if (givenName != null) nameString.append(givenName);
        }
        else if (nameForm == NameForm.HISPANIC) {
            if (givenName != null) nameString.append(givenName).append(" ");
            if (middleName != null) nameString.append(middleName).append(" ");
            if (paternalName != null) nameString.append(paternalName).append(" ");
            if (maternalName != null) nameString.append(maternalName);
        }

        return nameString.toString().trim();
    }

    public NameForm getNameForm() {
        return nameForm;
    }

    public String getGivenName() {
        return givenName;
    }
    public void setGivenName(String name) {
        this.givenName = name;
    }
    public boolean isAbbrFirst() {
        return abbrFirst;
    }
    public void setAbbrFirst(boolean abbr) {
        this.abbrFirst = abbr;
    }
    public boolean isAtomicFirst() {
        return atomicFirst;
    }
    public void setAtomicFirst(boolean atomic) {
        this.atomicFirst = atomic;
    }
    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String name) {
        this.middleName = name;
    }
    public boolean isAbbrMiddle() {
        return abbrMiddle;
    }
    public void setAbbrMiddle(boolean abbr) {
        this.abbrMiddle = abbr;
    }
    public boolean isAtomicMiddle() {
        return atomicMiddle;
    }
    public void setAtomicMiddle(boolean atomic) {
        this.atomicMiddle = atomic;
    }
    public String getFamilyName() {
        return familyName;
    }
    public void setFamilyName(String name) {
        this.familyName = name;
    }
    public String getBirthSurname() {
        return this.birthSurname;
    }
    @SuppressWarnings("unused")
    private void setBirthSurname(String name) {
        this.birthSurname = name;
    }
    public String getPaternalName() {
        return this.paternalName;
    }
    public void setPaternalName(String name) {
        this.paternalName = name;
    }
    public String getMaternalName() {
        return this.maternalName;
    }
    public void setMaternalName(String name) {
        this.maternalName = name;
    }
    public List<String> getNicknames() {
        return this.nicknames;
    }
    public void addNickname(String name) {
        this.nicknames.add(name);
    }
    public void removeNickname(String name) {
        this.nicknames.remove(name);
    }
    public boolean isIncludeNickname() {
        return includeNickname;
    }
    public void setIncludeNickname(boolean include) {
        this.includeNickname = include;
    }
    public String getOrdinal() {
        return this.ordinal;
    }
    public void setOrdinal(String ordinal) {
        this.ordinal = ordinal;
    }
    private Set<DisplayOption> getDisplayOptions() {
        return displayOptions;
    }
    private List<String> getSuffixes() {
        return suffixes;
    }

    public String toRepr(){
        String[] nicknamesStrings = new String[nicknames.size()];
        for (int i = 0; i < nicknames.size(); i++) {
            nicknamesStrings[i] = nicknames.get(i);
        }
        String nicknamesRepr = Repr.arrayToReprList(nicknamesStrings);

        String[] suffixesStrings = new String[suffixes.size()];
        for (int i = 0; i < suffixes.size(); i++) {
            suffixesStrings[i] = suffixes.get(i);
        }
        String suffixesRepr = Repr.arrayToReprList(suffixesStrings);

        String repr = String.format("%s:[nameForm=\"%s\";givenName=\"%s\";abbrFirst=%b;atomicFirst=%b;middleName=\"%s\";abbrMiddle=%b;atomicMiddle=%b;familyName=\"%s\";birthSurname=\"%s\";paternalName=\"%s\";maternalName=\"%s\";nicknames=[%s];includeNickname=%b;honorific=\"%s\";ordinal=\"%s\";suffixes=[%s];];",
            this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1],
            this.nameForm.toString(),
            givenName,
            abbrFirst,
            atomicFirst,
            middleName,
            abbrMiddle,
            atomicMiddle,
            familyName,
            birthSurname,
            paternalName,
            maternalName,
            nicknamesRepr,
            includeNickname,
            honorific,
            ordinal,
            suffixesRepr
        );
        return repr;
    }
    public Name fromRepr(String repr){
        return this;
    }
    public Name fromJson(JSONObject nameJson) {
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 7;
        hash = prime * hash + (givenName == null ? 0 : givenName.hashCode());
        hash = prime * hash + (middleName == null ? 0 : middleName.hashCode());
        hash = prime * hash + (familyName == null ? 0 : familyName.hashCode());
        return hash;
    }
    @Override
    public JSONObject toJson() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJson'");
    }


    /** Returns the Legal Name of the Character, which is how their name might appear on a government form.
     * <p>
     * Western: Given [Middle(s)] Family
     * @return String Legal Name
     */
    public String getLegalName() {
        return switch(nameForm) {
            case WESTERN -> {
                if (displayOptions.contains(DisplayOption.INCLUDE_ORDINAL))
                    yield String.format("%s %s %s %s", givenName, middleName, familyName, ordinal).replace("  ", " ");
                else
                    yield String.format("%s %s %s", givenName, middleName, familyName).replace("  ", " ");
            }
            case EASTERN -> {
                if (displayOptions.contains(DisplayOption.WESTERN_FIRST))
                    yield String.format("%s %s%s %s", westernName, middleName, givenName, familyName).replace("  ", " ");
                if (displayOptions.contains(DisplayOption.EASTERN_FIRST))
                    yield String.format("%s%s %s %s", middleName, givenName, westernName, familyName).replace("  ", " ");
                yield "IMPROPER DISPLAY OPTIONS";
            }
            case HISPANIC -> {
                if (displayOptions.contains(DisplayOption.MATERNAL_FIRST))
                    yield String.format("%s %s %s", givenName, maternalName, paternalName).replace("  ", " ");
                if (displayOptions.contains(DisplayOption.PATERNAL_FIRST))
                    yield String.format("%s %s %s", givenName, maternalName, paternalName).replace("  ", " ");
                yield "IMPROPER DISPLAY OPTIONS";
            }
            case NATIVE_AMERICAN -> {
                if (displayOptions.contains(DisplayOption.WESTERN_FIRST))
                    yield String.format("%s %s %s", westernName, givenName, familyName).replace("  ", " ");
                if (displayOptions.contains(DisplayOption.NATIVE_FIRST))
                    yield String.format("%s %s %s", givenName, westernName, familyName).replace("  ", " ");
                yield "IMPROPER DISPLAY OPTIONS";
            }
        };
    }

    /** May include Honorifics and Suffixes */
    public String getFormalName() {
        if (suffixes.size() > 0)
            return String.format("%s %s, %s", honorific, getLegalName(), String.join(", ", suffixes)).replace("  ", " ");
        else
            return String.format("%s %s", honorific, getLegalName()).replace("  ", " ");
    }

    /** May include a nickname in quotations between given/middle name(s) and family name(s). */
    public String getBiographicalName() {
        return switch(nameForm) {
            case WESTERN -> {
                if (displayOptions.contains(DisplayOption.INCLUDE_NICKNAME))
                    yield String.format("%s %s %s \"%s\" %s %s, %s",
                            honorific,
                            givenName,
                            middleName,
                            nicknames.get(0),
                            familyName,
                            ordinal,
                            String.join(", ", suffixes)
                        ).replace("  ", " ");
                else
                    yield String.format("%s %s %s %s %s, %s",
                            honorific,
                            givenName,
                            middleName,
                            familyName,
                            ordinal,
                            String.join(", ", suffixes)
                        ).replace("  ", " ");
            }
            case EASTERN -> {
                yield "";
            }
            case HISPANIC -> {
                yield "";
            }
            case NATIVE_AMERICAN -> {
                yield "";
            }
        };
        
    }


}