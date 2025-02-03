import java.util.HashMap;

public class DemographicsManager
{
    // required filenames
    private static final String blocs_filename = "blocs.json";

    //final static Bloc EVERYONE = Bloc.matchBlocName("everyone");
    //final static Bloc VOTERS = Bloc.matchBlocName("voters");
    final static Bloc EVERYONE = new Bloc("Everyone", null, 1.0f);
    final static Bloc VOTERS = new Bloc("Voters", null, 0.7083f);

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
        // read the JSON file and create blocs
        HashMap<Object, Object> json = Engine.readJSONFile(blocs_filename);
        // json will be a map Strings to maps of strings to floats
        for(Object key : json.keySet()){
            @SuppressWarnings("unchecked")
            HashMap<Object, Object> group = (HashMap<Object, Object>) json.get(key); // known structure of the JSON file
            for(Object bloc : group.keySet()){
                new Bloc(bloc.toString(), key.toString(), Float.parseFloat((String) group.get(bloc)));
            }
        }
    }
}