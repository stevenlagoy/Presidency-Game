package main.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public final class Logger {
    
    /** This class is uninstantiable. All functions should be called in a static way. */
    private Logger() {}
    
    /**
     * Write the passed logline to the standard error file.
     * @param logline String to be written, explaining information about the event logged.
     * @see #log(Exception)
     * @see #log(String, String)
     * @see #log(String, String, Exception)
     */
    public static void log(String logline) {
        try {
            File errorFile = new File(FilePaths.ERROR_LOG.toString());
            errorFile.createNewFile(); // does nothing if already exists
            PrintWriter logWriter = new PrintWriter(new FileWriter(errorFile, true));

            logline = logline.replace("\n", " | ").replace("\r","");
            logWriter.printf("%s : %s%n", getDate(), logline);
            IOUtil.stdout.printf("%s : %s%n", getDate(), logline);
            logWriter.close();
            return;
        }
        catch (IOException e) {
            IOUtil.stdout.println(e);
            System.exit(-1);
        }
    }

    /**
     * Write the Exception to the standard error file.
     * @param logE Exception to be written. The Exception.printStackTrace() method will be used.
     * @see #log(String, String, Exception)
     */
    public static void log(Exception logE) {
        try {
            File errorFile = new File(FilePaths.ERROR_LOG.toString());
            errorFile.createNewFile(); // does nothing if already exists
            PrintWriter logWriter = new PrintWriter(new FileWriter(errorFile, true));

            StringWriter sw = new StringWriter();
            logE.printStackTrace(new PrintWriter(sw));
            String stackTrace = sw.toString().replace("\t", " -> ").replace("\n", "").replace("\r", ""); // Handle any carriage return characters
            logWriter.printf("%s : %s %n", getDate(), stackTrace);
            IOUtil.stdout.printf("%s : %s %n", getDate(), stackTrace);
            logWriter.close();
            return;
        }
        catch (IOException e) {
            IOUtil.stdout.println(e);
            System.exit(-1);
        }
    }

    /**
     * Write the passed logline to the standard error file, with the context string as a label.
     * @param context String for the label/context in which the log is being written. Will be put in full-capitals.
     * @param logline String to be written, explaining information about the event logged.
     * @see #log(String, String, Exception)
     */
    public static void log(String context, String logline) {
        try {
            File errorFile = new File(FilePaths.ERROR_LOG.toString());
            errorFile.createNewFile(); // does nothing if already exists
            PrintWriter logWriter = new PrintWriter(new FileWriter(errorFile, true));

            logline = logline.replace("\n", " | ").replace("\r","");
            logWriter.printf("%s : %s: %s%n", getDate(), context.toUpperCase(), logline);
            IOUtil.stdout.printf("%s : %s: %s%n", getDate(), context.toUpperCase(), logline);
            logWriter.close();
            return;
        }
        catch (IOException e) {
            IOUtil.stdout.println(e);
            System.exit(-1);
        }
    }

    /**
     * Write the passed logline to the standard error file, with the context string as a label and with the passed Exception's stack trace also being written.
     * @param context String for the label/context in which the log is being written. Will be put in full-capitals.
     * @param logline String to be written, explaining information about the event logged.
     * @param logE Exception to be written. The Exception.printStackTrace() method will be used.
     */
    public static void log(String context, String logline, Exception logE) {
        try {
            File errorFile = new File(FilePaths.ERROR_LOG.toString());
            errorFile.createNewFile(); // does nothing if already exists
            PrintWriter logWriter = new PrintWriter(new FileWriter(errorFile, true));

            StringWriter sw = new StringWriter();
            logE.printStackTrace(new PrintWriter(sw));
            String stackTrace = sw.toString().replace("\t", " -> ").replace("\n", "").replace("\r", "");
            logline = logline.replace("\n", " | ").replace("\r","");
            logWriter.printf("%s : %s: %s @ %s%n", getDate(), context.toUpperCase(), logline, stackTrace);
            IOUtil.stdout.printf("%s : %s: %s @ %s%n", getDate(), context.toUpperCase(), logline, stackTrace);
            logWriter.close();
            return;
        }
        catch (IOException e) {
            IOUtil.stdout.println(e);
            System.exit(-1);
        }
    }

    /**
     * Gives the current date as a formatted string for logging purposes.
     * @return The current date, formatted as {@code yyyy.MM.dd.HH.mm.ss.SSS}
     */
    private static String getDate() {
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new java.util.Date());
    }

    /**
     * Writes the contents of the Error file into the longterm Log file.
     */
    public static boolean writeErrorToLog() {
        boolean successFlag = true;
        try {
            File errorFile = new File(FilePaths.ERROR_LOG.toString());
            errorFile.createNewFile(); // does nothing if already exists
            File logFile = new File(FilePaths.LOG_FILE.toString());
            logFile.createNewFile();
            ArrayList<String> contents = new ArrayList<>();
            try (Scanner scanner = IOUtil.createScanner(errorFile)) {
                while (scanner.hasNext())
                    contents.add(scanner.nextLine());
            }
            catch (FileNotFoundException e) {
                log("ERROR FILE NOT FOUND", "The error file was unable to be found.", e);
                successFlag = false;
            }
            try (PrintWriter writer = IOUtil.createWriter(logFile, true)) {
                for (String line : contents)
                    writer.println(line);
            }
            catch (IOException e) {
                log("LOG FILE NOT FOUND", "The log file was unable to be found.", e);
                successFlag = false;
            }
        }
        catch (IOException e) {
            log("ERROR/LOG FILE NOT FOUND", "The error file or log file was unable to be found.", e);
            successFlag = false;
        }
        return successFlag;
    }

    /**
     * Empties the Error file of all contents.
     */
    public static boolean clearErrorFile() {
        boolean successFlag = true;
        try {
            File errorFile = new File(FilePaths.ERROR_LOG.toString());
            errorFile.createNewFile();
            FileOutputStream errorStream = new FileOutputStream(errorFile, false);
            errorStream.close();
        }
        catch (IOException e) {
            log("ERROR/LOG FILE NOT FOUND", "Somehow, the error file or log file was unable to be found.", e);
            successFlag = false;
        }
        return successFlag;
    }

    public static void generateMemoryReport() {
        Runtime runtime = Runtime.getRuntime();
        StringBuilder sb = new StringBuilder();

        long freeMemory = runtime.freeMemory();
        long totalMemory = runtime.totalMemory();
        long maxMemory = runtime.maxMemory();
        long usedMemory = totalMemory - freeMemory;
        double usagePercent = (usedMemory * 100.0) / totalMemory;
        double maxPercent = (usedMemory * 100.0) / maxMemory;

        sb.append(String.format("Free Memory: %,.2f KB%n", (freeMemory * 1.0) / 1024));
        sb.append(String.format("Used Memory: %,.2f KB%n", (usedMemory * 1.0) / 1024));
        sb.append(String.format("Total Memory: %,.2f KB%n", (totalMemory * 1.0) / 1024));
        sb.append(String.format("Max Memory: %,.2f KB%n", (maxMemory * 1.0) / 1024));
        sb.append(String.format("Usage Percent: %.2f%%%n", usagePercent));
        sb.append(String.format("Usage of Max Percent: %.2f%%%n", maxPercent));
        log("MEMORY REPORT", sb.toString().trim());
    }

}
