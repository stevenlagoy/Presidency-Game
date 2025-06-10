/*
 * CharacterManager.java
 * Steven LaGoy
 * Created: March 14, 2025 at 1:13 AM
 * Modified: 30 May 2025
 */

package main.core.characters;

// IMPORTS ----------------------------------------------------------------------------------------

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import core.JSONObject;
import core.JSONProcessor;
import main.core.DateManager;
import main.core.Engine;
import main.core.FilePaths;
import main.core.demographics.Bloc;
import main.core.demographics.Demographics;
import main.core.demographics.DemographicsManager;

/**
 * CharacterManager manages the generation, access, control, and interaction of Character classes (including the Player).
 * <p>
 * This class is final and has no instance variables, and is not designed to be instantiated.
 */
public final class CharacterManager {

    /** This class is non-instantiable. All values and functions should be accessed in a static way. */
    private CharacterManager() {} // Non-Instantiable
    
    /** The Player Character */
    private static Player playerCandidate;
    public static Player getPlayer() { return playerCandidate; }

    // Lists of Characters
    /** A list of all tracked Character objects. Most constructors in Character classes add the created instances to this Set. */
    private static Set<Character> characters = new HashSet<Character>();
    public static Set<Character> getCharacters() { return characters; }
    public static int getNumCharacters() { return characters.size(); }
    public static boolean addCharacter(Character character) {
        boolean added = true;
        added = added && characters.add(character);
        added = added && DemographicsManager.addCharacterToBlocs(character, character.getDemographics());
        return added;
    }
    public static boolean removeCharacter(Character character) {
        boolean removed = true;
        removed = removed && characters.remove(character);
        removed = removed && DemographicsManager.removeCharacterFromBlocs(character, character.getDemographics());
        return removed;
    }
    private static Set<Candidate> candidates = new HashSet<Candidate>();
    public static Set<Candidate> getCandidates() { return candidates; }
    public static int getNumCandidates() { return candidates.size(); }
    public static boolean addCandidate(Candidate candidate) {
        boolean added = true;
        added = added && candidates.add(candidate);
        addCharacter(candidate);
        return added;
    }
    public static boolean removeCandidate(Candidate candidate) {
        boolean removed = true;
        removed = removed && candidates.remove(candidate);
        removeCharacter(candidate);
        return removed;
    }

    // Individual Characters
    private static PoliticalActor president;
    public static PoliticalActor getPresident() { return president; }
    private static PoliticalActor vicePresident;
    public static PoliticalActor getVicePresident() { return vicePresident; }
    private static Character firstLady;
    public static Character getFirstLady() { return firstLady; }
    private static PoliticalActor houseSpeaker;
    public static PoliticalActor getHouseSpeaker() { return houseSpeaker; }

    public static boolean init(){
        boolean successFlag = true;
        successFlag = successFlag && characterSetup();

        return successFlag;
    }

    private static boolean characterSetup(){

        /* 
         * Create player candidate
         * Generate player family
         *     if any family member should be a character subclass, generate them as an instance of the subclass
         * Generate some other candidates and characters
         *     Continually ensure normal distribution of skills, experiences, demographics, alignments, preferences
         * Generate family for those candidates
         * Create some connections between characters
         *     Start with proximity between characters (may need a function to determine proximity)
         * Deterministically cascade relationships away from selected seed characters, including the player
         *     Select a semirandom number of connection chains between acceptable values
         *     Select a relationship type and establish it between two applicable characters
         */

        playerCandidate = createPlayerCharacter();
        createPlayerFamily();

        return true;

    }

    public static double determineExperientialProximity(Character char1, Character char2){
        // proximity is informed by current location, living location, birth location, closeness of experiences, prominence, and demographics
        double proximity = 0.0;
        // add some small amount depending on how near to each other their current locations are
        // add some amount depending on how near to each other their places of residence are
        // add some amount depending on how near to each other their places of birth are
        // add some amount depending on their experiences, IE if they were both governors then they would be more likely to know each other more
        // add an amount for how close to each other their demographics are - this is determined by the demographics manager and certain bloc memberships have a greater effect than others
        // use a multiplicative modifier depending on both prominence values
        // normalize the number?

        // select maximum value that this method can return
        
        return proximity;
    }
    private static Player createPlayerCharacter(){
        return new Player();
    }
    private static void createPlayerFamily(){
        
    }

