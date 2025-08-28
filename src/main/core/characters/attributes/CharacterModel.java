package main.core.characters.attributes;

import core.JSONObject;
import core.Jsonic;
import main.core.Repr;

public class CharacterModel implements Repr<CharacterModel>, Jsonic<CharacterModel> {
    
    int visualAge;
    
    public CharacterModel() {
        this(45);
    }

    public CharacterModel(CharacterModel other) {
        this.visualAge = other.visualAge;
    }

    public CharacterModel(int visualAge) {
        this.visualAge = visualAge;
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    @Override
    public String toRepr() {
        String repr = String.format("%s:[visualAge:%d;];",
            this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1],
            this.visualAge
        );
        return repr;
    }

    @Override
    public CharacterModel fromRepr(String repr) {
        return this;
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject("character_model", new Object());
    }

    @Override
    public CharacterModel fromJson(JSONObject arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }

}
