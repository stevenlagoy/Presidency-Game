package main.core.map;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.print.DocFlavor.STRING;

import core.JSONObject;
import core.JSONProcessor;
import main.core.Engine;
import main.core.FilePaths;

public class MapManager
{
    public static String[] mapModeNames = {"Default", "States", "Polling", "Population", "Conventions", "Districts", "Counties", "Travel"};
    /*
     * Default view - Switch between States, Districts, and Counties depending on zoom level.
     * States view - See states in different colors.
     * Districts view - See congressional districts in different colors.
     * Counties view - See counties and equivalents in different colors.
     * Polling view - Shade areas between Blue, White, and Red according to poll results.
     * Population view - Shade areas between Black and White depending on membership of selected bloc.
     * Conventions view - Shade states between Black and White depending on how soon their Convention is.
     * Travel view - Show roadways, airports, and other Routes between cities.
     */
    public static int mapMode; // Currently selected map mode / view. 0 is Default.
    public static double zoomLevel; // Number of horizontal pixels of the map currently on screen.
    public static double mapCameraX; // X-coordinate on the map which the camera is currently centered on. 0.0 is center of map.
    public static double mapCameraY; // Y-coordinate on the map which the camera is currently centered on. 0.0 is center of map.

    public static List<State>                   states                  = new ArrayList<>();
    public static List<CongressionalDistrict>   congressionalDistricts  = new ArrayList<>();
    public static List<County>                  counties                = new ArrayList<>();
    public static List<City>                    cities                  = new ArrayList<>();

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

    public static void createMap() {
        createStates();
        createCongressionalDistricts();
        createCounties();
        createCities();
        resolveLocationFields();
    }
    public static void createStates() {
        JSONObject json = JSONProcessor.processJson(FilePaths.STATES);

        for (Object element : json) {
            System.out.println(element.toString());
        }

        // TODO: Fix old json functionality
        // for (Object o : json.keySet()) {
        //     HashMap<String, String> state = (HashMap<String, String>) json.get(o);
        //     states.add(new State(
        //         Integer.parseInt(state.get("FIPS")),
        //         state.get("name"),
        //         Integer.parseInt(state.get("population")),
        //         state.get("abbreviation"),
        //         state.get("motto"),
        //         state.get("nickname")
        //     ));
        // }
    }
    public static void createCongressionalDistricts() {
        JSONObject json = JSONProcessor.processJson(FilePaths.CONGRESSIONAL_DISTRICTS);

        for (Object element : json) {
            System.out.println(element.toString());
        }

        // TODO: Fix old json functionality
        // for (Object o : json.keySet()) {
        //     HashMap<String, String> district = (HashMap<String, String>) json.get(o);
        //     congressionalDistricts.add(new CongressionalDistrict(
        //         district.get("hexID"),
        //         district.get("nameLSAD"),
        //         district.get("districtNum"),
        //         district.get("officeID")
        //     ));
        // }
    }
    public static void createCounties() {
        JSONObject json = JSONProcessor.processJson(FilePaths.COUNTIES);

        for (Object element : json) {
            System.out.println(element.toString());
        }

        // TODO: Fix old json functionality
        // for (Object o : json.keySet()) {
        //     HashMap<String, String> county = (HashMap<String, String>) json.get(o);
        //     counties.add(new County(
        //         county.get("FIPS"),
        //         county.get("name"),
        //         Integer.parseInt(county.get("population")),
        //         Double.parseDouble(county.get("square_milage"))
        //     ));
        // }
    }
    public static void createCities() {
        JSONObject json = JSONProcessor.processJson(FilePaths.CITIES);

        for (Object element : json) {
            System.out.println(element.toString());
        }

        // TODO: Fix old json functionality
        // for (Object o : json.keySet()) {
        //     HashMap<String, String> city = (HashMap<String, String>) json.get(o);
        //     cities.add(new City(
        //         city.get("name"),
        //         Integer.parseInt(city.get("population2027")),
        //         Double.parseDouble(city.get("landArea2020"))
        //     ));
        // }
    }

    private static void resolveLocationFields() {
        JSONObject json;

        // Fix Congressional Districts
        json = JSONProcessor.processJson(FilePaths.CONGRESSIONAL_DISTRICTS);

        for (Object element : json) {
            System.out.println(element.toString());
        }

        // TODO: Fix old json functionality
        // for (Object o : json.keySet()) {
        //     HashMap<String, String> d = (HashMap<String, String>) json.get(o);
        //     CongressionalDistrict congressionalDistrict = matchCongressionalDistrict(d.get("officeID"));
        //     congressionalDistrict.setState(matchState(d.get("state")));
        // }

        // Fix Counties
        json = JSONProcessor.processJson(FilePaths.COUNTIES);

        for (Object element : json) {
            System.out.println(element.toString());
        }

        // TODO: Fix old json functionality
        // for (Object o : json.keySet()) {
        //     HashMap<String, String> c = (HashMap<String, String>) json.get(o);
        //     County county = matchCounty(c.get("FIPS"));
        //     county.setState(matchState(c.get("state")));
        //     //county.setCongressionalDistrict(null);
        //     //county.setCountySeat(matchCity(c.get("county_seat"), c.get("state")));
        // }

        // Fix Cities
        json = JSONProcessor.processJson(FilePaths.CITIES);

        for (Object element : json) {
            System.out.println(element.toString());
        }

        // TODO: Fix old json functionality
        // for (Object o : json.keySet()) {
        //     HashMap<String, String> c = (HashMap<String, String>) json.get(o);
        //     City city = matchCity(c.get("name"), Integer.parseInt(c.get("population2027")), Double.parseDouble(c.get("landArea2020")));
        //     city.setState(matchState(c.get("state")));
        //     //city.setDistrict(null);
        // }
    }

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

    public static City matchCity(String cityName, State state) {
        for (City city : cities) {
            if (city.getName().equals(cityName) && city.getState().equals(state)) return city;
        }
        Engine.log("INVALID CITY", String.format("The city name, %s, or the state, %s, could not be matched.", cityName, state.getName()), new Exception());
        return null;
    }
    public static City matchCity(String cityName, String state) {
        return matchCity(cityName, matchState(state));
    }
    // This method to be used before cities have assigned states. Assumes all cities are unique by (name, population, area)
    public static City matchCity(String cityName, int population, double area) {
        for (City city : cities) {
            if (city.getName().equals(cityName) && city.getPopulation() == population && city.getArea() == area) return city;
        }
        Engine.log("INVALID CITY", String.format("No city exists with the name %s, a population of %d, and an area of %f.", cityName, population, area), new Exception());
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

    public static List<City> getCities() {
        return cities;
    }

    public static String generateSaveString() {
        StringBuilder saveString = new StringBuilder();
        for (State state : states) {
            saveString.append(state.toRepr()).append("\n");
        }
        return saveString.toString();
    }
}
