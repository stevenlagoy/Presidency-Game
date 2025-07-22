package main.core.map.travel.route;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import core.JSONObject;
import main.core.Jsonic;
import main.core.Repr;
import main.core.map.Municipality;

public class Railway extends Route implements Repr<Railway>, Jsonic<Railway> {
    
    private String name;
    private List<Municipality> connections;

    public Railway() {
        name = "";
        connections = new ArrayList<>();
    }

    public Railway(String name, Municipality... connections) {
        this(name, List.of(connections));
    }
    public Railway(String name, Collection<Municipality> connections) {
        this.name = name;
        this.connections = new ArrayList<>(connections);
    }

    public String getName() {
        return name;
    }

    public List<Municipality> getConnections() {
        return connections;
    }

    public boolean connects(Municipality connection) {
        return connections.contains(connection);
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    @Override
    public JSONObject toJson() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJson'");
    }

    @Override
    public Railway fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }

    @Override
    public String toRepr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRepr'");
    }

    @Override
    public Railway fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }

    @Override
    public String toString() {
        return "";
    }

}
