package src.org.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import src.org.core.map.County;

public class readCSV {
    
    public static void main(String[] args){

        File inFile = new File("USA_Counties_1658646944295846491.csv");

        String[] lines = new String[3200];

        try{
            Scanner scan = new Scanner(inFile);
            int i = 0;
            while(scan.hasNext()){
                lines[i] = scan.nextLine();
                i++;
            }
            scan.close();
        }
        catch(FileNotFoundException e){
            System.out.println(e);
            System.exit(0);
        }

        ArrayList<County> counties = new ArrayList<County>();
        int i = 0;
        lines[0] = "";
        for(String line : lines){
            //System.out.println(line);
            if(line == null) break;
            try{
                if(line.isEmpty()) continue;
                counties.add(County.parseCounty(line));
                i++;
            }
            catch(NumberFormatException e){
                System.out.println(e);
            }
            catch(NullPointerException e){
                System.out.println(e);
            }
        }
        for(County county : counties){
            if(county != null) System.out.println(county.FIPS);
        }

        // write to JSON
        File outputFile = new File("output.txt");
        int j = 0;
        try{
            PrintWriter out = new PrintWriter(outputFile);
            out.write("{\n");
            for(County county : counties){
                out.write(String.format("\t\"%s\" : {\n", county.FID));

                out.write(String.format("\t\t\"FIPS\" : \"%s\",\n", county.FIPS));
                out.write(String.format("\t\t\"name\" : \"%s\",\n", county.name));
                out.write(String.format("\t\t\"state\" : \"%s\",\n", county.state));
                out.write(String.format("\t\t\"county_seat\" : \"%s\",\n", "CAPITAL CITY"));
                out.write(String.format("\t\t\"largest_city\" : \"%s\",\n", "LARGEST CITY"));
                out.write(String.format("\t\t\"population\" : %s,\n", county.pop20));
                out.write(String.format("\t\t\"population_desity\" : %s,\n", county.popDens20));
                out.write(String.format("\t\t\"pop_change_rate\" : %s,\n", (county.pop20-county.pop10) / 10.0));
                out.write(String.format("\t\t\"square_milage\" : %s,\n", county.squareMilage));
                out.write(String.format("\t\t\"white_population\" : %s,\n", county.popWhite));
                out.write(String.format("\t\t\"black_population\" : %s,\n", county.popBlack));
                out.write(String.format("\t\t\"native_indian_population\" : %s,\n", county.popNativeIndian));
                out.write(String.format("\t\t\"asian_population\" : %s,\n", county.popAsian));
                out.write(String.format("\t\t\"pacific_islander_population\" : %s,\n", county.popHawaiianPacific));
                out.write(String.format("\t\t\"hispanic_population\" : %s,\n", county.popHispanic));
                out.write(String.format("\t\t\"other_race_ethnicity_population\" : %s,\n", county.popOther));
                out.write(String.format("\t\t\"multiple_race_population\" : %s,\n", county.popMultiRace));
                out.write(String.format("\t\t\"pop_male\" : %s,\n", county.popMale));
                out.write(String.format("\t\t\"pop_female\" : %s,\n", county.popFemale));
                out.write(String.format("\t\t\"pop_0to4\" : %s,\n", county.pop0005));
                out.write(String.format("\t\t\"pop_5to9\" : %s,\n", county.pop0509));
                out.write(String.format("\t\t\"pop_10to14\" : %s,\n", county.pop1014));
                out.write(String.format("\t\t\"pop_15to19\" : %s,\n", county.pop1519));
                out.write(String.format("\t\t\"pop_20to24\" : %s,\n", county.pop2024));
                out.write(String.format("\t\t\"pop_25to34\" : %s,\n", county.pop2534));
                out.write(String.format("\t\t\"pop_35to44\" : %s,\n", county.pop3544));
                out.write(String.format("\t\t\"pop_45to54\" : %s,\n", county.pop4554));
                out.write(String.format("\t\t\"pop_55to64\" : %s,\n", county.pop5564));
                out.write(String.format("\t\t\"pop_65to74\" : %s,\n", county.pop6574));
                out.write(String.format("\t\t\"pop_75to84\" : %s,\n", county.pop7584));
                out.write(String.format("\t\t\"pop_85to120\" : %s,\n", county.pop8500));
                out.write(String.format("\t\t\"median_age\" : %s,\n", county.medAge));
                out.write(String.format("\t\t\"median_age_male\" : %s,\n", county.medAgeM));
                out.write(String.format("\t\t\"median_age_female\" : %s,\n", county.medAgeF));
                out.write(String.format("\t\t\"number_households\" : %s,\n", county.households));
                out.write(String.format("\t\t\"average_household_size\" : %s,\n", county.avgHHSize));
                out.write(String.format("\t\t\"number_households_singleM_wo_child\" : %s,\n", county.HH_1M));
                out.write(String.format("\t\t\"number_households_singleF_wo_child\" : %s,\n", county.HH_1F));
                out.write(String.format("\t\t\"number_households_married_w_child\" : %s,\n", county.MarriedHH_Child));
                out.write(String.format("\t\t\"number_households_married_wo_child\" : %s,\n", county.MarriedHH_NoChild));
                out.write(String.format("\t\t\"number_households_singleM_w_child\" : %s,\n", county.MaleHeadH_child));
                out.write(String.format("\t\t\"number_households_singleF_w_child\" : %s,\n", county.FemaleHeadH_child));
                out.write(String.format("\t\t\"number_families\" : %s,\n", county.families));
                out.write(String.format("\t\t\"average_family_size\" : %s,\n", county.avgFamSize));
                out.write(String.format("\t\t\"housing_units\" : %s,\n", county.households));
                out.write(String.format("\t\t\"housing_vacant\" : %s,\n", county.vacant));
                out.write(String.format("\t\t\"owner_occupied_housing\" : %s,\n", county.ownerOccupied));
                out.write(String.format("\t\t\"renter_occupied_housing\" : %s,\n", county.renterOccupied));
                out.write(String.format("\t\t\"number_farms\" : %s,\n", county.numFarms));
                out.write(String.format("\t\t\"average_farm_acreage\" : %s,\n", county.aveFarmAcreage));
                out.write(String.format("\t\t\"total_crop_acreage\" : %s\n", county.cropAcreage));

                out.write("\t},\n");
            }
            out.write("}");
            try{
                Thread.sleep(1000);
            }
            catch(Exception e){
                System.out.println(e);
            }
            out.close();
        }
        catch(FileNotFoundException e){
            System.out.println(e);
            System.exit(0);
        }

        /*
        ArrayList<CongressionalDistrict> districts = new ArrayList<CongressionalDistrict>();
        String[] stringList = new String[20];
        int i = 0;
        lines[0] = "";
        for(String line : lines){
            //System.out.println(line);
            if(line == null) break;
            try{
                if(line.isEmpty()) continue;
                stringList = line.split(",");
                districts.add(CongressionalDistrict.fromCSV(line));
                i++;
            }
            catch(NumberFormatException e){
                System.out.println(e);
            }
            catch(NullPointerException e){
                System.out.println(e);
            }
        }
        for(CongressionalDistrict district : districts){
            if(district != null) System.out.println(district.getFuncStat());
        }

        String[] statesAndTerritories = {
            "ALABAMA",
            "ALASKA",
            "AMERICAN SAMOA", // FIPS PUB 5-1 reserved
            "ARIZONA",
            "ARKANSAS",
            "CALIFORNIA",
            "CANAL ZONE", // FIPS PUB 5-1 reserved
            "COLORADO",
            "CONNECTICUT",
            "DELAWARE",
            "DISTRICT OF COLUMBIA",
            "FLORIDA",
            "GEORGIA",
            "GUAM", // FIPS PUB 5-1 reserved
            "HAWAII",
            "IDAHO",
            "ILLINOIS",
            "INDIANA",
            "IOWA",
            "KANSAS",
            "KENTUCKY",
            "LOUISIANA",
            "MAINE",
            "MARYLAND",
            "MASSACHUSETTS",
            "MICHIGAN",
            "MINNESOTA",
            "MISSISSIPPI",
            "MISSOURI",
            "MONTANA",
            "NEBRASKA",
            "NEVADA",
            "NEW HAMPSHIRE",
            "NEW JERSEY",
            "NEW MEXICO",
            "NEW YORK",
            "NORTH CAROLINA",
            "NORTH DAKOTA",
            "OHIO",
            "OKLAHOMA",
            "OREGON",
            "PENNSYLVANIA",
            "PUERTO RICO", // FIPS PUB 5-1 reserved
            "RHODE ISLAND",
            "SOUTH CAROLINA",
            "SOUTH DAKOTA",
            "TENNESSEE",
            "TEXAS",
            "UTAH",
            "VERMONT",
            "VIRGINIA",
            "VIRGIN ISLANDS OF THE U.S.", // FIPS PUB 5-1 reserved
            "WASHINGTON",
            "WEST VIRGINIA",
            "WISCONSIN",
            "WYOMING",
            "",
            "",
            "",
            "AMERICAN SAMOA",
            "",
            "",
            "",
            "FEDERATED STATES OF MICRONESIA",
            "",
            "GUAM",
            "JOHNSTON ATOLL",
            "MARSHALL ISLANDS",
            "NORTHERN MARIANA ISLANDS",
            "PALAU",
            "MIDWAY ISLANDS",
            "PUERTO RICO",
            "",
            "U.S. MINOR OUTLYING ISLANDS",
            "",
            "NAVISSA ISLAND",
            "",
            "VIRGIN ISLANDS OF THE U.S.",
            "WAKE ISLAND",
            "",
            "BAKER ISLAND",
            "",
            "",
            "HOWLAND ISLAND",
            "",
            "JARVIS ISLAND",
            "",
            "",
            "KINGMAN ISLAND",
            "",
            "",
            "",
            "",
            "",
            "PALMYRA ATOLL"
        };

        // write to JSON
        File outputFile = new File("output.txt");
        int j = 0;
        try{
            PrintWriter out = new PrintWriter(outputFile);
            out.write("{\n");
            for(CongressionalDistrict district : districts){
                out.write(String.format("\t\"%s\" : {\n", district.getOfficeID()));
                out.write(String.format("\t\t\"hexID\" : \"#%06X\",%n", j+=(37700*48))); j%=16777216;
                out.write(String.format("\t\t\"nameLSAD\" : \"%s\",%n", district.getNameLSAD()));
                out.write(String.format("\t\t\"state\" : \"%s\",%n", statesAndTerritories[district.getStateFIPS()-1]));
                out.write(String.format("\t\t\"districtNum\" : \"%02d\",%n", district.getCD118FP()));
                out.write(String.format("\t\t\"officeID\" : \"%04d\",%n", district.getGeoID()));
                out.write("\t},\n");
            }
            out.write("}");
            try{
                Thread.sleep(1000);
            }
            catch(Exception e){
                System.out.println(e);
            }
            out.close();
        }
        catch(FileNotFoundException e){
            System.out.println(e);
            System.exit(0);
        }
        */

        System.out.println("Done!");
    }

}
