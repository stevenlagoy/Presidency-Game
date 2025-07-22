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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Internal Imports
import core.JSONObject;
import main.core.IOUtil;
import main.core.Jsonic;
import main.core.Logger;
import main.core.Repr;

/** Models the Personal Name of a Character, with options for several Name Forms, Patterns, and Display Options.
 * <p>
 * Contains fields and methods for basic parts of a person's name, data about the way those parts are used, and various forms of displaying that name.
 */
public final class Name implements Repr<Name>, Jsonic<Name> {
    
    /** Enum for parts of a name. */
    private static enum NamePart {
        HONORIFIC,
        GIVEN_NAME,
        PREFERRED_NAME,
        PREFERRED_FIRST,
        MIDDLE_NAME,
        PREFERRED_MIDDLE,
        GENERATION,
        PREFERRED_GENERATION,
        WESTERN_NAME,
        WESTERN_NAME_QUOTED,
        NICKNAME,
        NICKNAME_QUOTED,
        FAMILY_NAME,
        APELLIDO_1, // Usually Paternal Surname
        APELLIDO_2, // Usually Maternal Surname
        ORDINAL,
        SUFFIXES;
    }

    /** Enum for styles of writing a name, and patterns for that style. */
    private static enum NameStyle {
        LEGAL (Map.of(
            NameForm.WESTERN, List.of(NamePart.GIVEN_NAME, NamePart.MIDDLE_NAME, NamePart.FAMILY_NAME, NamePart.ORDINAL),
            NameForm.EASTERN, List.of(NamePart.GENERATION, NamePart.GIVEN_NAME, NamePart.FAMILY_NAME),
            NameForm.HISPANIC, List.of(NamePart.GIVEN_NAME, NamePart.APELLIDO_1, NamePart.APELLIDO_2),
            NameForm.NATIVE_AMERICAN, List.of(NamePart.GIVEN_NAME, NamePart.FAMILY_NAME)
            )),
        FORMAL (Map.of(
            NameForm.WESTERN, List.of(NamePart.HONORIFIC, NamePart.GIVEN_NAME, NamePart.MIDDLE_NAME, NamePart.FAMILY_NAME, NamePart.ORDINAL),
            NameForm.EASTERN, List.of(NamePart.HONORIFIC, NamePart.FAMILY_NAME, NamePart.GENERATION, NamePart.GIVEN_NAME),
            NameForm.HISPANIC, List.of(NamePart.HONORIFIC, NamePart.GIVEN_NAME, NamePart.APELLIDO_1, NamePart.APELLIDO_2, NamePart.ORDINAL),
            NameForm.NATIVE_AMERICAN, List.of(NamePart.HONORIFIC, NamePart.GIVEN_NAME, NamePart.FAMILY_NAME)
            )),
        BIOGRAPHICAL (Map.of(
            NameForm.WESTERN, List.of(NamePart.HONORIFIC, NamePart.GIVEN_NAME, NamePart.MIDDLE_NAME, NamePart.NICKNAME_QUOTED, NamePart.FAMILY_NAME, NamePart.ORDINAL, NamePart.SUFFIXES),
            NameForm.EASTERN, List.of(NamePart.HONORIFIC, NamePart.WESTERN_NAME, NamePart.FAMILY_NAME, NamePart.GENERATION, NamePart.GIVEN_NAME, NamePart.SUFFIXES),
            NameForm.HISPANIC, List.of(NamePart.HONORIFIC, NamePart.GIVEN_NAME, NamePart.NICKNAME_QUOTED, NamePart.APELLIDO_1, NamePart.APELLIDO_2, NamePart.SUFFIXES),
            NameForm.NATIVE_AMERICAN, List.of(NamePart.HONORIFIC, NamePart.GIVEN_NAME, NamePart.FAMILY_NAME, NamePart.SUFFIXES)
            )),
        COMMON (Map.of(
            NameForm.WESTERN, List.of(NamePart.PREFERRED_FIRST, NamePart.PREFERRED_MIDDLE, NamePart.FAMILY_NAME),
            NameForm.EASTERN, List.of(NamePart.FAMILY_NAME, NamePart.PREFERRED_GENERATION, NamePart.GIVEN_NAME),
            NameForm.HISPANIC, List.of(NamePart.PREFERRED_NAME, NamePart.APELLIDO_1, NamePart.APELLIDO_2),
            NameForm.NATIVE_AMERICAN, List.of(NamePart.PREFERRED_FIRST, NamePart.FAMILY_NAME)
            )),
        INFORMAL (Map.of(
            NameForm.WESTERN, List.of(NamePart.PREFERRED_NAME, NamePart.FAMILY_NAME),
            NameForm.EASTERN, List.of(NamePart.FAMILY_NAME, NamePart.PREFERRED_GENERATION, NamePart.GIVEN_NAME),
            NameForm.HISPANIC, List.of(NamePart.PREFERRED_NAME, NamePart.APELLIDO_1, NamePart.APELLIDO_2),
            NameForm.NATIVE_AMERICAN, List.of(NamePart.PREFERRED_FIRST, NamePart.FAMILY_NAME)
            ));

