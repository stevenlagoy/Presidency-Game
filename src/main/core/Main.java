/*
 * Main.java
 * Steven LaGoy
 */

package main.core;

public class Main {

    /** This class is non-instantiable. The {@code main()} entry point should be accessed in a static way. */
    private Main() {} // Non-Instantiable

    private static Engine engine;

    public static Engine Engine() { return engine; }

    public static void main(String[] args) {
        int errorCode = 0;
        engine = new Engine();
        if (!engine.init()) return;

        IOUtil.stdout.println(engine.toJson().toString());

        engine.cleanup();
        Logger.log("Main Done");
        System.exit(errorCode);
    }
}