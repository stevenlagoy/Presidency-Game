package main.core.map;

public class University {
    
    private Municipality location;
    private String name;
    private int graduationSize;

    public University(String locationName, String state, String name, int graduationSize) {
        this(MapManager.matchMunicipality(locationName, state), name, graduationSize);
    }

    public University(Municipality location, String name, int graduationSize) {
        this.location = location;
        this.name = name;
        this.graduationSize = graduationSize;
    }

    public Municipality getLocation() {
        return location;
    }
    public void setLocation(Municipality location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getGraduationSize() {
        return graduationSize;
    }
    public void setGraduationSize(int graduationSize) {
        this.graduationSize = graduationSize;
    }



}
