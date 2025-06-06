/*
 * Engine.java
 * Steven LaGoy
 * Created: 26 September 2024 at 12:21 AM
 * Modified: 30 May 2025
 */

package main.core;

// IMPORTS ----------------------------------------------------------------------------------------

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import main.core.characters.CharacterManager;
import main.core.characters.names.NameManager;
import main.core.demographics.DemographicsManager;
import main.core.graphics.ILogic;
import main.core.graphics.MouseInput;
import main.core.graphics.Window;
import main.core.graphics.game.TestGame;
import main.core.graphics.utils.Consts;
import main.core.map.MapManager;

/**
 * Engine is the main driver of the game engine, facilitating the initialization and function
 * of the game by tracking critical details for game settings and other information.
 * <p>
 * This class is final and has no instance variables, and is not designed to be instantiated.
 */
public final class Engine {

    /** This class is non-instantiable. All values and functions should be accessed in a static way. */
    private Engine() {} // Non-Instantiable

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                    CONSTANTS AND ENUMS                                    //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    // GRAPHICS CONSTANTS -------------------------------------------------------------------------

    public static final long NANOSECOND = 1_000_000_000;
    public static final float FRAMERATE = 1000;
    public static int fps;
    private static float frametime = 1.0f / FRAMERATE;
    private static boolean isRunning;
    private static Window window;
    public static Window getWindow() { return window; }
    private static GLFWErrorCallback errorCallback;
    private static MouseInput mouse;
    private static ILogic gameLogic;
    
    // DIFFICULTY ---------------------------------------------------------------------------------

    /** Current difficulty level of the game. */
    private static Difficulty gameDifficulty;
    public static Difficulty getGameDifficulty() { return gameDifficulty; }
    public static boolean setGameDifficulty(Difficulty difficulty) { return (gameDifficulty = difficulty) != null; }
    /** Dificulty values impact some calculations, impacting the difficulty of the game. */
    public static enum Difficulty {

        LEVEL_1 (1, "Aspiring Politician"),
        LEVEL_2 (2, "Fledgling Politician"),
        LEVEL_3 (3, "Hometown Hero"),
        LEVEL_4 (4, "Career Politican"),
        LEVEL_5 (5, "Political Machine");

        public final int value;
        public final String name;
        private Difficulty(int value, String name) {
            this.value = value;
            this.name = name;
        }
        public static Difficulty level(int value) {
            for (Difficulty diff : Difficulty.values())
                if (diff.value == value)
                    return diff;
            throw new IllegalArgumentException("Invalid difficulty level: " + value);
        }
        public static Difficulty label(String label) {
            String target = label.trim().toUpperCase().replace("\\s", "_");
            for (Difficulty diff : Difficulty.values())
                if (diff.toString().equals(target))
                    return diff;
            throw new IllegalArgumentException("Invalid difficulty label: " + label);
        }
        public static Difficulty name(String name) {
            for (Difficulty diff : Difficulty.values())
                if (diff.name.equals(name))
                    return diff;
            throw new IllegalArgumentException("Invalid difficulty name: " + name);
        }
    }

    // LANGUAGE -----------------------------------------------------------------------------------

    /** Current language of the game. */
    private static Language gameLanguage;
    public static Language getGameLanguage() { return gameLanguage; }
    /**
     * Loads localization for the language, and if successful sets the game language.
     * @return {@code true} if successfully loaded and set language, {@code false} otherwise.
     */
    public static boolean setGameLanguage(Language language) {
        if (loadLocalizations(language)) {
            gameLanguage = language;
            return true;
        }
        return false;
    }
    /** Default language for engine initialization. */
    public static final Language defaultLanguage = Language.EN;
    /** Languages which game text could appear in. */
    public static enum Language {
        
        EN ("English"),
        ZH ("简体中文"),
        RU ("Русский"),
        ES ("Español"),
        PT ("Português"),
        DE ("Deutsch"),
        FR ("Français"),
        JA ("日本語"),
        PL ("Polski"),
        TR ("Türkçe");

