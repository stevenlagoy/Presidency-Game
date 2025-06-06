package main.core.map;

// IMPORTS ----------------------------------------------------------------------------------------

// Standard Library Imports
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Internal Imports
import core.JSONObject;
import core.JSONProcessor;
import core.StringOperations;
import main.core.Engine;
import main.core.FilePaths;
import main.core.demographics.Bloc;
import main.core.demographics.Demographics;
import main.core.demographics.DemographicsManager;

public final class MapManager {
    private MapManager() {} // Non-Instantiable

    public static String[] mapModeNames = {"Default", "States", "Polling", "Population", "Conventions", "Districts", "Counties", "Travel"};
    /*
     * Default view - Switch between States, Districts, and Counties depending on zoom level.
     * States view - See states in different colors.
     * Districts view - See congressional districts in different colors.
     * Counties view - See counties and equivalents in different colors.
     * Polling view - Shade areas between Blue, White, and Red according to poll results.
     * Population view - Shade areas between Black and White depending on membership of selected bloc.
     * Conventions view - Shade states between Black and White depending on how soon their Convention is.
     * Travel view - Show roadways, airports, and other Routes between municipalities.
     */
    public static int mapMode; // Currently selected map mode / view. 0 is Default.
    public static double zoomLevel; // Number of horizontal pixels of the map currently on screen.
    public static double mapCameraX; // X-coordinate on the map which the camera is currently centered on. 0.0 is center of map.
    public static double mapCameraY; // Y-coordinate on the map which the camera is currently centered on. 0.0 is center of map.

    public static List<State>                   states                  = new ArrayList<>();
    public static List<CongressionalDistrict>   congressionalDistricts  = new ArrayList<>();
    public static List<County>                  counties                = new ArrayList<>();
    public static List<Municipality>            municipalities          = new ArrayList<>();

    public static void init(){
        setMapMode(0);
        centerCamera();
        resetZoom();
    }
    public static void moveCamera(double x, double y){
        mapCameraX = x;
        mapCameraY = y;
    }
    public static void centerCamera(){
        mapCameraX = 0.0;
        mapCameraY = 0.0;
    }
    public static void resetZoom(){
        zoomLevel = 2560.0;
    }
    public static void setMapMode(int mode){
        mapMode = mode;
    }

    public static boolean createMap() {
        // Two-Phase Initialization
        // 1) Construct skeleton objects top-down
        // 2) Resolve references bottom-up
        boolean successFlag = true;
        successFlag = successFlag && createStates();
        successFlag = successFlag && createCongressionalDistricts();
        successFlag = successFlag && createCounties(false);
        successFlag = successFlag && createCities();
        successFlag = successFlag && resolveLocationFields();
        return successFlag;
    }
    public static boolean createStates() {
        JSONObject json = JSONProcessor.processJson(FilePaths.STATES);

        for (Object stateObj : json.getAsList()) {
            if (stateObj instanceof JSONObject stateJson) {
                try {
                    int FIPS = Integer.parseInt(stateJson.get("FIPS").toString());
                    String name = stateJson.get("name").toString();
                    int population = Integer.parseInt(stateJson.get("population").toString());
                    String abbreviation = stateJson.get("abbreviation").toString();
                    String motto = stateJson.get("motto").toString();
                    String nickname = stateJson.get("nickname").toString();
                    MapManager.states.add(new State(
                        FIPS, name, population, abbreviation, motto, nickname
                    ));
                }
                catch (NumberFormatException e) {
                    Engine.log("INVALID STATE ENTRY", "The parsed state either lacked necessary values or had malformed numbers. State: " + stateJson.getKey(), e);
                }
            }
        }
        return true;
    }
    public static boolean createCongressionalDistricts() {
        JSONObject json = JSONProcessor.processJson(FilePaths.CONGRESSIONAL_DISTRICTS);

        for (Object districtObj : json.getAsList()) {
            if (districtObj instanceof JSONObject districtJson) {
                try {
                    String hexID = districtJson.get("hexID").toString();
                    String nameLSAD = districtJson.get("nameLSAD").toString();
                    String state = districtJson.get("state").toString();
                    String districtNum = districtJson.get("districtNum").toString();
                    String officeID = districtJson.get("officeID").toString();
                    MapManager.congressionalDistricts.add(new CongressionalDistrict(
                        hexID, nameLSAD, state, districtNum, officeID
                    ));
                }
                catch (NumberFormatException e) {
                    Engine.log("INVALID CONGRESSIONAL DISTRICT ENTRY", "The parsed congressional district either lacked necessary values or had malformed numbers. District: " + districtJson.getKey(), e);
                }
            }
        }
        return true;
    }

