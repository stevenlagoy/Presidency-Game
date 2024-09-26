import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.io.IOException;

public class Engine {
    public void log(String logline) throws IOException {
        File logFile = new File("log.txt");
        logFile.createNewFile(); // does nothing if already exists
        PrintWriter logWriter = new PrintWriter(new FileWriter(logFile, true));

        logWriter.printf("%s : %s%n", getDate(), logline);
        logWriter.close();
        return;
    }
    public void log(String context, String logline) throws IOException {
        File logFile = new File("log.txt");
        logFile.createNewFile(); // does nothing if already exists
        PrintWriter logWriter = new PrintWriter(new FileWriter(logFile, true));

        logWriter.printf("%s : %s: %s%n", getDate(), context.toUpperCase(), logline);
        logWriter.close();
        return;
    }
    public void log(String context, String logline, String trace) throws IOException {
        File logFile = new File("log.txt");
        logFile.createNewFile(); // does nothing if already exists
        PrintWriter logWriter = new PrintWriter(new FileWriter(logFile, true));

        logWriter.printf("%s : %s: %s @ %s%n", getDate(), context.toUpperCase(), logline, trace);
        logWriter.close();
        return;
    }
    public void log(Exception e) throws IOException {
        File logFile = new File("log.txt");
        logFile.createNewFile(); // does nothing if already exists
        PrintWriter logWriter = new PrintWriter(new FileWriter(logFile, true));

        logWriter.printf("%s : %s @ %s%n", getDate(), e.toString(), e.getStackTrace()[0].toString());
        logWriter.close();
        return;
    }

    public String getDate(){
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new java.util.Date());
    }

    public void reset() throws IOException {
        File logFile = new File("log.txt");
        logFile.createNewFile(); // does nothing if already exists
        FileOutputStream logStream = new FileOutputStream(logFile, false);
        logStream.close();
        return;
    }
}
