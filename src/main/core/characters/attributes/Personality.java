package main.core.characters.attributes;

import core.JSONObject;
import core.Jsonic;
import main.core.Repr;

public class Personality implements Repr<Personality>, Jsonic<Personality> {
    
    @Override
    public String toRepr() {
        String repr = String.format("%s:[];",
            this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1]
        );
        return repr;
    }

    @Override
    public Personality fromJson(JSONObject arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject("personality");
    }

    @Override
    public Personality fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }
    
    @Override
    public Personality clone() {
        return new Personality();
    }

}
