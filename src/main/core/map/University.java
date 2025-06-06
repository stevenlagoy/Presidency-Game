package main.core.map;

public class University {
    
    private Municipality municipality;
    private String name;

    public University(String municipality, String state, String name) {
        this(MapManager.matchMunicipality(municipality, state), name);
    }

    public University(Municipality municipality, String name) {
        this.municipality = municipality;
        this.name = name;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
