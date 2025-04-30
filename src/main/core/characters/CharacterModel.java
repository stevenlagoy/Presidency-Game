package main.core.characters;

import main.core.Repr;

public class CharacterModel implements Repr {
    
    public String toRepr() {
        String repr = String.format("%s:[];",
            this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1]
        );
        return repr;
    }

    public void fromRepr(String repr) {

    }

}
