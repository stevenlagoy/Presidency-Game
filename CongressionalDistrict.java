public class CongressionalDistrict {

    // GIS and Census Information
    private int objectID;
    private int stateFIPS;
    private int geoID;
    private int CD118FP;
    private String nameLSAD;
    private String LSAD;
    private int CDSessn;
    private String MTFCC;
    private String funcStat;
    private long aLand;
    private long aWater;
    private double intptLat;
    private double intptLon;

    // Congressional Office and Representative Information
    private String officeID;
    private String bioGuideID;
    private int officeAuditID;
    private String prefix;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String listingName;
    private String phone;
    private String websiteURL;
    private boolean vacant;
    private String contactFormURL;
    private String photoURL;

    // Social Media Information
    private String facebookURL;
    private String twitterURL;
    private String youtubeURL;
    private String instagramURL;
    private String flickrURL;

    // Additional Information
    private String party;
    private int district;
    private String state;
    private boolean vacancy;
    private String roomNum;
    private String HOB;
    private String committeeAssignments;
    private String lastUpdated;
    private double shapeArea;
    private double shapeLength;

    // Constructor
    public CongressionalDistrict(
            int objectID, int stateFIPS, int geoID, int CD118FP, String nameLSAD, String LSAD, int CDSessn,
            String MTFCC, String funcStat, long aLand, long aWater, double intptLat, double intptLon, String officeID,
            String bioGuideID, int officeAuditID, String prefix, String firstName, String middleName, String lastName,
            String suffix, String listingName, String phone, String websiteURL, boolean vacant, String contactFormURL,
            String photoURL, String facebookURL, String twitterURL, String youtubeURL, String instagramURL,
            String flickrURL, String party, int district, String state, boolean vacancy, String roomNum, String HOB,
            String committeeAssignments, String lastUpdated, double shapeArea, double shapeLength) {

        this.objectID = objectID;
        this.stateFIPS = stateFIPS;
        this.geoID = geoID;
        this.CD118FP = CD118FP;
        this.nameLSAD = nameLSAD;
        this.LSAD = LSAD;
        this.CDSessn = CDSessn;
        this.MTFCC = MTFCC;
        this.funcStat = funcStat;
        this.aLand = aLand;
        this.aWater = aWater;
        this.intptLat = intptLat;
        this.intptLon = intptLon;
        this.officeID = officeID;
        this.bioGuideID = bioGuideID;
        this.officeAuditID = officeAuditID;
        this.prefix = prefix;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.suffix = suffix;
        this.listingName = listingName;
        this.phone = phone;
        this.websiteURL = websiteURL;
        this.vacant = vacant;
        this.contactFormURL = contactFormURL;
        this.photoURL = photoURL;
        this.facebookURL = facebookURL;
        this.twitterURL = twitterURL;
        this.youtubeURL = youtubeURL;
        this.instagramURL = instagramURL;
        this.flickrURL = flickrURL;
        this.party = party;
        this.district = district;
        this.state = state;
        this.vacancy = vacancy;
        this.roomNum = roomNum;
        this.HOB = HOB;
        this.committeeAssignments = committeeAssignments;
        this.lastUpdated = lastUpdated;
        this.shapeArea = shapeArea;
        this.shapeLength = shapeLength;
    }

    public static CongressionalDistrict fromCSV(String csvLine) {
        String[] fields = csvLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    
        int objectID = Integer.parseInt(fields[0].trim());
        int stateFIPS = Integer.parseInt(fields[1].trim());
        int geoID = Integer.parseInt(fields[2].trim());
        int CD118FP = Integer.parseInt(fields[3].trim());
        String nameLSAD = fields[4].trim();
        String LSAD = fields[5].trim();
        int CDSessn = Integer.parseInt(fields[6].trim());
        String MTFCC = fields[7].trim();
        String funcStat = fields[8].trim();
        long aLand = Long.parseLong(fields[9].trim());
        long aWater = Long.parseLong(fields[10].trim());
        double intptLat = Double.parseDouble(fields[11].trim());
        double intptLon = Double.parseDouble(fields[12].trim());
        String officeID = fields[13].trim();
        String bioGuideID = fields[14].trim();
        int officeAuditID = Integer.parseInt(fields[15].trim());
        String prefix = fields[16].trim();
        String firstName = fields[17].trim();
        String middleName = fields[18].trim();
        String lastName = fields[19].trim();
        String suffix = fields[20].trim();
        String listingName = fields[21].trim();
        String phone = fields[22].trim();
        String websiteURL = fields[23].trim();
        boolean vacant = fields[24].trim().equalsIgnoreCase("Y");
        String contactFormURL = fields[25].trim();
        String photoURL = fields[26].trim();
        String facebookURL = fields[27].trim();
        String twitterURL = fields[28].trim();
        String youtubeURL = fields[29].trim();
        String instagramURL = fields[30].trim();
        String flickrURL = fields[31].trim();
        String party = fields[32].trim();
        int district = parseStringOrDefault(fields[33].trim(), 0);
        String state = fields[34].trim();
        boolean vacancy = fields[35].trim().equals("0");
        String roomNum = fields[36].trim();
        String HOB = fields[37].trim();
        String committeeAssignments = fields[38].trim();
        String lastUpdated = fields[39].trim();
        double shapeArea = Double.parseDouble(fields[40].trim());
        double shapeLength = Double.parseDouble(fields[41].trim());
    
        return new CongressionalDistrict(
                objectID, stateFIPS, geoID, CD118FP, nameLSAD, LSAD, CDSessn, MTFCC, funcStat, aLand, aWater,
                intptLat, intptLon, officeID, bioGuideID, officeAuditID, prefix, firstName, middleName, lastName,
                suffix, listingName, phone, websiteURL, vacant, contactFormURL, photoURL, facebookURL, twitterURL,
                youtubeURL, instagramURL, flickrURL, party, district, state, vacancy, roomNum, HOB,
                committeeAssignments, lastUpdated, shapeArea, shapeLength
        );
    }
    
    // Helper method to parse optional integer fields
    private static int parseStringOrDefault(String str, int defaultValue) {
        return str.isEmpty() ? defaultValue : Integer.parseInt(str);
    }    


    // Getter and Setter methods

    public int getObjectID() { return objectID; }
    public void setObjectID(int objectID) { this.objectID = objectID; }

    public int getStateFIPS() { return stateFIPS; }
    public void setStateFIPS(int stateFIPS) { this.stateFIPS = stateFIPS; }

    public int getGeoID() { return geoID; }
    public void setGeoID(int geoID) { this.geoID = geoID; }

    public int getCD118FP() { return CD118FP; }
    public void setCD118FP(int CD118FP) { this.CD118FP = CD118FP; }

    public String getNameLSAD() { return nameLSAD; }
    public void setNameLSAD(String nameLSAD) { this.nameLSAD = nameLSAD; }

    public String getLSAD() { return LSAD; }
    public void setLSAD(String LSAD) { this.LSAD = LSAD; }

    public int getCDSessn() { return CDSessn; }
    public void setCDSessn(int CDSessn) { this.CDSessn = CDSessn; }

    public String getMTFCC() { return MTFCC; }
    public void setMTFCC(String MTFCC) { this.MTFCC = MTFCC; }

    public String getFuncStat() { return funcStat; }
    public void setFuncStat(String funcStat) { this.funcStat = funcStat; }

    public long getALand() { return aLand; }
    public void setALand(long aLand) { this.aLand = aLand; }

    public long getAWater() { return aWater; }
    public void setAWater(long aWater) { this.aWater = aWater; }

    public double getIntptLat() { return intptLat; }
    public void setIntptLat(double intptLat) { this.intptLat = intptLat; }

    public double getIntptLon() { return intptLon; }
    public void setIntptLon(double intptLon) { this.intptLon = intptLon; }

    public String getOfficeID() { return officeID; }
    public void setOfficeID(String officeID) { this.officeID = officeID; }

    public String getBioGuideID() { return bioGuideID; }
    public void setBioGuideID(String bioGuideID) { this.bioGuideID = bioGuideID; }

    public int getOfficeAuditID() { return officeAuditID; }
    public void setOfficeAuditID(int officeAuditID) { this.officeAuditID = officeAuditID; }

    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getSuffix() { return suffix; }
    public void setSuffix(String suffix) { this.suffix = suffix; }

    public String getListingName() { return listingName; }
    public void setListingName(String listingName) { this.listingName = listingName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getWebsiteURL() { return websiteURL; }
    public void setWebsiteURL(String websiteURL) { this.websiteURL = websiteURL; }

    public boolean isVacant() { return vacant; }
    public void setVacant(boolean vacant) { this.vacant = vacant; }

    public String getContactFormURL() { return contactFormURL; }
    public void setContactFormURL(String contactFormURL) { this.contactFormURL = contactFormURL; }

    public String getPhotoURL() { return photoURL; }
    public void setPhotoURL(String photoURL) { this.photoURL = photoURL; }

    public String getFacebookURL() { return facebookURL; }
    public void setFacebookURL(String facebookURL) { this.facebookURL = facebookURL; }

    public String getTwitterURL() { return twitterURL; }
    public void setTwitterURL(String twitterURL) { this.twitterURL = twitterURL; }

    public String getYoutubeURL() { return youtubeURL; }
    public void setYoutubeURL(String youtubeURL) { this.youtubeURL = youtubeURL; }

    public String getInstagramURL() { return instagramURL; }
    public void setInstagramURL(String instagramURL) { this.instagramURL = instagramURL; }

    public String getFlickrURL() { return flickrURL; }
    public void setFlickrURL(String flickrURL) { this.flickrURL = flickrURL; }

    public String getParty() { return party; }
    public void setParty(String party) { this.party = party; }

    public int getDistrict() { return district; }
    public void setDistrict(int district) { this.district = district; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public boolean isVacancy() { return vacancy; }
    public void setVacancy(boolean vacancy) { this.vacancy = vacancy; }

    public String getRoomNum() { return roomNum; }
    public void setRoomNum(String roomNum) { this.roomNum = roomNum; }

    public String getHOB() { return HOB; }
    public void setHOB(String HOB) { this.HOB = HOB; }

    public String getCommitteeAssignments() { return committeeAssignments; }
    public void setCommitteeAssignments(String committeeAssignments) { this.committeeAssignments = committeeAssignments; }

    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }

    public double getShapeArea() { return shapeArea; }
    public void setShapeArea(double shapeArea) { this.shapeArea = shapeArea; }

    public double getShapeLength() { return shapeLength; }
    public void setShapeLength(double shapeLength) { this.shapeLength = shapeLength; }

}
