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

    private static final String birthdate_popularity_filename = "birthdate_popularities.JSON";
    private static final String birthdate_percentages_filename = "birthyear_percentages.JSON";

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

        // read from JSON
        String[] contents = new String[numberOfYears * Bloc.getNumberOfBlocs() + 10]; // make sure there's enough space for extra JSON syntax lines
        try{
            Scanner scanner = new Scanner(new File(birthdate_percentages_filename));
            int i = 0;
            while(scanner.hasNext()){
                contents[i++] = scanner.nextLine();
            }
            scanner.close();
        }
        catch(FileNotFoundException e){
            Engine.log(e);
            return null;
        }
        // split the contents up by the demographic group they discribe
        // JSON file: { "firstGroup": { "1900" : 0.02, "1901" : 0.01 }, "secondGroup": { "1900" : 0.03, "1901" : 0.02 } }
        // find JSON objects
        Stack<Integer> stack = new Stack<Integer>();
        Integer[] indices = new Integer[contents.length];
        for(int i = 0; i < contents.length; i++){
            if(contents[i] == null) break;
            if(contents[i].contains("{")) stack.push(i);
            if(contents[i].contains("}")){
                try{
                    indices[stack.pop()] = i;
                }
                catch(EmptyStackException e){
                    Engine.log("JSON file is malformed, missing opening at line " + i);
                    return null;
                }
            }
        }
        if(!stack.isEmpty()){
            Engine.log("JSON file is malformed, missing closing at line " + stack.pop());
            return null;
        }
        ageDistribution = new HashMap<String, HashMap<Integer, Double>>(); // initialize the map
        for(int i = 1; i < indices.length; i++){ // skip the first object (the enclosing brackets)
            if(indices[i] != null){
                String objectName = contents[i].split(":")[0].trim().replace("\"","");
                HashMap<Integer, Double> distribution = new HashMap<Integer, Double>();

                // extract weights from contents, from the start of the object to the end
                for(int j = i + 1; j < indices[i].intValue(); j++){
                    String[] splitString = contents[j].split("[\\s+,]");
                    distribution.put(j - i - 1, Double.parseDouble(splitString[splitString.length-1]));
                }
                // add the object to the map
                ageDistribution.put(objectName, distribution);
            }
        }
        return getAgeDistribution(demographicGroup); // the map is now populated
    }
    private static HashMap<String, Double> birthdateDistribution; // Distribution of birthdates in a year, indexed by day
    public static HashMap<String, Double> getBirthdateDistribution(){
        if(birthdateDistribution != null) return birthdateDistribution;

        CharacterManager.birthdateDistribution = new HashMap<String, Double>();
        // read from JSON
        String[] contents = new String[DateManager.daysInYear + 10]; // make sure there's enough space for extra JSON syntax lines
        try{
            Scanner scanner = new Scanner(new File(birthdate_popularity_filename));
            int i = 0;
            while(scanner.hasNext() && i < contents.length){
                contents[i++] = scanner.nextLine();
            }
            scanner.close();
        }
        catch(FileNotFoundException e){
            Engine.log(e);
            return null;
        }
        // get rid of the opening and closing brackets, and trim to length
        String[] daysData = new String[DateManager.daysInYear];
        int i = 0;
        for(int j = 0; j < contents.length; j++){
            if(contents[j] == null) break;
            String line = contents[j].trim();
            if(line.equals("{") || line.equals("}")) continue;
            daysData[i++] = line.replace(",", "");
        }
        // extract weights from contents
        for(int j = 0; j < daysData.length; j++){
            String[] splitString = daysData[j].split("\\s+");
            birthdateDistribution.put(DateManager.ordinalToDateFormat(j), Double.parseDouble(splitString[splitString.length-1]));
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
