package main.core.map;

import java.lang.reflect.Field;

// IMPORTS ----------------------------------------------------------------------------------------

// Standard Library Imports
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Internal Imports
import core.JSONObject;
import core.JSONProcessor;
import core.StringOperations;
import main.core.FilePaths;
import main.core.Logger;
import main.core.Main;
import main.core.Manager;
import main.core.NumberOperations;
import main.core.demographics.Bloc;
import main.core.demographics.Demographics;
import main.core.demographics.DemographicsManager;
import main.core.map.travel.route.Airport;

public final class MapManager extends Manager {

    // STATIC VARIABLES ---------------------------------------------------------------------------

    public static enum MapMode {
        /** Default view - Switch between States, Districts, and Counties depending on zoom level. */
        DEFAULT,
        /** States view - See states in different colors. */
        STATES,
        /** Polling view - Shade areas between Blue, White, and Red according to poll results. */
        POLLING,
        /** Population view - Shade areas between Black and White depending on membership of selected bloc. */
        POPULATION,
        /** Conventions view - Shade states between Black and White depending on how soon their Convention is. */
        CONVENTIONS,
        /** Districts view - See congressional districts in different colors. */
        DISTRICTS,
        /** Counties view - See counties and equivalents in different colors. */
        COUNTIES,
        /** Travel view - Show roadways, airports, and other Routes between municipalities. */
        TRAVEL;
    }

    // STATIC FUNCTIONS ---------------------------------------------------------------------------

    // DISTANCE FUNCTIONS -------------------------------------------------------------------------