        public final String name;
        private Language(String name) { this.name = name; }
        public static Language fromName(String name) {
            for (Language lang : Language.values())
                if (lang.name.equals(name))
                    return lang;
            throw new IllegalArgumentException("Invalid language name: " + name);
        }
        public static Language label(String label) {
            String target = label.trim().toUpperCase().replace("\\s", "_");
            for (Language lang : Language.values())
                if (lang.toString().equals(target))
                    return lang;
            throw new IllegalArgumentException("Invalid language label: " + label);
        }
    }
    /** For each language, stores tag : sentence pairs for localization tags. */
    public static Map<Language, Map<String, String>> localizations;

    public static boolean loadLocalizations(Language language) {
        boolean successFlag = true;

        if (localizations == null)
            localizations = new HashMap<Language, Map<String, String>>();

        HashMap<String, String> local = new HashMap<>();

        List<String> contents = IOUtil.readFile(Path.of(String.format("%s/%s%s", FilePaths.LOCALIZATION_RESOURCES, language, FilePaths.SYSTEM_TEXT_LOC)));
        if (contents == null) successFlag = false;
        for (String line : contents) {
            if (line == null || line.isBlank()) continue;
            String[] parts = StringOperations.splitByUnquotedString(line, ":", 2);
            if (parts.length != 2) {
                Engine.log("INVALID LOCALIZATION ENTRY", String.format("In localization file for language %s, the entry \"%s\" was invalid.", language.toString(), line), new Exception());
                continue;
            }
            local.put(parts[0], parts[1]);
        }
        if (local.size() == 0) successFlag = false;
        Engine.localizations.put(language, local);

        return successFlag;
    }

    public static String getLocalization(String tag) {
        if (gameLanguage == null) {
            Engine.log("UNINITIALIZED GAME LANGUAGE", String.format("The game language was never initialized or was set to null."), new Exception());
            return null;
        }
        return getLocalization(tag, gameLanguage);
    }

    public static String getLocalization(String tag, Language language) {
        String res = localizations.get(language).get(tag);
        if (res == null) {
            Engine.log("INVALID LOCALIZATION TAG", String.format("Attempted to access localization tag %s for language %s, which is invalid.", tag, language.toString()), new Exception());
            return "INVALID LOCALIZATION TAG";
        }
        return res;
    }

