package main.core.demographics;

import java.util.List;

import core.JSONObject;
import main.core.Engine;
import main.core.Jsonic;
import main.core.Main;
import main.core.Repr;
import main.core.characters.CharacterManager;
import main.core.characters.attributes.Skills;
import main.core.demographics.DemographicsManager.DemographicCategory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;

public class Bloc implements Repr<Bloc>, Jsonic<Bloc> {

    private static List<Bloc> instances = new ArrayList<>();

    private static HashMap<DemographicCategory, HashSet<Bloc>> demographics = new HashMap<DemographicCategory, HashSet<Bloc>>();

    public static List<Bloc> getInstances(){
        return instances;
    }
    public static int getNumberOfBlocs(){
        return instances.size();
    }
    public static int getNumberOfCategories(){
        return demographics.size();
    }

    // INSTANCE VARIABLES -------------------------------------------------------------------------

    private String name;
    private int numVoters;
    private List<main.core.characters.Character> members;
    private float percentageVoters;
    private DemographicCategory category;
    private HashMap<Bloc, Double> overlaps = new HashMap<Bloc, Double>();
    private Bloc superBloc;
    private List<Bloc> subBlocs = new ArrayList<>();

    public Bloc(String name, DemographicCategory category){
        this.name = name;
        this.numVoters = 0;
        this.percentageVoters = 0.0f;
        this.category = category;
        this.superBloc = null;
        this.members = new ArrayList<>();

        Bloc.instances.add(this);
        if(!demographics.containsKey(category)) demographics.put(category, new HashSet<Bloc>());
        demographics.get(category).add(this);
    }

    // CONSTRUCTORS -------------------------------------------------------------------------------

    public Bloc(String name, DemographicCategory category, int numVoters) {
        this.name = name;
        this.numVoters = numVoters;
        this.percentageVoters = numVoters * 1.0f / Main.Engine().DemographicsManager().getNumberVoters();
        this.category = category;
        this.superBloc = null;
        this.members = new ArrayList<>();

        Bloc.instances.add(this);
        if(!demographics.containsKey(category)) demographics.put(category, new HashSet<Bloc>());
        demographics.get(category).add(this);
    }
    public Bloc(String name, DemographicCategory category, float percentageVoters) {
        this.name = name;
        this.numVoters = Math.round(percentageVoters * Main.Engine().DemographicsManager().getNumberVoters());
        this.percentageVoters = percentageVoters;
        this.category = category;
        this.superBloc = null;
        this.members = new ArrayList<>();

        Bloc.instances.add(this);
        if(!demographics.containsKey(category))
            demographics.put(category, new HashSet<Bloc>());
        demographics.get(category).add(this);
    }

    // INSTANCE METHODS ---------------------------------------------------------------------------

    public int getNumVoters(){
        return numVoters;
    }
    public void setNumVoters(int numVoters){
        this.numVoters = numVoters;
        this.percentageVoters = numVoters * 1.0f / Main.Engine().DemographicsManager().getNumberVoters();
    }
    public float getPercentageVoters(){
        return percentageVoters;
    }
    public void setPercentageVoters(float percentageVoters){
        this.percentageVoters = percentageVoters;
        this.numVoters = Math.round(percentageVoters * Main.Engine().DemographicsManager().getNumberVoters());
    }
    public List<main.core.characters.Character> getMembers(){
        return members;
    }
    public void addMember(main.core.characters.Character member){
        this.members.add(member);
    }
    public void removeMember(main.core.characters.Character member) {
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
    public DemographicCategory getDemographicGroup() {
        return this.category;
    }
    public void setDemographicGroup(DemographicCategory category) {
        this.category = category;
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
    @Override
    public Bloc fromRepr(String repr){
        return this;
    }
    public String toRepr(){
        String repr = String.format(
            "%s:[name:\"%s\";numVoters=%d;percentageVoters=%ff;category=\"%s\";];",
            this.getClass().toString().replace("class ", ""),
            this.name,
            this.numVoters,
            this.percentageVoters,
            this.category
        );
        return repr;
    }
    @Override
    public JSONObject toJson() {
        List<JSONObject> fields = new ArrayList<>();
        
        fields.add(new JSONObject("name", name));
        fields.add(new JSONObject("number_voters", numVoters));
        fields.add(new JSONObject("percentage_voters", percentageVoters));
        fields.add(new JSONObject("category", category));
        fields.add(new JSONObject("super_bloc", superBloc != null ? superBloc.getName() : null));
        // List<String> subBlocsNames = new ArrayList<>();
        // for (Bloc subBloc : subBlocs) {
        //     subBlocsNames.add(subBloc.name);
        // }
        // fields.add(new JSONObject("sub_blocs", subBlocsNames));
        // sub-blocs can be resconstructed from super-bloc relationships
        
        String blocJsonName = this.name.replace(" ", "_").toLowerCase().strip();
        return new JSONObject(blocJsonName, fields);
    }
    @Override
    public Bloc fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }
}