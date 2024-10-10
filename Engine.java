import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
}