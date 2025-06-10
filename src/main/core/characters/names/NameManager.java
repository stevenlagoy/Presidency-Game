/*
 * NameManager.java
 * Steven LaGoy
 * Created: 1 June 2025 at 1:04 AM
 * Modified: 3 June 2025
 */

package main.core.characters.names;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lwjgl.system.linux.liburing.IOURing;

import core.JSONObject;
import core.JSONProcessor;
import main.core.Engine;
import main.core.FilePaths;
import main.core.IOUtil;
import main.core.characters.names.Name.NameForm;
import main.core.characters.names.Name.DisplayOption;
import main.core.demographics.Bloc;
import main.core.demographics.Demographics;
import main.core.demographics.DemographicsManager;

/**
 * NameManager manages the generation of Name objects.
 * <p>
 * This class is final and has no instance variables, and is not designed to be instantiated. All members and functions should be accessed in a static way.
 */
public final class NameManager {
    private NameManager() {} // Non-Instantiable

    /** Percentage of Asian people who should have an Eastern nameform. */
    private static final float asianEasternNamePercent = 0.50f;
    /** Percentage of Hispanic/Latino people who should have a Hispanic name. */
    private static final float hispanicHispanicNamePercent = 0.80f;
    /** Percentage of Native American people who should have a Native American name. */
    private static final float nativeNativeNamePercent = 0.25f;
    /** Percentage of people with a middle name. */
    private static final float hasMiddleNamePercent = 0.80f;
    /** Percentage of people with a generational name given they have an Eastern-style name. */
    private static final float hasGenerationNamePercent = 0.60f;
    /** Percentage of people who have a Generation name but choose not to use it. */
    private static final float latentGenerationNamePercent = 0.30f;
    /** Percentage of people with mupltiple first names. */
    private static final float multipleFirstNamesPercent = 0.10f;
    /** Percentage of people with two forenames (nombres) given they have a Hispanic-style name. */
    private static final float hispanicMultipleForenamesPercent = 0.35f;
    /** Liklihood that a person with a Hispanic-style name has multuple surnames in either the maternal or paternal apellido. */
    private static final float hispanicCompositeSurnamePercent = 0.30f;
    /** Percentage of people with more middle names. 11% of people have 2, 0.121% have 3 etc. */
    private static final float multipleMiddleNamesPercent = 0.11f;
    /** Percentage of people who commonly use a nickname. */
    private static final float hasNicknamePercent = 0.23f;
    /** Percentage of people with a nickname which does not resemble any of their given names. */
    private static final float nicknameNotFromNamesPercent = 0.05f;
    /** Percentage of people with a double-barreled or multiple last/family names. */
    private static final float doubleBarrelledNamePercent = 0.11f;
    /** Percentage of people whose first family name is their maternal family name. */
    private static final float maternalNameFirstPercent = 0.05f;
    /** Percentage of people with a non-Western-style name who also use a Western name. */
    private static final float hasWesternNamePercent = 0.20f;
    /** Percentage of people who hyphenate their last names. */
    private static final float hyphenatedNamePercent = 0.06f;
    /** Percentage of people who abbreviate only their First Name(s) in their Common Name. */
    private static final float abbreviateFirstNamesPercent = 0.04f;
    /** Percentage of people who abbreviate only their Middle Name(s) in their Common Name. */
    private static final float abbreviateMiddleNamesPercent = 0.38f;
    /** Percentage of people who abbreviate their First and Middle Names in their Common Name. */
    private static final float abbreviateBothNamesPercent = 0.08f;
    /** Percentage of people who include their Middle Name in their Common Name. */
    private static final float useMiddleNicknamePercent = 0.04f;
    /** Percentage of people with the I or Senior ordination. */
    private static final float srOrdinationPercent = 0.04f;
    /** Percentage of people with the II or Junior ordination. */
    private static final float jrOrdinationPercent = 0.08f;
    /** Percentage of people with the II or Second ordination (applied repeatedly to choose higher ordinations). */
    private static final float iiOrdinationPercent = 0.06f;

