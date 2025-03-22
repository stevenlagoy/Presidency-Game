package main.core.map;
public class CongressionalDistrict {

    // // GIS and Census Information
    // private int objectID; // Unique identifier for the object in the dataset
    // private int stateFIPS; // FIPS code identifying state/territory
    // private int geoID; // US Census unique identifier for congressional district (combines FIPS + district number)
    // private int CD118FP; // Congressional District number for the 118th Congress
    // private String _nameLSAD; // Name and legal/statistical area description
    // private String LSAD; // Description code for area description
    // private int CDSessn; // Congressional session associated with the district (e.g., 118 for 118th Congress)
    // private String MTFCC; // MAF/TIGER Feature Class Code, indicates geographic feature type
    // private String funcStat; // Functional status of the district (e.g., "A" for active, "I" for inactive)
    // private long aLand; // Land area in square meters
    // private long aWater; // Water area in square meters
    // private double intptLat; // Latitude of district's center point
    // private double intptLon; // Longitude of district's center point

    // // Congressional Office and Representative Information
    // private String _officeID; // ID for the congressional office
    // private String bioGuideID; // Biographical identifier for the representative
    // private int officeAuditID; // Audit identifier for the office
    // private String prefix; // Name prefix (e.g., "Mr.", "Ms.")
    // private String firstName; // Representative's first name
    // private String middleName; // Representative's middle name
    // private String lastName; // Representative's last name
    // private String suffix; // Name suffix (e.g., "Jr.", "III")
    // private String listingName; // Formal listing name for representative
    // private String phone; // Contact phone number
    // private String websiteURL; // Representative's official website URL
    // private boolean vacant; // Indicates if the seat is vacant
    // private String contactFormURL; // Contact form URL for the representative
    // private String photoURL; // URL for the representative's photo

    // // Social Media Information
    // private String facebookURL; // Facebook profile URL
    // private String twitterURL; // Twitter profile URL
    // private String youtubeURL; // YouTube profile URL
    // private String instagramURL; // Instagram profile URL
    // private String flickrURL; // Flickr profile URL

    // // Additional Information
    // private String party; // Political party of the representative
    // private int district; // District number
    // private String _state; // State abbreviation
    // private boolean vacancy; // True if the district is vacant
    // private String roomNum; // Office room number
    // private String HOB; // House office building name
    // private String committeeAssignments; // Committee assignments of the representative
    // private String lastUpdated; // Date of last update
    // private double shapeArea; // Area of the district's shape
    // private double shapeLength; // Length of the district's shape

    private String hexID;
    private String nameLSAD;
    private State state;
    private String districtNum;
    private String officeID;

    public CongressionalDistrict(String hexID, String nameLSAD, String districtNum, String officeID) {
        this.hexID = hexID;
        this.nameLSAD = nameLSAD;
        this.districtNum = districtNum;
        this.officeID = officeID;
    }

    public CongressionalDistrict(String hexID, String nameLSAD, String state, String districtNum, String officeID) {
        this(hexID, nameLSAD, MapManager.matchState(state), districtNum, officeID);
    }
    public CongressionalDistrict(String hexID, String nameLSAD, State state, String districtNum, String officeID) {
        this.hexID = hexID;
        this.nameLSAD = nameLSAD;
        this.state = state;
        this.districtNum = districtNum;
        this.officeID = officeID;
    }

    // public CongressionalDistrict(
    //         int objectID, int stateFIPS, int geoID, int CD118FP, String nameLSAD, String LSAD, int CDSessn,
    //         String MTFCC, String funcStat, long aLand, long aWater, double intptLat, double intptLon, String officeID,
    //         String bioGuideID, int officeAuditID, String prefix, String firstName, String middleName, String lastName,
    //         String suffix, String listingName, String phone, String websiteURL, boolean vacant, String contactFormURL,
    //         String photoURL, String facebookURL, String twitterURL, String youtubeURL, String instagramURL,
    //         String flickrURL, String party, int district, String state, boolean vacancy, String roomNum, String HOB,
    //         String committeeAssignments, String lastUpdated, double shapeArea, double shapeLength) {

    //     this.objectID = objectID;
    //     this.stateFIPS = stateFIPS;
    //     this.geoID = geoID;
    //     this.CD118FP = CD118FP;
    //     this.nameLSAD = nameLSAD;
    //     this.LSAD = LSAD;
    //     this.CDSessn = CDSessn;
    //     this.MTFCC = MTFCC;
    //     this.funcStat = funcStat;
    //     this.aLand = aLand;
    //     this.aWater = aWater;
    //     this.intptLat = intptLat;
    //     this.intptLon = intptLon;
    //     this.officeID = officeID;
    //     this.bioGuideID = bioGuideID;
    //     this.officeAuditID = officeAuditID;
    //     this.prefix = prefix;
    //     this.firstName = firstName;
    //     this.middleName = middleName;
    //     this.lastName = lastName;
    //     this.suffix = suffix;
    //     this.listingName = listingName;
    //     this.phone = phone;
    //     this.websiteURL = websiteURL;
    //     this.vacant = vacant;
    //     this.contactFormURL = contactFormURL;
    //     this.photoURL = photoURL;
    //     this.facebookURL = facebookURL;
    //     this.twitterURL = twitterURL;
    //     this.youtubeURL = youtubeURL;
    //     this.instagramURL = instagramURL;
    //     this.flickrURL = flickrURL;
    //     this.party = party;
    //     this.district = district;
    //     this._state = state;
    //     this.vacancy = vacancy;
    //     this.roomNum = roomNum;
    //     this.HOB = HOB;
    //     this.committeeAssignments = committeeAssignments;
    //     this.lastUpdated = lastUpdated;
    //     this.shapeArea = shapeArea;
    //     this.shapeLength = shapeLength;
    // }

