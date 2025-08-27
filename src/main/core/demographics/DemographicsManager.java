/*
 * DemographicsManager.java
 * Steven LaGoy
 * Created: 10 December 2024 at 8:21 PM
 * Modified: 26 August 2025
 */

package main.core.demographics;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.JSONObject;
import core.JSONProcessor;
import main.core.FilePaths;
import main.core.Logger;
import main.core.Manager;
import main.core.NumberOperations;
import main.core.characters.Character;
import main.core.characters.CharacterManager;

/**
 * DemographicsManager is responsible for generating, storing, and calculating demographic data.
 */
public class DemographicsManager extends Manager {

    // STATIC CLASS VARIABLES ---------------------------------------------------------------------

    public static enum DemographicCategory {
        GENERATION ("Generation"),
        MARITAL_STATUS ("Marital Status"),
        FAMILY_SIZE ("Family Size"),
        RELIGION ("Religion"),
        EDUCATION ("Education"),
        RACE_ETHNICITY ("Race / Ethnicity"),
        EMPLOYMENT ("Employment"),
        RESIDENCY ("Residency"),
        PRESENTATION ("Presentation");

        public final String label;
        private DemographicCategory(String label) { this.label = label; }

        public static DemographicCategory fromString(String label) {
            for (DemographicCategory category : DemographicCategory.values()) {
                if (category.label.equals(label)) {
                    return category;
                }
            }
            Logger.log("INVALID DEMOGRAPHIC CATEGORY", "The Demographic Category, " + label + " was invalid and could not be matched.", new Exception());
            return null;
        }
    }
    /** Demographic Categories applicible to Character Bloc memberships. */
    public static final DemographicCategory[] characterBlocCategories = {
        DemographicCategory.GENERATION,
        DemographicCategory.RELIGION,
        DemographicCategory.RACE_ETHNICITY,
        DemographicCategory.PRESENTATION
    };

    public static final long GAME_START_VOTERS = 341_275_500; // as of 1 Feb 2025

    // STATIC CLASS FUNCTIONS ---------------------------------------------------------------------

    public static DemographicCategory[] getCharacterBlocCategories() {
        return characterBlocCategories;
    }
    public static boolean isCharacterBlocCategory(DemographicCategory category) {
        return Arrays.asList(characterBlocCategories).contains(category);
    }

    // INSTANCE VARIABLES -------------------------------------------------------------------------

    private long numVoters;

    private Map<DemographicCategory, List<Bloc>> demographicBlocs = new HashMap<>();

    //final static Bloc EVERYONE = Bloc.matchBlocName("everyone");
    //final static Bloc VOTERS = Bloc.matchBlocName("voters");
    //public final static Bloc EVERYONE = new Bloc("Everyone", null, 1.0f);
    //public final static Bloc VOTERS = new Bloc("Voters", null, 0.7083f);

    private ManagerState currentState;

    // CONSTRUCTORS -------------------------------------------------------------------------------

    public DemographicsManager() {
        currentState = ManagerState.INACTIVE;
        numVoters = -1;
        demographicBlocs = new HashMap<>();
    }

    // MANAGER METHODS ----------------------------------------------------------------------------

    @Override
    public boolean init() {
        boolean successFlag = true;
        numVoters = GAME_START_VOTERS;
        createDemographicBlocs();
        currentState = successFlag ? ManagerState.ACTIVE : ManagerState.ERROR;
        return successFlag;
    }

    @Override
    public ManagerState getState() {
        return currentState;
    }

    @Override
    public boolean cleanup() {
        boolean successFlag = true;
        currentState = ManagerState.INACTIVE;
        if (!successFlag) currentState = ManagerState.ERROR;
        return successFlag;
    }

    // INSTANCE METHODS ---------------------------------------------------------------------------


    private List<Bloc> createBlocs(DemographicCategory category, JSONObject structure) {
        List<Bloc> blocs = new ArrayList<>();
        
        for (Object key : structure.getAsList()) {
            if (key instanceof JSONObject keyObj) {
                String blocName = keyObj.getKey();
                Object value = keyObj.getValue();
                
                Bloc parent;
                if (value instanceof Number numVal) {
                    // Base case: numerical value represents percentage
                    float percentageOrCount = numVal.floatValue();
                    if (percentageOrCount == (int) percentageOrCount) { // Count
                        parent = new Bloc(blocName, category, (int) percentageOrCount);
                    }
                    else { // Percentage
                        parent = new Bloc(blocName, category, percentageOrCount);
                    }
                    blocs.add(parent);
                } 
                else if (value instanceof List<?>) {
                    // Recursive case: nested blocs
                    parent = new Bloc(blocName, category);
                    parent.addSubBlocs(createBlocs(category, keyObj));
                    blocs.add(parent);
                }
            }
        }    
        return blocs;
    }