    public static boolean createCounties() { return createCounties(); }
    public static boolean createCounties(boolean citiesLoaded) {
        JSONObject json = JSONProcessor.processJson(FilePaths.COUNTIES);

        for (Object countyObj : json.getAsList()) {
            if (countyObj instanceof JSONObject countyJson) {
                String FIPS = countyJson.get("FIPS").toString();
                String name = countyJson.get("name").toString();
                String state = countyJson.get("state").toString();
                String countySeat = countyJson.get("county_seat").toString();
                int population = Integer.parseInt(countyJson.get("population").toString());
                double area = Double.parseDouble(countyJson.get("square_mileage").toString());
                MapManager.counties.add(new County(
                    FIPS, name, state, citiesLoaded ? countySeat : null, population, area
                ));
            }
        }
        return true;
    }
    private static boolean resolveCountySeats() {
        JSONObject json = JSONProcessor.processJson(FilePaths.COUNTIES);

        for (Object countyObj : json.getAsList()) {
            if (countyObj instanceof JSONObject countyJson) {
                String FIPS = countyJson.get("FIPS").toString();
                String state = countyJson.get("state").toString();
                String countySeat = countyJson.get("county_seat").toString();
                MapManager.matchCounty(FIPS).setCountySeat(MapManager.matchMunicipality(countySeat, state));
            }
        }
        return true;
    }

    public static boolean createCities() { return createCities(true); }
    public static boolean createCities(boolean countiesLoaded) {
        JSONObject json = JSONProcessor.processJson(FilePaths.CITIES);

        for (Object municipalityObj : json.getAsList()) {
            if (municipalityObj instanceof JSONObject municipalityJson) {
                String name = municipalityJson.get("name").toString();
                String state = municipalityJson.get("state").toString();
                String FIPS = municipalityJson.get("FIPS").toString();
                List<String> counties = new ArrayList<>();
                for (String county : (ArrayList<String>) municipalityJson.get("counties")) {
                    counties.add(county);
                }
                String typeClass = municipalityJson.get("type_class").toString();
                int population = Integer.parseInt(municipalityJson.get("population_2027").toString());
                double area = Double.parseDouble(municipalityJson.get("land_area_2020").toString());
                MapManager.municipalities.add(new Municipality(
                    FIPS, name, state, counties, typeClass, population, area, null, null
                ));
            }
        }
        return true;
    }

    private static boolean resolveLocationFields() {
        boolean successFlag = true;
        successFlag = successFlag && resolveCountySeats();
        return successFlag;
    }

    /**
     * Finds and returns a state which matches the passed name.
     * @param stateName The name or abbreviation of the state.
     * @return The matched state, if found, or {@code null} otherwise.
     */
    public static State matchState(String stateName) {
        for (State state : states) {
            if (state.getName().equals(stateName) || state.getAbbreviation().equals(stateName)) return state;
        }
        Engine.log("INVALID STATE", String.format("The state, %s, could not be matched.", stateName), new Exception());
        return null;
    }

    public static CongressionalDistrict matchCongressionalDistrict(String officeID) {
        for (CongressionalDistrict district : congressionalDistricts) {
            if (district.getOfficeID().equals(officeID)) return district;
        }
        Engine.log("INVALID OFFICE ID", String.format("The office ID, %s, could not be matched.", officeID), new Exception());
        return null;
    }
    public static CongressionalDistrict matchCongressionalDistrict(State state, String districtNum) {
        for (CongressionalDistrict district : congressionalDistricts) {
            if (district.getState().equals(state) && district.getDistrictNum().equals(districtNum)) return district;
        }
        Engine.log("INVALID STATE OR DISTRICT", String.format("The state, %s, or the district number, %s, could not be matched.", state.getName(), districtNum), new Exception());
        return null;
    }
    public static CongressionalDistrict matchCongressionalDistrict(String stateName, String districtNum) {
        return matchCongressionalDistrict(matchState(stateName), districtNum);
    }

