package main.core.map.travel;

public class TrainVehicle extends Vehicle {
    
    public String type;

    public TrainVehicle(double speed, double costPerMile, String type) {
        super(speed, costPerMile);
        this.type = type;
    }

    @Override
    public String getType() { return type; }

}
