/*
 * Municipality.java
 * Steven LaGoy
 * Created: 05 June 2025 at 11:55 PM. Renamed from City.java
 * Modified: 12 June 2025
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
import java.util.TimeZone;

// Internal Imports
import core.JSONObject;
import main.core.Engine;
import main.core.Jsonic;
import main.core.Logger;
import main.core.Main;
import main.core.Repr;
import main.core.characters.LocalOfficial;
import main.core.characters.LocalOfficial.LocalRole;
import main.core.demographics.DemographicsManager;
import main.core.demographics.Bloc;

/**
 * Municipalities are various types of inhabited places in the United States. This is the broadest
 * term used to describe Cities, Villages, Towns, and other kinds of Census-Designated populated areas.
 */
public class Municipality implements MapEntity, Repr<Municipality>, Jsonic<Municipality> {

    /** TypeClass describes the various classes or levels of local government, which vary by state. */
    public static enum TypeClass {
        CITY ("City"),
        TOWN ("Town"),
        VILLAGE ("Village"),
        FIRST_CLASS ("First Class"),
        SECOND_CLASS ("Second Class"),
        THIRD_CLASS ("Third Class"),
        HOME_RULE ("Home Rule");

        public final String title;
        private TypeClass(String title) { this.title = title; }

        public static final TypeClass defaultTypeClass = TypeClass.CITY;
        public static TypeClass matchTypeClass(String typeClass) {
            String target = typeClass.toUpperCase().trim().replaceAll("\\s+","_");
            for (TypeClass tc : TypeClass.values()) {
                if (tc.toString().equals(target)) return tc;
            }
            return null;
        }
    }

    // INSTANCE VARIABLES -------------------------------------------------------------------------
    
    /** Unique FIPS code, including the State (and possibly County) FIPS. */
    private String FIPS;
    /** Population of the Municipality. */
    private int population;
    /** Land area of the Municipality. */
    private double landArea;
    /** Name of the Municipality, potentially including "City of ..." */
    private String name;
    /** TypeClass of the Municipality, like City, Town, Village, &c. */
    private TypeClass typeClass;
    private TimeZone standardTimeZone;
    private TimeZone daylightTimeZone;
    private List<County> counties;
    private State state;

    private LocalOfficial mayor;

    /** Geographic descriptors. County and State descriptors will be inherited. */
    private Set<String> descriptors;
    /** Percentages of the municipality which belong to each Bloc. */
    private Map<Bloc, Float> demographics;

    // For contract / proxy calculations in Travel
    private Municipality contractLocation;
    private double connectivity;

    // CONSTRUCTORS -------------------------------------------------------------------------------

    public Municipality(String FIPS, int population, double landArea, String name, String typeClass, String standardTimeZone, String daylightTimeZone, String stateName, List<String> countiesNames, Set<String> descriptors) {
        this.FIPS = FIPS;
        setPopulation(population);
        setLandArea(landArea);
        this.name = name;
        this.typeClass = TypeClass.matchTypeClass(typeClass);
        this.standardTimeZone = TimeZone.getTimeZone(standardTimeZone);
        this.daylightTimeZone = TimeZone.getTimeZone(daylightTimeZone);
        this.counties = new ArrayList<>();
        setState(Main.Engine().MapManager().matchState(stateName));
        setCountiesByNames(countiesNames);
        setDescriptors(descriptors);
    }

    public Municipality(String FIPS, int population, double landArea, String name, TypeClass typeClass, TimeZone standardTimeZone, TimeZone daylightTimeZone, List<County> counties, Set<String> descriptors) {
        this.FIPS = FIPS;
        this.population = population;
        this.landArea = landArea;
        this.name = name;
        this.typeClass = typeClass;
        this.standardTimeZone = standardTimeZone;
        this.daylightTimeZone = daylightTimeZone;
        this.counties = counties != null ? counties : new ArrayList<>();
        try {
            this.state = this.counties.get(0).getState();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            Logger.log("NO COUNTIES OR STATE", String.format("A Municipality was created without counties or state. Name: %s", name), e);
        }
        setDescriptors(descriptors);
        evaluateDemographics();

        Main.Engine().MapManager().addMunicipality(this);
    }

