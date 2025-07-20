package main.core.map.travel.route;

import main.core.map.Municipality;

public class Seaport extends Route {
    

    private String fullName;
    private String commonName;
    private Municipality location;

    public Seaport(String fullName, String commonName, Municipality location) {
        this.fullName = fullName;
        this.commonName = commonName;
        this.location = location;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String name) {
        this.fullName = name;
    }

    public String getCommonName() {
        return commonName;
    }
    public void setCommonName(String name) {
        this.commonName = name;
    }

    public Municipality getMunicipality() {
        return location;
    }
    public void setMunicipality(Municipality location) {
        this.location = location;
    }

}