        public final Map<NameForm, List<NamePart>> pattern;
        private NameStyle(Map<NameForm, List<NamePart>> pattern) { this.pattern = pattern; }
    }

    /** Enum for broader forms of a name, from culture or religion etc. */
    public static enum NameForm {
        EASTERN,
        HISPANIC,
        NATIVE_AMERICAN,
        WESTERN;

        public static NameForm defaultForm = NameForm.WESTERN;
    }

    /** Options for displaying a name. */
    public static enum DisplayOption {
        ABBREVIATE_FIRST,
        ABBREVIATE_MIDDLE,
        PREFER_MIDDLE,
        WESTERN_FIRST,
        EASTERN_FIRST,
        LATENT_GENERATION,
        NATIVE_FIRST,
        PATERNAL_FIRST,
        MATERNAL_FIRST,
        INCLUDE_WESTERN,
        INCLUDE_NICKNAME,
        INCLUDE_ORDINAL,
        INCLUDE_HONORIFIC,
        INCLUDE_SUFFIXES;
    }

    // INSTANCE VARIABLES -------------------------------------------------------------------------

    private NameForm nameForm;

    /** First or Given Name */
    private String givenName;
    /** Middle or Generation Name */
    private String middleName;    
    /** Last or Family Name */
    private String familyName;
    private String birthSurname;
    private String paternalName;
    private String maternalName;
    /** Nicknames */
    private String nickname;
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

    // CONSTRUCTORS -------------------------------------------------------------------------------

    /**
     * Creates a Name with all fields default. The nameform will be {@code NameForm.defaultForm}
     * @see #Name(NameForm, String, boolean, boolean, String, boolean, boolean, String, String, String, String, List, boolean, String, String, String, String, String, List)
     */
    public Name() {
        this.nameForm = NameForm.defaultForm;
        this.givenName = "";
        this.middleName = "";
        this.familyName = "";
        this.birthSurname = "";
        this.paternalName = "";
        this.maternalName = "";
        this.nickname = "";
        this.religiousName = "";
        this.westernName = "";
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
        this();
        this.nameForm = other.getNameForm();
        this.givenName = other.getGivenName();
        this.middleName = other.getMiddleName();
        this.familyName = other.getFamilyName();
        this.birthSurname = other.getBirthSurname();
        this.paternalName = other.getPaternalName();
        this.maternalName = other.getMaternalName();
        this.nickname = other.getNickname();
        this.religiousName = other.religiousName;
        this.westernName = other.getWesternName();
        this.legalName = other.legalName;
        this.informalName = other.informalName;
        this.honorific = other.honorific;
        this.ordinal = other.ordinal;
        for (String suffix : other.getSuffixes())
            this.suffixes.add(suffix);
        for (DisplayOption option : other.getDisplayOptions())
            this.displayOptions.add(option);
    }

    public Name(String firstName, String middleName, String lastName) {
        this(NameForm.defaultForm, firstName, middleName, lastName);
    }

