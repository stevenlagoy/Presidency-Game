/*
 * CongressionalDistrict.java
 * Steven LaGoy
 * Created: 29 October 2024 at 11:20 PM
 * Modified: 12 June 2025
 */

package main.core.map;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import core.JSONObject;
import main.core.Jsonic;
import main.core.Repr;
import main.core.characters.FederalOfficial;
import main.core.characters.FederalOfficial.FederalRole;
import main.core.demographics.Bloc;
import main.core.demographics.DemographicsManager;

public class CongressionalDistrict implements MapEntity, Repr<State>, Jsonic<State> {

    // INSTANCE VARIBALES -------------------------------------------------------------------------

    /** Office ID of the District, including the 2-digit FIPS state portion. I.E.: "3625" is New York's 25th District. */
    private String officeID;
    /** Population of the District. */
    private int population;
    /** Land Area of the District. */
    private double landArea;
    /** Name of the District, I.E.: "Congressional District 1". */
    private String name;
    /** State which the District is within. */
    private State state;
    /** Number of the District. At-large districts are 0. */
    private int districtNum;

    private FederalOfficial representative;

    private Set<String> descriptors;
    private Map<Bloc, Float> demographics;

    // CONSTRUCTORS -------------------------------------------------------------------------------

    public CongressionalDistrict(String officeID, int population, double landArea, String name, String stateName, int districtNum, Set<String> descriptors) {
        this(officeID, population, landArea, name, MapManager.matchState(stateName), districtNum, descriptors);
    }

    public CongressionalDistrict(String officeID, int population, double landArea, String name, State state, int districtNum, Set<String> descriptors) {
        this.officeID = officeID;
        this.population = population;
        this.landArea = landArea;
        this.name = name;
        this.state = state;
        this.districtNum = districtNum;
        setDescriptors(descriptors);
    }

    // GETTERS AND SETTERS ------------------------------------------------------------------------

    // Population : int
    @Override
    public int getPopulation() {
        return population;
    }
    @Override
    public void setPopulation(int population) {
        this.population = Math.max(0, population);
    }
    @Override
    public void addPopulation(int population) {
        this.population = Math.max(0, this.population + population);
    }

    // Land Area : double
    
    @Override
    public double getLandArea() {
        return landArea;
    }
    @Override
    public void setLandArea(double area) {
        this.landArea = Math.max(0.0, area);        
    }

    // Name : string

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // State : State

    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;
    }
    public void setState(String stateName) {
        setState(MapManager.matchState(stateName));
    }

    // District Num : int
    
    public int getDistrictNum() {
        return districtNum;
    }
    public void setDistrictNum(int districtNum) {
        this.districtNum = districtNum;
    }

    // Office ID : String    

    public String getOfficeID() {
        return officeID;
    }
    public void setOfficeID(String officeID) {
        this.officeID = officeID;
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
        this.descriptors.addAll(state.getDescriptors());
        this.demographics = DemographicsManager.demographicsFromDescriptors(descriptors);
    }

    // Representative : FederalOfficial

    public FederalOfficial getRepresentative() {
        return representative;
    }
    public void setRepresentative(FederalOfficial representative) {
        if (representative == null) {
            this.representative = new FederalOfficial();
            this.representative.addRole(FederalRole.REPRESENTATIVE);
        }
        else this.representative = representative;
    }
    
    // REPRESENTATION METHODS ---------------------------------------------------------------------

    @Override
    public JSONObject toJson() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJson'");
    }

    @Override
    public State fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }

    @Override
    public String toRepr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRepr'");
    }

    @Override
    public State fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
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
