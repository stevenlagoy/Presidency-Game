package main.core.map.travel.vehicle;

public class RoadVehicle extends Vehicle {

    public String type;

    public RoadVehicle(double speed, double costPerMile, String type) {
        super(speed, costPerMile);
        this.type = type;
    }

    @Override
    public String getType() { return type; }

}
