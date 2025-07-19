package main.core.map.travel;

public class AirVehicle extends Vehicle {

    public String type;

    public AirVehicle(double speed, double costPerMile, String type) {
        super(speed, costPerMile);
        this.type = type;
    }

    @Override
    public String getType() { return type; }

}
