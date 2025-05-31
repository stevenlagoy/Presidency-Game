package main.core;

import java.util.Collection;

/*
 * Repr.java
 * Steven LaGoy
 * Created: 06 January 2025 at 12:09 AM
 * Modified: 29 May 2025 at 5:40 PM
 */

/**
 * Interface allowing Repr methods to be used to create or store Repr Strings
 * representing instances of classes which implement this interface.
 * <p>
 * <b> Example Usage: </b>
 * <p>
 * <code> class MyClass implements Repr&lt;MyClass&gt; { ... } </code>
 * @param <T> The type of the object implementing this interface (should be the same as the Class Name).
 */
public interface Repr<T extends Repr<T>> {

    // INTERFACE OVERRIDE METHODS -----------------------------------------------------------------

    /**
     * Create a Repr String representation of this object.
     * @return A String in valid Repr format representing the object.
     */
    public String toRepr();

    /**
     * Interpret an Object from a valid Repr String.
     * @param repr A Repr-formatted String to parse.
     * @return The created object if interpreted properly from the Repr String, {@code null} otherwise.
     */
    public T fromRepr(String repr);

    // STATIC CONCRETE METHODS --------------------------------------------------------------------

    /**
     * Takes any object and returns a skeleton/blank Repr-format String representation of an instance of that object's class. For a filled version, use the object's toRepr() method.
     * @param <O> Object
     * @param clazz Any class
     * @return Skeleton (unfilled) Repr-format String representation of an object of the class.
     */
    public static <O> String classRepr(Class<O> clazz) {
        String[] singleTypes = {"int", "java.lang.String", "char", "long", "float", "double"}; // All the types that are represented by a single value, all others are a list.

        String repr = "";

        repr += String.format("%s:[", clazz.toString().replace("class ", ""));

        for (java.lang.reflect.Field f : clazz.getDeclaredFields()) {
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
    }

    /**
     * Turns an array of Objects into a Repr-format list containing those Objects. Uses the Objects' toString() method.
     * @param <T> Object
     * @param array An array of Objects.
     * @return A Repr-format String containing a List of all the Objects' String (or Repr, when available) forms.
     */
    public static <T> String arrayToReprList(T[] array) {
        if (array == null) return "";
        StringBuilder repr = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            repr.append(String.format("%d:\"%s\";", i, array[i].toString()));
        }
        return repr.toString();
    }

    /**
     * Turns a Collection of Objects into a Repr-format list containing those Objects. Uses the Objects' toString() method.
     * @param <T> Object
     * @param list A Collection of Objects.
     * @return A Repr-format String containing a List of all the Objects' String (or Repr, when available) forms.
     */
    public static <T> String arrayToReprList(Collection<T> list) {
        if (list == null) return "";
        StringBuilder repr = new StringBuilder();
        int i = 0;
        for (Object item : list) {
            repr.append(String.format("%d:\"%s\";", i++, item.toString()));
        }
        return repr.toString();
    }
}
