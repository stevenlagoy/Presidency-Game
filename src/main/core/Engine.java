package main.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import main.core.characters.Candidate;
import main.core.characters.CharacterManager;
import main.core.demographics.DemographicsManager;
import main.core.graphics.ILogic;
import main.core.graphics.MouseInput;
import main.core.graphics.WindowManager;
import main.core.graphics.utils.Consts;
import main.core.map.City;
import main.core.map.CongressionalDistrict;
import main.core.map.County;
import main.core.map.MapManager;
import main.core.map.State;
import main.core.politics.EventManager;

public class Engine {

    /*
     * Graphics Constants
     */
    public static final long NANOSECOND = 1_000_000_000;
    public static final float FRAMERATE = 1000;
    public static int fps;
    private static float frametime = 1.0f / FRAMERATE;
    private static boolean isRunning;
    private static WindowManager window;
    private static GLFWErrorCallback errorCallback;
    private static MouseInput mouse;
    private static ILogic gameLogic;


    /*
     * Game Constants
     */
    public enum Difficulty
    {
        LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, LEVEL_5
    }

    public static Language language;
    public static enum Language
    {
        EN, ZH, RU, ES, PT, DE, FR, JA, PL, TR
    }
    
    public static HashMap<String, HashMap<String, String>> localizations;

    /*
     * Speed Settings
     */
    public static final long baseSpeed = 2000L; // Make sure this is a multiple of the smallest speedsetting
    public static final long[] speedSettings = {baseSpeed, baseSpeed/2, baseSpeed/4, baseSpeed/8, baseSpeed/16}; // Time in between ticks
    public static int speedSetting = 4;
    public static long tickSpeed = speedSettings[speedSetting];

    // Static class to control how scanners operate: should always use UTF-8 encoding to allow special characters 
    public static class ScannerUtil {
        public Scanner createScanner(InputStream inputStream) {
            return new Scanner(inputStream, StandardCharsets.UTF_8.name());
        }
        public Scanner createScanner(File file) throws FileNotFoundException {
            return new Scanner(file, StandardCharsets.UTF_8.name());
        }
    }

    static ScannerUtil scannerUtil = new ScannerUtil();
    static Scanner stdin = scannerUtil.createScanner(System.in);

    public static MapManager mapManager = new MapManager();
    public static CharacterManager characterManager = new CharacterManager();
    public static DemographicsManager demograpicsManager = new DemographicsManager();
    public static EventManager eventManager = new EventManager();
    public static DateManager dateManager = new DateManager();

    public static Difficulty gameDifficulty;

    public static Candidate playerCandidate;

    public static void init() {
        try {
            // GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
            reset();

            openLocalizationFiles();
            // window = new WindowManager(Consts.TITLE, 0, 0, false);
            // gameLogic = new TestGame();
            // mouse = new MouseInput();
            // window.init();
            // gameLogic.init();
            // mouse.init();

            DemographicsManager.createDemographicBlocs();
            CharacterManager.readAllNamesFiles();
            MapManager.createMap();
        }
        catch (Exception e) {
            // Engine.cleanup();
            e.printStackTrace();
            Engine.log("Failed to initialize game engine. Exiting.");
            System.exit(-1);
        }
    }

    public static void run() {
        Engine.isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while (isRunning && false) {
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) NANOSECOND;
            frameCounter += passedTime;

            // Engine.input();

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
        // Engine.cleanup();
    }

    public static void reset() throws IOException {
        writeErrorToLog();
        clearErrorFile();
        log("RESET", "Reset Engine");
        return;
    }