    public Name(NameForm nameform, String givenName, String middleName, String familyName) {
        this();
        if (nameform == null) {
            Logger.log("INVALID NAMEFORM: ", String.format("Invalid nameform supplied: %s", nameform), new Exception());
            throw new IllegalArgumentException(String.format("Invalid nameform supplied: %s", nameform));
        }
        this.nameForm = nameform;
        switch (nameform) {
            case WESTERN :
                this.givenName = givenName != null ? givenName : "";
                this.middleName = middleName != null ? middleName : "";
                this.familyName = familyName != null ? familyName : "";
                this.birthSurname = familyName != null ? familyName : "";
                break;
            case EASTERN :
                this.familyName = familyName != null ? familyName : "";
                this.middleName = middleName != null ? middleName : "";
                this.givenName = givenName != null ? givenName : "";
                break;
            case HISPANIC :
                this.givenName = givenName != null ? givenName : "";
                this.middleName = middleName != null ? middleName : "";
                this.paternalName = familyName.split("\s+")[0];
                this.maternalName = familyName.split("\s+")[1];
                break;
            case NATIVE_AMERICAN :
                this.givenName = givenName != null ? givenName : "";
                this.familyName = familyName != null ? familyName : "";
                break;
        }
        this.suffixes = new ArrayList<>();
    }

    public Name(
        NameForm nameForm,
        String givenName,
        String middleName,
        String familyName,
        String birthSurname,
        String paternalName,
        String maternalName,
        String nickname,
        String religiousName,
        String legalName,
        String informalName,
        String honorific,
        String ordinal,
        List<String> suffixes,
        Set<DisplayOption> displayOptions
    ) {
        this.nameForm  = nameForm;
        this.givenName = givenName != null ? givenName : "";
        this.middleName = middleName != null ? middleName : "";
        this.familyName = familyName != null ? familyName : "";
        this.birthSurname = birthSurname != null ? birthSurname : "";
        this.paternalName = paternalName != null ? paternalName : "";
        this.maternalName = maternalName != null ? maternalName : "";
        this.nickname = nickname != null ? nickname : "";
        this.religiousName = religiousName != null ? religiousName : "";
        this.honorific = honorific;
        this.ordinal = ordinal;
        this.suffixes = suffixes;
        this.displayOptions = displayOptions;
    }

    public NameForm getNameForm() {
        return nameForm;
    }
    public void setNameForm(NameForm nameForm) {
        this.nameForm = nameForm;
    }

