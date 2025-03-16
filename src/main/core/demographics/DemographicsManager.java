package main.core.demographics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.core.Engine;
import main.core.FilePaths;

public class DemographicsManager
{

    private static Map<String, List<Bloc>> demographicBlocs = new HashMap<>();

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
        int totalMembers = applicableBloc.getMembership();
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

}
