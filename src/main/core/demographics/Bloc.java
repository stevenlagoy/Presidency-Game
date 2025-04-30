package main.core.demographics;

import java.util.List;

import main.core.Engine;
import main.core.Repr;
import main.core.characters.CharacterManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;

public class Bloc implements Repr {

    private static List<Bloc> instances = new ArrayList<>();

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

    private String name;
    private int numVoters;
    private List<main.core.characters.Character> members;
    private float percentageVoters;
    private String demographicGroup;
    private HashMap<Bloc, Double> overlaps = new HashMap<Bloc, Double>();
    private Bloc superBloc;
    private List<Bloc> subBlocs = new ArrayList<>();

    public Bloc(String name, String demographicGroup){
        this.name = name;
        this.numVoters = 0;
        this.percentageVoters = 0.0f;
        this.demographicGroup = demographicGroup;
        this.superBloc = null;
        this.members = new ArrayList<>();

        Bloc.instances.add(this);
        if(!demographics.containsKey(demographicGroup)) demographics.put(demographicGroup, new HashSet<Bloc>());
        demographics.get(demographicGroup).add(this);
    }
    public Bloc(String name, String demographicGroup, int numVoters) {
        this.name = name;
        this.numVoters = numVoters;
        this.percentageVoters = numVoters * 1.0f / DemographicsManager.totalVoters;
        this.demographicGroup = demographicGroup;
        this.superBloc = null;
        this.members = new ArrayList<>();

        Bloc.instances.add(this);
        if(!demographics.containsKey(demographicGroup)) demographics.put(demographicGroup, new HashSet<Bloc>());
        demographics.get(demographicGroup).add(this);
    }
    public Bloc(String name, String demographicGroup, float percentageVoters) {
        this.name = name;
        this.numVoters = Math.round(percentageVoters * DemographicsManager.totalVoters);
        this.percentageVoters = percentageVoters;
        this.demographicGroup = demographicGroup;
        this.superBloc = null;
        this.members = new ArrayList<>();

        Bloc.instances.add(this);
        if(!demographics.containsKey(demographicGroup)) demographics.put(demographicGroup, new HashSet<Bloc>());
        demographics.get(demographicGroup).add(this);
    }

    public int getNumVoters(){
        return numVoters;
    }
    public void setNumVoters(int numVoters){
        this.numVoters = numVoters;
        this.percentageVoters = numVoters * 1.0f / DemographicsManager.totalVoters;
    }
    public float getPercentageVoters(){
        return percentageVoters;
    }
    public void setPercentageVoters(float percentageVoters){
        this.percentageVoters = percentageVoters;
        this.numVoters = Math.round(percentageVoters * DemographicsManager.totalVoters);
    }
    public List<main.core.characters.Character> getMembers(){
        return members;
    }
    public void addMember(main.core.characters.Character member){
        this.members.add(member);
    }
    public void removeMember(Character member) {
        this.members.remove(member);
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public List<String> getNestedNames() {
        // return a list of the names of this bloc and all superblocs
        List<String> names = new ArrayList<>();
        Bloc bloc = this;
        do {
            names.add(bloc.getName());
            bloc = bloc.getSuperBloc();
        } while (bloc != null);
        return names;
    }
    public List<Bloc> getNestedSuperBlocs() {
        List<Bloc> blocs = new ArrayList<>();
        Bloc bloc = this;
        do {
            blocs.add(bloc);
            bloc = bloc.getSuperBloc();
        } while (bloc != null);
        return blocs;
    }
    public String getDemographicGroup() {
        return this.demographicGroup;
    }
    public void setDemographicGroup(String group) {
        this.demographicGroup = group;
    }
    public Bloc getSuperBloc() {
        return superBloc;
    }
    public void setSuperBloc(Bloc superBloc) {
        this.superBloc = superBloc;
    }
    public List<Bloc> getSubBlocs() {
        return this.subBlocs;
    }
    public void addSubBloc(Bloc bloc) {
        bloc.setSuperBloc(this);
        this.subBlocs.add(bloc);
    }
    public void addSubBlocs(Bloc[] blocs) {
        for (Bloc bloc : blocs) {
            this.addSubBloc(bloc);
        }
    }
    public <T extends Collection<Bloc>> void addSubBlocs(T blocs) {
        for (Bloc bloc : blocs) {
            this.addSubBloc(bloc);
        }
    }
    public void removeSubBloc(Bloc bloc) {
        bloc.setSuperBloc(null);
        this.subBlocs.remove(bloc);
    }
    public void clearSubBlocs() {
        this.subBlocs.clear();
    }

    /**
     * Calculates how over- or under-represented a bloc is among all Character instances. Ratio of actual membership to expected membership. O(1)
     * @param bloc The bloc to be evaluated for representation.
     * @return A float value for representation. <1 indicates the bloc is under-represented, while >1 indicates the bloc is over-represented. 
     */

    public void fromRepr(String repr){

    }
    public String toRepr(){
        String repr = String.format(
            "%s:[name:\"%s\";numVoters=%d;percentageVoters=%ff;demographicGroup=\"%s\";];",
            this.getClass().toString().replace("class ", ""),
            this.name,
            this.numVoters,
            this.percentageVoters,
            this.demographicGroup
        );
        return repr;
    }
}