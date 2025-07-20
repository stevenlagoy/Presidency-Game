package main.core.map.travel.vehicle;

public class WaterVehicle extends Vehicle {
    
    public String type;

    public WaterVehicle(double speed, double costPerMile, String type) {
        super(speed, costPerMile);
        this.type = type;
    }

    @Override
    public String getType() { return type; }

}
