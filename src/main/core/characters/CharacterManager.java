package main.core.characters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import core.JSONObject;
import core.JSONProcessor;
import main.core.DateManager;
import main.core.Engine;
import main.core.FilePaths;
import main.core.demographics.Bloc;
import main.core.demographics.Demographics;
import main.core.demographics.DemographicsManager;
import main.core.map.City;
import main.core.map.MapManager;

public class CharacterManager
{
    
    private static List<Character>          characters          = new LinkedList<Character>();
    private static List<GovernmentOfficial> governmentOfficials = new LinkedList<GovernmentOfficial>();
    private static List<Representative>     representatives     = new LinkedList<Representative>();
    private static List<Senator>            senators            = new LinkedList<Senator>();
    private static List<StateOfficial>      stateOfficials      = new LinkedList<StateOfficial>();
    private static List<Governor>           governors           = new LinkedList<Governor>();
    private static List<Mayor>              mayors              = new LinkedList<Mayor>();
    private static List<Candidate>          candidates          = new LinkedList<Candidate>();
    private static President                president;
    private static VicePresident            vicePresident;
    private static Character                firstLady;
    private static Representative           HouseSpeaker;

    private static final float asianEasternNamePercent      = 0.50f; // percentage of Asian people who should have an Eastern name
    private static final float asianWesternNamePercent      = 0.25f; // percentage of Asian people with an Eastern name who should also have a Western name
    private static final float hispanicHispanicNamePercent  = 0.80f; // percentage of Hispanic/Latino people who should have a Hispanic name
    private static final float nativeNativeNamePercent      = 0.25f; // percentage of Native American people who should have a Native American name
    private static final float hasMiddleNamePercent         = 0.80f; // percentage of people with a middle name
    private static final float multipleMiddleNamesPercent   = 0.11f; // percentage of people with more middle names. 11% of people have 2, 0.121% have 3 etc
    private static final float abbreviateFirstNamesPercent  = 0.04f;
    private static final float abbreviateMiddleNamesPercent = 0.38f;
    private static final float abbreviateBothNamesPercent   = 0.08f;
    private static final float useNicknamePercent           = 0.23f; // Percentage of people who usually use a nickname. alternately could have each name in nicknames file have it's own percentage
    private static final float useMiddleNicknamePercent     = 0.04f;
    private static final float srOrdinationPercent          = 0.04f;
    private static final float jrOrdinationPercent          = 0.08f;
    private static final float iiOrdinationPercent          = 0.04f;

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
    public static List<Character> getAllCharacters(){
        return characters;
    }
    public static int getNumCharacters(){
        return characters.size();
    }
    public static void addCharacter(Character character){
        characters.add(character);
        DemographicsManager.addCharacterToBlocs(character, character.getDemographics());
    }
    public static List<Candidate> getAllCandidates(){
        return candidates;
    }
    public static int getNumCandidates() {
        return candidates.size();
    }

    public static Personality matchPersonality(HasPersonality character){
        return new Personality();
    }
    /** Key is the name of a demographic group, values are a list of age distributions among members of that group. Indexes within the arrays correspond to the year, starting in 1900. 0 = 1900, 1 = 1901, 100 = 2000, 101 = 2001, etc. */
    private static HashMap<String, HashMap<Integer, Double>> ageDistribution;
    /** Assume that no more than this many years will be read. */
    private static int numberOfYears = 200;
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

    private static Map<Set<Bloc>, Map<String, Double>> firstNamesDistribution;
    private static Map<Set<Bloc>, Map<String, Double>> middleNamesDistribution;

    private static HashMap<Bloc, HashMap<String, Double>> firstNamesManDistribution;
    private static HashMap<Bloc, HashMap<String, Double>> firstNamesWomanDistribution;
    private static HashMap<Bloc, HashMap<String, Double>> middleNamesManDistribution;
    private static HashMap<Bloc, HashMap<String, Double>> middleNamesWomanDistribution;
    private static HashMap<Bloc, HashMap<String, Double>> lastNamesDistribution;
    private static HashMap<String, List<String>> nicknames;

    public static Map<Set<Bloc>, Map<String, Double>> getFirstNamesDistribution() {
        if (firstNamesDistribution == null) readFirstNamesFile();
        return firstNamesDistribution;
    }

    public static Map<String, Double> getFirstNamesDistribution(Demographics demographics) {
        return getFirstNamesDistribution(demographics.toBlocsArray());
    }
    
    public static Map<String, Double> getFirstNamesDistribution(Bloc... blocs) {
        return getFirstNamesDistribution(Set.of(blocs));
    }

    public static Map<String, Double> getFirstNamesDistribution(Collection<Bloc> blocs) {
        if (firstNamesDistribution == null) readFirstNamesFile();
        Set<Bloc> key = new HashSet<>(blocs);
        return firstNamesDistribution.get(key);
    }

    private static void readFirstNamesFile() {
        JSONObject json = JSONProcessor.processJson(FilePaths.FIRSTNAME_DISTR);
        firstNamesDistribution = new HashMap<>();

        processFirstNameStructure(json, new HashSet<>());
    }

