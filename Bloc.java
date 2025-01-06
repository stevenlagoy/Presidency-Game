import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

public class Bloc implements Repr
{
    private static List<Bloc> instances = new ArrayList<>();
    public static int totalVoters;

    private static HashMap<String, HashSet<Bloc>> demographics = new HashMap<String, HashSet<Bloc>>();
    private static int numberOfBlocs;
    public static Bloc[] selectBlocs(){
        /*
         * Determine which bloc is most underrepresented out of all currently active characters
         * Starting with that bloc, determine which of that bloc's overlap is most underrepresented
         * If two are equally underrepresented, select the one that appears first on the list
         * Continue until all demographic fields have been filled
         * Return the demographics
         */
        // this function selects the most underrepresented of each group, ignoring overlaps.
        // should be changed to evaluate overlaps
        Bloc[] characterBlocs = new Bloc[numberOfBlocs];
        int i = 0;

        Bloc underrepresentedBloc;
        double underrepresentedValue;
        for(HashSet<Bloc> group : demographics.values()){
            underrepresentedBloc = (Bloc) group.toArray()[0];
            underrepresentedValue = underrepresentedBloc.determineRepresentationRatio();
            for(Bloc bloc : group){
                if(bloc.determineRepresentationRatio() < underrepresentedValue){
                    underrepresentedBloc = bloc;
                    underrepresentedValue = bloc.determineRepresentationRatio();
                }
            }
            characterBlocs[i++] = underrepresentedBloc;
        }
        return characterBlocs;
    }

    public static Bloc matchBlocName(String name){
        for(Bloc bloc : instances){
            if(bloc.name.equals(name)) return bloc;
        }
        Engine.log("INVALID BLOC NAME", String.format("The Bloc name \"%s\" is non-existent and could not be matched.", name), Thread.currentThread().getStackTrace().toString());
        return null;
    }

    private String name;
    private int numVoters;
    private int membership;
    private int characterMembership;
    private float percentageVoters;
    private String demographicGroup;
    private HashMap<Bloc, Double> overlaps = new HashMap<Bloc, Double>();

    public Bloc(String name, int numVoters, String demographicGroup)
    {
        this.name = name;
        this.numVoters = numVoters;
        this.percentageVoters = numVoters * 1.0f / totalVoters;
        this.demographicGroup = demographicGroup;
    }
    public Bloc(String name, float percentageVoters, String demographicGroup)
    {
        this.name = name;
        this.numVoters = Math.round(percentageVoters * totalVoters);
        this.percentageVoters = percentageVoters;
    }

    public int getNumVoters(){
        return numVoters;
    }
    public void setNumVoters(int numVoters){
        this.numVoters = numVoters;
        this.percentageVoters = numVoters * 1.0f / totalVoters;
    }
    public float getPercentageVoters(){
        return percentageVoters;
    }
    public void setPercentageVoters(float percentageVoters){
        this.percentageVoters = percentageVoters;
        this.numVoters = Math.round(percentageVoters * totalVoters);
    }
    public int getMembership(){
        return membership;
    }
    public void setMembership(int membership){
        this.membership = membership;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public List<Bloc> getInstances(){
        return instances;
    }
    /**
     * Calculates how over- or under-represented a bloc is among all Character instances. Ratio of actual membership to expected membership. O(1)
     * @param bloc The bloc to be evaluated for representation.
     * @return A float value for representation. <1 indicates the bloc is under-represented, while >1 indicates the bloc is over-represented. 
     */
    private float determineRepresentationRatio(){
        // Returns ratio of actual character membership to expected membership
        // <1 if underrepresented, >1 if overrepresented
        try
        {
            if(CharacterManager.numCharacters() == 0) return 1.0f;
            return (this.characterMembership * 1.0f / CharacterManager.numCharacters()) / (this.percentageVoters);
        }
        catch(ArithmeticException e){
            return 1.0f;
        }
    }
}