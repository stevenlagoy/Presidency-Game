package main.core.map.travel.route;

import core.JSONObject;
import main.core.Jsonic;
import main.core.Main;
import main.core.Repr;
import main.core.map.MapManager;
import main.core.map.Municipality;

public class Airport implements Repr<Airport>, Jsonic<Airport> {
    
    // STATIC VARIABLES ---------------------------------------------------------------------------

    public static enum AirportSize {
        LARGE,
        MEDIUM,
        SMALL;

        public static AirportSize fromString(String str) {
            str = str.toUpperCase().replaceAll("\\s+","").trim();
            return switch(str) {
                case "LARGE" -> LARGE;
                case "L" -> LARGE;
                case "MEDIUM" -> MEDIUM;
                case "M" -> MEDIUM;
                case "SMALL" -> SMALL;
                case "S" -> SMALL;
                default -> null;
            };
        }
    }

    // INSTANCE VARIABLES -------------------------------------------------------------------------

    private String fullName;
    private String commonName;
    private String IATA;
    private Municipality location;
    private AirportSize size;
    private int enplanement;

    // CONSTRUCTORS -------------------------------------------------------------------------------

    public Airport() {
        fullName = "";
        commonName = "";
        IATA = "";
        location = null;
        size = null;
        enplanement = 0;
    }

    public Airport(String fullName, String commonName, String IATA, Municipality location, AirportSize size, int enplanement) {
        this.fullName = fullName;
        this.commonName = commonName;
        this.IATA = IATA;
        this.location = location;
        this.size = size;
        this.enplanement = enplanement;
    }

    public Airport(String fullName, String commonName, String IATA, String locationName, String size, int enplanement) {
        this(fullName, commonName, IATA, Main.Engine().MapManager().matchMunicipality(locationName), AirportSize.fromString(size), enplanement);
    }

    public String getFullName() { return fullName; }

    public String getCommonName() { return commonName; }

    public String getIATA() { return IATA; }

    public Municipality getMunicipality() { return location; }

    public AirportSize getSize() { return size; }

    public int getEnplanement() { return enplanement; }

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    @Override
    public JSONObject toJson() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJson'");
    }

    @Override
    public Airport fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }

    @Override
    public String toRepr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRepr'");
    }

    @Override
    public Airport fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }

}