    // GAME SPEED SETTINGS ------------------------------------------------------------------------
    /** The Base Speed of the game, representing the minimum tick time in miliseconds */
    public static final long baseSpeed = 125L;
    public static final long[] speedSettings = {baseSpeed, baseSpeed*2, baseSpeed*4, baseSpeed*8, baseSpeed*16}; // Time in between ticks
    private static int speedSetting = 4;
    public static int getSpeedSetting() { return speedSetting; }
    public static void setSpeedSetting(int speed) {
        speedSetting = Math.clamp(speed, 0, speedSettings.length - 1);
        tickSpeed = speedSettings[speedSetting];
    }
    private static long tickSpeed = speedSettings[speedSetting];
    public static long getTickSpeed() { return tickSpeed; }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                        GAME SETUP                                         //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public static boolean init() {
        boolean successFlag = true;
        try {
            long startTime = System.nanoTime();
            GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
            reset();
            setGameLanguage(defaultLanguage); // Load localization and set language
            window = new Window(Consts.TITLE, 0, 0, false);
            gameLogic = new TestGame();
            mouse = new MouseInput();
            window.init();
            gameLogic.init();
            mouse.init();

            successFlag = successFlag && DemographicsManager.createDemographicBlocs();
            successFlag = successFlag && NameManager.readAllNamesFiles();
            successFlag = successFlag && MapManager.createMap();
            long elapsedTime = System.nanoTime() - startTime;
            Engine.log("Engine initialization complete in " + String.valueOf(elapsedTime / 1000000) + " miliseconds.");
        }
        catch (Exception e) {
            // Engine.cleanup();
            Engine.log("Failed to initialize game engine.");
            Engine.log(e);
            successFlag = false;
        }
        return successFlag;
    }

    public static void run() {
        Engine.isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while (isRunning) {
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) NANOSECOND;
            frameCounter += passedTime;

            Engine.input();

            while (unprocessedTime > frametime) {
                render = true;
                unprocessedTime -= frametime;

                if (window.windowShouldClose())
                    stop();
                
                if (frameCounter >= NANOSECOND) {
                    fps = frames;
                    window.setTitle(Consts.TITLE + " - FPS: " + fps);
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if (render) {
                Engine.update(frametime);
                Engine.render();
                frames++;
            }
        }
        Engine.cleanup();
    }

    public static boolean reset() {
        boolean successFlag = true;
        successFlag = successFlag && writeErrorToLog();
        successFlag = successFlag && clearErrorFile();
        log("RESET", "Reset Engine");
        return successFlag;
    }

    public static void stop() {
        if(!isRunning) return;
        window.cleanup();
        gameLogic.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
        Engine.isRunning = false;
        log("DONE");
        writeErrorToLog();
    }

    private static void input() {
        gameLogic.input();
        mouse.input();
    }

    private static void render() {
        gameLogic.render();
        window.update();
    }

    private static void update(float interval) {
        gameLogic.update(interval, mouse);
    }

    public static boolean tick() {
        input();
        boolean active = true;

        //System.out.println(DateManager.currentGameDate);
        active = active && DateManager.incrementQuarterHour();
        return active;
    }

    public static void writeSave() {
        if (CharacterManager.getPlayer() == null) return;
        try {
            String saveName = String.format("%s - %s", CharacterManager.getPlayer().getName().getLegalName(), DateManager.formattedCurrentDate());
            File saveFile = new File(FilePaths.SAVES_DIR.toString() + saveName + ".txt");
            for (int i = 1; saveFile.exists(); i++) {
                saveName = String.format("%s - %s %s", CharacterManager.getPlayer().getName().getLegalName(), DateManager.formattedCurrentDate(), String.format("(%d)", i));
                saveFile = new File(FilePaths.SAVES_DIR.toString() + saveName + ".txt");
            }
            saveFile.createNewFile();
            FileWriter fw = new FileWriter(saveFile);
            fw.append(CharacterManager.generateSaveString());
            fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cleanup() {
        window.cleanup();
        gameLogic.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    public static List<String> listSaveNames() {
        return new ArrayList<>();
    }

    public static void readSave(String saveName) {

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // NUMBER FUNCTIONS //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private static Map<Integer, Boolean> primeCache = new HashMap<>();
    public static boolean isPrime(int value) {
        if (value <= 1) return false;
        if (primeCache.containsKey(value))
            return primeCache.get(value);

        for (int i = 2; i <= Math.sqrt(value); i++) {
            if (value % i == 0) {
                primeCache.put(value, false);
                return false;
            }
        }
        primeCache.put(value, true);
        return true;
    }

    public static int nextPrime(int value) {
        if (value < 1) return 1;
        else if (value == 1) return 2;
        while (!isPrime(value++));
        return value;
    }

    /** Suffixes to place after numbers in ordinal form. */
    static String[] suffixes = {"th", "st", "nd", "rd", "th"};
    /**
     * Takes an int value and returns a String for the ordinal form of that number. Example: toOrdinal(1) -> "1st", toOrdinal(2) -> "2nd", toOrdinal(5) -> "5th"
     * @param value A number.
     * @return The ordinal form of the value.
     */
    public static String toOrdinal(int value) {
        int index;
        switch (Math.abs(value) % 100) {
            case 11:
            case 12:
            case 13:
                index = 0;
                break;
            default:
                index = Math.abs(value) % 10 <= 3 ? Math.abs(value) % 10 : 4;
        }
        return (value < 0 ? "negative " : "") + String.valueOf(Math.abs(value)) + suffixes[index];
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                     LOGGING FUNCTIONS                                     //
    ///////////////////////////////////////////////////////////////////////////////////////////////

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

            logWriter.printf("%s : %s%n", getDate(), logline);
            System.out.printf("%s : %s%n", getDate(), logline);
            logWriter.close();
            return;
        }
        catch (IOException e) {
            System.out.println(e);
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
            System.out.printf("%s : %s %n", getDate(), stackTrace);
            logWriter.close();
            return;
        }
        catch (IOException e) {
            System.out.println(e);
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

            logWriter.printf("%s : %s: %s%n", getDate(), context.toUpperCase(), logline);
            System.out.printf("%s : %s: %s%n", getDate(), context.toUpperCase(), logline);
            logWriter.close();
            return;
        }
        catch (IOException e) {
            System.out.println(e);
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
            logWriter.printf("%s : %s: %s @ %s%n", getDate(), context.toUpperCase(), logline, stackTrace);
            System.out.printf("%s : %s: %s @ %s%n", getDate(), context.toUpperCase(), logline, stackTrace);
            logWriter.close();
            return;
        }
        catch (IOException e) {
            System.out.println(e);
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
    private static boolean writeErrorToLog() {
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
    private static boolean clearErrorFile() {
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

    //////////////////////////////////////////////////////////////////////////////////////////
    //                          RANDOMNESS AND SELECTION FUNCTIONS                          //
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Selects a float between 0.0 and 1.0 which can be used as a percentage.
     * @return A pseudorandomly selected float in the range [0.0, 1.0)
     * @see #randPercent(float, float)
     */
    public static float randPercent() {
        return randPercent(0.0f, 1.0f);
    }

    /**
     * Selects a float between the min and the max.
     * <p>
     * <i>If min is a larger value than max, their values will be swapped.</i>
     * @param min The minimum value which can be selected (inclusive)
     * @param max The maximum value which can be selected (exclusive)
     * @return A pseudorandomly selected float in the range [min, max)
     */
    public static float randPercent(float min, float max) {
        // will perform the same if min and max are flipped
        Random rand = new Random();
        return (max - min) * rand.nextFloat() + min; // return a float between min and max (exclusive), equally distributed
    }

    /**
     * Selects a double between the min and the max.
     * @param min The minimum value which can be selected (inclusive)
     * @param max The maximum value which can be selected (exclusive)
     * @return A pseudorandomly selected double in the range [min, max)
     */
    public static double randDouble(double min, double max) {
        // will perform the same if min and max are flipped
        Random rand = new Random();
        return (max - min) * rand.nextDouble() + min; // return a double between min and max (exclusive), equally distributed
    }

    /**
     * Selects an integer between zero and the max, evenly distributed.
     * @param max The maximum value which can be randomly selected (exclusive)
     * @return A pseudorandomly generated integer in the range [0, max)
     */
    public static int randInt(int max) {
        return randInt(0, max);
    }

    /**
     * Selects an integer between the min and the max, evenly distributed.
     * @param min The minimum value which can be selected (inclusive)
     * @param max The maximum value which can be selected (exclusive)
     * @return A pseudorandomly generated integer in the range [min, max)
     */
    public static int randInt(int min, int max) {
        if (max < min) throw new IllegalArgumentException(String.format("The minimum is less than the maximum: %d < %d.%n", max, min));
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min; // return an integer between min and max (inclusive), equally distributed
    }

    /** Selects one value from an array.
     * @param items The array to select from.
     * @return One randomly selected value.
     */
    public static <T> T randSelect(T[] items) {
        if (items.length == 0) return null;
        Random rand = new Random();
        int randomNumber = rand.nextInt(items.length);
        return items[randomNumber];
    }

    /** Selects one value from a collection.
     * @param items The collection to select from.
     * @return One randomly selected value.
     */
    public static <T> T randSelect(Collection<T> items) {
        if (items == null || items.size() == 0) return null;

        Random rand = new Random();
        int randomNumber = rand.nextInt(items.size());
        int i = 0;
        for (T item : items) {
            if (i == randomNumber) {
                return item;
            }
            i++;
        }
        return null; // Should never reach here if items.size() > 0
    }

    /**
     * Selects a value from the items array, with the weight for selection given by the weights array. The arrays must have the same length.
     * @param <T> Object
     * @param items Array of selectable values. Must have same length as weights array.
     * @param weights Array of weights for each value. Must have same length as items array.
     *                For any index n within the items of either array, {@code items[n]} corresponds to {@code weights[n]}.
     *                The probability that any item i will be selected is given by {@code weights[i] / sum(weights)}
     * @return One value selected from the items array, or {@code null} if unsuccessful.
     */
    public static <T> T weightedRandSelect(T[] items, double[] weights) {
        if (items.length < 1 || weights.length < 1) {
            Engine.log("INVALID SELECTION FROM EMPTY ARRAY", String.format("Unable to select an item from an array with length < 1."), new Exception());
            return null;
        }
        if (items.length != weights.length) {
            Engine.log("WEIGHTED SELECTION FROM MISMATCHED ARRAYS", String.format("Provided arrays for a weighted selection have mismatched lengths."), new Exception());
            return null;
        }

        double totalWeight = 0;
        for (double weight : weights) totalWeight += weight;

        Random rand = new Random();
        double randomNumber = rand.nextDouble() * totalWeight;

        double cumulativeWeight = 0;
        for (int i = 0; i < items.length; i++) {
            cumulativeWeight += weights[i];
            if (randomNumber < cumulativeWeight) {
                return items[i];
            }
        }
        Engine.log("FAILURE TO SELECT", String.format("The weighted selection failed to select an item."), new Exception());
        return null; // Edge-case failure to select
    }

    /**
     * Selects a value from the items array, with the weight for selection given by the weights array. The arrays must have the same length.
     * @param <T> Object
     * @param items Collection of selectable values. Must have same size as weights array.
     * @param weights Collection of weights for each value. Must have same size as items array.
     *                For any index n within the items of either collection, {@code items[n]} corresponds to {@code weights[n]}.
     *                The probability that any item i will be selected is given by {@code weights[i] / sum(weights)}
     * @return One value selected from the items collection, or {@code null} if unsuccessful.
     */
    public static <T> T weightedRandSelect(Collection<T> items, Collection<Number> weights) {
        if (items.size() < 1 || weights.size() < 1) {
            Engine.log("INVALID SELECTION FROM EMPTY ARRAY", String.format("Unable to select an item from an array with length < 1."), new Exception());
            return null;
        }
        if (items.size() != weights.size()) {
            Engine.log("WEIGHTED SELECTION FROM MISMATCHED ARRAYS", String.format("Provided arrays for a weighted selection have mismatched lengths."), new Exception());
            return null;
        }

        double totalWeight = 0;
        for (Number weight : weights) totalWeight += weight.doubleValue();

        Random rand = new Random();
        double randomNumber = rand.nextDouble() * totalWeight;

        double cumulativeWeight = 0;
        for (T item : items) {
            cumulativeWeight += weights.iterator().next().doubleValue();
            if (randomNumber < cumulativeWeight) {
                return item;
            }
        }
        Engine.log("FAILURE TO SELECT", String.format("The weighted selection failed to select an item."), new Exception());
        return null; // Edge-case failure to select
    }

    /**
     * Selects a value from the items map, with the weight for selection given by the item's map value. 
     * @param <T> Object
     * @param items Map of Object to Number, where the keys are Objects to select from and the values are the weights for each object.
     *              The probability that any item i will be selected is given by {@code items[i] / sum(items.values)}
     * @return One value selected from the items map, or {@code null} if unsuccessful.
     */
    public static <T> T weightedRandSelect(Map<T, ? extends Number> items) {
        if (items == null || items.isEmpty()) {
            Engine.log("INVALID SELECTION FROM EMPTY ARRAY", String.format("Unable to select an item from an empty or null array."), new Exception());
            return null;
        }
        // map requires bijective relationship between keys and values

        double totalWeight = 0.0;
        for (Number weight : items.values()) totalWeight += weight.doubleValue();
        if (totalWeight == 0.0) return randSelect(items.keySet());

        Random rand = new Random();
        double randomNumber = rand.nextDouble() * totalWeight;

        double cumulativeWeight = 0;
        for (T item : items.keySet()) {
            cumulativeWeight += items.get(item).doubleValue();
            if (randomNumber < cumulativeWeight) {
                return item;
            }
        }
        Engine.log("FAILURE TO SELECT", String.format("The weighted selection failed to select an item."), new Exception());
        return null; // Edge-case failure to select
    }

    public static int probabilisticCount(float f) {
        int count = 0;
        while (randPercent() <= f) count++;
        return count;
    }
}
