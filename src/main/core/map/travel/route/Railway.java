package main.core.map.travel.route;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import main.core.map.Municipality;

public class Railway extends Route {
    
    private String name;
    private List<Municipality> connections;

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

}
