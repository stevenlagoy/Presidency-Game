/*
 * State.java
 * Steven LaGoy
 * Created: 28 August 2024 at 11:25 PM
 * Modified: 11 June 2025
 */

package main.core.map;

// IMPORTS ----------------------------------------------------------------------------------------

// Standard Library Imports
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Internal Imports
import core.JSONObject;
import main.core.Engine;
import main.core.Jsonic;
import main.core.Repr;
import main.core.characters.FederalOfficial;
import main.core.characters.PoliticalActor;
import main.core.characters.StateOfficial;
import main.core.characters.FederalOfficial.FederalRole;
import main.core.characters.StateOfficial.StateRole;
import main.core.demographics.Bloc;
import main.core.demographics.DemographicsManager;

/**
 * Map entity for the second-largest geographical division, including States, Commonwealths, District, and Territories.
 * <p>
 * Holds values describing information about the state, its demographics and voting patterns, and leadership.
 */
public class State implements MapEntity, Repr<State>, Jsonic<State> {
    
    private Nation nation;
    /** Unique state FIPS code between 01 and 56. */
    private String FIPS;
    /** Total population of the State. */
    private int population;
    /** Land area of the State. */
    private double landArea;
    /** Full name of the State, IE: State of Alabama, Commonwealth of Virginia. */
    private String fullName;
    /** Common name of the State, IE: Louisiana, Utah. */
    private String commonName;
    /** Two-letter postal abbreviation of the State, IE: NM, CO. */
    private String abbreviation;
    /** Nickname of the state. */
    private String nickname;
    /** Motto of the state. */
    private String motto;
    /** Capital municipality of the state. */
    private Municipality capital;
    private Set<String> descriptors;
    private Map<Bloc, Float> demographics;
    private List<FederalOfficial> senators;
    private StateOfficial governor;
    private StateOfficial lieutenantGovernor;

    // CONSTRUCTORS -------------------------------------------------------------------------------

    /** To be used before Characters are ready to be generated. */
    public State(String FIPS, int population, double landArea, String fullName, String commonName, String abbreviation, String nickname, String motto, String capitalName, Set<String> descriptors) {
        nation = Nation.getInstance();
        setFIPS(FIPS);
        setPopulation(population);
        setLandArea(landArea);
        this.fullName = fullName;
        this.commonName = commonName;
        this.abbreviation = abbreviation;
        this.nickname = nickname;
        this.motto = motto;
        this.capital = capitalName.isEmpty() ? null : MapManager.matchMunicipality(capitalName, abbreviation);
        setDescriptors(descriptors);
    }

    public State(String FIPS, int population, double landArea, String fullName, String commonName, String abbreviation, String nickname, String motto, String capitalName, Set<String> descriptors, List<FederalOfficial> senators, StateOfficial governor, StateOfficial lieutenantGovernor) {
        this(FIPS, population, landArea, fullName, commonName, abbreviation, nickname, motto, capitalName.isEmpty() ? null : MapManager.matchMunicipality(capitalName, abbreviation), descriptors, senators, governor, lieutenantGovernor);
    }

    public State(String FIPS, int population, double landArea, String fullName, String commonName, String abbreviation, String nickname, String motto, Municipality capital, Set<String> descriptors, List<FederalOfficial> senators, StateOfficial governor, StateOfficial lieutenantGovernor) {
        nation = Nation.getInstance();
        setFIPS(FIPS);
        setPopulation(population);
        setLandArea(landArea);
        this.fullName = fullName;
        this.commonName = commonName;
        this.abbreviation = abbreviation;
        this.nickname = nickname;
        this.motto = motto;
        this.capital = capital;
        setDescriptors(descriptors);
        setSenators(senators);
        this.governor = governor != null ? governor : new StateOfficial(this);
        this.lieutenantGovernor = lieutenantGovernor != null ? lieutenantGovernor : new StateOfficial(this);
    }

    // GETTERS AND SETTERS ------------------------------------------------------------------------

    // FIPS Code String
    public String getFIPS() {
        return FIPS;
    }
    public void setFIPS(String FIPS) {
        for (char c : FIPS.toCharArray()) {
            if (!Character.isDigit(c)) {
                Engine.log("INVALID FIPS", String.format("Attempted to assign FIPS code %s, which is not numeric.", FIPS), new Exception());
                return;
            }
        }
        this.FIPS = FIPS;
    }

    // Population : int
    public int getPopulation() {
        return population;
    }
    public void setPopulation(int population) {
        this.population = Math.max(0, population);
    }
    public void addPopulation(int population) {
        this.population = Math.max(0, this.population + population);
    }

    // Land Area : double
    public double getLandArea() {
        return landArea;
    }
    public void setLandArea(double area) {
        this.landArea = Math.max(0, area);
    }

    // Full Name : String
    public String getFullName(){
        return Engine.getLocalization(fullName);
    }
    public void setFullName(String name){
        this.fullName = name;
    }

    // Common Name : String
    public String getCommonName() {
        return commonName;
    }
    public void setCommonName(String name) {
        this.commonName = name;
    }

    // Abbreviation : String
    public String getAbbreviation() {
        return abbreviation;
    }
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    // Nickname : String
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    // Motto : String
    public String getMotto() {
        return Engine.getLocalization(motto);
    }
    public void setMotto(String motto) {
        this.motto = motto;
    }

