package main.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class NumberOperations {
    
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
            Logger.log("INVALID SELECTION FROM EMPTY ARRAY", String.format("Unable to select an item from an array with length < 1."), new Exception());
            return null;
        }
        if (items.length != weights.length) {
            Logger.log("WEIGHTED SELECTION FROM MISMATCHED ARRAYS", String.format("Provided arrays for a weighted selection have mismatched lengths."), new Exception());
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
        Logger.log("FAILURE TO SELECT", String.format("The weighted selection failed to select an item."), new Exception());
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
            Logger.log("INVALID SELECTION FROM EMPTY ARRAY", String.format("Unable to select an item from an array with length < 1."), new Exception());
            return null;
        }
        if (items.size() != weights.size()) {
            Logger.log("WEIGHTED SELECTION FROM MISMATCHED ARRAYS", String.format("Provided arrays for a weighted selection have mismatched lengths."), new Exception());
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
        Logger.log("FAILURE TO SELECT", String.format("The weighted selection failed to select an item."), new Exception());
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
            Logger.log("INVALID SELECTION FROM EMPTY ARRAY", String.format("Unable to select an item from an empty or null array."), new Exception());
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
        Logger.log("FAILURE TO SELECT", String.format("The weighted selection failed to select an item."), new Exception());
        return null; // Edge-case failure to select
    }

    public static int probabilisticCount(float f) {
        int count = 0;
        while (randPercent() <= f) count++;
        return count;
    }

}
