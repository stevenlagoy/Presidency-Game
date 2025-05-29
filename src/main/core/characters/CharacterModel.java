package main.core.characters;

import core.JSONObject;
import main.core.Repr;

public class CharacterModel implements Repr<CharacterModel> {
    
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

    public String toRepr() {
        String repr = String.format("%s:[visualAge:%d;];",
            this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1],
            this.visualAge
        );
        return repr;
    }

    public CharacterModel fromRepr(String repr) {
        return this;
    }

    public static CharacterModel fromJson(JSONObject appearanceJson) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }

}
