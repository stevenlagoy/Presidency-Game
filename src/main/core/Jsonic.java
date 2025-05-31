package main.core;

/*
 * Jsonic.java
 * Steven LaGoy
 * Created: 29 May 2025 at 12:15 AM
 * Modified: 29 May 2025 at 5:42 PM
 */

import core.JSONObject;

/**
 * Interface allowing Json methods to be used to create and store JSONObjects
 * representing instances of classes which implement this interface.
 * <p>
 * <b> Example Usage: </b>
 * <p>
 * <code> class MyClass implements Jsonic<MyClass> { ... } </code>
 * @param <T> The type of the object implementing this interface (should be the same as the Class Name).
 */
public interface Jsonic<T extends Jsonic<T>> {

    /**
     * Turn this object's fields into an accurately-modeled JSONObject.
     * @return A JSONObject representing this object.
     */
    public JSONObject toJson();

    /**
     * Interpret a JSONObject into an object of this type.
     * @param json A JSONObject containing any number of the fields for this object as key-value pairs.
     * @return The Object interpreted from the Json. Note that this object's fields will also be set.
     */
    public T fromJson(JSONObject json);
}