    public static double getRoadDistance(Municipality start, Municipality destination) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoadDistance'");
    }
    public static double getTrainDistance(Municipality start, Municipality destination) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTrainDistance'");
    }
    public static double getAirDistance(Municipality start, Municipality destination) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAirDistance'");
    }
    public static double getWaterDistance(Municipality start, Municipality destination) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWaterDistance'");
    }

    // private MapMode mapMode; // Currently selected map mode / view.
    // private double zoomLevel; // Number of horizontal pixels of the map currently on screen.
    // private double mapCameraX; // X-coordinate on the map which the camera is currently centered on. 0.0 is center of map.
    // private double mapCameraY; // Y-coordinate on the map which the camera is currently centered on. 0.0 is center of map.

    // public void moveCamera(double x, double y){
    //     mapCameraX = x;
    //     mapCameraY = y;
    // }
    // public void centerCamera(){
    //     mapCameraX = 0.0;
    //     mapCameraY = 0.0;
    // }
    // public void resetZoom(){
    //     zoomLevel = 2560.0;
    // }
    // public MapMode getMapMode() {
    //     return mapMode;
    // }
    // public void setMapMode(MapMode mode){
    //     mapMode = mode;
    // }

    // INSTANCE VARIABLES -------------------------------------------------------------------------

    private Nation                      nation;
    private List<State>                 states;
    private List<CongressionalDistrict> congressionalDistricts;
    private List<County>                counties;
    private List<Municipality>          municipalities;
    private List<Airport>               airports;
    private List<University>            universities;

    private ManagerState currentState;

    // CONSTRUCTORS -------------------------------------------------------------------------------

    public MapManager() {

        currentState = ManagerState.INACTIVE;

        nation = Nation.getInstance();
        states = new ArrayList<>();
        congressionalDistricts = new ArrayList<>();
        counties = new ArrayList<>();
        municipalities = new ArrayList<>();
        airports = new ArrayList<>();
        universities = new ArrayList<>();
    }

    // MANAGER METHODS ----------------------------------------------------------------------------

    @Override
    public boolean init(){
        boolean successFlag = true;
        // setMapMode(MapMode.DEFAULT);
        // centerCamera();
        // resetZoom();

        // Create Map data
        successFlag = successFlag && createStates(false);
        successFlag = successFlag && createCongressionalDistricts();
        successFlag = successFlag && createCounties(false);
        successFlag = successFlag && createMunicipalities();

        // Resolve location fields
        successFlag = successFlag && resolveCapitals();
        successFlag = successFlag && resolveCountySeats();
        successFlag = successFlag && resolveGovernmentOfficials();

        currentState = successFlag ? ManagerState.ACTIVE : ManagerState.ERROR;
        return successFlag;
    }

    @Override
    public ManagerState getState() {
        return currentState;
    }

    @Override
    public boolean cleanup() {
        boolean successFlag = true;
        currentState = ManagerState.INACTIVE;
        if (!successFlag) currentState = ManagerState.ERROR;
        return successFlag;
    }

    // INSTANCE METHODS ---------------------------------------------------------------------------

    public boolean createStates() { return createStates(true); }
    public boolean createStates(boolean citiesLoaded) {
        JSONObject json = JSONProcessor.processJson(FilePaths.STATES);

        for (Object stateObj : json.getAsList()) {
            if (stateObj instanceof JSONObject stateJson) {
                try {
                    String FIPS = stateJson.get("FIPS").toString();
                    int population = Integer.parseInt(stateJson.get("population").toString());
                    double landArea = Double.parseDouble(stateJson.get("land_area").toString());
                    String fullName = stateJson.get("full_name").toString();
                    String commonName = stateJson.get("common_name").toString();
                    String abbreviation = stateJson.get("abbreviation").toString();
                    String nickname = stateJson.get("nickname").toString();
                    String motto = stateJson.get("motto").toString();
                    String capital = stateJson.get("capital").toString();
                    Set<String> descriptors = Set.copyOf((ArrayList<String>) stateJson.get("descriptors"));
                    this.states.add(new State(
                        FIPS, population, landArea, fullName, commonName, abbreviation, nickname, motto, citiesLoaded ? capital : "", descriptors
                    ));
                }
                catch (NumberFormatException e) {
                    Logger.log("INVALID STATE ENTRY", "The parsed state either lacked necessary values or had malformed numbers. State: " + stateJson.getKey(), e);
                }
            }
        }
        return true;
    }
    public boolean createCongressionalDistricts() {
        JSONObject json = JSONProcessor.processJson(FilePaths.CONGRESSIONAL_DISTRICTS);

        for (Object districtObj : json.getAsList()) {
            if (districtObj instanceof JSONObject districtJson) {
                try {
                    String officeID = districtJson.get("office_ID").toString();
                    int population = Integer.parseInt(districtJson.get("population").toString());
                    double landArea = Double.parseDouble(districtJson.get("land_area").toString());
                    String name = districtJson.get("name").toString();
                    String stateName = districtJson.get("state").toString();
                    int districtNum = Integer.parseInt(districtJson.get("district_num").toString());
                    Set<String> descriptors = Set.copyOf((ArrayList<String>) districtJson.get("descriptors"));
                    this.congressionalDistricts.add(new CongressionalDistrict(
                        officeID, population, landArea, name, stateName, districtNum, descriptors
                    ));
                }
                catch (NumberFormatException e) {
                    Logger.log("INVALID CONGRESSIONAL DISTRICT ENTRY", "The parsed congressional district either lacked necessary values or had malformed numbers. District: " + districtJson.getKey(), e);
                }
            }
        }
        return true;
    }

    public boolean createCounties() { return createCounties(true); }
    public boolean createCounties(boolean citiesLoaded) {
        JSONObject json = JSONProcessor.processJson(FilePaths.COUNTIES);

        for (Object countyObj : json.getAsList()) {
            if (countyObj instanceof JSONObject countyJson) {
                String stateName = countyJson.get("state").toString();
                String FIPS = countyJson.get("FIPS").toString();
                int population = Integer.parseInt(countyJson.get("population").toString());
                double landArea = Double.parseDouble(countyJson.get("land_area").toString());
                String fullName = countyJson.get("full_name").toString();
                String commonName = countyJson.get("common_name").toString();
                String countySeatName = countyJson.get("county_seat").toString();
                Set<String> descriptors = Set.copyOf((ArrayList<String>) countyJson.get("descriptors"));
                this.counties.add(new County(
                    FIPS, population, landArea, fullName, commonName, stateName, citiesLoaded ? countySeatName : "", descriptors
                ));
            }
        }
        return true;
    }

    private boolean resolveCapitals() {
        JSONObject json = JSONProcessor.processJson(FilePaths.STATES);

        for (Object stateObj : json.getAsList()) {
            if (stateObj instanceof JSONObject stateJson) {
                String FIPS = stateJson.get("FIPS").toString();
                String stateAbbr = stateJson.get("abbreviation").toString();
                String capital = stateJson.get("capital").toString();
                this.matchState(stateAbbr).setCapital(this.matchMunicipality(capital, stateAbbr));
            }
        }
        return true;
    }

    private boolean resolveCountySeats() {
        JSONObject json = JSONProcessor.processJson(FilePaths.COUNTIES);

        for (Object countyObj : json.getAsList()) {
            if (countyObj instanceof JSONObject countyJson) {
                String FIPS = countyJson.get("FIPS").toString();
                String state = countyJson.get("state").toString();
                String countySeat;
                // Handle null county seats
                if (countyJson.get("county_seat").getClass() == Object.class) {
                    countySeat = null;
                }
                else countySeat = countyJson.get("county_seat").toString();
                this.matchCounty(FIPS).setCountySeat(this.matchMunicipality(countySeat, state));
            }
        }
        return true;
    }

    private boolean resolveGovernmentOfficials() {
        boolean successFlag = true;
        successFlag = successFlag && nation.getPresident() != null;
        successFlag = successFlag && nation.getVicePresident() != null;
        for (State state : states) {
            state.setSenators(state.getSenators());
            state.setGovernor(state.getGovernor());
            state.setLieutenantGovernor(state.getLieutenantGovernor());
        }
        for (CongressionalDistrict district : congressionalDistricts) {
            district.setRepresentative(district.getRepresentative());
        }
        for (Municipality municipality : municipalities) {
            // municipality.setMayor(municipality.getMayor()); // not all municipalities in the United States have a mayor, this varies by state. Also generating a character for EVERY municipality introduces massiave bloat
            if (municipality.getPopulation() >= 100000) municipality.setMayor(municipality.getMayor());
        }
        return successFlag;
    }

    public boolean createMunicipalities() { return createMunicipalities(true); }
    public boolean createMunicipalities(boolean countiesLoaded) {
        JSONObject json = JSONProcessor.processJson(FilePaths.MUNICIPALITIES);

        for (Object municipalityObj : json.getAsList()) {
            try {
                if (municipalityObj instanceof JSONObject municipalityJson) {
                    String FIPS = municipalityJson.get("FIPS").toString();
                    int population = Integer.parseInt(municipalityJson.get("population_2027").toString());
                    double landArea = Double.parseDouble(municipalityJson.get("land_area_2020").toString());
                    String name = municipalityJson.get("name").toString();
                    String typeClass = municipalityJson.get("type_class").toString();
                    String standardTimeZone = municipalityJson.get("standard_timezone").toString();
                    String daylightTimeZone = municipalityJson.get("daylight_timezone").toString();
                    String stateName = municipalityJson.get("state").toString();
                    List<String> countiesNames = (ArrayList<String>) municipalityJson.get("counties");
                    Set<String> descriptors = Set.copyOf((ArrayList<String>) municipalityJson.get("descriptors"));
                    this.municipalities.add(new Municipality(
                        FIPS, population, landArea, name, typeClass, standardTimeZone, daylightTimeZone, stateName, countiesNames, descriptors
                    ));
                }
            }
            catch (NullPointerException e) {
                System.out.println(municipalityObj.toString());
                throw e;
            }
        }
        return true;
    }

    // MATCHING METHODS ---------------------------------------------------------------------------

    /**
     * Finds and returns a state which matches the passed name.
     * @param stateName The name or abbreviation of the state.
     * @return The matched state, if found, or {@code null} otherwise.
     */
    public State matchState(String stateName) {
        for (State state : states) {
            if (state.getCommonName().equals(stateName) || state.getAbbreviation().equals(stateName)) return state;
        }
        Logger.log("INVALID STATE", String.format("The state, %s, could not be matched.", stateName), new Exception());
        return null;
    }

    public CongressionalDistrict matchCongressionalDistrict(String officeID) {
        for (CongressionalDistrict district : congressionalDistricts) {
            if (district.getOfficeID().equals(officeID)) return district;
        }
        Logger.log("INVALID OFFICE ID", String.format("The office ID, %s, could not be matched.", officeID), new Exception());
        return null;
    }
    public CongressionalDistrict matchCongressionalDistrict(State state, int districtNum) {
        for (CongressionalDistrict district : congressionalDistricts) {
            if (district.getState().equals(state) && district.getDistrictNum() == districtNum)
                return district;
        }
        Logger.log("INVALID STATE OR DISTRICT", String.format("The state, %s, or the district number, %s, could not be matched.", state.getCommonName(), districtNum), new Exception());
        return null;
    }
    public CongressionalDistrict matchCongressionalDistrict(String stateName, int districtNum) {
        return matchCongressionalDistrict(matchState(stateName), districtNum);
    }

    public County matchCounty(String FIPS) {
        for (County county : counties) {
            if (county.getFIPS().equals(FIPS))
                return county;
        }
        Logger.log("INVALID COUNTY", String.format("The county FIPS, %s, could not be matched.", FIPS), new Exception());
        return null;
    }
    public County matchCounty(String countyName, String stateName) {
        return matchCounty(countyName, matchState(stateName));
    }
    public County matchCounty(String name, State state) {
        for (County county : counties) {
            if (county.getState().equals(state) && county.getFullName().equals(name) || county.getCommonName().equals(name))
                return county;
        }
        Logger.log("INVALID COUNTY", String.format("The county name, %s, could not be matched to a county in %s.", name, state.getCommonName()), new Exception());
        return null;
    }
    public List<County> matchCounties(Collection<String> countiesNames, String stateName) {
        return matchCounties(countiesNames, matchState(stateName));
    }
    public List<County> matchCounties(Collection<String> names, State state) {
        List<County> result = new ArrayList<>();
        for (String name : names) {
            County matched = matchCounty(name, state);
            if (matched != null) result.add(matched);
        }
        return result;
    }

    /**
     * Finds and matches a municipality from a string containing the name of the municipality and the name or abbreviation of a state.
     * @param municipalityAndStateName A String contining the municipality name and and state names or abbreviation, separated by a comma {@code ,}
     * @return The found municipality if successfully matched, or {@code null} otherwise.
     */
    public Municipality matchMunicipality(String municipalityAndStateName) {
        String[] nameParts;
        if (StringOperations.containsUnquotedChar(municipalityAndStateName, ',')) {
            nameParts = StringOperations.splitByUnquotedString(municipalityAndStateName, ",");
        }
        // Assume the last word in the string is a state name or abbreviation. Will not work for state names with more than one word.
        else {
            nameParts = municipalityAndStateName.split("(?s)\\s(?=[^\\s]*$)", 2);
        }
        String municipalityName = nameParts[0];
        String stateNameOrAbbr = nameParts[1];
        return matchMunicipality(municipalityName, stateNameOrAbbr);
    }

    public Municipality matchMunicipality(String municipalityName, State state) {
        if (municipalityName == null) return null;
        for (Municipality municipality : municipalities) {
            if (municipality.getName().equals(municipalityName) && municipality.getState().equals(state))
                return municipality;
        }
        Logger.log("INVALID MUNICIPALITY", String.format("The municipality name, %s, or the state, %s, could not be matched.", municipalityName, state.getCommonName()), new Exception());
        return null;
    }

    /**
     * Finds and returns the municipality that matches the passed values.
     * @param municipalityName Name of the municipality, without a state abbreviation.
     * @param state Either the name or abbreviation of a state.
     * @return The found municipality, or {@code null} if not found.
     */
    public Municipality matchMunicipality(String municipalityName, String state) {
        return matchMunicipality(municipalityName, matchState(state));
    }
    /** This method to be used before cities have assigned states. Assumes all cities are unique by (name, population, area) */
    public Municipality matchMunicipality(String municipalityName, int population, double area) {
        for (Municipality municipality : municipalities) {
            if (municipality.getName().equals(municipalityName) && municipality.getPopulation() == population && municipality.getLandArea() == area)
                return municipality;
        }
        Logger.log("INVALID MUNICIPALITY", String.format("No municipality exists with the name %s, a population of %d, and an area of %f.", municipalityName, population, area), new Exception());
        return null;
    }

    /**
     * Finds and returns the airport with a full name, common name, or IATA code matching the passed string.
     * @param airportName Full or Common name, or IATA code, of an airport.
     * @return Matched airport, if found, or {@code null} otherwise.
     */
    public Airport matchAirport(String airportName) {
        for (Airport airport : airports) {
            if (airport.getFullName().equals(airportName) || airport.getCommonName().equals(airportName) || airport.getIATA().equals(airportName))
                return airport;
        }
        Logger.log("INVALID AIRPORT", String.format("No airport exists with a name or IATA code matching %s.", airportName));
        return null;
    }

    /**
     * Finds and returns the university with a full name or common name matching the passed string.
     * @param universityName Full or Common name of a university.
     * @return Matched university, if found, or {@code null} otherwise.
     */
    public University matchUniversity(String universityName) {
        for (University university : universities) {
            if (university.getFullName().equals(universityName) || university.getCommonName().equals(universityName))
                return university;
        }
        Logger.log("INVALID UNIVERSITY", String.format("No university exists with a name matching %s.", universityName));
        return null;
    }

    // GETTERS AND SETTERS ------------------------------------------------------------------------

    public List<State> getStates() {
        return states;
    }

    public List<CongressionalDistrict> getCongressionalDistricts(){
        return congressionalDistricts;
    }

    public List<County> getCounties() {
        return counties;
    }

    public List<Municipality> getMunicipalities() {
        return municipalities;
    }
    public boolean addMunicipality(Municipality municipality) {
        return this.municipalities.add(municipality);
    }

    // SELECTION METHODS --------------------------------------------------------------------------
    
    public Municipality selectMunicipality() {
        Map<Municipality, Integer> populations = new HashMap<>();
        for (Municipality municipality : municipalities) {
            populations.put(municipality, municipality.getPopulation());
        }
        Municipality selected = NumberOperations.weightedRandSelect(populations);
        return selected;
    }
    public Municipality selectMunicipality(Demographics demographics) {
        Map<Municipality, Integer> populations = new HashMap<>();
        for (Municipality municipality : municipalities) {
            int blocsPop = 0;
            for (Bloc bloc : demographics.toBlocsArray()) {
                blocsPop += (int) (municipality.getDemographicPopulation(bloc));
            }
            populations.put(municipality, blocsPop);
        }
        Municipality selected = NumberOperations.weightedRandSelect(populations);
        return selected;
    }

    private Map<Bloc, Float> defaultDemographics;
    public Map<Bloc, Float> getDefaultDemographics() {
        if (defaultDemographics != null) {
            return defaultDemographics;
        }

        // Must create default demographics
        defaultDemographics = new HashMap<>();
        for (List<Bloc> blocs : Main.Engine().DemographicsManager().getDemographicBlocs().values()) {
            for (Bloc bloc : blocs) {
                defaultDemographics.put(bloc, bloc.getPercentageVoters());
            }
        }

        return getDefaultDemographics();
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    @Override
    public String toRepr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRepr'");
    }

    @Override
    public Manager fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }

    private static final Map<String, String> fieldsJsons = Map.of(
        "nation", "nation",
        "states", "states",
        "congressionalDistricts", "congressional_districts",
        "counties", "counties",
        "municipalities", "municipalities",
        "airports", "airports",
        "universities", "universities"
    );

    @Override
    public JSONObject toJson() {
        try {
            List<JSONObject> fields = new ArrayList<>();
            for (String fieldName : fieldsJsons.keySet()) {
                Field field = getClass().getDeclaredField(fieldName);
                fields.add(new JSONObject(fieldName, field.get(this)));
            }
            return new JSONObject(this.getClass().getSimpleName(), fields);
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            currentState = ManagerState.ERROR;
            Logger.log("JSON SERIALIZATION ERROR", "Failed to serialize " + getClass().getSimpleName() + " to JSON.", e);
            return null;
        }
    }

    @Override
    public Manager fromJson(JSONObject json) {
        currentState = ManagerState.INACTIVE;
        for (String fieldName : fieldsJsons.keySet()) {
            String jsonKey = fieldsJsons.get(fieldName);
            Object value = json.get(jsonKey);
            if (value == null) continue;
            try {
                Field field = getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Class<?> type = field.getType();
                if (type.isEnum()) {
                    // For enums, convert string to enum constant
                    @SuppressWarnings({ "unchecked", "rawtypes" })
                    Object enumValue = Enum.valueOf((Class<Enum>) type, value.toString());
                    field.set(this, enumValue);
                }
                else {
                    // For other types, set directly (may need conversion for complex types)
                    field.set(this, value);
                }
            }
            catch (Exception e) {
                currentState = ManagerState.ERROR;
                Logger.log("JSON DESERIALIZATION ERROR", "Failed to set field " + fieldName + " in LanguageManager from JSON.", e);
            }
        }
        return this;
    }

}