    public String getGivenName() {
        if (nameForm.equals(NameForm.EASTERN) && !middleName.isEmpty()) return givenName.toLowerCase();
        return givenName;
    }
    public void setGivenName(String name) {
        this.givenName = name;
    }
    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String name) {
        this.middleName = name;
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
    public void setBirthSurname(String name) {
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
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
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
    public String getWesternName() {
        return westernName;
    }
    public void setWesternName(String westernName) {
        this.westernName = westernName;
    }
    public void addDisplayOption(DisplayOption option) {
        this.displayOptions.add(option);
    }

    public String toRepr() {
        String[] suffixesStrings = new String[suffixes.size()];
        for (int i = 0; i < suffixes.size(); i++) {
            suffixesStrings[i] = suffixes.get(i);
        }
        String suffixesRepr = Repr.arrayToReprList(suffixesStrings);

        String repr = String.format("%s:[nameForm=\"%s\";givenName=\"%s\";middleName=\"%s\";familyName=\"%s\";paternalName=\"%s\";maternalName=\"%s\";nickname=\"%s\";honorific=\"%s\";ordinal=\"%s\";suffixes=[%s];];",
            this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1],
            this.nameForm.toString(),
            givenName,
            middleName,
            familyName,
            paternalName,
            maternalName,
            nickname,
            honorific,
            ordinal,
            suffixesRepr
        );
        return repr;
    }
    public Name fromRepr(String repr) {
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

    public String getNameInStyle(NameStyle style) {
        StringBuilder sb = new StringBuilder();
        for (NamePart part : style.pattern.get(nameForm)) {
            sb.append(getNamePart(part));
        }
        String name = sb.toString().replaceAll("\\s+", " ").trim();
        if (name.split("\\s+").length < 2) {
            for (NamePart part : style.pattern.get(nameForm)) {
                IOUtil.stdout.println(getNamePart(part));
            }
        }
        return name;
    }

    /**
     * Get this name in the Legal Style: Given Middle Family Ordinal
     * <p>
     * Example: Joseph Robinette Biden Jr.
     * @return Name in Legal Style
     */
    public String getLegalName() {
        return getNameInStyle(NameStyle.LEGAL);
    }
    /**
     * Get this name in the Formal Style: Honorific Given Middle Family Ordinal
     * <p>
     * Example: Rev. Martin Luther King Jr.
     */
    public String getFormalName() {
        return getNameInStyle(NameStyle.FORMAL);
    }
    /**
     * Get this name in the Biographical Style: Honorific Given Middle Nickname? Family Ordinal
     * <p>
     * Example: Pres. James Earl "Jimmy" Carter Jr.
     * @return Name in Biographical Style
     */
    public String getBiographicalName() {
        return getNameInStyle(NameStyle.BIOGRAPHICAL);
    }
    /**
     * Get this name in the Common Style: First Middle Family
     * <p>
     * Example: George W. Bush
     * @return Name in Common Style
     */
    public String getCommonName() {
        return getNameInStyle(NameStyle.COMMON);
    }
    /**
     * Get this name in the Informal Style: First/Nickname Family
     * <p>
     * Example: Bill Clinton
     * @return Name in Informal Style
     */
    public String getInformalName() {
        return getNameInStyle(NameStyle.INFORMAL);
    }

    public String getNamePart(NamePart part) {
        return switch(part) {
            case HONORIFIC -> getHonorific() + " ";
            case GIVEN_NAME -> getGivenName() + " ";
            case PREFERRED_NAME -> getPreferredName() + " ";
            case PREFERRED_FIRST -> getPreferredFirst() + " ";
            case MIDDLE_NAME -> getMiddleName() + " ";
            case PREFERRED_MIDDLE -> getPreferredMiddle() + " ";
            case NICKNAME -> getNickname() + " ";
            case NICKNAME_QUOTED -> ("\"" + getNickname() + "\"").replace("\"\"", "").trim() + " ";
            case FAMILY_NAME -> getFamilyName() + " ";
            case APELLIDO_1 -> {
                if (displayOptions.contains(DisplayOption.MATERNAL_FIRST))
                    yield getMaternalName() + " ";
                else if (displayOptions.contains(DisplayOption.PATERNAL_FIRST))
                    yield getPaternalName() + " ";
                else yield "";
            }
            case APELLIDO_2 -> {
                if (displayOptions.contains(DisplayOption.MATERNAL_FIRST))
                    yield getPaternalName() + " ";
                else if (displayOptions.contains(DisplayOption.PATERNAL_FIRST))
                    yield getMaternalName() + " ";
                else yield "";
            }
            case ORDINAL -> getOrdinal() + " ";
            case SUFFIXES -> getFormattedSuffixes() + " ";
            case GENERATION -> getMiddleName(); // No space following generation name
            case PREFERRED_GENERATION -> {
                if (displayOptions.contains(DisplayOption.LATENT_GENERATION))
                    yield "";
                else yield getMiddleName();
            }
            case WESTERN_NAME -> getWesternName() + " ";
            case WESTERN_NAME_QUOTED -> ("\"" + getWesternName() + "\"").replace("\"\"", "").trim() + " ";
        };
    }
    
    private String getPreferredName() {
        if (displayOptions.contains(DisplayOption.INCLUDE_NICKNAME)) {
            return nickname;
        }
        if (displayOptions.contains(DisplayOption.PREFER_MIDDLE)) {
            return middleName;
        }
        else {
            return givenName;
        }
    }

    private String getPreferredFirst() {
        return displayOptions.contains(DisplayOption.ABBREVIATE_FIRST) ? abbreviate(givenName) : givenName;
    }

    private String getPreferredMiddle() {
        return displayOptions.contains(DisplayOption.ABBREVIATE_MIDDLE) ? abbreviate(middleName) : middleName;
    }
    
    private String getHonorific() {
        return honorific;
    }

    private String abbreviate(String name) {
        char initial;
        name = name.strip();
        for (int i = 0; i < name.length(); i++) {
            initial = name.charAt(i);
            if (Character.isAlphabetic(initial))
                return String.valueOf(initial);
        }
        return "";
    }

    private String getFormattedSuffixes() {
        StringBuilder sb = new StringBuilder();
        for (String suffix : suffixes) {
            sb.append(", ").append(suffix);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getBiographicalName();
    }
}
