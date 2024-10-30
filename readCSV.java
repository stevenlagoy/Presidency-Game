import java.io.File;
import java.io.FileNotFoundException;
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

        CongressionalDistrict[] states = new CongressionalDistrict[500];
        String[] stringList = new String[20];
        int i = 0;
        lines[0] = "";
        for(String line : lines){
            //System.out.println(line);
            if(line == null) break;
            try{
                if(line.isEmpty()) continue;
                stringList = line.split(",");
                states[i] = CongressionalDistrict.fromCSV(line);
                i++;
            }
            catch(NumberFormatException e){
                System.out.println(e);
            }
            catch(NullPointerException e){
                System.out.println(e);
            }
        }

        System.out.println("Done!");
    }

}
