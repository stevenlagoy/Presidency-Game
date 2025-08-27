/*
 * Nation.java
 * Steven LaGoy
 * Created: 10 June 2025 at 10:44 PM
 * Modified: 10 June 2025
 */

package main.core.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// IMPORTS ----------------------------------------------------------------------------------------

// Internal Imports
import main.core.Engine;
import main.core.Logger;
import main.core.Main;
import main.core.characters.CharacterManager;
import main.core.characters.FederalOfficial;
import main.core.demographics.Bloc;
import main.core.demographics.DemographicsManager;

/**
 * Singleton class holding values for the Nation, or the United States.
 * <p>
 * Most important field is population, but other values like the name, capital,
 * and president can also be accessed through this class.
 * <p>
 * This is a Singleton class. It cannot be instantiated, and access to the single
 * class instance is achieved through the {@link #getInstance()} method.
 */
public class Nation implements MapEntity {

    // SINGLETON PATTERN --------------------------------------------------------------------------

    /** Singleton instance of the Nation */
    private static Nation instance;
    /**  This is a Singleton class. It cannot be instantiated, and access to the single class instance is achieved through the {@link #getInstance()} method. */
    private Nation() {} // Non-Instantiable

    /**
     * Get the singleton Nation instance.
     */
    public static Nation getInstance() {
        if (instance == null)
            instance = new Nation();
            instance.descriptors = new HashSet<>();
            instance.demographics = new HashMap<>();
        return instance;
    }

    // INSTANCE VARIABLES -------------------------------------------------------------------------

    /** Population of the Nation */
    private int population;
    /** Land area of the Nation */
    private double landArea;

    /** Full Name of the Nation; in English, "United States of America" */
    private final String fullName = "UNITED_STATES_OF_AMERICA";
    /** Common Name of the Nation; in English, "United States" */
    private final String commonName = "UNITED_STATES";
    /** Motto of the Nation; in English, "In God We Trust" */
    private final String motto = "US_MOTTO";
    /** Abbreviation of the Nation; in English, "U.S." */
    private final String abbreviation = "US";
    /** String representing the capital of the Nation, Washington, DC. */
    private final String capitalString = "Washington, DC";
    /** Capital Municipality of the Nation. */
    private Municipality capital;
    /** President of the Nation. */
    private FederalOfficial president;
    /** Vice President of the Nation. */
    private FederalOfficial vicePresident;
    /** Demographics of the whole Nation. */
    private Map<Bloc, Float> demographics;
    /** Descriptors shared by every geographic division of the Nation. */
    private Set<String> descriptors;

    // GETTERS AND SETTERS ------------------------------------------------------------------------

    // Population : int

    /** Get the total population of the Nation. */
    @Override
    public int getPopulation() {
        return population;
    }
    /**
     * Set the total population of the Nation.
     * <p>
     * Minimum population is zero. If the population is found to be lower, it will be set to zero.
     * @param population New population of the Nation.
     */
    @Override
    public void setPopulation(int population) {
        this.population = Math.max(0, population);
    }
    /**
     * Add to the total population of the Nation.
     * <p>
     * Minimum population is zero. If the population is found to be lower, it will be set to zero.
     * @param population Amount of population to be added.
     */
    @Override
    public void addPopulation(int population) {
        this.population = Math.max(0, this.population + population);
    }

    // Land Area : double

    /** Get the total land area of the Nation. */
    @Override
    public double getLandArea() {
        return landArea;
    }
    /**
     * Set the total land area of the Nation.
     * <p>
     * Minimum land area is zero. If the land area is found to be lower, it will be set to zero.
     * @param area New land area of the Nation.
     */
    @Override
    public void setLandArea(double area) {
        this.landArea = Math.max(0, area);
    }

    // Full Name : String

    /**
     * Get the localized full name of the Nation.
     * <p>
     * In English, "United States of America"
     * @return String name.
     */
    public String getFullName() {
        return Main.Engine().LanguageManager().getLocalization(fullName);
    }

    // Commmon Name : String

    /**
     * Get the localized common name of the Nation.
     * <p>
     * In English, "United States"
     * @return
     */
    public String getCommonName() {
        return Main.Engine().LanguageManager().getLocalization(commonName);
    }

    // Motto : String

    /**
     * Get the localized motto of the Nation.
     * <p>
     * In English, "In God We Trust"
     * @return String motto.
     */
    public String getMotto() {
        return Main.Engine().LanguageManager().getLocalization(motto);
    }

    // Abbreviation : String

    /**
     * Get the localized abbreviation of the Nation.
     * <p>
     * In English, "U.S."
     * @return String abbreviation.
     */
    public String getAbbreviation() {
        return Main.Engine().LanguageManager().getLocalization(abbreviation);
    }

    // Capital : Municipality

    public Municipality getCapital () {
        if (capital == null) {
            capital = Main.Engine().MapManager().matchMunicipality(capitalString);
            if (capital == null) {
                Logger.log("NATIONAL CAPITAL UNFOUND", String.format("The national capital, %s, was unable to be found.", capitalString), new Exception());
            }
        }
        return capital;
    }

    // President : FederalOfficial

    public FederalOfficial getPresident() {
        if (president == null)
            president = CharacterManager.getPresident();
        return president;
    }

    // Vice President : FederalOfficial

    public FederalOfficial getVicePresident() {
        if (vicePresident == null)
            vicePresident = CharacterManager.getVicePresident();
        return vicePresident;
    }

    // Descriptors : Set of Strings

    @Override
    public Set<String> getDescriptors() {
        return descriptors;
    }
    @Override
    public void setDescriptors(Set<String> descriptors) {
        this.descriptors = descriptors != null ? descriptors : new HashSet<>();
        evaluateDemographics();
    }
    @Override
    public boolean hasDescriptor(String descriptor) {
        return this.descriptors.contains(descriptor);
    }
    @Override
    public boolean addDescriptor(String descriptor) {
        boolean modified = this.descriptors.add(descriptor);
        if (modified) evaluateDemographics();
        return modified;
    }
    @Override
    public boolean addAllDescriptors(Collection<String> descriptors) {
        boolean modified = false;
        for (String descriptor : descriptors) {
            modified = addDescriptor(descriptor) || modified;
        }
        return modified;
    }
    @Override
    public boolean removeDescriptor(String descriptor) {
        boolean modified = this.descriptors.add(descriptor);
        if (modified) evaluateDemographics();
        return modified;
    }
    @Override
    public boolean removeAllDescriptors(Collection<String> descriptors) {
        boolean modified = descriptors.removeAll(descriptors);
        if (modified) evaluateDemographics();
        return modified;
    }

    // Demographics : Map of Bloc to Float

    @Override
    public Map<Bloc, Float> getDemographics() {
        return demographics;
    }
    @Override
    public float getDemographicPercentage(Bloc bloc) {
        return this.demographics.get(bloc) != null ? this.demographics.get(bloc) : 0.0f;
    }
    @Override
    public int getDemographicPopulation(Bloc bloc) {
        return Math.round(getDemographicPercentage(bloc) * population);
    }
    @Override
    public void evaluateDemographics() {
        // TODO this.demographics = Main.Engine().DemographicsManager().demographicsFromDescriptors(descriptors);
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    public String toRepr() {
        return "";
    }

    public Nation fromRepr() {
        return this;
    }

    @Override
    public String toString() {
        return fullName;
    }

    // OBJECT METHODS -----------------------------------------------------------------------------

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

}
