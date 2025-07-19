package main.core.map.travel;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.crypto.Data;

import main.core.DateManager;
import main.core.characters.Character;
import main.core.map.MapManager;
import main.core.map.Municipality;

public class Travel {
    
    public abstract class Leg {

        protected Municipality start;
        protected Municipality destination;
        private Vehicle vehicle;
        private List<Character> travelers;

        public Leg(Municipality start, Municipality destination, Vehicle vehicle, Character... travelers) {
            this(start, destination, vehicle, List.of(travelers));
        }
        public Leg(Municipality start, Municipality destination, Vehicle vehicle, Collection<Character> travelers) {
            this.start = start;
            this.destination = destination;
            this.travelers = new ArrayList<Character>(travelers);
        }

        public Municipality getStart() { return start; }
        
        public Municipality getDestination() { return destination; }

        public Vehicle getVehicle() { return vehicle; }
        
        public List<Character> getTravelers() { return travelers; }

        public abstract double getDistance();

        public abstract double getCost();

        public abstract long getTravelTime();

    }

    public class RoadLeg extends Leg {

        private RoadVehicle vehicle;
        private double distance;
        private double cost;
        private long travelTime;

        private Roadway roadway;

        public RoadLeg(Municipality start, Municipality destination, RoadVehicle vehicle, Roadway roadway, Character... travelers) {
            this(start, destination, vehicle, roadway, List.of(travelers));
        }
        public RoadLeg(Municipality start, Municipality destination, RoadVehicle vehicle, Roadway roadway, Collection<Character> travelers) {
            super(start, destination, vehicle, travelers);
            this.roadway = roadway;
            distance = -1.0;
            getDistance();
            cost = -1.0;
            getCost();
            travelTime = -1L;
            getTravelTime();
        }

        public Roadway getRoadway() { return roadway; }

        @Override
        public RoadVehicle getVehicle() { return vehicle; }

        @Override
        public double getDistance() {
            if (distance < 0)
                distance = MapManager.getRoadDistance(start, destination);
            return distance;
        }

        @Override
        public double getCost() {
            if (cost < 0)
                cost = getVehicle().getCostPerMile() * getDistance();
            return cost;
        }

        @Override
        public long getTravelTime() {
            if (travelTime < 0)
                travelTime = (long) (getDistance() / roadway.getSpeed() * DateManager.hourDuration);
            return travelTime;
        }

    }

    public class TrainLeg extends Leg {

        private TrainVehicle vehicle;
        private double distance;
        private double cost;

        public TrainLeg(Municipality start, Municipality destination, TrainVehicle vehicle, Character... travelers) {
            this(start, destination, vehicle, List.of(travelers));
        }
        public TrainLeg(Municipality start, Municipality destination, TrainVehicle vehicle, Collection<Character> travelers) {
            super(start, destination, vehicle, travelers);
            distance = -1.0;
            getDistance();
            cost = -1.0;
            getCost();
            travelTime = -1L;
            getTravelTime();
        }

        @Override
        public TrainVehicle getVehicle() { return vehicle; }

        @Override
        public double getDistance() {
            if (distance < 0)
                distance = MapManager.getTrainDistance(start, destination);
            return distance;
        }

        @Override
        public double getCost() {
            if (cost < 0)
                cost = getVehicle().getCostPerMile() * getDistance();
            return cost;
        }

        @Override
        public long getTravelTime() {
            if (travelTime < 0)
                travelTime = (long) (getDistance() * vehicle.getSpeed() * DateManager.hourDuration);
            return travelTime;
        }

    }

    public class AirLeg extends Leg {

        private AirVehicle vehicle;
        private double distance;
        private double cost;
        private long travelTime;

        public AirLeg(Municipality start, Municipality destination, AirVehicle vehicle, Character... travelers) {
            this(start, destination, vehicle, List.of(travelers));
        }
        public AirLeg(Municipality start, Municipality destination, AirVehicle vehicle, Collection<Character> travelers) {
            super(start, destination, vehicle, travelers);
            distance = -1.0;
            getDistance();
            cost = -1.0;
            getCost();
            travelTime = -1L;
            getTravelTime();
        }

