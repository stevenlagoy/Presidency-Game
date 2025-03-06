package src.org.core.map;
public class County {

    // basic info
    public int FID;
    public String name;
    public String state;
    public String FIPS;
    public int pop20;
    public float popDens20;
    public int pop10;
    public float popDens10;
    public float squareMilage;
    public double shapeLength, shapeArea;
    public double shapeLength2, shapeArea2;

    // demographic info
    public int popWhite, popBlack, popNativeIndian, popAsian, popHawaiianPacific, popHispanic, popOther, popMultiRace;
    
    // gender and age info
    public int popMale, popFemale;
    public int pop0005, pop0509, pop1014, pop1519, pop2024, pop2534, pop3544, pop4554, pop5564, pop6574, pop7584, pop8500;
    public float medAge, medAgeM, medAgeF;
    
    // household info
    public int households;
    public float avgHHSize;
    public int HH_1M, HH_1F;
    public int MarriedHH_Child, MarriedHH_NoChild;
    public int MaleHeadH_child, FemaleHeadH_child;
    public int families;
    public float avgFamSize;

    // housing info
    public int housingUnits, vacant, ownerOccupied, renterOccupied;
    public int numFarms;
    public int aveFarmAcreage, cropAcreage, avgFarmSales;


    public County(
        int FID, String name, String state,
        String FIPS,
        int pop20,
        float popDens20,
        int pop10,
        float popDens10,
        float squareMilage,
        double shapeLength, double shapeArea,
        double shapeLength2, double shapeArea2,
        int popWhite, int popBlack, int popNativeIndian, int popAsian, int popHawaiianPacific, int popHispanic, int popOther, int popMultiRace,
        int popMale, int popFemale,
        int pop0005, int pop0509, int pop1014, int pop1519, int pop2024, int pop2534, int pop3544, int pop4554, int pop5564, int pop6574, int pop7584, int pop8500,
        float medAge, float medAgeM, float medAgeF,
        int households,
        float avgHHSize,
        int HH_1M, int HH_1F,
        int MarriedHH_Child, int MarriedHH_NoChild,
        int MaleHeadH_child, int FemaleHeadH_child,
        int families,
        float avgFamSize,

        int housingUnits, int vacant, int ownerOccupied, int renterOccupied,
        int numFarms,
        int aveFarmAcreage, int cropAcreage, int avgFarmSales
    ){
        this.FID = FID; this.name = name; this.state = state;
        this.FIPS = FIPS;
        this.pop20 = pop20;
        this.popDens20 = popDens20;
        this.pop10 = pop10;
        this.popDens10 = popDens10;
        this.squareMilage = squareMilage;
        this.shapeLength = shapeLength; this.shapeArea = shapeArea;
        this.shapeLength2 = shapeLength2; this.shapeArea2 = shapeArea2;
        this.popWhite = popWhite; this.popBlack = popBlack; this.popNativeIndian = popNativeIndian; this.popAsian = popAsian; this.popHawaiianPacific = popHawaiianPacific; this.popHispanic = popHispanic; this.popOther = popOther; this.popMultiRace = popMultiRace;
        this.popMale = popMale; this.popFemale = popFemale;
        this.pop0005 = pop0005; this.pop0509 = pop0509; this.pop1014 = pop1014; this.pop1519 = pop1519; this.pop2024 = pop2024; this.pop2534 = pop2534; this.pop3544 = pop3544; this.pop4554 = pop4554; this.pop5564 = pop5564; this.pop6574 = pop6574; this.pop7584 = pop7584; this.pop8500 = pop8500;
        this.medAge = medAge; this.medAgeM = medAgeM; this.medAgeF = medAgeF;
        this.households = households;
        this.avgHHSize = avgHHSize;
        this.HH_1M = HH_1M; this.HH_1F = HH_1F;
        this.MarriedHH_Child = MarriedHH_Child; this.MarriedHH_NoChild = MarriedHH_NoChild;
        this.MaleHeadH_child = MaleHeadH_child; this.FemaleHeadH_child = FemaleHeadH_child;
        this.families = families;
        this.avgFamSize = avgFamSize;

        this.housingUnits = housingUnits; this.vacant = vacant; this.ownerOccupied = ownerOccupied; this.renterOccupied = renterOccupied;
        this.numFarms = numFarms;
        this.aveFarmAcreage = aveFarmAcreage; this.cropAcreage = cropAcreage; this.avgFarmSales = avgFarmSales;
    }

