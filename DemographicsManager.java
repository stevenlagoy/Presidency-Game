public class DemographicsManager
{
    final static Bloc EVERYONE = Bloc.matchBlocName("everyone");
    final static Bloc VOTERS = Bloc.matchBlocName("voters");

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
        return new Demographics("Millenial", "Evangelical", "European (White)", "Woman");
    }
}
