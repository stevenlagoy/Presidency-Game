package main.core.map.travel.vehicle;

public abstract class Vehicle {
    static String type = "Vehicle";

    private double speed;
    private double costPerMile;

    public Vehicle(double speed, double costPerMile) {
        this.speed = speed;
        this.costPerMile = costPerMile;
    }

    public String getType() {
        return type;
    }

    public double getSpeed() {
        return speed;
    }

    public double getCostPerMile() {
        return costPerMile;
    }
}