    // Capital : Municipality
    public Municipality getCapital() {
        return capital;
    }
    public void setCapital(Municipality capital) {
        this.capital = capital;
    }

    // Descriptors : List of String
    @Override
    public Set<String> getDescriptors() {
        return descriptors;
    }
    @Override
    public void setDescriptors(Set<String> descriptors) {
        this.descriptors = new HashSet<>(descriptors);
        evaluateDemographics();
    }
    @Override
    public boolean hasDescriptor(String descriptor) {
        return this.descriptors.contains(descriptor);
    }
    @Override
    public boolean addDescriptor(String descriptor) {
        boolean modified = this.descriptors.add(descriptor);
        if (modified) evaluateDemographics();
        return modified;
    }
    @Override
    public boolean addAllDescriptors(Collection<String> descriptors) {
        boolean modified = this.descriptors.addAll(descriptors);
        if (modified) evaluateDemographics();
        return modified;
    }
    @Override
    public boolean removeDescriptor(String descriptor) {
        boolean modified = this.descriptors.remove(descriptor);
        if (modified) evaluateDemographics();
        return modified;
    }
    @Override
    public boolean removeAllDescriptors(Collection<String> descriptors) {
        boolean modified = this.descriptors.removeAll(descriptors);
        if (modified) evaluateDemographics();
        return modified;
    }

    // Demographics : Map of Bloc to Float
    
    @Override
    public Map<Bloc, Float> getDemographics() {
        return demographics;
    }
    @Override
    public float getDemographicPercentage(Bloc bloc) {
        return this.demographics.get(bloc) != null ? this.demographics.get(bloc) : 0.0f;
    }
    @Override
    public int getDemographicPopulation(Bloc bloc) {
        return Math.round(getDemographicPercentage(bloc) * population);
    }
    @Override
    public void evaluateDemographics() {
        this.descriptors.addAll(nation.getDescriptors());
        this.demographics = DemographicsManager.demographicsFromDescriptors(descriptors);
    }

    // Senators List of Federal Official
    public List<FederalOfficial> getSenators() {
        return senators;
    }
    /**
     * Sets the senators list to the first two FederalOfficials in the passed List, and generates any necessary.
     * Pass with an empty list or {@code null} to have both senators generated.
     */
    public void setSenators(List<FederalOfficial> senators) {
        this.senators = new ArrayList<>();
        if (senators == null || senators.isEmpty()) {
            addSenator(new FederalOfficial());
            addSenator(new FederalOfficial());
        }
        else if (senators.size() == 1) {
            addSenator(senators.get(0));
            addSenator(new FederalOfficial());
        }
        else {
            addSenator(senators.get(0));
            addSenator(senators.get(1));
        }
    }
    public boolean hasSenator(PoliticalActor senator) {
        return senators.contains(senator);
    }
    public boolean addSenator(FederalOfficial senator) {
        senator.addRole(FederalRole.SENATOR);
        return this.senators.add(senator);
    }
    public boolean removeSenator(FederalOfficial senator) {
        senator.removeRole(FederalRole.SENATOR);
        return senators.remove(senator);
    }

    // Governor State Official
    public StateOfficial getGovernor() {
        return governor;
    }
    public void setGovernor(StateOfficial governor) {
        if (governor == null) {
            this.governor = new StateOfficial(this);
            this.governor.addRole(StateRole.GOVERNOR);
        }
        else this.governor = governor;
    }
    
    // Lieutenant Governor State Official
    public StateOfficial getLieutenantGovernor() {
        return lieutenantGovernor;
    }
    public void setLieutenantGovernor(StateOfficial lieutenantGovernor) {
        if (lieutenantGovernor == null) {
            this.lieutenantGovernor = new StateOfficial(this);
            this.lieutenantGovernor.addRole(StateRole.LIEUTENANT_GOVERNOR);
        }
        else this.lieutenantGovernor = lieutenantGovernor;
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public State fromRepr(String repr) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public String toRepr() {
        String [] demographicsStrings = new String[demographics.size()];
        for (int i = 0; i < demographics.size(); i++) {
            demographicsStrings[i] = "PLACEHOLDER : 0.0";
        }
        String demographicsRepr = Repr.arrayToReprList(demographicsStrings);
        String [] senatorsStrings = new String[senators.size()];
        for (int i = 0; i < senators.size(); i++) {
            senatorsStrings[i] = senators.get(i).getName() + ", " + senators.get(i).getName().getLegalName();
        }
        String senatorsRepr = Repr.arrayToReprList(senatorsStrings);
        String repr = String.format(
            "%s:[FIPS:%s;name=%s;population=%d;abbreviation=%s;motto=%s;nickname=%s;congressionalDistricts=[%s];counties=[%s];cities=[%s];universities=[%s];demographics=[%s];senators=[%s];governor=%s;];",
            this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1],
            this.FIPS,
            this.fullName,
            this.population,
            this.abbreviation,
            this.motto,
            this.nickname,
            demographicsRepr,
            senatorsRepr,
            this.governor.getName().getLegalName()
        );
        return repr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJson() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJson'");
    }

    // OBJECT METHODS -----------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        State other = (State) obj;
        return this.toString().equals(other.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return 0;
    }
}