    public static County matchCounty(String FIPS) {
        for (County county : counties) {
            if (county.getFIPS().equals(FIPS)) return county;
        }
        Engine.log("INVALID COUNTY", String.format("The county FIPS, %s, could not be matched.", FIPS), new Exception());
        return null;
    }
    public static County matchCounty(String name, State state) {
        for (County county : counties) {
            if (county.getName().equals(name) && county.getState().equals(state)) return county;
            if (county.getName().replace("County", "").trim().equals(name.replace("County", "").trim()) && county.getState().equals(state)) return county;
        }
        Engine.log("INVALID COUNTY", String.format("The county name, %s, could not be matched to a county in %s.", name, state.getName()), new Exception());
        return null;
    }
    public static List<County> matchCounties(Collection<String> names, State state) {
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
    public static Municipality matchMunicipality(String municipalityAndStateName) {
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

    public static Municipality matchMunicipality(String municipalityName, State state) {
        for (Municipality municipality : municipalities) {
            if (municipality.getName().equals(municipalityName) && municipality.getState().equals(state)) return municipality;
        }
        Engine.log("INVALID MUNICIPALITY", String.format("The municipality name, %s, or the state, %s, could not be matched.", municipalityName, state.getName()), new Exception());
        return null;
    }

    /**
     * Finds and returns the municipality that matches the passed values.
     * @param municipalityName Name of the municipality, without a state abbreviation.
     * @param state Either the name or abbreviation of a state.
     * @return The found municipality, or {@code null} if not found.
     */
    public static Municipality matchMunicipality(String municipalityName, String state) {
        return matchMunicipality(municipalityName, matchState(state));
    }
    /** This method to be used before cities have assigned states. Assumes all cities are unique by (name, population, area) */
    public static Municipality matchMunicipality(String municipalityName, int population, double area) {
        for (Municipality municipality : municipalities) {
            if (municipality.getName().equals(municipalityName) && municipality.getPopulation() == population && municipality.getArea() == area) return municipality;
        }
        Engine.log("INVALID MUNICIPALITY", String.format("No municipality exists with the name %s, a population of %d, and an area of %f.", municipalityName, population, area), new Exception());
        return null;
    }

    public static List<State> getStates() {
        return states;
    }

    public static List<CongressionalDistrict> getCongressionalDistricts(){
        return congressionalDistricts;
    }

    public static List<County> getCounties() {
        return counties;
    }

    public static List<Municipality> getCities() {
        return municipalities;
    }

    public static String generateSaveString() {
        StringBuilder saveString = new StringBuilder();
        for (State state : states) {
            saveString.append(state.toRepr()).append("\n");
        }
        return saveString.toString();
    }
    
    public static Municipality selectMunicipality() {
        Map<Municipality, Integer> populations = new HashMap<>();
        for (Municipality municipality : municipalities) {
            populations.put(municipality, municipality.getPopulation());
        }
        Municipality selected = Engine.weightedRandSelect(populations);
        return selected;
    }
    public static Municipality selectMunicipality(Demographics demographics) {
        Map<Municipality, Integer> populations = new HashMap<>();
        for (Municipality municipality : municipalities) {
            int blocsPop = 0;
            for (Bloc bloc : demographics.toBlocsArray()) {
                blocsPop += (int) (municipality.getPopulation() * municipality.getDemographicPercentage(bloc));
            }
            populations.put(municipality, blocsPop);
        }
        Municipality selected = Engine.weightedRandSelect(populations);
        return selected;
    }

    private static Map<Bloc, Float> defaultDemographics;
    public static Map<Bloc, Float> getDefaultDemographics() {
        if (defaultDemographics != null) {
            return defaultDemographics;
        }

        // Must create default demographics
        defaultDemographics = new HashMap<>();
        for (List<Bloc> blocs : DemographicsManager.getDemographicBlocs().values()) {
            for (Bloc bloc : blocs) {
                defaultDemographics.put(bloc, bloc.getPercentageVoters());
            }
        }

        return getDefaultDemographics();
    }
}
