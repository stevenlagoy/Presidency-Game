package main.core.map.travel.route;

import core.JSONObject;
import main.core.Jsonic;
import main.core.Repr;
import main.core.map.Municipality;

public class Seaport extends Route implements Repr<Seaport>, Jsonic<Seaport> {
    

    private String fullName;
    private String commonName;
    private Municipality location;

    public Seaport() {
        fullName = "";
        commonName = "";
        location = null;
    }

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

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    @Override
    public JSONObject toJson() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJson'");
    }

    @Override
    public Seaport fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }

    @Override
    public String toRepr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRepr'");
    }

    @Override
    public Seaport fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }

}
