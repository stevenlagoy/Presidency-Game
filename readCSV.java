import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class readCSV {
    
    public static void main(String[] args){

        File inFile = new File("NTAD_Congressional_Districts_4866715788873924109.csv");

        String[] lines = new String[500];

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

        System.out.println("Done!");
    }

}
