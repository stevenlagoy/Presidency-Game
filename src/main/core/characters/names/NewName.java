package main.core.characters.names;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class NewName {
    public enum NameForm {
        WESTERN, CHINESE, HISPANIC, HYPHENATED, MARRIED, BIRTH_SURNAME, NATIVE_AMERICAN, RELIGIOUS
    }
    
    public enum DisplayOption {
        INCLUDE_HONORIFICS, USE_INITIALS, NICKNAME_AS_GIVEN_NAME, FORMAL, INFORMAL
    }
    
    private String firstName;
    private String middleName;
    private String lastName;
    private boolean abbreviateFirst;
    private boolean abbreviateMiddle;
    private boolean nonSplittableFirst; // For names like Maryann, JeanLuc that shouldn't be split
    
    private String familyName;
    private String generationalName;
    private String givenName;
    private boolean isChineseStyle;
    private boolean hasWesternName;
    
    private String birthSurname;
    private String spouseSurname;
    private NameForm nameForm;
    
    private String nativeAmericanName;
    
    private String honorific;
    private String ordinal;
    private List<String> professionalSuffixes; // For Esq., Ph.D., M.D., J.D., etc.
    
    private List<String> nicknames;
    private boolean includeNickname;
    
    // Religious or formal names
    private String religiousName;
    private String legalName;
    private String informalName;
    
    // Display preferences
    private List<DisplayOption> displayOptions;
    
    public NewName(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.abbreviateFirst = false;
        this.abbreviateMiddle = false;
        this.nonSplittableFirst = false;
        this.isChineseStyle = false;
        this.hasWesternName = false;
        this.nameForm = NameForm.WESTERN;
        this.nicknames = new ArrayList<>();
        this.includeNickname = false;
        this.professionalSuffixes = new ArrayList<>();
        this.displayOptions = new ArrayList<>();
    }
    
    public NewName(String familyName, String generationalName, String givenName, Object flag) {
        this.familyName = familyName;
        this.generationalName = generationalName;
        this.givenName = givenName;
        this.isChineseStyle = true;
        this.hasWesternName = false;
        this.nameForm = NameForm.CHINESE;
        this.nicknames = new ArrayList<>();
        this.includeNickname = false;
        this.professionalSuffixes = new ArrayList<>();
        this.displayOptions = new ArrayList<>();
    }
    
    public NewName(String nativeAmericanName) {
        this.nativeAmericanName = nativeAmericanName;
        this.nameForm = NameForm.NATIVE_AMERICAN;
        this.nicknames = new ArrayList<>();
        this.includeNickname = false;
        this.professionalSuffixes = new ArrayList<>();
        this.displayOptions = new ArrayList<>();
    }
    
    public NewName(String firstName, String middleName, String lastName, String familyName, String generationalName, String givenName) {
        this(firstName, middleName, lastName);
        this.familyName = familyName;
        this.generationalName = generationalName;
        this.givenName = givenName;
        this.isChineseStyle = true;
        this.hasWesternName = true;
        this.nameForm = NameForm.WESTERN;
    }
    
    // Methods for setting honorifics and ordinals
    public void setHonorific(String honorific) {
        this.honorific = honorific;
    }
    
    public void setOrdinal(String ordinal) {
        this.ordinal = ordinal;
    }
    
    // Methods for professional suffixes
    public void addProfessionalSuffix(String suffix) {
        this.professionalSuffixes.add(suffix);
    }
    
    public void setProfessionalSuffixes(String... suffixes) {
        this.professionalSuffixes = new ArrayList<>(Arrays.asList(suffixes));
    }
    
    // Methods for abbreviations
    public void setAbbreviateFirst(boolean abbreviateFirst) {
        this.abbreviateFirst = abbreviateFirst;
    }
    
    public void setAbbreviateMiddle(boolean abbreviateMiddle) {
        this.abbreviateMiddle = abbreviateMiddle;
    }
    
    // Method for non-splittable first names
    public void setNonSplittableFirst(boolean nonSplittableFirst) {
        this.nonSplittableFirst = nonSplittableFirst;
    }
    
    // Methods for married names
    public void setMarriedName(String spouseSurname, boolean hyphenate) {
        this.spouseSurname = spouseSurname;
        if (hyphenate) {
            this.lastName = this.lastName + "-" + spouseSurname;
            this.nameForm = NameForm.HYPHENATED;
        } else {
            this.birthSurname = this.lastName;
            this.lastName = spouseSurname;
            this.nameForm = NameForm.MARRIED;
        }
    }
    
    // Method for Hispanic names
    public void setHispanicNames(String paternalSurname, String maternalSurname) {
        this.lastName = paternalSurname + " " + maternalSurname;
        this.nameForm = NameForm.HISPANIC;
    }
    
    // Methods for nickname handling
    public void addNickname(String nickname) {
        this.nicknames.add(nickname);
    }
    
    public void setIncludeNickname(boolean includeNickname) {
        this.includeNickname = includeNickname;
    }
    
    // Methods for religious, legal, and informal names
    public void setReligiousName(String religiousName) {
        this.religiousName = religiousName;
        this.nameForm = NameForm.RELIGIOUS;
    }
    
    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }
    
    public void setInformalName(String informalName) {
        this.informalName = informalName;
    }
    
    // Methods for display options
    public void addDisplayOption(DisplayOption option) {
        this.displayOptions.add(option);
    }
    
    public void setDisplayOptions(DisplayOption... options) {
        this.displayOptions = new ArrayList<>(Arrays.asList(options));
    }
    
    public void clearDisplayOptions() {
        this.displayOptions.clear();
    }
    
    // Helper methods for formatting
    private String abbreviate(String name) {
        if (name != null && !name.isEmpty()) {
            if (nonSplittableFirst) {
                // For names like JeanLuc, return J. instead of J.L.
                return String.valueOf(name.charAt(0)) + ".";
            } else {
                return String.valueOf(name.charAt(0)) + ".";
            }
        }
        return "";
    }
    
    private String formatProfessionalSuffixes() {
        if (professionalSuffixes.isEmpty()) {
            return "";
        }
        return ", " + String.join(", ", professionalSuffixes);
    }
    
    @Override
    public String toString() {
        // Check if specific display options are set
        boolean showHonorific = displayOptions.contains(DisplayOption.INCLUDE_HONORIFICS);
        boolean useInitials = displayOptions.contains(DisplayOption.USE_INITIALS);
        boolean useNicknameAsGiven = displayOptions.contains(DisplayOption.NICKNAME_AS_GIVEN_NAME);
        boolean useFormal = displayOptions.contains(DisplayOption.FORMAL);
        boolean useInformal = displayOptions.contains(DisplayOption.INFORMAL);
        
        // If no display options are set, use default behavior
        if (displayOptions.isEmpty()) {
            showHonorific = true;
            useInitials = abbreviateFirst || abbreviateMiddle;
            useNicknameAsGiven = includeNickname;
        }
        
        StringBuilder nameString = new StringBuilder();
        
        // Handle religious name format
        if (nameForm == NameForm.RELIGIOUS && religiousName != null) {
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
            nameString.append(nativeAmericanName);
        } else if (isChineseStyle) {
            String fullChineseName = familyName + " " + (generationalName != null ? generationalName + " " : "") + givenName;
            if (hasWesternName) {
                String first;
                if (useNicknameAsGiven && !nicknames.isEmpty()) {
                    first = nicknames.get(0);
                } else {
                    first = useInitials || abbreviateFirst ? abbreviate(firstName) : firstName;
                }
                
                String middle = (middleName != null && !middleName.isEmpty()) ? 
                    (useInitials || abbreviateMiddle ? abbreviate(middleName) : middleName) : "";
                
                String westernName = first + (middle.isEmpty() ? "" : " " + middle) + " " + lastName;
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
                first = useInitials || abbreviateFirst ? abbreviate(firstName) : firstName;
            }
            
            String middle = (middleName != null && !middleName.isEmpty()) ? 
                (useInitials || abbreviateMiddle ? abbreviate(middleName) : middleName) : "";
            
            nameString.append(first);
            if (!middle.isEmpty()) {
                nameString.append(" ").append(middle);
            }
            nameString.append(" ").append(lastName);
            
            // Add birth surname if applicable
            if (nameForm == NameForm.MARRIED && birthSurname != null && !birthSurname.isEmpty() && useFormal) {
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
    
    // Add methods to get specific formatted versions
    public String getFormalName() {
        List<DisplayOption> originalOptions = new ArrayList<>(displayOptions);
        displayOptions.clear();
        displayOptions.add(DisplayOption.FORMAL);
        displayOptions.add(DisplayOption.INCLUDE_HONORIFICS);
        String result = toString();
        displayOptions = originalOptions;
        return result;
    }
    
    public String getInformalName() {
        List<DisplayOption> originalOptions = new ArrayList<>(displayOptions);
        displayOptions.clear();
        displayOptions.add(DisplayOption.INFORMAL);
        String result = toString();
        displayOptions = originalOptions;
        return result;
    }
    
    public String getInitialsName() {
        List<DisplayOption> originalOptions = new ArrayList<>(displayOptions);
        displayOptions.clear();
        displayOptions.add(DisplayOption.USE_INITIALS);
        String result = toString();
        displayOptions = originalOptions;
        return result;
    }
    
    public String getNameWithNickname() {
        List<DisplayOption> originalOptions = new ArrayList<>(displayOptions);
        displayOptions.clear();
        displayOptions.add(DisplayOption.NICKNAME_AS_GIVEN_NAME);
        String result = toString();
        displayOptions = originalOptions;
        return result;
    }
}