    /** Map of first / given names and frequency weights, keyed with the blocs those names are associated with. Names can be accessed through a set containing the applicable blocs. */
    private static Map<Set<Bloc>, Map<String, Double>> firstNamesDistribution;
    /** Map of middle names and frequency weights, keyed with the blocs those names are associated with. Names can be accessed through a set containing the applicable blocs. */
    private static Map<Set<Bloc>, Map<String, Double>> middleNamesDistribution;
    /** Map of last` / family names and frequency weights, keyed with the blocs those names are associated with. Names can be accessed through a set containing the applicable blocs. */
    private static Map<Set<Bloc>, HashMap<String, Double>> lastNamesDistribution;
    /** Map of names and Lists of nicknames associated with their key name. */
    private static Map<String, List<String>> nicknames;

    /**
     * Refines a Set of Blocs to be used as a key in name distributions. Determines whether the passed blocs are expected
     * to have an entry in the distribution maps and permutes them through their superblocs until a valid set of Blocs is found.
     * @param blocs Set of Blocs to refine.
     * @return Set of Blocs relatively close to the original key but which is expected to have a value in the distribution map.
     */
    private static Set<Bloc> refineBlocsKey(Set<Bloc> blocs) {

        // Base case: check if the set is empty or null
        if (blocs == null || blocs.isEmpty())
            return new HashSet<>();

        // Check if this set exists in any distribution map
        if (hasMatchingKey(blocs)) return blocs;

        Set<Bloc> bestMatch = null;
        int bestScore = -1; // higher score is closer to original

        Set<Set<Bloc>> candidates = generateKeyCandidates(blocs);

        for (Set<Bloc> candidate : candidates) {
            if (hasMatchingKey(candidate)) {
                int score = calculateScore(blocs, candidate);
                if (score > bestScore) {
                    bestScore = score;
                    bestMatch = candidate;
                }
            }
        }

        return bestMatch;
    }

    private static Set<Bloc> refineBlocsKey(Set<Bloc> blocs, String... avoidCategories) {
        Set<Bloc> candidate = new HashSet<>();
        for (Bloc bloc : blocs) {
            boolean avoid = false;
            for (String category : avoidCategories) {
                if (bloc.getNestedNames().contains(category) || bloc.getDemographicGroup().equals(category))
                    avoid = true;
            }
            if (!avoid) candidate.add(bloc);
        }
        return refineBlocsKey(candidate);
    }

    private static boolean hasMatchingKey(Set<Bloc> blocs) {
        return firstNamesDistribution.containsKey(blocs) ||
               middleNamesDistribution.containsKey(blocs) ||
               lastNamesDistribution.containsKey(blocs);
    }

    private static Set<Set<Bloc>> generateKeyCandidates(Set<Bloc> originalBlocs) {
        Set<Set<Bloc>> candidates = new HashSet<>();
        List<Bloc> blocsList = new ArrayList<>(originalBlocs);
        
        // For each bloc, get all possible alternatives (original + all superblocs + removal)
        List<List<Bloc>> allAlternatives = new ArrayList<>();
        
        for (Bloc bloc : blocsList) {
            List<Bloc> alternatives = new ArrayList<>();
            alternatives.add(null); // Option to remove this bloc
            alternatives.add(bloc); // Option to keep original bloc
            
            // Add all nested superblocs
            List<Bloc> superBlocs = bloc.getNestedSuperBlocs();
            if (superBlocs != null) {
                alternatives.addAll(superBlocs);
            }
            
            allAlternatives.add(alternatives);
        }
        
        // Generate all combinations using recursive approach
        generateCombinations(allAlternatives, 0, new ArrayList<>(), candidates, originalBlocs);
        
        return candidates;
    }