    public static void stop() {
        if(!isRunning) return;
        isRunning = false;
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

    private static void cleanup() {
        window.cleanup();
        gameLogic.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    public static boolean tick() {
        boolean active = true;
        String[] responses = {"Q", "QUIT", "L CHARACTERS", "LIST CHARACTERS", "L STATES", "LIST STATES", "L CONGRESSIONAL_DISTRICTS", "LIST CONGRESSIONAL_DISTRICTS", "L COUNTIES", "LIST COUNTIES", "L CITIES", "LIST CITIES"};
        String input = getInput(responses);
        switch (input) {
            case "Q" :
            case "QUIT" :
                System.out.print("Quitting\n");
                return false;
            case "L CHARACTERS" :
            case "LIST CHARACTERS" :
                System.out.print("Listing Characters:\n");
                for (main.core.characters.Character character : CharacterManager.getAllCharacters()) {
                    System.out.printf("\t%s\n", character.getName());
                }
                System.out.print("Done\n");
                break;
            case "L STATES" :
            case "LIST STATES" :
                System.out.print("Listing States:\n");
                for (State state : MapManager.getStates()) {
                    System.out.printf("\t%s\n", state.getName());
                }
                System.out.print("Done\n");
                break;
            case "L CONGRESSIONAL_DISTRICTS" :
            case "LIST CONGRESSIONAL_DISTRICTS" :
                System.out.print("Listing Congressional Districts:\n");
                for (CongressionalDistrict district : MapManager.getCongressionalDistricts()) {
                    System.out.printf("\t%s\n", district.getOfficeID());
                }
                System.out.print("Done\n");
                break;
            case "L COUNTIES" :
            case "LIST COUNTIES" :
                System.out.print("Listing Counties:\n");
                for (County county : MapManager.getCounties()) {
                    System.out.printf("\t%s, %s\n", county.getName(), county.getState().getAbbreviation());
                }
                System.out.print("Done\n");
                break;
            case "L CITIES" :
            case "LIST CITIES" :
                System.out.print("Listing Cities:\n");
                for (City city : MapManager.getCities()) {
                    System.out.printf("\t%s, %s\n", city.getName(), city.getState().getAbbreviation());
                }
                System.out.print("Done\n");
                break;
        }

        //System.out.println(DateManager.currentGameDate);
        active = active && DateManager.incrementQuarterHour();
        return active;
    }

    public static void writeSave() {
        try {
            String saveName = String.format("%s - %s", playerCandidate.getName().getFullName(), DateManager.formattedCurrentDate());
            File saveFile = new File(FilePaths.saves, saveName + ".txt");
            for (int i = 1; saveFile.exists(); i++) {
                saveName = String.format("%s - %s %s", playerCandidate.getName().getFullName(), DateManager.formattedCurrentDate(), String.format("(%d)", i));
                saveFile = new File(FilePaths.saves, saveName + ".txt");
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

    public static List<String> listSaveNames() {
        return null;
    }

    public static void readSave(String saveName) {

    }

    // public static void initOpenGL() {
    //     // Initialize GLFW
    //     if (!glfwInit()) {
    //         throw new IllegalStateException("Unable to initialize GLFW");
    //     }

    //     // Configure GLFW
    //     glfwDefaultWindowHints();
    //     glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    //     glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

    //     // Create the window
    //     window = glfwCreateWindow(800, 600, "OpenGL Window", NULL, NULL);
    //     if (window == NULL) {
    //         throw new RuntimeException("Failed to create the GLFW window");
    //     }

    //     // Make the OpenGL context current
    //     glfwMakeContextCurrent(window);
    //     // Enable v-sync
    //     glfwSwapInterval(1);
    //     // Make the window visible
    //     glfwShowWindow(window);
    // }

    // public static void runOpenGL() {
    //     // This line is critical for LWJGL's interoperation with GLFW's
    //     // OpenGL context, or any context that is managed externally.
    //     // LWJGL detects the context that is current in the current thread,
    //     // creates the GLCapabilities instance and makes the OpenGL
    //     // bindings available for use.
    //     GL.createCapabilities();

    //     // Set the clear color
    //     glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    //     // Run the rendering loop until the user has attempted to close
    //     // the window or has pressed the ESCAPE key.
    //     while (!glfwWindowShouldClose(window)) {
    //         glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

    //         // Swap the color buffers
    //         glfwSwapBuffers(window);

    //         // Poll for window events. The key callback above will only be
    //         // invoked during this call.
    //         glfwPollEvents();
    //     }

    //     // Free the window callbacks and destroy the window
    //     glfwFreeCallbacks(window);
    //     glfwDestroyWindow(window);

    //     // Terminate GLFW and free the error callback
    //     glfwTerminate();
    // }

    // public static long getWindow() {
    //     return window;
    // }

    public static void writeErrorToLog() {
        try {
            File errorFile = new File(FilePaths.ERROR);
            errorFile.createNewFile(); // does nothing if already exists
            File logFile = new File(FilePaths.LOG);
            logFile.createNewFile();
            Scanner scanner = scannerUtil.createScanner(errorFile);
            ArrayList<String> contents = new ArrayList<>();
            while (scanner.hasNext()) contents.add(scanner.nextLine());
            scanner.close();
            PrintWriter writer = new PrintWriter(new FileOutputStream(logFile, true));
            for (String line : contents) writer.println(line);
            writer.close();
        }
        catch (IOException e) {
            log("ERROR/LOG FILE NOT FOUND", "The error file or log file was unable to be found.", e);
            return;
        }
    }
    public static void clearErrorFile() {
        try {
            File errorFile = new File(FilePaths.ERROR);
            errorFile.createNewFile();
            FileOutputStream errorStream = new FileOutputStream(errorFile, false);
            errorStream.close();
        }
        catch (IOException e) {
            log("ERROR/LOG FILE NOT FOUND", "Somehow, the error file or log file was unable to be found.", e);
            return;
        }
    }

    public static String getInput(String[] responses) {
        /* getInput
         * takes responses, a list of acceptable strings
         * only returns when an acceptable input is given
         * returns an integer corresponding to the index of the accepted string in the responses list
        */

        String input = "";
        while (true) {
            System.out.print("> ");
            input = stdin.nextLine().toUpperCase();
            for (int i = 0; i < responses.length; i++) {
                if (responses[i].equals(input)) {
                    return responses[i];
                }
            }
            System.out.println("Unrecognized Input");
        }
    }
    public static int getInput() {
        /* getInput
         * asks for an input until one is given
         * returns when input is recieved
        */

        String input = "";
        while (true) {
            System.out.print("> ");
            input = stdin.nextLine().toUpperCase();
            if (input != null) return 1;
        }
    }
    public static HashMap<Object, Object> readJSONFile(String filename) {
        ArrayList<String> contents = new ArrayList<String>();
        try {
            File file = new File(filename);
            Scanner scanner = scannerUtil.createScanner(file);
            while (scanner.hasNextLine()) {
                contents.add(scanner.nextLine());
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {
            Engine.log("FILE NOT FOUND", String.format("The file %s was not found.", filename), e);
            return null;
        }
        // split the contents by JSON object, use nested Lists to represent the JSON structure
        Stack<Integer> stack = new Stack<Integer>();
        Integer[] indices = new Integer[contents.size()];
        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i) == null) break;
            if (contents.get(i).contains("{")) stack.push(i);
            if (contents.get(i).contains("}")) {
                try {
                    indices[stack.pop()] = i;
                }
                catch (EmptyStackException e) {
                    Engine.log(String.format("JSON file \"%s\" is malformed, missing opening at line %d.", filename, i));
                    return null;
                }
            }
        }
        if (!stack.isEmpty()) {
            Engine.log("JSON file is malformed, missing closing at line " + stack.pop());
            return null;
        }
        return readJSONObject(contents.subList(1, contents.size()-1));
    }
    public static HashMap<Object, Object> readJSONObject(List<String> contents) {
        HashMap<Object, Object> object = new HashMap<Object, Object>();
        for (int i = 0; i < contents.size(); i++) {
            String line = contents.get(i);
            if (line.trim().isEmpty()) continue; // Skip empty lines
            String key = line.split(":")[0].trim().replace("\"", "");
            if (key.equals("")) continue; // Skip empty keys: invalid entry format
            String value = "";
            try {
                int colonIndex = -1;
                for (int j = 0; j < line.length(); j++) {
                    if (line.charAt(j) == ':' && !isInString(line, j)) {
                        colonIndex = j;
                        break;
                    }
                }
                if (colonIndex != -1) {
                    value = line.substring(colonIndex + 1).trim();
                }
                else {
                    value = "";
                }
                if (containsUnquotedChar(value, '[')) { // the value is a list
                    if (value.replaceAll("\\[\\s*\\]", "").replace(",", "").trim().equals("")) {
                        // empty list
                        value = "";
                    }
                    else {
                        List<String> list = new ArrayList<String>();
                        int startIndex = 2;
                        for (int j = 2; j < value.length() - 2; j++) {
                            if (value.charAt(j) == ',' && !isInString(value, j)) {
                                list.add(value.substring(startIndex, j).replace("\"", "").replace(",", "").trim());
                                startIndex = j;
                            }
                        }
                        list.add(value.substring(startIndex, value.length() - 2).replace("\"", "").replace(",", "").trim());
                        value = list.toString();
                    }
                }
                else
                    value = value.replace(",","");
            }
            catch (ArrayIndexOutOfBoundsException e) {
                // do nothing - this is expected at the end of a JSON object
            }
            if (containsUnquotedChar(line, '{') && !containsUnquotedChar(line, '}')) { // start of an object
                // jump over recursively-read lines to start of next object
                int braceCount = 1, startIndex = i + 1;
                while (braceCount > 0 && i < contents.size() - 1) {
                    i++;
                    String currentLine = contents.get(i);
                    if (containsUnquotedChar(currentLine, '{')) braceCount++;
                    if (containsUnquotedChar(currentLine, '}')) braceCount--;
                }
                // read the object
                object.put(key, readJSONObject(contents.subList(startIndex, contents.size())));
            }
            else if (!containsUnquotedChar(line, '{') && containsUnquotedChar(line, '}')) { // end of an object
                // end the object
                return object;
            }
            else if (containsUnquotedChar(line, '{') && containsUnquotedChar(line, '}')) { // object all on one line
                String objectContent = line.substring(line.indexOf("{") + 1, line.indexOf("}")).trim();
                HashMap<Object, Object> innerObject = new HashMap<>();
                if (!objectContent.isEmpty()) {
                    String[] pairs = objectContent.split(",");
                    for (String pair : pairs) {
                        String[] keyValue = pair.split(":");
                        if (keyValue.length == 2) {
                            String innerKey = keyValue[0].trim().replace("\"", "");
                            String innerValue = keyValue[1].trim().replace("\"", "");
                            innerObject.put(innerKey, innerValue);
                        }
                    }
                }
                object.put(key, innerObject);
                return object;
            }
            else{
                object.put(key, value.trim().replace("\"", ""));
            }
        }
        return object;
    }

    private static boolean isInString(String line, int position) {
        boolean inString = false;
        for (int i = 0; i < position; i++) {
            if (line.charAt(i) == '"' && (i == 0 || line.charAt(i-1) != '\\')) {
                inString = !inString;
            }
        }
        return inString;
    }
    
    private static boolean containsUnquotedChar(String line, char target) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == target && !isInString(line, i)) {
                return true;
            }
        }
        return false;
    }

    public static void log(String logline) {
        try {
            File errorFile = new File(FilePaths.ERROR);
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
    public static void log(String context, String logline) {
        try {
            File errorFile = new File(FilePaths.ERROR);
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
    public static void log(String context, String logline, Exception logE) {
        try {
            File errorFile = new File(FilePaths.ERROR);
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
    public static void log(Exception logE) {
        try {
            File errorFile = new File(FilePaths.ERROR);
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

    public static String getDate() {
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new java.util.Date());
    }

    public static float randPercent() {
        return randPercent(0.0f, 1.0f);
    }

    public static float randPercent(float min, float max) {
        // will perform the same if min and max are flipped
        Random rand = new Random();
        return (max - min) * rand.nextFloat() + min; // return a float between min and max (exclusive), equally distributed
    }
    public static double randDouble(double min, double max) {
        // will perform the same if min and max are flipped
        Random rand = new Random();
        return (max - min) * rand.nextDouble() + min; // return a double between min and max (exclusive), equally distributed
    }

    public static int randInt(int max) {
        return randInt(0, max);
    }

    public static int randInt(int min, int max) {
        if (max < min) throw new IllegalArgumentException(String.format("The minimum is less than the maximum: %d < %d.%n", max, min));
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min; // return an integer between min and max (inclusive), equally distributed
    }

    /**
     * Generic method. Selects a value from the items in accordance with the weights array. The two arrays must have the same length. Returns null if unsuccessful.
     * @param items Any object array. The values from which to pick. Must have same length as weights array.
     * @param weights The weight for each value. Must have same length as items array. For any index n within the items of either array, items[n] corresponds to weights[n].
     * @return An entry selected from the items array weighted in accordance with the weights array.
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
    public static <T, P extends Number> T weightedRandSelect(Collection<T> items, Collection<P> weights) {
        if (items.size() < 1 || weights.size() < 1) {
            Engine.log("INVALID SELECTION FROM EMPTY ARRAY", String.format("Unable to select an item from an array with length < 1."), new Exception());
            return null;
        }
        if (items.size() != weights.size()) {
            Engine.log("WEIGHTED SELECTION FROM MISMATCHED ARRAYS", String.format("Provided arrays for a weighted selection have mismatched lengths."), new Exception());
            return null;
        }

        double totalWeight = 0;
        for (P weight : weights) totalWeight += weight.doubleValue();

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
    public static <K, V extends Number> K weightedRandSelect(Map<K, V> items) {
        if (items.size() < 1) {
            Engine.log("INVALID SELECTION FROM EMPTY ARRAY", String.format("Unable to select an item from an array with length < 1."), new Exception());
            return null;
        }
        // map requires bijective relationship between keys and values

        double totalWeight = 0;
        for (V weight : items.values()) totalWeight += weight.doubleValue();

        Random rand = new Random();
        double randomNumber = rand.nextDouble() * totalWeight;

        double cumulativeWeight = 0;
        for (K key : items.keySet()) {
            cumulativeWeight += items.get(key).doubleValue();
            if (randomNumber < cumulativeWeight) {
                return key;
            }
        }
        Engine.log("FAILURE TO SELECT", String.format("The weighted selection failed to select an item."), new Exception());
        return null; // Edge-case failure to select
    }

    static String[] suffixes = {"th", "st", "nd", "rd", "th"};
    public static String toOrdinal(int value) {
        if (value < 0) {
            Engine.log("ILLEGAL ARGUMENT EXCEPTION:", String.format("A value of %d was supplied, when the minimum value is 0.", value), new Exception());
            return null;
        }
        return value + suffixes[value <= 3 ? value : 4];
    }

    /**
     * Takes any object and returns a blank repr string representation of an instance of that object's class. For a filled version, use the object's toRepr() method.
     * @param <O> Generic object
     * @param object
     * @return Generic (unfilled) repr representation of the object. Does not complete field spaces with actual values, but does determine single value and list fields.
     */
    public static <O> String toRepr(O object) {
        String[] singleTypes = {"int", "java.lang.String", "char", "long", "float", "double"}; // All the types that are represented by a single value, all others are a list.

        String repr = "";

        repr += String.format("%s:[", object.getClass().toString().replace("class ", ""));

        for (java.lang.reflect.Field f : object.getClass().getDeclaredFields()) {
            boolean added = false;
            String field = f.toString();

            //System.out.println(field);
            // loop through all non-static fields
            if (!field.contains("static")) {
                // the name of the field is the last portion only
                System.out.println(field);
                String[] parts = field.split(" ");
                String type = parts[1];
                field = parts[parts.length-1];

                for (String singleType : singleTypes) {
                    if (type.equals(singleType)) {
                        repr += String.format("%s:\"", field.split("\\.")[1]);
                        repr += String.format("\";");
                        added = true;
                        break;
                    }
                }
                if (!added) repr += String.format("%s:[];", field.split("\\.")[1]);
            }
        }

        repr += String.format("];");
        return repr;
        
        /*
        * first line should be the type of the passed object 
        * for each field in the passed object:
        *      append the name of the field, followed by :
        *      if the field is a single item, put "";
        *      if the field is a list, put [];
        *          for each item in the list, add an index or key followed by :
        *              if the value is a single item, put "";
        *              if the value is a list, put [];
        *                  etc ...
        * end with ;
        */
    }

    public static String arrayToReprList(String[] array) {
        if (array == null) return "";
        StringBuilder repr = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            repr.append(String.format("%d:\"%s\";", i, array[i]));
        }
        return repr.toString();
    }

    public static <T extends Collection<String>> String arrayToReprList(T list) {
        if (list == null) return "";
        StringBuilder repr = new StringBuilder();
        int i = 0;
        for (String item : list) {
            repr.append(String.format("%d:\"%s\";", i++, item));
        }
        return repr.toString();
    }

    public static void openLocalizationFiles(){
        localizations = new HashMap<String, HashMap<String, String>>();
        // for each language in Language enum, open the language system text file and read key-value pairs
        for(Language language : Language.values()){
            ArrayList<String> contents = new ArrayList<>();
            try {
                File file = new File(String.format("%s/%s%s", FilePaths.localizationFolder_loc, language, FilePaths.systemText_loc));
                Scanner scanner = scannerUtil.createScanner(file);
                while (scanner.hasNext()) contents.add(scanner.nextLine());
                scanner.close();
            }
            catch (FileNotFoundException e) {
                log("INVALID LANGUAGE ABBREVIATION", String.format("Attempted to access language abbreviation %s which has no associated localization file.", language.toString()), new Exception());
                continue;
            }
            localizations.put(language.toString(), new HashMap<String, String>());
            for(String line : contents){
                if(line.equals("")) continue;
                localizations.get(language.toString()).put(line.split(":")[0].trim(), line.split(":")[1].trim());
            }
        }
    }

    public static String getLocalization(String tag) {
        if (language == null) {
            Engine.log("UNINITIALIZED GAME LANGUAGE", String.format("The game language was never initialized or was set to null."), new Exception());
            return null;
        }
        return getLocalization(tag, language);
    }
    public static String getLocalization(String tag, Language language){
        String res = localizations.get(language.toString()).get(tag);
        if (res == null) {
            Engine.log("INVALID LOCALIZATION TAG", String.format("Attempted to access localization tag %s for language %s, which is invalid.", tag, language.toString()), new Exception());
            return String.format("[%s : %s]", language.toString(), tag);
        }
        return res;
    }

    public static boolean isPrime(int value) {
        if (value < 2) return false;
        for (int i = 2; i <= Math.sqrt(value); i++) {
            if (value % i == 0) return false;
        }
        return true;
    }

    public static int nextPrime(int value) {
        if (value < 2) return 2;
        while (!isPrime(value)) value++;
        return value;
    }
}