    private static Map<String, Double> processFirstNameStructure(JSONObject json, Set<Bloc> currentBlocs) {
        Map<String, Double> distributions = new HashMap<String,Double>();

        for (Object obj : json.getAsList()) {
            if (!(obj instanceof JSONObject entry)) continue;
            
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Number) {
                // This is a name-number pair
                // Add or update the name distribution for all current blocs
                if (!currentBlocs.isEmpty()) {
                    distributions.put(key, ((Number) value).doubleValue());
                    Map<String, Double> existingDistributions = firstNamesDistribution
                        .computeIfAbsent(currentBlocs, k -> new HashMap<>());
                    existingDistributions.put(key, ((Number) value).doubleValue());
                }
            }
            else if (value instanceof ArrayList<?>) {
                // This is a nested structure

                // If key is a valid bloc name, add it to the current set of blocs
                Bloc bloc = DemographicsManager.matchBlocName(key);
                Set<Bloc> updatedBlocs = new HashSet<>(currentBlocs);
                if (bloc != null) {
                    updatedBlocs.add(bloc);
                }

                // Recurse with updated bloc set
                Map<String, Double> nestedDistributions = processFirstNameStructure(new JSONObject(key, (ArrayList<?>) value), updatedBlocs);
                distributions.putAll(nestedDistributions);
            }
        }
        return distributions;
    }

    public static Map<Set<Bloc>, Map<String, Double>> getMiddleNamesDistribution() {
        if (firstNamesDistribution == null) readMiddleNamesFile();
        return middleNamesDistribution;
    }

    public static Map<String, Double> getMiddleNamesDistribution(Demographics demographics) {
        return getMiddleNamesDistribution(demographics.toBlocsArray());
    }
    
    public static Map<String, Double> getMiddleNamesDistribution(Bloc... blocs) {
        return getMiddleNamesDistribution(Set.of(blocs));
    }

    public static Map<String, Double> getMiddleNamesDistribution(Collection<Bloc> blocs) {
        if (firstNamesDistribution == null) readMiddleNamesFile();
        Set<Bloc> key = new HashSet<>(blocs);
        return middleNamesDistribution.get(key);
    }

    private static void readMiddleNamesFile() {
        JSONObject json = JSONProcessor.processJson(FilePaths.FIRSTNAME_DISTR);
        middleNamesDistribution = new HashMap<>();

        processMiddleNameStructure(json, new HashSet<>());
    }

    private static Map<String, Double> processMiddleNameStructure(JSONObject json, Set<Bloc> currentBlocs) {
        Map<String, Double> distributions = new HashMap<String,Double>();

        for (Object obj : json.getAsList()) {
            if (!(obj instanceof JSONObject entry)) continue;
            
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Number) {
                // This is a name-number pair
                // Add or update the name distribution for all current blocs
                if (!currentBlocs.isEmpty()) {
                    distributions.put(key, ((Number) value).doubleValue());
                    Map<String, Double> existingDistributions = middleNamesDistribution
                        .computeIfAbsent(currentBlocs, k -> new HashMap<>());
                    existingDistributions.put(key, ((Number) value).doubleValue());
                }
            }
            else if (value instanceof ArrayList<?>) {
                // This is a nested structure

                // If key is a valid bloc name, add it to the current set of blocs
                Bloc bloc = DemographicsManager.matchBlocName(key);
                Set<Bloc> updatedBlocs = new HashSet<>(currentBlocs);
                if (bloc != null) {
                    updatedBlocs.add(bloc);
                }

                // Recurse with updated bloc set
                Map<String, Double> nestedDistributions = processFirstNameStructure(new JSONObject(key, (ArrayList<?>) value), updatedBlocs);
                distributions.putAll(nestedDistributions);
            }
        }
        return distributions;
    }

    public static HashMap<Bloc, HashMap<String, Double>> getLastNameDistribution() {
        if (lastNamesDistribution == null) readLastNamesFile();
        return lastNamesDistribution;
    }

    private static void readLastNamesFile() {
        JSONObject json = JSONProcessor.processJson(FilePaths.LASTNAME_DISTR);
        lastNamesDistribution = new HashMap<Bloc, HashMap<String, Double>>();

        for (Object blocObj: json.getAsList()) {
            if (blocObj instanceof JSONObject blocJson) {
                Bloc bloc = DemographicsManager.matchBlocName(blocJson.getKey());
                if (bloc == null) continue;
                
                HashMap<String, Double> blocDistributions = new HashMap<>();
                for (Object lastnameObj : blocJson.getAsList()) {
                    if (lastnameObj instanceof JSONObject lastnameJson) {
                        String lastname = lastnameJson.getKey();
                        double value = lastnameJson.getAsNumber().doubleValue();
                        blocDistributions.put(lastname, value);
                    }
                }
                CharacterManager.lastNamesDistribution.put(bloc, blocDistributions);
            }
        }
    }

    private static void readNicknamesFile() {
        JSONObject json = JSONProcessor.processJson(FilePaths.NICKNAMES);
        nicknames = new HashMap<String, List<String>>();

        for (Object nicknameObj : json.getAsList()) {
            if (nicknameObj instanceof JSONObject nicknameJson) {
                String name = nicknameJson.getKey();
                List<?> rawNicks = nicknameJson.getAsList();
                List<String> nicks = new ArrayList<>();
                for (Object obj : rawNicks) {
                    if (obj != null) nicks.add(obj.toString());
                }
                CharacterManager.nicknames.put(name, nicks);
            }
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
        if (presentation.equals("Woman") || nf != Name.NameForm.WESTERN) {}
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
        year = Engine.weightedRandSelect(CharacterManager.getAgeDistribution(demographics));
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