    private static void generateCombinations(List<List<Bloc>> allAlternatives, int index, List<Bloc> currentCombination, Set<Set<Bloc>> candidates, Set<Bloc> originalBlocs) {
        if (index == allAlternatives.size()) {
            // Create candidate set from current combination (excluding nulls)
            Set<Bloc> candidate = new HashSet<>();
            for (Bloc bloc : currentCombination) {
                if (bloc != null) {
                    candidate.add(bloc);
                }
            }
            
            // Only add non-empty candidates that are different from original
            if (!candidate.isEmpty() && !candidate.equals(originalBlocs)) {
                candidates.add(candidate);
            }
            return;
        }
        
        // Try each alternative for the current bloc
        List<Bloc> alternatives = allAlternatives.get(index);
        for (Bloc alternative : alternatives) {
            currentCombination.add(alternative);
            generateCombinations(allAlternatives, index + 1, currentCombination, candidates, originalBlocs);
            currentCombination.remove(currentCombination.size() - 1); // backtrack
        }
    }

    private static int calculateScore(Set<Bloc> original, Set<Bloc> candidate) {
        int score = 0;

        // Count preserved original blocs
        for (Bloc bloc : original) {
            if (candidate.contains(bloc)) {
                score += 100;
            }
        }

        // Count superblocs
        for (Bloc originalBloc : original) {
            if (!candidate.contains(originalBloc)) {
                List<Bloc> superBlocs = originalBloc.getNestedSuperBlocs();
                if (superBlocs != null) {
                    for (int i = 0; i < superBlocs.size(); i++) {
                        Bloc superBloc = superBlocs.get(i);
                        if (candidate.contains(superBloc)) {
                            // Closer superblocs get higher scores
                            // First superbloc gets 500, second gets 400, etc.
                            score += (500 - (i * 100));
                            break; // Only count the closest matching superbloc
                        }
                    }
                }
            }
        }

        // Prefer larger sets
        score += candidate.size();

        return score;
    }

    /**
     * Get the distribution of first / given names.
     * @return Map of first / given names and frequency weights, keyed with the blocs those names are associated with. Names can be accessed through a set containing the applicable blocs.
     */
    public static Map<Set<Bloc>, Map<String, Double>> getFirstNamesDistribution() {
        if (firstNamesDistribution == null || firstNamesDistribution.size() == 0) readFirstNamesFile();
        return firstNamesDistribution;
    }

    /** Get the distribution of first / given names for a particular demographic.
     * @param demographics Demographics with which to filter the distributions. Only names matching <i>all</i> of the Blocs will be returned.
     * @return Map of String to Double giving names and the relative frequencies of those names.
     * @see #getFirstNamesDistribution(Bloc[])
     * @see #getFirstNamesDistribution(Collection)
    */
    public static Map<String, Double> getFirstNamesDistribution(Demographics demographics) {
        return getFirstNamesDistribution(demographics.toBlocsArray());
    }
    
    /**
     * Get the distribution of first / given names for a particular set of Blocs.
     * @param blocs Blocs with which to filter the distributions. Only names matching <i>all</i> of the Blocs will be returned.
     * @return Map of String to Double giving names and the relative frequencies of those names.
     * @see #getFirstNamesDistribution(Demographics)
     * @see #getFirstNamesDistribution(Collection)
     */
    public static Map<String, Double> getFirstNamesDistribution(Bloc... blocs) {
        return getFirstNamesDistribution(Set.of(blocs));
    }

    /**
     * Get the distribution of first / given names for a particular set of Blocs.
     * @param blocs Blocs with which to filter the distributions. Only names matching <i>all</i> of the Blocs will be returned.
     * @return Map of String to Double giving names and the relative frequencies of those names.
     * @see #getFirstNamesDistribution(Demographics)
     * @see #getFirstNamesDistribution(Bloc[])
     */
    public static Map<String, Double> getFirstNamesDistribution(Collection<Bloc> blocs) {
        if (firstNamesDistribution == null && !readFirstNamesFile()) {
            Engine.log("FIRST NAMES UNREADABLE", "The first names file was unable to be read properly.", new Exception());
            return null;
        }
        Set<Bloc> key = new HashSet<>(blocs);
        return firstNamesDistribution.get(key);
    }

    /**
     * Read in the first names file.
     * @return {@code true} if successful, {@code false} otherwise.
    */
    private static boolean readFirstNamesFile() {
        JSONObject json = JSONProcessor.processJson(FilePaths.FIRSTNAME_DISTR);
        firstNamesDistribution = new HashMap<>();

        processFirstNameStructure(json, new HashSet<>());
        return true;
    }

