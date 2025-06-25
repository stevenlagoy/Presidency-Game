package main.core.map;

public class Airport {
    
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

    private String fullName;
    private String commonName;
    private String IATA;
    private Municipality location;
    private AirportSize size;
    private int enplanement;

    public Airport(String fullName, String commonName, String IATA, Municipality location, AirportSize size, int enplanement) {
        this.fullName = fullName;
        this.commonName = commonName;
        this.IATA = IATA;
        this.location = location;
        this.size = size;
        this.enplanement = enplanement;
    }

    public Airport(String fullName, String commonName, String IATA, String locationName, String size, int enplanement) {
        this(fullName, commonName, IATA, MapManager.matchMunicipality(locationName), AirportSize.fromString(size), enplanement);
    }

    public String getFullName() { return fullName; }

    public String getCommonName() { return commonName; }

    public String getIATA() { return IATA; }

    public Municipality getLocation() { return location; }

    public AirportSize getSize() { return size; }

    public int getEnplanement() { return enplanement; }

}
