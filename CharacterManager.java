import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.io.File;
import java.io.FileNotFoundException;

public class CharacterManager
{
    // required filenames
    private static final String birthdate_popularity_filename = "birthdate_popularities.JSON";
    private static final String birthyear_percentages_filename = "birthyear_percentages.JSON";
    
    private static List<Character> characters = new LinkedList<Character>();
    private static List<GovernmentOfficial> governmentOfficials = new LinkedList<GovernmentOfficial>();
    private static List<Representative> representatives = new LinkedList<Representative>();
    private static List<Senator> senators = new LinkedList<Senator>();
    private static List<StateOfficial> stateOfficials = new LinkedList<StateOfficial>();
    private static List<Governor> governors = new LinkedList<Governor>();
    private static List<Mayor> mayors = new LinkedList<Mayor>();
    private static List<Candidate> candidates = new LinkedList<Candidate>();
    private static President president;
    private static VicePresident vicePresident;
    private static Character firstLady;
    private static Representative HouseSpeaker;

    

    public static boolean init(){
        boolean successFlag = true;
        characterSetup();

        return successFlag;
    }

    private static void characterSetup(){

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

        Engine.playerCandidate = createPlayerCharacter();
        createPlayerFamily();

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
        
        return 0.0f;
    }
    private static Candidate createPlayerCharacter(){
        return new Candidate();
    }
    private static void createPlayerFamily(){
        Candidate player = Engine.playerCandidate;
        
    }
    public static Character[] getAllCharacters(){
        Character[] charactersArray = new Character[characters.size()];
        for(int i = 0; i < characters.size(); i++){
            charactersArray[i] = characters.get(i);
        }
        return charactersArray;
    }
    public static int numCharacters(){
        return characters.size();
    }
    public static void addCharacter(Character character){
        characters.add(character);
    }
    public static Candidate[] getAllCandidates(){
        Candidate[] candidatesArray = new Candidate[candidates.size()];
        for(int i = 0; i < candidates.size(); i++){
            candidatesArray[i] = candidates.get(i);
        }
        return candidatesArray;
    }

    public static Personality matchPersonality(HasPersonality character){
        return new Personality();
    }
    private static HashMap<String, HashMap<Integer, Double>> ageDistribution; // Key is the name of a demographic group, values are a list of age distributions among members of that group. Indexes within the arrays correspond to the year, starting in 1900. 0 = 1900, 1 = 1901, 100 = 2000, 101 = 2001, etc.
    private static int numberOfYears = 200; // assume that no more than this many years will be read
    private static int startYear = 1900; // assume that the first year will be 1900
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
        for(int i = 0; i < response.size(); i++){
            result.put(i + startYear, response.get(i + startYear) + response.get(i + startYear));
            totalPercentages += response.get(i);
        }
        // presentation data
        response = getAgeDistribution(demographics.getPresentation());
        for(int i = 0; i < response.size(); i++){
            result.put(i + startYear, response.get(i + startYear) + response.get(i + startYear));
            totalPercentages += response.get(i);
        }
        // race/ethnicity data
        response = getAgeDistribution(demographics.getRaceEthnicity());
        for(int i = 0; i < response.size(); i++){
            result.put(i + startYear, response.get(i + startYear) + response.get(i + startYear));
            totalPercentages += response.get(i);
        }
        // religion data
        response = getAgeDistribution(demographics.getReligion());
        for(int i = 0; i < response.size(); i++){
            result.put(i + startYear, response.get(i + startYear) + response.get(i + startYear));
            totalPercentages += response.get(i);
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

        HashMap<Object, Object> json = Engine.readJSONFile(birthyear_percentages_filename); // read the JSON file
        CharacterManager.ageDistribution = new HashMap<String, HashMap<Integer, Double>>(); // initialize the map

        for(Object key : json.keySet()){
            String keyString = (String) key;
            HashMap<Integer, Double> distribution = new HashMap<Integer, Double>();
            for(int i = 0; i < numberOfYears; i++){
                distribution.put(i + startYear, 0.0);
            }
            @SuppressWarnings("unchecked")
            HashMap<Object, Object> values = (HashMap<Object, Object>) json.get(key); // known structure of the JSON file
            for(Object year : values.keySet()){
                int yearInt = Integer.parseInt((String) year);
                distribution.put(yearInt, Double.parseDouble((String) values.get(year)));
            }
            ageDistribution.put(keyString, distribution);
        }

        return getAgeDistribution(demographicGroup); // the map is now populated
    }
    private static HashMap<String, Double> birthdateDistribution; // Distribution of birthdates in a year, indexed by day
    public static HashMap<String, Double> getBirthdateDistribution(){
        if(birthdateDistribution != null) return birthdateDistribution;

        HashMap <Object, Object> json = Engine.readJSONFile(birthdate_popularity_filename); // read the JSON file
        CharacterManager.birthdateDistribution = new HashMap<String, Double>();

        for(Object key : json.keySet()){
            String keyString = (String) key;
            Double value = Double.parseDouble((String) json.get(key));
            birthdateDistribution.put(keyString, value);
        }

        return birthdateDistribution;
    }

    public static Demographics generateDemographics(){
        return null;
    }

    public static Name generateName(Demographics demographics){
        return null;
    }

    public static CharacterModel generateAppearance(Character character){
        CharacterModel model = new CharacterModel();

        return model;
    }

    public static Bloc generatePresentation(Demographics demographics){
        // Using the two other fields of the demographics object, select a presentation.
        
        return Bloc.matchBlocName("");
    }
}
