package main.core.map.travel;

import main.core.map.Municipality;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Roadway {
    
    private static Map<String, Designation> designations;

    public class Designation {
        private String name;
        private double speed;

        public Designation(String name, double speed) {
            this.name = name;
            this.speed = speed;
            designations.put(name, this);
        }

        public String getName() { return name; }

        public double getSpeed() { return speed; }
    }

    private String name;
    private String code;
    private Designation designation;
    private List<Municipality> connections;

    public Roadway(String name, String code, String designationName, Municipality... connections) {
        this(name, code, designationName, List.of(connections));
    }
    public Roadway(String name, String code, String designationName, Collection<Municipality> connections) {
        this(name, code, designations.get(designationName), connections);
    }
    public Roadway(String name, String code, Designation designation, Municipality... connections) {
        this(name, code, designation, List.of(connections));
    }
    public Roadway(String name, String code, Designation designation, Collection<Municipality> connections) {
        this.name = name;
        this.code = code;
        this.designation = designation;
        this.connections = new ArrayList<>(connections);
    }

    public String getName() { return name; }

    public String getCode() { return code; }

    public Designation getDesignation() { return designation; }

    public double getSpeed() { return designation.speed; }

    public List<Municipality> getConnection() { return connections; }

    public boolean connects(Municipality connection) { return connections.contains(connection); }

}
