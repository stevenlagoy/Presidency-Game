package main.core.demographics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.core.Engine;
import main.core.FilePaths;
import main.core.characters.Character;
import main.core.characters.CharacterManager;

public class DemographicsManager
{

    private static Map<String, List<Bloc>> demographicBlocs = new HashMap<>();
    private static final String[] characterBlocCategories = { "Generation", "Religion", "Race / Ethnicity", "Presentation" };

    //final static Bloc EVERYONE = Bloc.matchBlocName("everyone");
    //final static Bloc VOTERS = Bloc.matchBlocName("voters");
    public final static Bloc EVERYONE = new Bloc("Everyone", null, 1.0f);
    public final static Bloc VOTERS = new Bloc("Voters", null, 0.7083f);

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

    public static Demographics getMostCommonDemographics(){
        // generation, presentation, raceEthnicity, religion
        // should make this adaptive to the current population
        return new Demographics("Millennial", "Evangelical", "White", "Woman");
    }
    public static void createDemographicBlocs() {
        HashMap<Object, Object> json = Engine.readJSONFile(FilePaths.blocs);

        // Loop over Demographic categories
        for (Object categoryKey : json.keySet()) {
            String categoryName = categoryKey.toString();
            @SuppressWarnings("unchecked")
            HashMap<Object, Object> structure = (HashMap<Object, Object>) json.get(categoryKey);
            DemographicsManager.demographicBlocs.put(categoryName, createBlocs(categoryName, structure));
        }
    }

    private static List<Bloc> createBlocs(String category, HashMap<Object, Object> structure){
        /*
         * Structure is a JSON object:object map.
         * We assume structure has only one key entry
         * The top-level key object is the parent bloc
         * If the value is numerical, then the bloc has no children
         * If the value is another structure, recursively call 
         */
        List<Bloc> blocs = new ArrayList<>();
        Bloc parent;
        for (Object key : structure.keySet()) {
            try {
                // assume base case: no nested blocs
                parent = new Bloc(key.toString(), category, Float.parseFloat(structure.get(key).toString()));
                blocs.add(parent);
            }
            catch (Exception e) {
                // recursive case: has nested blocs
                parent = new Bloc(key.toString(), category);
                parent.addSubBlocs(createBlocs(category, (HashMap<Object, Object>) structure.get(key)));
                blocs.add(parent);
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

        if (CharacterManager.getAllCharacters().length == 0) {
            return getMostCommonDemographics();
        }

        Demographics underrepresentedDemographics = new Demographics();

        for (String category : characterBlocCategories){
            Bloc underrepresentedBloc = null;
            float underrepresentedValue = Float.MAX_VALUE;
            Bloc candidate = findMostUnderrepresentedBloc(demographicBlocs.get(category));
            float ratio = determineRepresentationRatio(candidate);

            if (ratio < underrepresentedValue) {
                underrepresentedValue = ratio;
                underrepresentedBloc = candidate;
            }
            else if (ratio == underrepresentedValue && candidate.getPercentageVoters() > underrepresentedBloc.getPercentageVoters()) {
                underrepresentedValue = ratio;
                underrepresentedBloc = candidate;
            }
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
    private static Bloc findMostUnderrepresentedBloc(List<Bloc> blocs){
        if (blocs == null || blocs.isEmpty()) return null;
        
        Bloc underrepresentedBloc = blocs.get(0);
        float underrepresentedValue = determineRepresentationRatio(underrepresentedBloc);

        for (Bloc bloc : blocs) {
            if (bloc.getSubBlocs().isEmpty()) {
                float representationRatio = determineRepresentationRatio(bloc);
                if (representationRatio < underrepresentedValue) {
                    underrepresentedBloc = bloc;
                    underrepresentedValue = representationRatio;
                }
                else if (representationRatio == underrepresentedValue && bloc.getPercentageVoters() > underrepresentedBloc.getPercentageVoters()) {
                    underrepresentedBloc = bloc;
                    underrepresentedValue = representationRatio;
                }
            }
            else {
                underrepresentedBloc = findMostUnderrepresentedBloc(bloc.getSubBlocs());
                underrepresentedValue = determineRepresentationRatio(underrepresentedBloc);
            }
        }

        return underrepresentedBloc;
    }

    private static float determineRepresentationRatio(Bloc bloc) {
        // Returns ratio of actual character membership to expected membership
        // <1 if underrepresented, >1 if overrepresented, =1 if perfectly represented
        try {
            if(CharacterManager.numCharacters() == 0) return 1.0f; // if there are no characters, every bloc is perfectly represented
            float expectedRepresentation = bloc.getPercentageVoters();
            float actualRepresentation = bloc.getMembers().size() * 1.0f / CharacterManager.numCharacters();
            float representationRatio = actualRepresentation / expectedRepresentation;

            System.out.printf("Bloc Name : %60s, \tCount : %5d, \tExpected : %1.5f, \tActual : %1.5f, \tRatio : %1.5f.%n", bloc.getName(), bloc.getMembers().size(), expectedRepresentation, actualRepresentation, representationRatio);

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

}