    public Municipality(String FIPS, int population, double landArea, String name, String nickname, TypeClass typeClass, TimeZone standardTimeZone, TimeZone daylightTimeZone, State state, Set<String> descriptors) {
        this.FIPS = FIPS;
        this.population = population;
        this.landArea = landArea;
        this.name = name;
        this.typeClass = typeClass;
        this.standardTimeZone = standardTimeZone;
        this.daylightTimeZone = daylightTimeZone;
        this.state = state;
        setDescriptors(getDescriptors());
        evaluateDemographics();

        Main.Engine().MapManager().addMunicipality(this);
    }

    // GETTERS AND SETTERS ------------------------------------------------------------------------

    // FIPS : String
    
    public String getFIPS() {
        return FIPS;
    }
    public void setFIPS(String FIPS) {
        this.FIPS = FIPS;
    }

    // Population : int

    public int getPopulation(){
        return this.population;
    }
    public void setPopulation(int population){
        this.population = population;
    }
    public void addPopulation(int population){
        this.population += population;
    }

    // Land Area : double

    public double getLandArea() {
        return landArea;
    }
    public void setLandArea(double area) {
        this.landArea = Math.max(0, area);
    }

    // Name : String

    public String getName() {
        return name;
    }
    public String getNameWithState() {
        return name + ", " + state.getCommonName();
    }
    public String getNameWithCountyAndState() {
        return name + ", " + counties.get(0).getFullName() + ", " + state.getCommonName();
    }
    public void setName(String name) {
        this.name = name;
    }

    // Type Class : TypeClass

    public TypeClass getTypeClass() {
        return typeClass;
    }
    public void setTypeClass(TypeClass typeClass) {
        this.typeClass = typeClass;
    }

    // Standard Time Zone : TimeZone

    public TimeZone getStandardTimeZone() {
        return standardTimeZone;
    }
    public void setStandardTimeZone(TimeZone timeZone) {
        this.standardTimeZone = timeZone;
    }

    // Daylight Time Zone : TimeZone

    public TimeZone getDaylightTimeZone() {
        return daylightTimeZone;
    }
    public void setDaylightTimeZone(TimeZone timeZone) {
        this.daylightTimeZone = timeZone;
    }

    // Counties : List of County

    public List<County> getCounties() {
        return counties;
    }
    public void setCounties(List<County> counties) {
        this.counties = counties;
    }
    public void SetCounties(County county) {
        this.counties = List.of(county);
    }
    public void setCountiesByNames(List<String> countiesNames) {
        this.counties = Main.Engine().MapManager().matchCounties(countiesNames, this.state.getAbbreviation());
    }
    public boolean addCounty(County county) {
        return this.counties.add(county);
    }
    public boolean addAllCounties(Collection<County> counties) {
        return this.counties.addAll(counties);
    }
    public boolean removeCounty(County county) {
        return this.counties.remove(county);
    }
    public boolean removeAllCounties(Collection<County> counties) {
        return this.counties.removeAll(counties);
    }

    // State : State

    public State getState(){
        return this.state;
    }
    public void setState(State state){
        this.state = state;
    }

    // Descriptors : List of String

    @Override
    public Set<String> getDescriptors() {
        return descriptors;
    }
    @Override
    public boolean hasDescriptor(String descriptor) {
        return this.descriptors.contains(descriptor);
    }
    @Override
    public void setDescriptors(Set<String> descriptors) {
        this.descriptors = descriptors != null ? descriptors : new HashSet<>();
        evaluateDemographics();
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
        return this.demographics;
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
        // TODO this.demographics = Main.Engine().DemographicsManager().demographicsFromDescriptors(descriptors);
    }

    // Mayor : LocalOfficial

    public LocalOfficial getMayor() {
        return mayor;
    }
    public void setMayor(LocalOfficial mayor) {
        if (mayor == null) {
            this.mayor = new LocalOfficial();
            this.mayor.addRole(LocalRole.MAYOR);
        }
        else this.mayor = mayor;
    }

    // Contract Location : Municipality
    public Municipality getContractLocation() {
        return contractLocation;
    }

    // Connectivity : double
    public double getConnectivity() {
        return connectivity;
    }

    // REPRESENATION METHODS ----------------------------------------------------------------------

    @Override
    public JSONObject toJson() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJson'");
    }

    @Override
    public Municipality fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }

    @Override
    public String toRepr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRepr'");
    }

    @Override
    public Municipality fromRepr(String repr) {
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
    public boolean equals(Object other){
        return this.toString().equals(other.toString());
        // TODO
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }
}