/*
 * County.java
 * Steven LaGoy
 * Created: 11 December 2024 at 5:18 PM
 * Modified: 11 June 2025
 */

package main.core.map;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import core.JSONObject;
import main.core.Jsonic;
import main.core.Main;
import main.core.Repr;
import main.core.demographics.Bloc;
import main.core.demographics.DemographicsManager;

public class County implements MapEntity, Repr<County>, Jsonic<County> {

    // INSTANCE VARIABLES

    /** State the County is within. */
    private State state;
    /** Unique 5-digit FIPS code, including the State FIPS. */
    private String FIPS;
    /** Population of the County. */
    private int population;
    /** Land area of the County. */
    private double landArea;
    /** Full name of the County, including "County of ..." or "... Borough" &c. */
    private String fullName;
    /** Common name of the County, without prefix. */
    private String commonName;
    /** Municipality serving as the County Seat. Does not have to be within the County itself, but must be in the same State. */
    private Municipality countySeat;

    private Set<String> descriptors;
    private Map<Bloc, Float> demographics;

    // CONSTRUCTORS -------------------------------------------------------------------------------

    public County(String FIPS, int population, double landArea, String fullName, String commonName, String stateName, String countySeatName, Set<String> descriptors) {
        this(FIPS, population, landArea, fullName, commonName, Main.Engine().MapManager().matchState(stateName), countySeatName.isEmpty() ? null : Main.Engine().MapManager().matchMunicipality(countySeatName, stateName), descriptors);
    }

    public County(String FIPS, int population, double landArea, String fullName, String commonName, State state, Municipality countySeat, Set<String> descriptors) {
        this.FIPS = FIPS;
        setPopulation(population);
        setLandArea(landArea);
        this.fullName = fullName;
        this.commonName = commonName;
        this.state = state;
        this.countySeat = countySeat;
        setDescriptors(descriptors);
    }

    // GETTERS AND SETTERS ------------------------------------------------------------------------

    // FIPS Code : String
    public String getFIPS() {
        return FIPS;
    }
    public void setFIPS(String FIPS) {
        this.FIPS = FIPS;
    }

    // Population : int
    public int getPopulation() {
        return population;
    }
    public void setPopulation(int population) {
        this.population = population;
        if (this.population < 0) this.population = 0;
    }
    public void addPopulation(int population) {
        this.population += population;
        if (this.population < 0) this.population = 0;
    }

    // Land Area : double
    public double getLandArea() {
        return this.landArea;
    }
    public void setLandArea(double area) {
        this.landArea = area;
        if (this.landArea < 0) this.landArea = 0;
    }

    // Full Name : String
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String name) {
        this.fullName = name;
    }

    // Common Name : String
    public String getCommonName() {
        return commonName;
    }
    public void setCommonName(String name) {
        this.commonName = name;
    }

    // State : State
    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;
    }
    public void setState(String stateName) {
        this.state = Main.Engine().MapManager().matchState(stateName);
    }

    // County Seat : Municipality
    public Municipality getCountySeat() {
        return countySeat;
    }
    public void setCountySeat(Municipality countySeat) {
        this.countySeat = countySeat;
    }
    public void setCountySeat(String countySeat) {
        this.countySeat = Main.Engine().MapManager().matchMunicipality(countySeat, this.state);
    }

    // Descriptors : List of Strings
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
        this.descriptors.addAll(state.getDescriptors());
        // TODO this.demographics = Main.Engine().DemographicsManager().demographicsFromDescriptors(descriptors);
    }


    // REPRESENTATION METHODS ---------------------------------------------------------------------

    @Override
    public JSONObject toJson() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJson'");
    }

    @Override
    public County fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }

    @Override
    public String toRepr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRepr'");
    }

    @Override
    public County fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }

    @Override
    public String toString() {
        return "";
        // TODO
    }
    
    // OBJECT METHODS -----------------------------------------------------------------------------

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }
}