        @Override
        public AirVehicle getVehicle() { return vehicle; }

        @Override
        public double getDistance() {
            if (distance < 0)
                distance = MapManager.getAirDistance(start, destination);
            return distance;
        }

        @Override
        public double getCost() {
            if (cost < 0)
                cost = getVehicle().getCostPerMile() * getDistance();
            return cost;
        }

        @Override
        public long getTravelTime() {
            if (travelTime < 0)
                travelTime = (long) (getDistance() * vehicle.getSpeed() * DateManager.hourDuration);
            return travelTime;
        }

    }

    public class WaterLeg extends Leg {

        private WaterVehicle vehicle;
        private double distance;
        private double cost;
        private long travelTime;

        public WaterLeg(Municipality start, Municipality destination, WaterVehicle vehicle, Character... travelers) {
            this(start, destination, vehicle, List.of(travelers));
        }
        public WaterLeg(Municipality start, Municipality destination, WaterVehicle vehicle, Collection<Character> travelers) {
            super(start, destination, vehicle, travelers);
            distance = -1.0;
            getDistance();
            cost = -1.0;
            getCost();
            travelTime = -1L;
            getTravelTime();
        }

        @Override
        public WaterVehicle getVehicle() { return vehicle; }

        @Override
        public double getDistance() {
            if (distance < 0)
                distance = MapManager.getWaterDistance(start, destination);
            return distance;
        }

        @Override
        public double getCost() {
            if (cost < 0)
                cost = getVehicle().getCostPerMile() * getDistance();
            return cost;
        }

        @Override
        public long getTravelTime() {
            if (travelTime < 0)
                travelTime = (long) (getDistance() * vehicle.getSpeed() * DateManager.hourDuration);
            return travelTime;
        }

    }

    private Map<Character, Municipality> travelersDestinations;
    private List<Vehicle> availableVehicles;

    private List<Leg> legs;
    private double distance;
    private double cost;
    private long travelTime;

    public Travel(Leg... legs) {
        this(List.of(legs));
    }
    public Travel(Collection<Leg> legs) {
        this.legs = new ArrayList<>();
        this.legs.addAll(legs);
        travelersDestinations = new HashMap<>();
        availableVehicles = new ArrayList<>();
    }

    // Legs : List of Leg

    public List<Leg> getLegs() { return legs; }

    public void addLeg(Leg leg) {
        this.legs.add(leg);
        distance = -1;
        cost = -1;
        getDistance();
        getCost();
    }

    public void addLeg(int index, Leg leg) {
        this.legs.add(index, leg);
        distance = -1;
        cost = -1;
        getDistance();
        getCost();
    }

    // Distance : double

    public double getDistance() {
        if (distance < 0) {
            distance = 0;
            for (Leg leg : legs) {
                distance += leg.getDistance();
            }
        }
        return distance;
    }

    // Cost : double

    public double getCost() {
        if (cost < 0) {
            cost = 0;
            for (Leg leg : legs) {
                cost += leg.getCost();
            }
        }
        return cost;
    }

    // Destinations : Map of Character to Municipality

    public Map<Character, Municipality> getTravelersDestinations() {
        return travelersDestinations;
    }

    /**
     * Adds a traveler with a destination to the Travel. Can be used to add or set a traveler's location.
     * @param traveler Character to add as a traveler on the Travel.
     * @param destination Destination of the traveler.
     * @return Municipality previously set as the destination of the traveler, or {@code null} if no current destination.
     */
    public Municipality putTraveler(Character traveler, Municipality destination) {
        return travelersDestinations.put(traveler, destination);
    }

    public Municipality removeTraveler(Character traveler) {
        return travelersDestinations.remove(traveler);
    }