    public static County parseCounty(String csvLine) {
        // Split the line by comma
        String[] fields = csvLine.split(",");
    
        // Parse and return a County object
        return new County(
            Integer.parseInt(fields[0].trim()), // FID
            fields[1].trim(),                   // name
            fields[2].trim(),                   // state
            fields[5].trim(),                   // FIPS (combined state and county FIPS code)
            Integer.parseInt(fields[6].trim()), // pop20 (population)
            Float.parseFloat(fields[7].trim()), // popDens20 (population density 2020)
            Integer.parseInt(fields[8].trim()), // pop10 (population 2010)
            Float.parseFloat(fields[9].trim()), // popDens10 (population density 2010)
            Float.parseFloat(fields[53].trim()),// squareMilage (total square mileage)
            Double.parseDouble(fields[58].trim()), // shapeLength (first shape length)
            Double.parseDouble(fields[59].trim()), // shapeArea (first shape area)
            Double.parseDouble(fields[60].trim()), // shapeLength2 (second shape length)
            Double.parseDouble(fields[61].trim()), // shapeArea2 (second shape area)
            Integer.parseInt(fields[10].trim()), // popWhite
            Integer.parseInt(fields[11].trim()), // popBlack
            Integer.parseInt(fields[12].trim()), // popNativeIndian (American Indian and Alaskan Native population)
            Integer.parseInt(fields[13].trim()), // popAsian
            Integer.parseInt(fields[14].trim()), // popHawaiianPacific (Native Hawaiian and Pacific Islander)
            Integer.parseInt(fields[15].trim()), // popHispanic
            Integer.parseInt(fields[16].trim()), // popOther
            Integer.parseInt(fields[17].trim()), // popMultiRace
            Integer.parseInt(fields[18].trim()), // popMale
            Integer.parseInt(fields[19].trim()), // popFemale
            Integer.parseInt(fields[20].trim()), // pop0005 (under age 5)
            Integer.parseInt(fields[21].trim()), // pop0509 (age 5-9)
            Integer.parseInt(fields[22].trim()), // pop1014 (age 10-14)
            Integer.parseInt(fields[23].trim()), // pop1519 (age 15-19)
            Integer.parseInt(fields[24].trim()), // pop2024 (age 20-24)
            Integer.parseInt(fields[25].trim()), // pop2534 (age 25-34)
            Integer.parseInt(fields[26].trim()), // pop3544 (age 35-44)
            Integer.parseInt(fields[27].trim()), // pop4554 (age 45-54)
            Integer.parseInt(fields[28].trim()), // pop5564 (age 55-64)
            Integer.parseInt(fields[29].trim()), // pop6574 (age 65-74)
            Integer.parseInt(fields[30].trim()), // pop7584 (age 75-84)
            Integer.parseInt(fields[31].trim()), // pop8500 (age 85+)
            Float.parseFloat(fields[32].trim()), // medAge (median age)
            Float.parseFloat(fields[33].trim()), // medAgeM (median age males)
            Float.parseFloat(fields[34].trim()), // medAgeF (median age females)
            Integer.parseInt(fields[35].trim()), // households
            Float.parseFloat(fields[36].trim()), // avgHHSize (average household size)
            Integer.parseInt(fields[37].trim()), // HH_1M (households with 1 male)
            Integer.parseInt(fields[38].trim()), // HH_1F (households with 1 female)
            Integer.parseInt(fields[39].trim()), // MarriedHH_Child (married households with children)
            Integer.parseInt(fields[40].trim()), // MarriedHH_NoChild (married households with no children)
            Integer.parseInt(fields[41].trim()), // MaleHeadH_child (male-headed households with children)
            Integer.parseInt(fields[42].trim()), // FemaleHeadH_child (female-headed households with children)
            Integer.parseInt(fields[43].trim()), // families
            Float.parseFloat(fields[44].trim()), // avgFamSize (average family size)
            Integer.parseInt(fields[45].trim()), // housingUnits
            Integer.parseInt(fields[46].trim()), // vacant (vacant housing units)
            Integer.parseInt(fields[47].trim()), // ownerOccupied
            Integer.parseInt(fields[48].trim()), // renterOccupied
            Integer.parseInt(fields[49].trim()), // numFarms (number of farms in 2012)
            Integer.parseInt(fields[50].trim()), // aveFarmAcreage (average farm acreage in 2012)
            Integer.parseInt(fields[51].trim()), // cropAcreage (crop acreage in 2012)
            Integer.parseInt(fields[52].trim())  // avgFarmSales (average farm sales in 2012)
        );
    }
    
}
