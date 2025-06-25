/*
 * Main.java
 * Steven LaGoy
 */

package main.core;

public class Main {

    /** This class is non-instantiable. The {@code main()} entry point should be accessed in a static way. */
    private Main() {} // Non-Instantiable

    public static void main(String[] args) {
        int errorCode = 0;
        try {
            if (!Engine.start()) return;
        }
        catch (Exception e) {
            errorCode = e.hashCode();
            e.printStackTrace();
        }
        finally {
            Engine.log("Main Done");
            System.exit(errorCode);
        }
    }
}