    public Demographics generateDemographics(){
        // select the currently most underrepresented demographic bloc
        // given that bloc's overlap, select the most underrepresented of the rest of the blocs
        // return those together

        if (CharacterManager.getNumCharacters() == 0) {
            return getMostCommonDemographics();
        }

        Demographics underrepresentedDemographics = new Demographics();

        for (DemographicCategory category : characterBlocCategories){
            Bloc underrepresentedBloc = findMostUnderrepresentedBloc(demographicBlocs.get(category));
            switch (category) {
                case GENERATION :
                    underrepresentedDemographics.setGeneration(underrepresentedBloc);
                    break;
                case RELIGION :
                    underrepresentedDemographics.setReligion(underrepresentedBloc);
                    break;
                case RACE_ETHNICITY :
                    underrepresentedDemographics.setRaceEthnicity(underrepresentedBloc);
                    break;
                case PRESENTATION :
                    underrepresentedDemographics.setPresentation(underrepresentedBloc);
                    break;
                default : ;
            }
        }

        return underrepresentedDemographics;
    }

    public Demographics generateWeightedDemographics() {
        Demographics demographics = new Demographics();
        for (DemographicCategory category : characterBlocCategories) {
            List<Bloc> blocs = demographicBlocs.get(category);
            Map<Bloc, Float> blocsWeights = new HashMap<>();
            for (Bloc bloc : blocs) {
                blocsWeights.put(bloc, bloc.getPercentageVoters());
            }
            Bloc selected = NumberOperations.weightedRandSelect(blocsWeights);
            switch (selected.getDemographicGroup()) {
                case GENERATION :
                    demographics.setGeneration(selected);
                    break;
                case RACE_ETHNICITY :
                    demographics.setRaceEthnicity(selected);
                    break;
                case RELIGION :
                    demographics.setReligion(selected);
                    break;
                case PRESENTATION :
                    demographics.setPresentation(selected);
                    break;
                default : ;
            }
        }
        return demographics;
    }

    public void createDemographicBlocs() {
        JSONObject json = JSONProcessor.processJson(FilePaths.BLOCS);

        for (Object categoryObj : json.getAsList()) {
            if (categoryObj instanceof JSONObject categoryJson) {
                DemographicCategory category = DemographicCategory.fromString(categoryJson.getKey());
                List<Bloc> blocs = createBlocs(category, categoryJson.getAsObject());
                demographicBlocs.put(category, blocs);
            }
        }
    }

