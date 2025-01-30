import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
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

    public static double[] getBirthdateDistribution(){
        double[] distribution = new double[DateManager.daysInYear];
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
            distribution[j] = Float.parseFloat(splitString[splitString.length-1]);
        }
        return distribution;
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
