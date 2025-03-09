package org.core.map;

import org.core.characters.StateOfficial;

public class CongressionalDistrict2 {

    private int stateFIPS; // FIPS code identifying state/territory
    private int districtNum; // Congressional District number for the 118th Congress
    private long aLand; // Land area in square meters
    private long aWater; // Water area in square meters
    private double intptLat; // Latitude of district's center point
    private double intptLon; // Longitude of district's center point
    private double shapeArea; // Area of the district's shape
    private double shapeLength; // Length of the district's shape

    private StateOfficial representative;

    // Constructor
    public CongressionalDistrict2(
        int stateFIPS,
        int districtNum,
        long aLand,
        long aWater,
        double intptLat,
        double intptLon,
        double shapeArea,
        double shapeLength,
        StateOfficial representative
        ) {

        this.stateFIPS = stateFIPS;
        this.districtNum = districtNum;
        this.aLand = aLand;
        this.aWater = aWater;
        this.intptLat = intptLat;
        this.intptLon = intptLon;
        this.shapeArea = shapeArea;
        this.shapeLength = shapeLength;
        this.representative = representative;
    }

    // Getter and Setter methods
    public int getStateFIPS() { return stateFIPS; }
    public void setStateFIPS(int stateFIPS) { this.stateFIPS = stateFIPS; }

    public int getDistrictNum() { return districtNum; }
    public void setDistrictNum(int districtNum) { this.districtNum = districtNum; }

    public long getALand() { return aLand; }
    public void setALand(long aLand) { this.aLand = aLand; }

    public long getAWater() { return aWater; }
    public void setAWater(long aWater) { this.aWater = aWater; }

    public double getIntptLat() { return intptLat; }
    public void setIntptLat(double intptLat) { this.intptLat = intptLat; }

    public double getIntptLon() { return intptLon; }
    public void setIntptLon(double intptLon) { this.intptLon = intptLon; }

    public double getShapeArea() { return shapeArea; }
    public void setShapeArea(double shapeArea) { this.shapeArea = shapeArea; }

    public double getShapeLength() { return shapeLength; }
    public void setShapeLength(double shapeLength) { this.shapeLength = shapeLength; }

    public StateOfficial getRepresentative() { return representative; }
    public void setRepresentative(StateOfficial representative) { this.representative = representative; }

}