    public static Personality matchPersonality(HasPersonality character){
        return new Personality();
    }
    /** Key is the name of a demographic group, values are a list of age distributions among members of that group. Indexes within the arrays correspond to the year, starting in 1900. 0 = 1900, 1 = 1901, 100 = 2000, 101 = 2001, etc. */
    private static HashMap<String, HashMap<Integer, Double>> ageDistribution;
    /** Assume that no more than this many years will be read. */
    private static int numberOfYears = 130;
    /** Assume that the first year will be 1900. */
    private static int startYear = 1900;
    public static HashMap<Integer, Double> getAgeDistribution(Demographics demographics){
        if(demographics == null) demographics = DemographicsManager.getMostCommonDemographics();

        HashMap<Integer, Double> result = new HashMap<Integer, Double>();
        for(int i = 0; i < numberOfYears; i++){
            result.put(i + startYear, 0.0);
        }
        HashMap<Integer, Double> response;
        double totalPercentages = 0.0;
        // generation data
        response = getAgeDistribution(demographics.getGeneration());
        if(response != null)
        for(int i = 0; i < response.size(); i++){
            result.put(i + startYear, response.get(i + startYear) + response.get(i + startYear));
            totalPercentages += response.get(i + startYear);
        }
        // presentation data
        response = getAgeDistribution(demographics.getPresentation());
        if(response != null)
        for(int i = 0; i < response.size(); i++){
            result.put(i + startYear, response.get(i + startYear) + response.get(i + startYear));
            totalPercentages += response.get(i + startYear);
        }
        // race/ethnicity data
        response = getAgeDistribution(demographics.getRaceEthnicity());
        if(response != null)
        for(int i = 0; i < response.size(); i++){
            result.put(i + startYear, response.get(i + startYear) + response.get(i + startYear));
            totalPercentages += response.get(i + startYear);
        }
        // religion data
        response = getAgeDistribution(demographics.getReligion());
        if(response != null)
        for(int i = 0; i < response.size(); i++){
            result.put(i + startYear, response.get(i + startYear) + response.get(i + startYear));
            totalPercentages += response.get(i + startYear);
        }
        // normalize the values
        for(int i = 0; i < result.size(); i++){
            result.put(i + startYear, result.get(i + startYear) / totalPercentages);
        }
        return result;
    }
    public static HashMap<Integer, Double> getAgeDistribution(Bloc bloc){
        return getAgeDistribution(bloc.getName());
    }
    public static HashMap<Integer, Double> getAgeDistribution(String demographicGroup){
        if(ageDistribution != null) {
            return ageDistribution.get(demographicGroup);
        }

        JSONObject json = JSONProcessor.processJson(FilePaths.BIRTHYEAR_DISTR); // read the JSON file
        CharacterManager.ageDistribution = new HashMap<String, HashMap<Integer, Double>>(); // initialize the map

        for (Object blocObj : json.getAsList()) {
            if (blocObj instanceof JSONObject blocJson) {
                String key = blocJson.getKey();
                HashMap<Integer, Double> distribution = new HashMap<>();
                for (int i = 0; i < numberOfYears; i++) {
                    distribution.put(i + startYear, 0.0);
                }
                
                for (Object dataObj : blocJson.getAsList()) {
                    if (dataObj instanceof JSONObject dataJson) {
                        int year = Integer.parseInt(dataJson.getKey());
                        double value = dataJson.getAsNumber().doubleValue();

                        distribution.put(year, value);
                    }
                }
                CharacterManager.ageDistribution.put(key, distribution);
            }
        }

        return getAgeDistribution(demographicGroup); // the map is now populated
    }
    private static HashMap<String, Double> birthdateDistribution; // Distribution of birthdates in a year, indexed by day
    public static HashMap<String, Double> getBirthdateDistribution(){
        if(birthdateDistribution != null) return birthdateDistribution;

        JSONObject json = JSONProcessor.processJson(FilePaths.BIRTHDATE_DISTR); // read the JSON file
        birthdateDistribution = new HashMap<String, Double>();

        for (Object dateObj : json.getAsList()) {
            if (dateObj instanceof JSONObject dateJson) {
                String date = dateJson.getKey();
                double value = dateJson.getAsNumber().doubleValue();
                birthdateDistribution.put(date, value);
            }
        }

        return birthdateDistribution;
    }

