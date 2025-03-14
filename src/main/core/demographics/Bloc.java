package main.core.demographics;

import java.util.List;

import main.core.Engine;
import main.core.Repr;
import main.core.characters.CharacterManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

public class Bloc implements Repr {

    private static List<Bloc> instances = new ArrayList<>();
    public static int totalVoters = 341_275_500; // as of 1 Feb 2025

    private static HashMap<String, HashSet<Bloc>> demographics = new HashMap<String, HashSet<Bloc>>();

    public static List<Bloc> getInstances(){
        return instances;
    }
    public static int getNumberOfBlocs(){
        return instances.size();
    }
    public static int getNumberOfCategories(){
        return demographics.size();
    }

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
        Bloc[] characterBlocs = new Bloc[getNumberOfCategories()];
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
        Engine.log("INVALID BLOC NAME", String.format("The Bloc name \"%s\" is non-existent and could not be matched.", name), new Exception());
        return null;
    }

    private String name;
    private int numVoters;
    private int membership; // number of characters
    private float percentageVoters;
    private String demographicGroup;
    private HashMap<Bloc, Double> overlaps = new HashMap<Bloc, Double>();

    public Bloc(String name, String demographicGroup){
        this.name = name;
        this.numVoters = 0;
        this.percentageVoters = 0.0f;
        this.demographicGroup = demographicGroup;

        Bloc.instances.add(this);
        if(!demographics.containsKey(demographicGroup)) demographics.put(demographicGroup, new HashSet<Bloc>());
        demographics.get(demographicGroup).add(this);
    }
    public Bloc(String name, String demographicGroup, int numVoters)
    {
        this.name = name;
        this.numVoters = numVoters;
        this.percentageVoters = numVoters * 1.0f / totalVoters;
        this.demographicGroup = demographicGroup;

        Bloc.instances.add(this);
        if(!demographics.containsKey(demographicGroup)) demographics.put(demographicGroup, new HashSet<Bloc>());
        demographics.get(demographicGroup).add(this);
    }
    public Bloc(String name, String demographicGroup, float percentageVoters)
    {
        this.name = name;
        this.numVoters = Math.round(percentageVoters * totalVoters);
        this.percentageVoters = percentageVoters;

        Bloc.instances.add(this);
        if(!demographics.containsKey(demographicGroup)) demographics.put(demographicGroup, new HashSet<Bloc>());
        demographics.get(demographicGroup).add(this);
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
            return (this.membership * 1.0f / CharacterManager.numCharacters()) / (this.percentageVoters);
        }
        catch(ArithmeticException e){
            return 1.0f;
        }
    }

    public void fromRepr(String repr){

    }
    public String toRepr(){
        String repr = String.format(
            "%s:[name:\"%s\";numVoters=%d;membership=%d;percentageVoters=%ff;demographicGroup=\"%s\";];",
            this.getClass().toString().replace("class ", ""),
            this.name,
            this.numVoters,
            this.membership,
            this.percentageVoters,
            this.demographicGroup
        );
        return repr;
    }
}