    // public static CongressionalDistrict fromCSV(String csvLine) {
    //     String[] fields = csvLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    
    //     int objectID = Integer.parseInt(fields[0].trim());
    //     int stateFIPS = Integer.parseInt(fields[1].trim());
    //     int geoID = Integer.parseInt(fields[2].trim());
    //     int CD118FP = Integer.parseInt(fields[3].trim());
    //     String nameLSAD = fields[4].trim();
    //     String LSAD = fields[5].trim();
    //     int CDSessn = Integer.parseInt(fields[6].trim());
    //     String MTFCC = fields[7].trim();
    //     String funcStat = fields[8].trim();
    //     long aLand = Long.parseLong(fields[9].trim());
    //     long aWater = Long.parseLong(fields[10].trim());
    //     double intptLat = Double.parseDouble(fields[11].trim());
    //     double intptLon = Double.parseDouble(fields[12].trim());
    //     String officeID = fields[13].trim();
    //     String bioGuideID = fields[14].trim();
    //     int officeAuditID = Integer.parseInt(fields[15].trim());
    //     String prefix = fields[16].trim();
    //     String firstName = fields[17].trim();
    //     String middleName = fields[18].trim();
    //     String lastName = fields[19].trim();
    //     String suffix = fields[20].trim();
    //     String listingName = fields[21].trim();
    //     String phone = fields[22].trim();
    //     String websiteURL = fields[23].trim();
    //     boolean vacant = fields[24].trim().equalsIgnoreCase("Y");
    //     String contactFormURL = fields[25].trim();
    //     String photoURL = fields[26].trim();
    //     String facebookURL = fields[27].trim();
    //     String twitterURL = fields[28].trim();
    //     String youtubeURL = fields[29].trim();
    //     String instagramURL = fields[30].trim();
    //     String flickrURL = fields[31].trim();
    //     String party = fields[32].trim();
    //     int district = parseStringOrDefault(fields[33].trim(), 0);
    //     String state = fields[34].trim();
    //     boolean vacancy = fields[35].trim().equals("0");
    //     String roomNum = fields[36].trim();
    //     String HOB = fields[37].trim();
    //     String committeeAssignments = fields[38].trim();
    //     String lastUpdated = fields[39].trim();
    //     double shapeArea = Double.parseDouble(fields[40].trim());
    //     double shapeLength = Double.parseDouble(fields[41].trim());
    
    //     return new CongressionalDistrict(
    //             objectID, stateFIPS, geoID, CD118FP, nameLSAD, LSAD, CDSessn, MTFCC, funcStat, aLand, aWater,
    //             intptLat, intptLon, officeID, bioGuideID, officeAuditID, prefix, firstName, middleName, lastName,
    //             suffix, listingName, phone, websiteURL, vacant, contactFormURL, photoURL, facebookURL, twitterURL,
    //             youtubeURL, instagramURL, flickrURL, party, district, state, vacancy, roomNum, HOB,
    //             committeeAssignments, lastUpdated, shapeArea, shapeLength
    //     );
    // }
    
    // Helper method to parse optional integer fields
    // private static int parseStringOrDefault(String str, int defaultValue) {
    //     return str.isEmpty() ? defaultValue : Integer.parseInt(str);
    // }    


    // Getter and Setter methods
    public String getHexID() {
        return hexID;
    }
    public void setHexID(String hexID) {
        this.hexID = hexID;
    }

