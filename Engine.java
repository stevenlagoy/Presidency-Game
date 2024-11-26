import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.io.IOException;

public class Engine {
    static Scanner in = new Scanner(System.in);
    public static int getInput(String[] responses){
        /* getInput
         * takes responses, a list of acceptable strings
         * only returns when an acceptable input is given
         * returns an integer corresponding to the index of the accepted string in the responses list
        */

        String input = "";
        while(true){
            System.out.print("> ");
            input = in.nextLine().toUpperCase();
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
            input = in.nextLine().toUpperCase();
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

    /**
     * Selects a float between 0.0 and 1.0 which can be used as a percentage.
     * 
     * @return A pseudorandomly selected float in the range [0.0, 1.0)
     */
    public static float randPercent(){
        return randPercent(0.0f, 1.0f);
    }
    /**
     * Selects a float between the min and the max.
     * 
     * @param min The minimum value which can be selected (inclusive)
     * @param max The maximum value which can be selected (exclusive)
     * @return A pseudorandomly selected float in the range [min, max)
     */
    public static float randPercent(float min, float max){
        // will perform the same if min and max are flipped
        Random rand = new Random();
        return (min - max) * rand.nextFloat() + min; // return a float between min and max (exclusive), evenly distributed
    }

    /**
     * Selects a double between 0.0 and the max.
     * 
     * @param max The maximum value which can be selected (exclusive)
     * @return A pseudorandomly selected double in the range [0.0, max)
     */
    public static double randDouble(double max){
        return randDouble(0.0, max);
    }
    /**
     * Selects a double between the min and the max.
     * 
     * @param min The minimum value which can be selected (inclusive)
     * @param max The maximum value which can be selected (exclusive)
     * @return A pseudorandomly selected double in the range [min, max)
     */
    public static double randDouble(double min, double max){
        // will perform the same if min and max are flipped
        Random rand = new Random();
        return (max - min) * rand.nextDouble() + min; // return a double between min and max (exclusive), evenly distributed
    }
    
    /**
     * Selects an integer between zero and the max, evenly distributed.
     * 
     * @param max The maximum value which can be randomly selected (exclusive)
     * @return A pseudorandomly generated integer in the range [0, max)
     */
    public static int randInt(int max){
        return randInt(0, max);
    }
    /**
     * Selects an integer between the min and the max, evenly distributed.
     * 
     * @param min The minimum value which can be selected (inclusive)
     * @param max The maximum value which can be selected (exclusive)
     * @return A pseudorandomly generated integer in the range [min, max)
     */
    public static int randInt(int min, int max){
        if(max < min) throw new IllegalArgumentException(String.format("The minimum is less than the maximum: %d < %d.%n", max, min));
        Random rand = new Random();
        return rand.nextInt(max - min) + min; // return an integer between min and max (exclusive), evenly distributed
    }
    
    /** 
     * Selects an integer based on a list of weights.
     * Probabilistically selects an index based on the values in the passed array, and returns the index.
     * No value in the weights array may be negative (<0). At least one value must be greater than zero.
     * 
     * @param weights Array of weights
     * @return Probabalistically selected index
     */
    public static int weightedInt(int[] weights) {
        int totalWeight = 0;
        int[] cumulativeWeights = new int[weights.length];
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] < 0) 
                throw new IllegalArgumentException(String.format("Negative value not allowed as probability weight: %d < 0.", weights[i]));
            totalWeight += weights[i];
            cumulativeWeights[i] = totalWeight;
        }
        if (totalWeight == 0) {
            throw new IllegalArgumentException("At least one weight must be greater than zero.");
        }
        int res = randInt(totalWeight); // Generates [0, totalWeight)
        int low = 0, high = weights.length - 1, mid;
        while (low < high) {
            mid = (low + high) / 2;
            if (res < cumulativeWeights[mid])
                high = mid;
            else
                low = mid + 1;
        }
        return low;
    }
    /**
     * Selects a double based on a list of weights.
     * Probabilistically selects an index based on the values in the passed array, and returns the index.
     * No value in the weights array may be negative (<0). At least one value must be greater than zero.
     * 
     * @param weights Array of weights
     * @return Probabalistically selected index
     */
    public static int weightedDouble(double[] weights) {
        double totalWeight = 0.0;
        double[] cumulativeWeights = new double[weights.length];
        
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] < 0) {
                throw new IllegalArgumentException(String.format("Negative value not allowed as probability weight: %f < 0.", weights[i]));
            }
            totalWeight += weights[i];
            cumulativeWeights[i] = totalWeight;
        }
    
        if (totalWeight == 0.0) {
            throw new IllegalArgumentException("At least one weight must be greater than zero.");
        }
    
        // Generate a random double in the range [0.0, totalWeight)
        double res = randDouble(totalWeight);
    
        // Binary search for the appropriate index
        int low = 0, high = weights.length - 1, mid;
        while (low < high) {
            mid = (low + high) / 2;
            if (res < cumulativeWeights[mid]) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
    
        return low;
    }
    
}