    public static CharacterModel generateAppearance(Character character){
        CharacterModel model = new CharacterModel();

        return model;
    }

    public static Bloc generatePresentation(Demographics demographics){
        // Using the other fields of the demographics object, select a presentation.
        
        return DemographicsManager.matchBlocName("Woman");
    }

    public static void generateBlocsReport() {
        int differenceValue = 5;
        System.out.println("TOTAL # CHARACTERS : " + getNumCharacters());
        for (Bloc bloc : Bloc.getInstances()) {
            if (bloc.getSubBlocs().isEmpty()) {
                if (!DemographicsManager.isCharacterBlocCategory(bloc.getDemographicGroup())) continue;
                float expectedRepresentation = bloc.getPercentageVoters();
                float actualRepresentation = bloc.getMembers().size() * 1.0f / CharacterManager.getNumCharacters();
                float representationRatio = actualRepresentation / expectedRepresentation;
                System.out.printf("Bloc Name : %70s,\tMember Count : %8d,\tExpected Count : %8d,\tExpected %% : %f,\tActual %% : %f,\tRatio : %f\t%s%n",
                    bloc.getName(), bloc.getMembers().size(), (int) (CharacterManager.getNumCharacters() * expectedRepresentation), expectedRepresentation, actualRepresentation, representationRatio, 
                    (bloc.getMembers().size() > (int) (CharacterManager.getNumCharacters() * expectedRepresentation) + differenceValue ? "EXCESS" : (bloc.getMembers().size() < (int) (CharacterManager.getNumCharacters() * expectedRepresentation) - differenceValue ? "LACK" : ""))
                );
            }
        }
        System.out.println("TOTAL # CHARACTERS : " + getNumCharacters());
    }

    public static String generateSaveString() {
        StringBuilder saveString = new StringBuilder();
        for (Character character : characters) {
            saveString.append(character.toRepr());
        }
        return saveString.toString();
    }

    public static Date generateBirthday(Demographics demographics) {
        return generateBirthday(demographics, Character.MIN_AGE, Character.MAX_AGE);
    }

    public static Date generateBirthday(Demographics demographics, int minAge, int maxAge) {
        Integer year;
        long birthdate;
        // Select a year
        HashMap<Integer, Double> ageDistribution = CharacterManager.getAgeDistribution(demographics);
        year = Engine.weightedRandSelect(ageDistribution);
        // Select a valid day of the year
        do {
            birthdate = DateManager.dateFormatToOrdinal(Engine.weightedRandSelect(CharacterManager.getBirthdateDistribution())) * DateManager.dayDuration;
        }
        while (DateManager.timeToYears(birthdate) < minAge || DateManager.timeToYears(birthdate) > maxAge ||
            (birthdate == 60 * DateManager.dayDuration && !DateManager.isLeapYear(year)));

        return new Date(DateManager.yearToMillis(year) + birthdate);
    }

    /**
     * Generates a character model based on the demographics and birthdate of a person. Incorporates some randomness.
     * @param demographics The demographics of the person.
     * @param birthdate The date of birth of the person.
     * @return A character model which looks like a person with the demographics and age.
     * @see #generateCharacterModel(Demographics, int)
     */
    public static CharacterModel generateCharacterModel(Demographics demographics, Date birthdate) {
        return generateCharacterModel(demographics, DateManager.yearsAgo(birthdate));
    }

    /**
     * Generates a character model based on the demographics and age of a person. Incorporates some randomness.
     * @param demographics The demographics of the person.
     * @param age The age in years of the person.
     * @return A character model which looks like a person with the demographics and age.
     */
    public static CharacterModel generateCharacterModel(Demographics demographics, int age) {
        return new CharacterModel();
    }
}
