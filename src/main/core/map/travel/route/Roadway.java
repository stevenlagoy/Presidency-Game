package main.core.map.travel.route;

import main.core.Jsonic;
import main.core.Repr;
import main.core.map.Municipality;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import core.JSONObject;

public class Roadway implements Repr<Roadway>, Jsonic<Roadway> {
    
    // STATIC VARIABLES ---------------------------------------------------------------------------

    private static Map<String, Designation> designations;

    public static class Designation {
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

    // INSTANCE VARIABLES -------------------------------------------------------------------------

    private String name;
    private String code;
    private Designation designation;
    private List<Municipality> connections;

    // CONSTRUCTORS -------------------------------------------------------------------------------

    public Roadway() {
        name = "";
        code = "";
        designation = null;
        connections = new ArrayList<>();
    }

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

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    @Override
    public JSONObject toJson() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJson'");
    }

    @Override
    public Roadway fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }

    @Override
    public String toRepr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRepr'");
    }

    @Override
    public Roadway fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }

}
