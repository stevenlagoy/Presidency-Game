package main.core.map;

import main.core.Main;

public class University {
    
    private Municipality location;
    private String fullName;
    private String commonName;
    private int graduationSize;

    public University(String locationName, String state, String fullName, String commonName, int graduationSize) {
        this(Main.Engine().MapManager().matchMunicipality(locationName, state), fullName, commonName, graduationSize);
    }

    public University(Municipality location, String fullName, String commonName, int graduationSize) {
        this.location = location;
        this.fullName = fullName;
        this.commonName = commonName;
        this.graduationSize = graduationSize;
    }

    public Municipality getLocation() { return location; }

    public String getFullName() { return fullName; }

    public String getCommonName() { return commonName; }

    public int getGraduationSize() { return graduationSize; }



}