    public String getNameLSAD() {
        return nameLSAD;
    }
    public void setNameLSAD(String nameLSAD) {
        this.nameLSAD = nameLSAD;
    }

    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;
    }
    public void setState(String stateName) {
        setState(MapManager.matchState(stateName));
    }

    public String getDistrictNum() {
        return districtNum;
    }
    public void setDistrictNum(String districtNum) {
        this.districtNum = districtNum;
    }

    public String getOfficeID() {
        return officeID;
    }
    public void setOfficeID(String officeID) {
        this.officeID = officeID;
    }


    // public int getObjectID() { return objectID; }
    // public void setObjectID(int objectID) { this.objectID = objectID; }

    // public int getStateFIPS() { return stateFIPS; }
    // public void setStateFIPS(int stateFIPS) { this.stateFIPS = stateFIPS; }

    // public int getGeoID() { return geoID; }
    // public void setGeoID(int geoID) { this.geoID = geoID; }

    // public int getCD118FP() { return CD118FP; }
    // public void setCD118FP(int CD118FP) { this.CD118FP = CD118FP; }

    // public String _getNameLSAD() { return nameLSAD; }
    // public void _setNameLSAD(String nameLSAD) { this.nameLSAD = nameLSAD; }

    // public String getLSAD() { return LSAD; }
    // public void setLSAD(String LSAD) { this.LSAD = LSAD; }

    // public int getCDSessn() { return CDSessn; }
    // public void setCDSessn(int CDSessn) { this.CDSessn = CDSessn; }

    // public String getMTFCC() { return MTFCC; }
    // public void setMTFCC(String MTFCC) { this.MTFCC = MTFCC; }

    // public String getFuncStat() { return funcStat; }
    // public void setFuncStat(String funcStat) { this.funcStat = funcStat; }

    // public long getALand() { return aLand; }
    // public void setALand(long aLand) { this.aLand = aLand; }

    // public long getAWater() { return aWater; }
    // public void setAWater(long aWater) { this.aWater = aWater; }

    // public double getIntptLat() { return intptLat; }
    // public void setIntptLat(double intptLat) { this.intptLat = intptLat; }

    // public double getIntptLon() { return intptLon; }
    // public void setIntptLon(double intptLon) { this.intptLon = intptLon; }

    // public String _getOfficeID() { return officeID; }
    // public void _setOfficeID(String officeID) { this.officeID = officeID; }

    // public String getBioGuideID() { return bioGuideID; }
    // public void setBioGuideID(String bioGuideID) { this.bioGuideID = bioGuideID; }

    // public int getOfficeAuditID() { return officeAuditID; }
    // public void setOfficeAuditID(int officeAuditID) { this.officeAuditID = officeAuditID; }

    // public String getPrefix() { return prefix; }
    // public void setPrefix(String prefix) { this.prefix = prefix; }

    // public String getFirstName() { return firstName; }
    // public void setFirstName(String firstName) { this.firstName = firstName; }

    // public String getMiddleName() { return middleName; }
    // public void setMiddleName(String middleName) { this.middleName = middleName; }

    // public String getLastName() { return lastName; }
    // public void setLastName(String lastName) { this.lastName = lastName; }

    // public String getSuffix() { return suffix; }
    // public void setSuffix(String suffix) { this.suffix = suffix; }

    // public String getListingName() { return listingName; }
    // public void setListingName(String listingName) { this.listingName = listingName; }

    // public String getPhone() { return phone; }
    // public void setPhone(String phone) { this.phone = phone; }

    // public String getWebsiteURL() { return websiteURL; }
    // public void setWebsiteURL(String websiteURL) { this.websiteURL = websiteURL; }

    // public boolean isVacant() { return vacant; }
    // public void setVacant(boolean vacant) { this.vacant = vacant; }

    // public String getContactFormURL() { return contactFormURL; }
    // public void setContactFormURL(String contactFormURL) { this.contactFormURL = contactFormURL; }

    // public String getPhotoURL() { return photoURL; }
    // public void setPhotoURL(String photoURL) { this.photoURL = photoURL; }

    // public String getFacebookURL() { return facebookURL; }
    // public void setFacebookURL(String facebookURL) { this.facebookURL = facebookURL; }

    // public String getTwitterURL() { return twitterURL; }
    // public void setTwitterURL(String twitterURL) { this.twitterURL = twitterURL; }

    // public String getYoutubeURL() { return youtubeURL; }
    // public void setYoutubeURL(String youtubeURL) { this.youtubeURL = youtubeURL; }

    // public String getInstagramURL() { return instagramURL; }
    // public void setInstagramURL(String instagramURL) { this.instagramURL = instagramURL; }

    // public String getFlickrURL() { return flickrURL; }
    // public void setFlickrURL(String flickrURL) { this.flickrURL = flickrURL; }

    // public String getParty() { return party; }
    // public void setParty(String party) { this.party = party; }

    // public int getDistrict() { return district; }
    // public void setDistrict(int district) { this.district = district; }

    // public String _getState() { return _state; }
    // public void _setState(String state) { this._state = state; }

    // public boolean isVacancy() { return vacancy; }
    // public void setVacancy(boolean vacancy) { this.vacancy = vacancy; }

    // public String getRoomNum() { return roomNum; }
    // public void setRoomNum(String roomNum) { this.roomNum = roomNum; }

    // public String getHOB() { return HOB; }
    // public void setHOB(String HOB) { this.HOB = HOB; }

    // public String getCommitteeAssignments() { return committeeAssignments; }
    // public void setCommitteeAssignments(String committeeAssignments) { this.committeeAssignments = committeeAssignments; }

    // public String getLastUpdated() { return lastUpdated; }
    // public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }

    // public double getShapeArea() { return shapeArea; }
    // public void setShapeArea(double shapeArea) { this.shapeArea = shapeArea; }

    // public double getShapeLength() { return shapeLength; }
    // public void setShapeLength(double shapeLength) { this.shapeLength = shapeLength; }

}
