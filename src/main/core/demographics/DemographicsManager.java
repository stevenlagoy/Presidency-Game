package main.core.demographics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.JSONObject;
import core.JSONProcessor;
import main.core.Engine;
import main.core.FilePaths;
import main.core.characters.Character;
import main.core.characters.CharacterManager;

public class DemographicsManager
{
    public static int totalVoters = 341_275_500; // as of 1 Feb 2025

    private static Map<String, List<Bloc>> demographicBlocs = new HashMap<>();
    private static final String[] characterBlocCategories = { "Generation", "Religion", "Race / Ethnicity", "Presentation" };

    //final static Bloc EVERYONE = Bloc.matchBlocName("everyone");
    //final static Bloc VOTERS = Bloc.matchBlocName("voters");
    //public final static Bloc EVERYONE = new Bloc("Everyone", null, 1.0f);
    //public final static Bloc VOTERS = new Bloc("Voters", null, 0.7083f);

    public static int[] getPopulationPyramid(Bloc applicableBloc){
        int[] pyramid = {1, 2, 3, 4, 5};
        return pyramid;
    }
    public static double[] getPopulationPyramidPercent(Bloc applicableBloc){
        int[] pyramid = getPopulationPyramid(applicableBloc);
        int totalMembers = applicableBloc.getMembers().size();
        double[] percentagesPyramid = new double[pyramid.length];

        for(int i = 0; i < pyramid.length; i++){
            percentagesPyramid[i] = pyramid[i] / totalMembers;
        }
        return percentagesPyramid;
    }

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
    }

    public static Demographics getMostCommonDemographics(){
        // generation, presentation, raceEthnicity, religion
        // should make this adaptive to the current population
        return new Demographics("Millennial", "White Catholic", "English", "Woman");
    }

    public static void createDemographicBlocs() {
        JSONObject json = JSONProcessor.processJson(FilePaths.BLOCS);

        for (Object categoryObj : json.getAsList()) {
            if (categoryObj instanceof JSONObject categoryJson) {
                String categoryName = categoryJson.getKey();
                List<Bloc> blocs = createBlocs(categoryName, categoryJson.getAsObject());
                demographicBlocs.put(categoryName, blocs);
            }
        }
    }

    private static List<Bloc> createBlocs(String category, JSONObject structure) {
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

    public static Map<String, List<Bloc>> getDemographicBlocs() {
        return demographicBlocs;
    }

    public static Demographics generateDemographics(){
        // select the currently most underrepresented demographic bloc
        // given that bloc's overlap, select the most underrepresented of the rest of the blocs
        // return those together

        if (CharacterManager.getNumCharacters() == 0) {
            return getMostCommonDemographics();
        }

        Demographics underrepresentedDemographics = new Demographics();

        for (String category : characterBlocCategories){
            Bloc underrepresentedBloc = findMostUnderrepresentedBloc(demographicBlocs.get(category));
            switch (category) {
                case "Generation" :
                    underrepresentedDemographics.setGeneration(underrepresentedBloc);
                    break;
                case "Religion" :
                    underrepresentedDemographics.setReligion(underrepresentedBloc);
                    break;
                case "Race / Ethnicity" :
                    underrepresentedDemographics.setRaceEthnicity(underrepresentedBloc);
                    break;
                case "Presentation" :
                    underrepresentedDemographics.setPresentation(underrepresentedBloc);
                    break;
            }
        }

        return underrepresentedDemographics;
    }
    public static String[] getCharacterBlocCategories() {
        return characterBlocCategories;
    }
    public static boolean isCharacterBlocCategory(String demographicGroup) {
        return Arrays.asList(characterBlocCategories).contains(demographicGroup);
    }
    /**
     * Selects the most Underrepresented Bloc from the passed List of Blocs their subBloc trees. Underrepresentation is determined by the bloc's Representation Ratio, or the actual representation / expected representation.
     * @param blocs A list of blocs to search (along with their subBloc trees).
     * @return The innermost bloc which is most underrepresented.
     */
    private static Bloc findMostUnderrepresentedBloc(List<Bloc> blocs){        
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

    private static float determineRepresentationRatio(Bloc bloc) {
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

    public static Demographics randomDemographics() {
        Bloc generation, religion, raceEthnicity, presentation;
        try {
            generation = demographicBlocs.get("Generation").get(Engine.randInt(demographicBlocs.get("Generation").size() - 1));
            while (generation.getSubBlocs().size() != 0) {
                generation = generation.getSubBlocs().get(Engine.randInt(generation.getSubBlocs().size() - 1));
            }
            religion = demographicBlocs.get("Religion").get(Engine.randInt(demographicBlocs.get("Religion").size() - 1));
            while (religion.getSubBlocs().size() != 0) {
                religion = religion.getSubBlocs().get(Engine.randInt(religion.getSubBlocs().size() - 1));
            }
            raceEthnicity = demographicBlocs.get("Race / Ethnicity").get(Engine.randInt(demographicBlocs.get("Race / Ethnicity").size() - 1));
            while (raceEthnicity.getSubBlocs().size() != 0) {
                raceEthnicity = raceEthnicity.getSubBlocs().get(Engine.randInt(raceEthnicity.getSubBlocs().size() - 1));
            }
            presentation = demographicBlocs.get("Presentation").get(Engine.randInt(demographicBlocs.get("Presentation").size() - 1));
            while (presentation.getSubBlocs().size() != 0) {
                presentation = presentation.getSubBlocs().get(Engine.randInt(presentation.getSubBlocs().size() - 1));
            }
            return new Demographics(generation, religion, raceEthnicity, presentation);
        }
        catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addCharacterToBlocs(Character character, Demographics demographics) {
        for (Bloc bloc : demographics.toBlocsArray()) {
            bloc.addMember(character);
        }
    }

    public static Bloc matchBlocName(String name){
        List<Bloc> blocs  = Bloc.getInstances();
        for(Bloc bloc : Bloc.getInstances()){
            if(bloc.getName().equals(name))
                return bloc;
        }
        Engine.log("INVALID BLOC NAME", String.format("The Bloc name \"%s\" is non-existent and could not be matched.", name), new Exception());
        return null;
    }

}
