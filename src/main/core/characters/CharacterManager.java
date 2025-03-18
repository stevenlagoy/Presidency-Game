package main.core.characters;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import main.core.Engine;
import main.core.FilePaths;
import main.core.demographics.Bloc;
import main.core.demographics.Demographics;
import main.core.demographics.DemographicsManager;

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

    private static final float asianEasternNamePercent = 0.50f; // percentage of Asian people who should have an Eastern name
    private static final float asianWesternNamePercent = 0.25f; // percentage of Asian people with an Eastern name who should also have a Western name
    private static final float hispanicHispanicNamePercent = 0.80f; // percentage of Hispanic/Latino people who should have a Hispanic name
    private static final float nativeNativeNamePercent = 0.25f; // percentage of Native American people who should have a Native American name
    private static final float hasMiddleNamePercent = 0.80f; // percentage of people with a middle name
    private static final float multipleMiddleNamesPercent = 0.11f; // percentage of people with more middle names. 11% of people have 2, 0.121% have 3 etc
    private static final float abbreviateFirstNamesPercent = 0.04f;
    private static final float abbreviateMiddleNamesPercent = 0.38f;
    private static final float abbreviateBothNamesPercent = 0.08f;
    private static final float useNicknamePercent = 0.23f; // Percentage of people who usually use a nickname. alternately could have each name in nicknames file have it's own percentage
    private static final float useMiddleNicknamePercent = 0.04f;
    private static final float srOrdinationPercent = 0.04f;
    private static final float jrOrdinationPercent = 0.08f;
    private static final float iiOrdinationPercent = 0.04f;


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
        
        return proximity;
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

        HashMap<Object, Object> json = Engine.readJSONFile(FilePaths.birthyear_percentages); // read the JSON file
        CharacterManager.ageDistribution = new HashMap<String, HashMap<Integer, Double>>(); // initialize the map

        for(Object key : json.keySet()){
            String keyString = (String) key;
            HashMap<Integer, Double> distribution = new HashMap<Integer, Double>();
            for(int i = 0; i < numberOfYears; i++){
                distribution.put(i + startYear, 0.0);
            }
            if(json.get(key).equals("null")) continue;
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

        HashMap<Object, Object> json = Engine.readJSONFile(FilePaths.birthdate_popularity); // read the JSON file
        birthdateDistribution = new HashMap<String, Double>();

        for(Object key : json.keySet()){
            String keyString = (String) key;
            Double value = Double.parseDouble((String) json.get(key));
            birthdateDistribution.put(keyString, value);
        }

        return birthdateDistribution;
    }

    private static HashMap<Bloc, HashMap<String, Double>> firstNamesManDistribution;
    private static HashMap<Bloc, HashMap<String, Double>> firstNamesWomanDistribution;
    private static HashMap<Bloc, HashMap<String, Double>> middleNamesManDistribution;
    private static HashMap<Bloc, HashMap<String, Double>> middleNamesWomanDistribution;
    private static HashMap<Bloc, HashMap<String, Double>> lastNamesDistribution;
    private static HashMap<String, List<String>> nicknames;

    public static HashMap<Bloc, HashMap<String, Double>> getFirstNameManDistribution() {
        if (firstNamesManDistribution != null) return firstNamesManDistribution;
        readFirstNamesFile();
        return firstNamesManDistribution;
    }
    public static HashMap<Bloc, HashMap<String, Double>> getFirstNameWomanDistribution() {
        if (firstNamesWomanDistribution != null) return firstNamesWomanDistribution;
        readFirstNamesFile();
        return firstNamesWomanDistribution;
    }

    private static void readFirstNamesFile() {
        HashMap<Object, Object> json = Engine.readJSONFile(FilePaths.firstname_popularity);
        firstNamesManDistribution = new HashMap<Bloc, HashMap<String, Double>>();
        firstNamesWomanDistribution = new HashMap<Bloc, HashMap<String, Double>>();

        for (Object key : json.keySet()) {
            String supercategory = key.toString(); // supercategories
            HashMap<Object, Object> values = (HashMap<Object, Object>) json.get(key);
            HashMap<Bloc, HashMap<String, Double>> distributions = new HashMap<>();
            for (Object blocName : values.keySet()) {
                Bloc bloc = Bloc.matchBlocName(blocName.toString());
                if (bloc == null) continue;
                HashMap<Object, Object> names = (HashMap<Object, Object>) values.get(blocName);
                HashMap<String, Double> blocDistributions = new HashMap<>();
                for (Object n : names.keySet()) {
                    String name = n.toString();
                    if (name.equals("null")) continue;
                    Double value = Double.parseDouble(names.get(n).toString());
                    blocDistributions.put(name, value);
                }
                distributions.put(bloc, blocDistributions);
            }
            if (supercategory.equals("Man")) firstNamesManDistribution = distributions;
            if (supercategory.equals("Woman")) firstNamesWomanDistribution = distributions;
        }
    }

    public static HashMap<Bloc, HashMap<String, Double>> getMiddleNameManDistribution() {
        if (middleNamesManDistribution != null) return middleNamesManDistribution;
        readMiddleNamesFile();
        return middleNamesManDistribution;
    }
    public static HashMap<Bloc, HashMap<String, Double>> getMiddleNameWomanDistribution() {
        if (middleNamesWomanDistribution != null) return middleNamesWomanDistribution;
        readMiddleNamesFile();
        return middleNamesWomanDistribution;
    }
    
    private static void readMiddleNamesFile() {
        HashMap<Object, Object> json = Engine.readJSONFile(FilePaths.middlename_popularity);
        middleNamesManDistribution = new HashMap<Bloc, HashMap<String, Double>>();
        middleNamesWomanDistribution = new HashMap<Bloc, HashMap<String, Double>>();

        for (Object key : json.keySet()) {
            String supercategory = key.toString(); // supercategories
            HashMap<Object, Object> values = (HashMap<Object, Object>) json.get(key);
            HashMap<Bloc, HashMap<String, Double>> distributions = new HashMap<>();
            for (Object blocName : values.keySet()) {
                Bloc bloc = Bloc.matchBlocName(blocName.toString());
                if (bloc == null) continue;
                HashMap<Object, Object> names = (HashMap<Object, Object>) values.get(blocName);
                HashMap<String, Double> blocDistributions = new HashMap<>();
                for (Object n : names.keySet()) {
                    String name = n.toString();
                    if (name.equals("null")) continue;
                    Double value = Double.parseDouble(names.get(n).toString());
                    blocDistributions.put(name, value);
                }
                distributions.put(bloc, blocDistributions);
            }
            if (supercategory.equals("Man")) middleNamesManDistribution = distributions;
            if (supercategory.equals("Woman")) middleNamesWomanDistribution = distributions;
        }
    }

    public static HashMap<Bloc, HashMap<String, Double>> getLastNameDistribution() {
        if (lastNamesDistribution != null) return lastNamesDistribution;
        readLastNamesFile();
        return lastNamesDistribution;
    }

    private static void readLastNamesFile() {
        HashMap<Object, Object> json = Engine.readJSONFile(FilePaths.lastname_popularity);
        lastNamesDistribution = new HashMap<Bloc, HashMap<String, Double>>();

        for (Object blocName : json.keySet()) {
            Bloc bloc = Bloc.matchBlocName(blocName.toString());
            if (bloc == null) continue;
            HashMap<Object, Object> names = (HashMap<Object, Object>) json.get(blocName);
            HashMap<String, Double> blocDistributions = new HashMap<>();
            for (Object n : names.keySet()) {
                String name = n.toString();
                if (name.equals("null")) continue;
                Double value = Double.parseDouble(names.get(n).toString());
                blocDistributions.put(name, value);
            }
            lastNamesDistribution.put(bloc, blocDistributions);
        }
    }

    public static void readNicknamesFile() {
        HashMap<Object, Object> json = Engine.readJSONFile(FilePaths.nicknames);
        nicknames = new HashMap<String, List<String>>();

        for (Object key : json.keySet()) {
            String name = key.toString();
            List<String> nicks = new ArrayList<>();
            for (String nickname : json.get(key).toString().replaceAll("\\[|\\]", "").split(",")) {
                nicks.add(nickname.trim());
            }
            nicknames.put(name, nicks);
        }
    }

    public static void readAllNamesFiles() {
        readFirstNamesFile();
        readMiddleNamesFile();
        readLastNamesFile();
        readNicknamesFile();
    }

    public static Name generateName(Demographics demographics){
        /*
         * select a nameform based on demographics (mostly raceEthnicity)
         * depending on selected form, choose how many of each type of name to select
         * create aggregations of name maps based on each demographic bloc
         * choose names from the aggregate maps in accordance with numbers selected
         * create a name object with the selected names
         * return the name
         */

        // Determine Nameform and Number of names
        Name.NameForm nf;
        int numGiven = 0, numMiddle = 0, numFamily = 0;
        if (demographics.getRaceEthnicity().getNestedNames().contains("Chinese")) {
            if (Engine.randPercent() <= asianEasternNamePercent) {
                nf = Name.NameForm.EASTERN;
                numGiven = 1; numMiddle = 1; numFamily = 1;
                if (Engine.randPercent() <= asianWesternNamePercent) {
                }
            }
            else {
                nf = Name.NameForm.WESTERN;
                numGiven = 1; numMiddle = 0; numFamily = 1;
                if (Engine.randPercent() <= hasMiddleNamePercent) numMiddle = 1;
                while (Engine.randPercent() <= multipleMiddleNamesPercent) numMiddle++;
            }
        }
        else if (demographics.getRaceEthnicity().getNestedNames().contains("Hispanic / Latino")) {
            if (Engine.randPercent() <= hispanicHispanicNamePercent) {
                nf = Name.NameForm.HISPANIC;
                numGiven = 1; numMiddle = 0; numFamily = 2;
                if (Engine.randPercent() <= hasMiddleNamePercent) numMiddle = 1;
                while (Engine.randPercent() <= multipleMiddleNamesPercent) numMiddle++;
            }
            else {
                nf = Name.NameForm.WESTERN;
                numGiven = 1; numMiddle = 0; numFamily = 1;
                if (Engine.randPercent() <= hasMiddleNamePercent) numMiddle = 1;
                while (Engine.randPercent() <= multipleMiddleNamesPercent) numMiddle++;
            }
        }
        else if (demographics.getRaceEthnicity().getNestedNames().contains("Indigenous American")) {
            if (Engine.randPercent() <= nativeNativeNamePercent) {
                nf = Name.NameForm.NATIVE_AMERICAN;
                numGiven = 1; numMiddle = 0; numFamily = 1;
            }
            else {
                nf = Name.NameForm.WESTERN;
                numGiven = 1; numMiddle = 0; numFamily = 1;
                if (Engine.randPercent() <= hasMiddleNamePercent) numMiddle = 1;
                while (Engine.randPercent() <= multipleMiddleNamesPercent) numMiddle++;
            }
        }
        else {
            nf = Name.NameForm.WESTERN;
            numGiven = 1; numMiddle = 0; numFamily = 1;
            if (Engine.randPercent() <= hasMiddleNamePercent) numMiddle = 1;
            while (numMiddle > 0 && Engine.randPercent() <= multipleMiddleNamesPercent) numMiddle++;
        }
        
        HashMap<String, Double> aggrGivenNameMap = new HashMap<>();
        HashMap<String, Double> aggrMiddleNameMap = new HashMap<>();
        HashMap<String, Double> aggrFamilyNameMap = new HashMap<>();
        // Construct aggregate name maps
        HashMap<String, Double> blocNamesDistribution;
        String presentation = demographics.getPresentation().getName();
        switch (presentation) {
            case "Man" :
                for (Bloc bloc : demographics.toBlocsArray()) {
                    for (Bloc supers : bloc.getNestedSuperBlocs()) {
                            blocNamesDistribution = firstNamesManDistribution.get(supers);
                        if (blocNamesDistribution == null) continue;
                        for (String name : blocNamesDistribution.keySet()) {
                            aggrGivenNameMap.put(name, aggrGivenNameMap.getOrDefault(name, 0.0) + blocNamesDistribution.get(name));
                        }
                        blocNamesDistribution = middleNamesManDistribution.get(supers);
                        if (blocNamesDistribution == null) continue;
                        for (String name : blocNamesDistribution.keySet()) {
                            aggrMiddleNameMap.put(name, aggrMiddleNameMap.getOrDefault(name, 0.0) + blocNamesDistribution.get(name));
                        }
                        blocNamesDistribution = lastNamesDistribution.get(supers);
                        if (blocNamesDistribution == null) continue;
                        for (String name : blocNamesDistribution.keySet()) {
                            aggrFamilyNameMap.put(name, aggrFamilyNameMap.getOrDefault(name, 0.0) + blocNamesDistribution.get(name));
                        }
                    }
                }
                break;
            case "Woman" :
                for (Bloc bloc : demographics.toBlocsArray()) {
                    for (Bloc supers : bloc.getNestedSuperBlocs()) {
                        blocNamesDistribution = firstNamesWomanDistribution.get(supers);
                        if (blocNamesDistribution == null) continue;
                        for (String name : blocNamesDistribution.keySet()) {
                            aggrGivenNameMap.put(name, aggrGivenNameMap.getOrDefault(name, 0.0) + blocNamesDistribution.get(name));
                        }
                        blocNamesDistribution = middleNamesWomanDistribution.get(supers);
                        if (blocNamesDistribution == null) continue;
                        for (String name : blocNamesDistribution.keySet()) {
                            aggrMiddleNameMap.put(name, aggrMiddleNameMap.getOrDefault(name, 0.0) + blocNamesDistribution.get(name));
                        }
                        blocNamesDistribution = lastNamesDistribution.get(supers);
                        if (blocNamesDistribution == null) continue;
                        for (String name : blocNamesDistribution.keySet()) {
                            aggrFamilyNameMap.put(name, aggrFamilyNameMap.getOrDefault(name, 0.0) + blocNamesDistribution.get(name));
                        }
                    }
                }
                break;
            case "Other / Non-Binary" :
            default :
                for (Bloc bloc : demographics.toBlocsArray()) {
                    for (Bloc supers : bloc.getNestedSuperBlocs()) {
                        blocNamesDistribution = firstNamesWomanDistribution.get(supers);
                        if (blocNamesDistribution == null) continue;
                        blocNamesDistribution.putAll(firstNamesManDistribution.get(supers));
                        for (String name : blocNamesDistribution.keySet()) {
                            aggrGivenNameMap.put(name, aggrGivenNameMap.getOrDefault(name, 0.0) + blocNamesDistribution.get(name));
                        }
                        blocNamesDistribution = middleNamesWomanDistribution.get(supers);
                        if (blocNamesDistribution == null) continue;
                        blocNamesDistribution.putAll(middleNamesManDistribution.get(supers));
                        for (String name : blocNamesDistribution.keySet()) {
                            aggrMiddleNameMap.put(name, aggrMiddleNameMap.getOrDefault(name, 0.0) + blocNamesDistribution.get(name));
                        }
                        blocNamesDistribution = lastNamesDistribution.get(supers);
                        if (blocNamesDistribution == null) continue;
                        for (String name : blocNamesDistribution.keySet()) {
                            aggrFamilyNameMap.put(name, aggrFamilyNameMap.getOrDefault(name, 0.0) + blocNamesDistribution.get(name));
                        }
                    }
                }
                break;
        }

        // Select names from the aggregate maps
        String givenNames[] = new String[numGiven > 0 ? numGiven : 1];
        String middleNames[] = new String[numMiddle > 0 ? numMiddle : 1];
        String familyNames[] = new String[numFamily > 0 ? numFamily : 1];
        String nickname = null;
        List<String> selectedNames = new ArrayList<>();
        String name;
        int MAX_TRIES = 5, tries = 0;

        for (int i = 0; i < numGiven && tries < MAX_TRIES; i++) {
            name = Engine.weightedRandSelect(aggrGivenNameMap);
            if (name == null) {
                Engine.log(String.format("Failed to select with demographic: %s", demographics.toRepr()));
                continue;
            }
            if (!selectedNames.contains(name)) {
                givenNames[i] = name;
                selectedNames.add(name);
            }
            else {
                i--;
                tries++;
            }
        }
        if (givenNames[0] == null && givenNames.length == 1) givenNames[0] = "";
        for (int i = 0; i < numMiddle && tries < MAX_TRIES; i++) {
            name = Engine.weightedRandSelect(aggrMiddleNameMap);
            if (name == null) {
                Engine.log(String.format("Failed to select with demographic: %s.", demographics.toRepr()));
                continue;
            }
            if (!selectedNames.contains(name)) {
                middleNames[i] = name;
                selectedNames.add(name);
            }
            else {
                i--;
                tries++;
            }
        }
        if (middleNames[0] == null && middleNames.length == 1) middleNames[0] = "";
        for (int i = 0; i < numFamily && tries < MAX_TRIES; i++) {
            name = Engine.weightedRandSelect(aggrFamilyNameMap);
            if (name == null) {
                Engine.log(String.format("Failed to select with demographic: %s.", demographics.toRepr()));
                continue;
            }
            if (!selectedNames.contains(name)) {
                familyNames[i] = name;
                selectedNames.add(name);
            }
            else {
                i--;
                tries++;
            }
        }
        if (familyNames[0] == null && familyNames.length == 1) familyNames[0] = "";

        // Determine abbreviations
        boolean abbreviateFirst = false, abbreviateMiddle = false;
        if (Engine.randPercent() <= abbreviateBothNamesPercent) {
            abbreviateFirst = true; abbreviateMiddle = true;
        }
        else if (Engine.randPercent() <= abbreviateMiddleNamesPercent) {
            abbreviateMiddle = true;
        }
        else if (Engine.randPercent() <= abbreviateFirstNamesPercent) {
            abbreviateFirst = true;
        }

        // Determine nickname(s)
        if (!abbreviateFirst && Engine.randPercent() <= useNicknamePercent) {
            for (String n : givenNames) {
                if (nicknames.get(n) != null && !nicknames.get(n).isEmpty()) {
                    nickname = nicknames.get(n).get(Engine.randInt(nicknames.get(n).size() - 1));
                }
            }
        }
        else if (!abbreviateMiddle && Engine.randPercent() <= useMiddleNicknamePercent) {
            for (String n : middleNames) {
                if (nicknames.get(n) != null && !nicknames.get(n).isEmpty()) {
                    nickname = nicknames.get(n).get(Engine.randInt(nicknames.get(n).size() - 1));
                }
            }
        }

        // Determine ordination
        String ordination = null;
        if (presentation.equals("Woman")) {}
        else if (Engine.randPercent() <= jrOrdinationPercent) {
            ordination = "Jr.";
        }
        else if (Engine.randPercent() <= srOrdinationPercent) {
            ordination = "Sr.";
        }
        else {
            float iPct = iiOrdinationPercent;
            while (Engine.randPercent() <= iPct) {
                switch (ordination) {
                    case null :
                        ordination = "II";
                        iPct = 0.80f;
                        break;
                    case "II" :
                        ordination = "III";
                        iPct = 0.50f;
                        break;
                    case "III" :
                        ordination = "IV";
                        iPct = 0.50f;
                        break;
                    case "IV" :
                        ordination = "V";
                        iPct = 0.50f;
                        break;
                    default:
                        iPct = 0.0f;
                }
            }
        }


        Name n = new Name(nf, String.join(" ", givenNames), String.join(" ", middleNames), String.join(" ", familyNames));
        n.setAbbrFirst(abbreviateFirst);
        n.setAbbrMiddle(abbreviateMiddle);
        if (nickname != null && !nickname.equals("")) {
            n.addNickname(nickname);
            n.setIncludeNickname(true);
        }
        if (ordination != null && !ordination.equals("")) {
            n.setOrdinal(ordination);
        }
        return n;
    }

    public static CharacterModel generateAppearance(Character character){
        CharacterModel model = new CharacterModel();

        return model;
    }

    public static Bloc generatePresentation(Demographics demographics){
        // Using the other fields of the demographics object, select a presentation.
        
        return Bloc.matchBlocName("");
    }
}