    public Demographics randomDemographics() {
        Bloc generation, religion, raceEthnicity, presentation;
        try {
            generation = demographicBlocs.get("Generation").get(NumberOperations.randInt(demographicBlocs.get("Generation").size() - 1));
            while (generation.getSubBlocs().size() != 0) {
                generation = generation.getSubBlocs().get(NumberOperations.randInt(generation.getSubBlocs().size() - 1));
            }
            religion = demographicBlocs.get("Religion").get(NumberOperations.randInt(demographicBlocs.get("Religion").size() - 1));
            while (religion.getSubBlocs().size() != 0) {
                religion = religion.getSubBlocs().get(NumberOperations.randInt(religion.getSubBlocs().size() - 1));
            }
            raceEthnicity = demographicBlocs.get("Race / Ethnicity").get(NumberOperations.randInt(demographicBlocs.get("Race / Ethnicity").size() - 1));
            while (raceEthnicity.getSubBlocs().size() != 0) {
                raceEthnicity = raceEthnicity.getSubBlocs().get(NumberOperations.randInt(raceEthnicity.getSubBlocs().size() - 1));
            }
            presentation = demographicBlocs.get("Presentation").get(NumberOperations.randInt(demographicBlocs.get("Presentation").size() - 1));
            while (presentation.getSubBlocs().size() != 0) {
                presentation = presentation.getSubBlocs().get(NumberOperations.randInt(presentation.getSubBlocs().size() - 1));
            }
            return new Demographics(generation, religion, raceEthnicity, presentation);
        }
        catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    // GETTERS AND SETTERS ------------------------------------------------------------------------

    public long getNumberVoters() {
        return numVoters;
    }

    public int[] getPopulationPyramid(Bloc applicableBloc){
        int[] pyramid = {1, 2, 3, 4, 5};
        return pyramid;
        // TODO
    }
    public double[] getPopulationPyramidPercent(Bloc applicableBloc){
        int[] pyramid = getPopulationPyramid(applicableBloc);
        int totalMembers = applicableBloc.getMembers().size();
        double[] percentagesPyramid = new double[pyramid.length];

        for(int i = 0; i < pyramid.length; i++){
            percentagesPyramid[i] = pyramid[i] / totalMembers;
        }
        return percentagesPyramid;
    }

    public Demographics getMostCommonDemographics(){
        // generation, presentation, raceEthnicity, religion
        // should make this adaptive to the current population
        return new Demographics("Millennial", "White Catholic", "English", "Woman");
    }

    public Map<DemographicCategory, List<Bloc>> getDemographicBlocs() {
        return demographicBlocs;
    }

    /**
     * Selects the most Underrepresented Bloc from the passed List of Blocs their subBloc trees. Underrepresentation is determined by the bloc's Representation Ratio, or the actual representation / expected representation.
     * @param blocs A list of blocs to search (along with their subBloc trees).
     * @return The innermost bloc which is most underrepresented.
     */
    private Bloc findMostUnderrepresentedBloc(List<Bloc> blocs){        
        Bloc underrepresentedBloc = blocs.get(0);
        float underrepresentedValue = determineRepresentationRatio(underrepresentedBloc);

        for (Bloc bloc : blocs) {
            if (bloc.getSubBlocs().isEmpty()) {
                float representationRatio = determineRepresentationRatio(bloc);
                if (representationRatio < underrepresentedValue ||
                    (representationRatio == underrepresentedValue &&
                        bloc.getPercentageVoters() > underrepresentedBloc.getPercentageVoters())) {
                    underrepresentedBloc = bloc;
                    underrepresentedValue = representationRatio;
                    // System.out.printf("Found %s with ratio %f.%n", underrepresentedBloc.getName(), underrepresentedValue);
                }
            }
            else {
                Bloc candidate = findMostUnderrepresentedBloc(bloc.getSubBlocs());
                float candidateRatio = determineRepresentationRatio(candidate);
                if (Float.isNaN(underrepresentedValue) || candidateRatio < underrepresentedValue ||
                    (candidateRatio == underrepresentedValue &&
                        bloc.getPercentageVoters() > underrepresentedBloc.getPercentageVoters())) {
                    underrepresentedBloc = candidate;
                    underrepresentedValue = candidateRatio;
                    // System.out.printf("Found %s with ratio %f.%n", underrepresentedBloc.getName(), underrepresentedValue);
                }
            }
        }

        return underrepresentedBloc;
    }

    private float determineRepresentationRatio(Bloc bloc) {
        // Returns ratio of actual character membership to expected membership
        // <1 if underrepresented, >1 if overrepresented, =1 if perfectly represented
        try {
            if(CharacterManager.getNumCharacters() == 0) return 1.0f; // if there are no characters, every bloc is perfectly represented
            float expectedRepresentation = bloc.getPercentageVoters();
            float actualRepresentation = bloc.getMembers().size() * 1.0f / CharacterManager.getNumCharacters();
            float representationRatio = actualRepresentation / expectedRepresentation;

            return (representationRatio);
        }
        catch (ArithmeticException e) {
            return 1.0f;
        }
    }

    public void addCharacterToBlocs(Character character, Demographics demographics) {
        for (Bloc bloc : demographics.toBlocsArray()) {
            bloc.addMember(character);
        }
    }

    public void removeCharacterFromBlocs(Character character, Demographics demographics) {
        
    }

    public Bloc matchBlocName(String name){
        List<Bloc> blocs  = Bloc.getInstances();
        for(Bloc bloc : Bloc.getInstances()){
            if(bloc.getName().equals(name))
                return bloc;
        }
        Logger.log("INVALID BLOC NAME", String.format("The Bloc name \"%s\" is non-existent and could not be matched.", name), new Exception());
        return null;
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    @Override
    public String toRepr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRepr'");
    }

    @Override
    public Manager fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }

    private static final Map<String, String> fieldsJsons = Map.of(
        "numVoters", "number_voters",
        "demographicBlocs", "demographic_blocs"
    );

    @Override
    public JSONObject toJson() {
        try {
            List<JSONObject> fields = new ArrayList<>();
            for (String fieldName : fieldsJsons.keySet()) {
                Field field = getClass().getDeclaredField(fieldName);
                fields.add(new JSONObject(fieldName, field.get(this)));
            }
            return new JSONObject(this.getClass().getSimpleName(), fields);
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            currentState = ManagerState.ERROR;
            Logger.log("JSON SERIALIZATION ERROR", "Failed to serialize " + getClass().getSimpleName() + " to JSON.", e);
            return null;
        }
    }

    @Override
    public Manager fromJson(JSONObject json) {
        currentState = ManagerState.INACTIVE;
        for (String fieldName : fieldsJsons.keySet()) {
            String jsonKey = fieldsJsons.get(fieldName);
            Object value = json.get(jsonKey);
            if (value == null) continue;
            try {
                Field field = getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Class<?> type = field.getType();
                if (type.isEnum()) {
                    // For enums, convert string to enum constant
                    @SuppressWarnings({ "unchecked", "rawtypes" })
                    Object enumValue = Enum.valueOf((Class<Enum>) type, value.toString());
                    field.set(this, enumValue);
                }
                else {
                    // For other types, set directly (may need conversion for complex types)
                    field.set(this, value);
                }
            }
            catch (Exception e) {
                currentState = ManagerState.ERROR;
                Logger.log("JSON DESERIALIZATION ERROR", "Failed to set field " + fieldName + " in LanguageManager from JSON.", e);
            }
        }
        return this;
    }

}