    // Available Vehicles : List of Vehicle

    public List<Vehicle> getAvailableVehicles() {
        return availableVehicles;
    }

    // Private Methods ----------------------------------------------------------------------------

    /**
     * Calculates the Legs of this Travel, with equal priority for distance, cost, and time.
     * @see #calculateLegs(float, float, float)
     */
    private void calculateLegs() {
        calculateLegs(1.0f, 1.0f, 1.0f);
    }
    /**
     * Calculates the Legs of this Travel, with the passed priorities for distance, cost, and time.
     * Priorities are relative, where equal priorities of any value equally prioritize each factor,
     * and where one priority being double the other two will result in that being weighted twice as high as the others.
     * @param distancePriority Priority to place on reducing the total distance of the travel.
     * @param costPriority Priority to place on reducing the total cost of the travel.
     * @param timePriority Priority to place on the reducing the total time of the travel.
     */
    private void calculateLegs(float distancePriority, float costPriority, float timePriority) {
        // Start with the largest chunk possible of the journey, and then work to the smallest distances
        
        /*
         * Goal is to get all travelers to their destinations
         * Approach 1:
         *      For each traveler, determine journey from start to end.
         *      Determine largest possible leg first, then do smaller ones
         *      See if any traveler is taking the same leg as another, and merge if they are.
         * PROS: Faster, simpler
         * CONS: May result in suboptimal pathing, overlapping legs
         */

        // Normalize priorities
        if (distancePriority < 0) {
            distancePriority -= distancePriority;
            costPriority -= distancePriority;
            timePriority -= distancePriority;
        }
        if (costPriority < 0) {
            distancePriority -= costPriority;
            costPriority -= costPriority;
            timePriority -= costPriority;
        }
        if (timePriority < 0) {
            distancePriority -= timePriority;
            costPriority -= timePriority;
            timePriority -= timePriority;
        }
        float totalPriority = distancePriority + costPriority + timePriority;
        if (totalPriority == 0) totalPriority = 1.0f;
        distancePriority /= totalPriority;
        costPriority /= totalPriority;
        timePriority /= totalPriority;

        // Score available vehicles based on priorities
        double maxCost = 0.0;
        double maxSpeed = 0.0;
        for (Vehicle vehicle : availableVehicles) {
            if (vehicle.getCostPerMile() > maxCost)
                maxCost = vehicle.getCostPerMile();
            if (vehicle.getSpeed() > maxSpeed)
                maxSpeed = vehicle.getSpeed();
        }
        Map<Vehicle, Float> vehicleScores = new HashMap<>();
        Vehicle maxVehicle = null;
        float maxScore = 0.0f;
        for (Vehicle vehicle : availableVehicles) {
            float costScore = (float) (1 - vehicle.getCostPerMile() / maxCost) * costPriority;
            float speedScore = (float) (1 - vehicle.getSpeed() / maxSpeed) * timePriority;
            float vehicleScore = (costScore + speedScore + 1.0f) / 3;
            vehicleScores.put(vehicle, vehicleScore);
            if (vehicleScore > maxScore) {
                maxVehicle = vehicle;
                maxScore = vehicleScore;
            }
        }

        

        // Start with highest ranked vehicle
        Municipality legStart = MapManager.getClosestStart(maxVehicle, );

        // Determine highest priority, apply algorithm accordingly
        if (distancePriority > costPriority && distancePriority > timePriority) {
            // Reducing distance most important
        }
        else if (costPriority > timePriority) {
            // Reducing cost most important
        }
        else {
            // Reducing time most important
            
            // Rank available vehicles by speed
            Map<Vehicle, Double> speedRanks = new HashMap<>();
            for (Vehicle vehicle : availableVehicles) {
                speedRanks.put(vehicle, vehicle.getSpeed());
            }
        }
    }

    // Should check capacity of vehicles to determine how many needed to transport all travelers
    // Two vehicles taking the same route at the same time should be counted as two different legs

}
