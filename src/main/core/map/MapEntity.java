/*
 * MapEntity.java
 * Steven LaGoy
 * Created: 10 June 2025 at 10:26 PM
 * Modified: 10 June 2025
 */

package main.core.map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import main.core.demographics.Bloc;

/**
 * Any Entity (Nation, State, Municipality, &c) which appears on the Map is a MapEntity. This is a blank interface to allow for polymorphism.
 */
public interface MapEntity {

    /** Get a descriptive identifying name for the MapEntity. */
    public String getName();

    /** Get the total population. */
    public int getPopulation();
    /**
     * Set the total population.
     * <p>
     * Minimum population is zero. If the population is found to be lower, it will be set to zero.
     * @param population New population.
     */
    public void setPopulation(int population);
    /**
     * Add to the total population.
     * <p>
     * Minimum population is zero. If the population is found to be lower, it will be set to zero.
     * @param population Amount of population to be added.
     */
    public void addPopulation(int population);

    /** Get the total land area. */
    public double getLandArea();
    /**
     * Set the total land area.
     * <p>
     * Minimum land area is zero. If the land area is found to be lower, it will be set to zero.
     * @param area New land area
     */
    public void setLandArea(double area);

    public Set<String> getDescriptors();

    public void setDescriptors(Set<String> descriptors);

    public boolean hasDescriptor(String descriptor);

    public boolean addDescriptor(String descriptor);

    public boolean addAllDescriptors(Collection<String> descriptors);

    public boolean removeDescriptor(String descriptor);

    public boolean removeAllDescriptors(Collection<String> descriptors);

    public Map<Bloc, Float> getDemographics();

    /**
     * Get the percentage of a demographic bloc's share within its category.
     * @param bloc The bloc to evaluate.
     * @return Float between 0.0 and 1.0 for the bloc's category share.
     * @see #getDemographicPopulation(Bloc)
     */
    public float getDemographicPercentage(Bloc bloc);

    /**
     * Get the population of a demographic bloc's membership.
     * @param bloc The bloc to evaluate.
     * @return int between zero and the population.
     * @see #getPopulation()
     * @see #getDemographicPercentage(Bloc)
     */
    public int getDemographicPopulation(Bloc bloc);

    public void evaluateDemographics();

}