    private static Map<String, Double> processFirstNameStructure(JSONObject json, Set<Bloc> currentBlocs) {
        Map<String, Double> distributions = new HashMap<String,Double>();

        for (Object obj : json.getAsList()) {
            if (!(obj instanceof JSONObject entry)) continue;
            
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Number) {
                // This is a name-number pair
                // Add or update the name distribution for all current blocs
                if (!currentBlocs.isEmpty()) {
                    distributions.put(key, ((Number) value).doubleValue());
                    Map<String, Double> existingDistributions = firstNamesDistribution
                        .computeIfAbsent(currentBlocs, k -> new HashMap<>());
                    existingDistributions.put(key, ((Number) value).doubleValue());
                }
            }
            else if (value instanceof ArrayList<?>) {
                // This is a nested structure

                // If key is a valid bloc name, add it to the current set of blocs
                Bloc bloc = DemographicsManager.matchBlocName(key);
                Set<Bloc> updatedBlocs = new HashSet<>(currentBlocs);
                if (bloc != null) {
                    updatedBlocs.add(bloc);
                }

                // Recurse with updated bloc set
                Map<String, Double> nestedDistributions = processFirstNameStructure(new JSONObject(key, (ArrayList<?>) value), updatedBlocs);
                distributions.putAll(nestedDistributions);
            }
        }
        return distributions;
    }

    /**
     * Get the distribution of middle names.
     * @return Map of middle names and frequency weights, keyed with the blocs those names are associated with. Names can be accessed through a set containing the applicable blocs.
     */
    public static Map<Set<Bloc>, Map<String, Double>> getMiddleNamesDistribution() {
        if (middleNamesDistribution == null || middleNamesDistribution.size() == 0) readMiddleNamesFile();
        return middleNamesDistribution;
    }

    /** Get the distribution of middle names for a particular demographic.
     * @param demographics Demographics with which to filter the distributions. Only names matching <i>all</i> of the Blocs will be returned.
     * @return Map of String to Double giving names and the relative frequencies of those names.
     * @see #getMiddleNamesDistribution(Bloc[])
     * @see #getMiddleNamesDistribution(Collection)
    */
    public static Map<String, Double> getMiddleNamesDistribution(Demographics demographics) {
        return getMiddleNamesDistribution(demographics.toBlocsSet());
    }
    
    /**
     * Get the distribution of middle names for a particular set of Blocs.
     * @param blocs Blocs with which to filter the distributions. Only names matching <i>all</i> of the Blocs will be returned.
     * @return Map of String to Double giving names and the relative frequencies of those names.
     * @see #getMiddleNamesDistribution(Demographics)
     * @see #getMiddleNamesDistribution(Collection)
     */
    public static Map<String, Double> getMiddleNamesDistribution(Bloc... blocs) {
        return getMiddleNamesDistribution(Set.of(blocs));
    }

    /**
     * Get the distribution of middle names for a particular set of Blocs.
     * @param blocs Blocs with which to filter the distributions. Only names matching <i>all</i> of the Blocs will be returned.
     * @return Map of String to Double giving names and the relative frequencies of those names.
     * @see #getMiddleNamesDistribution(Demographics)
     * @see #getMiddleNamesDistribution(Bloc[])
     */
    public static Map<String, Double> getMiddleNamesDistribution(Collection<Bloc> blocs) {
        if (middleNamesDistribution == null && !readMiddleNamesFile()) {
            Engine.log("MIDDLE NAMES UNREADABLE", "The middle names file was unable to be read properly.", new Exception());
            return null;
        }
        Set<Bloc> key = new HashSet<>(blocs);
        return middleNamesDistribution.get(key);
    }

    /**
     * Read in the middle names file.
     * @return {@code true} if successful, {@code false} otherwise.
    */
    private static boolean readMiddleNamesFile() {
        JSONObject json = JSONProcessor.processJson(FilePaths.FIRSTNAME_DISTR);
        middleNamesDistribution = new HashMap<>();

        processMiddleNameStructure(json, new HashSet<>());
        return true;
    }

    private static Map<String, Double> processMiddleNameStructure(JSONObject json, Set<Bloc> currentBlocs) {
        Map<String, Double> distributions = new HashMap<String,Double>();

        for (Object obj : json.getAsList()) {
            if (!(obj instanceof JSONObject entry)) continue;
            
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Number) {
                // This is a name-number pair
                // Add or update the name distribution for all current blocs
                if (!currentBlocs.isEmpty()) {
                    distributions.put(key, ((Number) value).doubleValue());
                    Map<String, Double> existingDistributions = middleNamesDistribution
                        .computeIfAbsent(currentBlocs, k -> new HashMap<>());
                    existingDistributions.put(key, ((Number) value).doubleValue());
                }
            }
            else if (value instanceof ArrayList<?>) {
                // This is a nested structure

                // If key is a valid bloc name, add it to the current set of blocs
                Bloc bloc = DemographicsManager.matchBlocName(key);
                Set<Bloc> updatedBlocs = new HashSet<>(currentBlocs);
                if (bloc != null) {
                    updatedBlocs.add(bloc);
                }

                // Recurse with updated bloc set
                Map<String, Double> nestedDistributions = processFirstNameStructure(new JSONObject(key, (ArrayList<?>) value), updatedBlocs);
                distributions.putAll(nestedDistributions);
            }
        }
        return distributions;
    }

    /**
     * Get the distribution of last / family names.
     * @return Map of last / family names and frequency weights, keyed with the blocs those names are associated with. Names can be accessed through a set containing the applicable blocs.
     */
    public static Map<Set<Bloc>, HashMap<String, Double>> getLastNameDistribution() {
        if (lastNamesDistribution == null || lastNamesDistribution.size() == 0) readLastNamesFile();
        return lastNamesDistribution;
    }

    /** Get the distribution of last / family names for a particular demographic.
     * @param demographics Demographics with which to filter the distributions. Only names matching <i>all</i> of the Blocs will be returned.
     * @return Map of String to Double giving names and the relative frequencies of those names.
     * @see #getLastNamesDistribution(Bloc[])
     * @see #getLastNamesDistribution(Collection)
    */
    public static Map<String, Double> getLastNameDistribution(Demographics demographics) {
        return getLastNameDistribution(demographics.toBlocsSet());
    }

    /**
     * Get the distribution of last / family names for a particular set of Blocs.
     * @param blocs Blocs with which to filter the distributions. Only names matching <i>all</i> of the Blocs will be returned.
     * @return Map of String to Double giving names and the relative frequencies of those names.
     * @see #getLastNamesDistribution(Demographics)
     * @see #getLastNamesDistribution(Collection)
     */
    public static Map<String, Double> getLastNameDistribution(Bloc... blocs) {
        return getLastNameDistribution(Set.of(blocs));
    }

    /**
     * Get the distribution of last / family names for a particular set of Blocs.
     * @param blocs Blocs with which to filter the distributions. Only names matching <i>all</i> of the Blocs will be returned.
     * @return Map of String to Double giving names and the relative frequencies of those names.
     * @see #getLastNamesDistribution(Demographics)
     * @see #getLastNamesDistribution(Bloc[])
     */
    public static Map<String, Double> getLastNameDistribution(Collection<Bloc> blocs) {
        if (lastNamesDistribution == null && !readLastNamesFile()) {
            Engine.log("LAST NAMES UNREADABLE", "The last names file was unable to be read properly.", new Exception());
            return null;
        }
        Set<Bloc> key = new HashSet<>(blocs);
        return lastNamesDistribution.get(key);
    }

    /**
     * Read in the last names file.
     * @return {@code true} if successful, {@code false} otherwise.
    */
    private static boolean readLastNamesFile() {
        JSONObject json = JSONProcessor.processJson(FilePaths.LASTNAME_DISTR);
        lastNamesDistribution = new HashMap<>();

        processLastNameStructure(json, new HashSet<>());
        return true;
    }

    private static Map<String, Double> processLastNameStructure(JSONObject json, Set<Bloc> currentBlocs) {
        Map<String, Double> distributions = new HashMap<>();

        for (Object obj : json.getAsList()) {
            if (!(obj instanceof JSONObject entry)) continue;
            
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Number) {
                // This is a name-number pair
                // Add or update the name distribution for all current blocs
                if (!currentBlocs.isEmpty()) {
                    distributions.put(key, ((Number) value).doubleValue());
                    HashMap<String, Double> existingDistributions = lastNamesDistribution
                        .computeIfAbsent(currentBlocs, k -> new HashMap<>());
                    existingDistributions.put(key, ((Number) value).doubleValue());
                }
            }
            else if (value instanceof ArrayList<?>) {
                // This is a nested structure
                // If key is a valid bloc name, add it to the current set of blocs
                Bloc bloc = DemographicsManager.matchBlocName(key);
                Set<Bloc> updatedBlocs = new HashSet<>(currentBlocs);
                if (bloc != null) {
                    updatedBlocs.add(bloc);
                }

                // Recurse with updated bloc set
                Map<String, Double> nestedDistributions = processLastNameStructure(
                    new JSONObject(key, (ArrayList<?>) value), 
                    updatedBlocs
                );
                distributions.putAll(nestedDistributions);
            }
        }
        return distributions;
    }

    /**
     * Read in the nicknames file.
     * @return {@code true} if successful, {@code false} otherwise.
    */
    private static boolean readNicknamesFile() {
        JSONObject json = JSONProcessor.processJson(FilePaths.NICKNAMES);
        nicknames = new HashMap<String, List<String>>();

        for (Object nicknameObj : json.getAsList()) {
            if (nicknameObj instanceof JSONObject nicknameJson) {
                String name = nicknameJson.getKey();
                List<?> rawNicks = nicknameJson.getAsList();
                List<String> nicks = new ArrayList<>();
                for (Object obj : rawNicks) {
                    if (obj != null) nicks.add(obj.toString());
                }
                NameManager.nicknames.put(name, nicks);
            }
        }
        return true;
    }

    /**
     * Read in all names files (first, middle, last, nicknames).
     * @return {@code true} if successful, {@code false} otherwise.
     */
    public static boolean readAllNamesFiles() {
        boolean successFlag = true;
        successFlag = successFlag && readFirstNamesFile();
        successFlag = successFlag && readMiddleNamesFile();
        successFlag = successFlag && readLastNamesFile();
        successFlag = successFlag && readNicknamesFile();
        return successFlag;
    }

    public static NameForm selectNameForm(Demographics demographics) {
        if (demographics.getRaceEthnicity().getNestedNames().contains("Asian")) {
            if (Engine.randPercent() <= asianEasternNamePercent) {
                return NameForm.EASTERN;
            }
        }
        else if (demographics.getRaceEthnicity().getNestedNames().contains("Hispanic / Latino")) {
            if (demographics.getRaceEthnicity().getNestedNames().contains("Argentinian")) {
                return NameForm.WESTERN; // Only the paternal apellido is inherited in the Argentinian custom
            }
            if (Engine.randPercent() <= hispanicHispanicNamePercent) {
                return NameForm.HISPANIC;
            }
        }
        else if (demographics.getRaceEthnicity().getNestedNames().contains("Native / Indian")) {
            if (Engine.randPercent() <= nativeNativeNamePercent) {
                return NameForm.NATIVE_AMERICAN;
            }
        }
        return NameForm.WESTERN;
    }

    public static String selectGivenName(Demographics demographics) {
        return selectGivenName(demographics.toBlocsSet());
    }

    public static String selectGivenName(Bloc... blocs) {
        return selectGivenName(Set.of(blocs));
    }

    public static String selectGivenName(Collection<Bloc> blocs) {
        Set<Bloc> key = Set.of(blocs.toArray(new Bloc[0]));
        key = refineBlocsKey(key);
        return Engine.weightedRandSelect(getFirstNamesDistribution(key));
    }

    public static String selectFamilyName(Demographics demographics) {
        return selectFamilyName(demographics.toBlocsSet());
    }

    public static String selectFamilyName(Bloc... blocs) {
        return selectGivenName(Set.of(blocs));
    }

    public static String selectFamilyName(Collection<Bloc> blocs) {
        Set<Bloc> key = Set.of(blocs.toArray(new Bloc[0]));
        key = refineBlocsKey(key, "Presentation");
        return Engine.weightedRandSelect(getLastNameDistribution(key));
    }

    @SuppressWarnings("unused")
    public static String selectNickName(String... names) {
        // Nicknames may be based on one of a person's actual names
        // (with their preferred first name being most commonly nicked)
        // or may be completely separate from their actual names.
        List<String> allNicknames;
        if (Engine.randPercent() <= nicknameNotFromNamesPercent && false) {
            // Disabled this because names from other blocs were being chosen too frequently: unrealistic numbers of women with masculine nicknames
            // Nickname not based on given names
            allNicknames = new ArrayList<>();
            for (List<String> nicksList : nicknames.values()) {
                    allNicknames.addAll(nicksList);
            }
            return Engine.randSelect(allNicknames);
        }
        else {
            // Nickname is based on given names
            String n = Engine.randSelect(names); // Select a name to nick
            allNicknames = nicknames.get(n);
            if (allNicknames != null)
                return Engine.randSelect(allNicknames);
        }
        return "";
    }

    private static int[] selectPartsCounts(NameForm form) {
        int[] counts = {0, 0, 0};
        switch (form) {
            case WESTERN :
                counts[0] = Engine.probabilisticCount(multipleFirstNamesPercent) + 1;
                counts[1] = Engine.randPercent() <= hasMiddleNamePercent ? 1 : 0;
                if (counts[1] == 1) counts[1] += Engine.probabilisticCount(multipleMiddleNamesPercent);
                counts[2] = Engine.probabilisticCount(doubleBarrelledNamePercent) + 1;
                break;
            case EASTERN :
                counts[0] = 1;
                counts[1] = Engine.randPercent() <= hasGenerationNamePercent ? 1 : 0;
                counts[2] = 1;
                break;
            case HISPANIC :
                counts[0] = Engine.probabilisticCount(hispanicMultipleForenamesPercent) + 1;
                counts[2] = Engine.probabilisticCount(hispanicCompositeSurnamePercent) + 2;
                break;
            case NATIVE_AMERICAN :
                counts[0] = 1;
                counts[1] = 1;
                counts[2] = 1;
                break;
        }
        return counts;
    }

    public static void assignGivenName(Name name, String[] givenNames) {
        switch (name.getNameForm()) {
            case EASTERN:
            case HISPANIC:
            case NATIVE_AMERICAN:
            case WESTERN:
                name.setGivenName(String.join(" ", givenNames));
                break;
        }
    }
    public static void assignMiddleName(Name name, String[] middleNames) {
        switch (name.getNameForm()) {
            case EASTERN:
            case HISPANIC:
            case NATIVE_AMERICAN:
            case WESTERN:
                name.setMiddleName(String.join(" ", middleNames));
                break;
        }
    }
    public static void assignFamilyName(Name name, String[] familyNames, Demographics demographics) {
        switch (name.getNameForm()) {
            case EASTERN:
                name.setFamilyName(familyNames[0]);
                break;
            case HISPANIC:
                String[] conjoiners = {" y ", " de ", "-"};
                int divide = Engine.randInt(1, familyNames.length - 1);
                String[] paternalNames = Arrays.copyOfRange(familyNames, 0, divide);
                String[] maternalNames = Arrays.copyOfRange(familyNames, divide, familyNames.length);
                String paternalName = "";
                for (String n : paternalNames) {
                    if (paternalName.isEmpty()) {
                        paternalName = n;
                        continue;
                    }
                    paternalName = paternalName + Engine.randSelect(conjoiners) + n;
                }
                String maternalName = "";
                for (String n : maternalNames) {
                    if (maternalName.isEmpty()) {
                        maternalName = n;
                        continue;
                    }
                    maternalName = maternalName + Engine.randSelect(conjoiners) + n;
                }
                if (demographics.getRaceEthnicity().getNestedNames().contains("Brazilian")) {
                    // Brazilian names list the Maternal surname first
                    name.setMaternalName(maternalName.replace(" y ", " e "));
                    name.setPaternalName(paternalName.replace(" y ", " e "));
                    name.addDisplayOption(DisplayOption.MATERNAL_FIRST);
                }
                name.setPaternalName(paternalName);
                name.setMaternalName(maternalName);
                if (Engine.randPercent() <= maternalNameFirstPercent)
                    name.addDisplayOption(DisplayOption.MATERNAL_FIRST);
                else
                    name.addDisplayOption(DisplayOption.PATERNAL_FIRST);
                break;
            case NATIVE_AMERICAN:
            case WESTERN:
                if (Engine.randPercent() <= hyphenatedNamePercent)
                    name.setFamilyName(String.join("-", familyNames));
                else
                    name.setFamilyName(String.join(" ", familyNames));
        }
    }

    public static Name generateName(Demographics demographics) {

        if (firstNamesDistribution == null || firstNamesDistribution.size() == 0) readFirstNamesFile();
        if (middleNamesDistribution == null || middleNamesDistribution.size() == 0) readMiddleNamesFile();
        if (lastNamesDistribution == null || lastNamesDistribution.size() == 0) readLastNamesFile();
        if (nicknames == null || nicknames.size() == 0) readNicknamesFile();

        Name name = new Name();
        NameForm form = selectNameForm(demographics);
        name.setNameForm(form);

        // Basic name parts
        int[] partsCounts = selectPartsCounts(form);
        String[] givenNames = new String[partsCounts[0]];
        for (int i = 0; i < partsCounts[0]; i++) {
            givenNames[i] = selectGivenName(demographics);
        }
        String[] middleNames = new String[partsCounts[1]];
        for (int i = 0; i < partsCounts[1]; i++) {
            middleNames[i] = selectGivenName(demographics);
        }
        String[] familyNames = new String[partsCounts[2]];
        for (int i = 0; i < partsCounts[2]; i++) {
            familyNames[i] = selectFamilyName(demographics);
        }
        assignGivenName(name, givenNames);
        assignMiddleName(name, middleNames);
        assignFamilyName(name, familyNames, demographics);
        
        // Extra name parts
        
        // Nicknames
        String[] allGivens = new String[givenNames.length + middleNames.length];
        System.arraycopy(givenNames, 0, allGivens, 0, givenNames.length);
        System.arraycopy(middleNames, 0, allGivens, givenNames.length, middleNames.length);
        String nickname = selectNickName(allGivens); 
        if (nickname != null && !nickname.isBlank()) {
            name.setNickname(nickname);
            name.addDisplayOption(DisplayOption.INCLUDE_NICKNAME);
        }
        
        // Western Name
        if (form != NameForm.WESTERN && form != NameForm.HISPANIC) {
            if (Engine.randPercent() <= hasWesternNamePercent) {
                String westernName = selectGivenName(demographics.getPresentation(), DemographicsManager.matchBlocName("White"));
                name.setWesternName(westernName);
                name.addDisplayOption(DisplayOption.INCLUDE_WESTERN);
            }
        }

        // Religious (Baptismal) Name

        // Honorific

        // Ordinal

        // Suffixes

        // DISPLAY OPTIONS

        // Abbreviation
        if (form.equals(NameForm.WESTERN)) {
            if (Engine.randPercent() <= abbreviateBothNamesPercent) {
                name.addDisplayOption(DisplayOption.ABBREVIATE_FIRST);
                name.addDisplayOption(DisplayOption.ABBREVIATE_MIDDLE);
            }
            else if (Engine.randPercent() <= abbreviateFirstNamesPercent) {
                name.addDisplayOption(DisplayOption.ABBREVIATE_FIRST);
            }
            else if (Engine.randPercent() <= abbreviateMiddleNamesPercent) {
                name.addDisplayOption(DisplayOption.ABBREVIATE_MIDDLE);
            }
        }

        return name;
    }
}
