package main.core.map;

public class University {
    
    private City city;
    private String name;

    public University(String city, String state, String name) {
        this(MapManager.matchCity(city, state), name);
    }

    public University(City city, String name) {
        this.city = city;
        this.name = name;
    }

    public City getCity() {
        return city;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
