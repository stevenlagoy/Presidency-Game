import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class State {
    static
    {
        Map<String, String> states = new HashMap<>();
        states.put("name", "Alabama");
        // this is where states.json will be read and the states objects created
    }

    private static List<State> instances = new ArrayList<>();

    private String name;
    private int population;
    private List<String> largestCities = new ArrayList<>();
    private String abbreviation;
    private List<String> universities = new ArrayList<>();
    private List<String> demographics = new ArrayList<>();

    public State(String name, int population, List<String> largestCities, String abbreviation, List<String> universities, List<String> demographics){
        this.name = name;
        this.population = population;
        this.largestCities = largestCities;
        this.abbreviation = abbreviation;
        this.universities = universities;
        this.demographics = demographics;

        instances.add(this);
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public List<String> getDemographics(){
        return this.demographics;
    }

    public String toString(){
        return String.format("State("+
        "name=%s," +
        "population=%s," +
        "largest_cities=%s," +
        "abbreviation=%s," +
        "universities=%s",
        this.name, this.population, this.largestCities, this.abbreviation, this.universities);
    }
}