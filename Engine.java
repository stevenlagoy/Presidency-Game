import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import java.io.IOException;

public class Engine {

    public static String language;
    public static String languageAbbreviation;
    public static final String systemText_loc = "_system_text.txt";
    public static final String descriptions_loc = "_descriptions.txt";

    public static final long baseSpeed = 2000L;
    public static final long[] speedSettings = {baseSpeed, baseSpeed/2, baseSpeed/4, baseSpeed/8, baseSpeed/16}; // Time in between ticks
    public static int speedSetting = 0;
    public static long tickSpeed = speedSettings[speedSetting];

    static Scanner stdin = new Scanner(System.in);

    public static MapManager mapManager = new MapManager();
    public static CharacterManager characterManager = new CharacterManager();
    public static DemographicsManager demograpicsManager = new DemographicsManager();
    public static EventManager eventManager = new EventManager();
    public static DateManager dateManager = new DateManager();

    public static Candidate playerCandidate;

    public static void init(){

    }

    public static boolean tick(){
        boolean active = true;

        System.out.println(DateManager.currentGameDate);
        active = active && DateManager.incrementQuarterHour();

        return active;
    }

    public static int getInput(String[] responses){
        /* getInput
         * takes responses, a list of acceptable strings
         * only returns when an acceptable input is given
         * returns an integer corresponding to the index of the accepted string in the responses list
        */

        String input = "";
        while(true){
            System.out.print("> ");
            input = stdin.nextLine().toUpperCase();
            for(int i = 0; i < responses.length; i++){
                if(responses[i].equals(input)){
                    return i;
                }
            }
        }
    }
    public static int getInput(){
        /* getInput
         * asks for an input until one is given
         * returns when input is recieved
        */

        String input = "";
        while(true){
            System.out.print("> ");
            input = stdin.nextLine().toUpperCase();
            if(input != null) return 1;
        }
    }
    public static void log(String logline){
        try {
            File logFile = new File("log.txt");
            logFile.createNewFile(); // does nothing if already exists
            PrintWriter logWriter = new PrintWriter(new FileWriter(logFile, true));

            logWriter.printf("%s : %s%n", getDate(), logline);
            logWriter.close();
            return;
        }
        catch(IOException e){
            System.out.println(e);
            System.exit(-1);
        }
    }
    public static void log(String context, String logline){
        try {
            File logFile = new File("log.txt");
            logFile.createNewFile(); // does nothing if already exists
            PrintWriter logWriter = new PrintWriter(new FileWriter(logFile, true));

            logWriter.printf("%s : %s: %s%n", getDate(), context.toUpperCase(), logline);
            logWriter.close();
            return;
        }
        catch(IOException e){
            System.out.println(e);
            System.exit(-1);
        }
    }
    public static void log(String context, String logline, String trace){
        try {
            File logFile = new File("log.txt");
            logFile.createNewFile(); // does nothing if already exists
            PrintWriter logWriter = new PrintWriter(new FileWriter(logFile, true));

            logWriter.printf("%s : %s: %s @ %s%n", getDate(), context.toUpperCase(), logline, trace);
            logWriter.close();
            return;
        }
        catch(IOException e){
            System.out.println(e);
            System.exit(-1);
        }
    }
    public static void log(Exception logE){
        try {
            File logFile = new File("log.txt");
            logFile.createNewFile(); // does nothing if already exists
            PrintWriter logWriter = new PrintWriter(new FileWriter(logFile, true));

            logWriter.printf("%s : %s @ %s%n", getDate(), logE.toString(), logE.getStackTrace()[0].toString());
            logWriter.close();
            return;
        }
        catch(IOException e){
            System.out.println(e);
            System.exit(-1);
        }
    }

    public static String getDate(){
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new java.util.Date());
    }

    public static void reset() throws IOException {
        File logFile = new File("log.txt");
        logFile.createNewFile(); // does nothing if already exists
        FileOutputStream logStream = new FileOutputStream(logFile, false);
        logStream.close();
        log("Reset Engine");
        return;
    }

    public static float randPercent(){
        return randPercent(0.0f, 1.0f);
    }

    public static float randPercent(float min, float max){
        // will perform the same if min and max are flipped
        Random rand = new Random();
        return (min - max) * rand.nextFloat() + min; // return a float between min and max (exclusive), equally distributed
    }
    public static double randDouble(double min, double max){
        // will perform the same if min and max are flipped
        Random rand = new Random();
        return (min - max) * rand.nextDouble() + min; // return a double between min and max (exclusive), equally distributed
    }

    public static int randInt(int max){
        return randInt(0, max);
    }

    public static int randInt(int min, int max){
        if(max > min) throw new IllegalArgumentException(String.format("The minimum is less than the maximum: %d < %d.%n", max, min));
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min; // return an integer between min and max (inclusive), equally distributed
    }
}