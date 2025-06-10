package main.core.characters;

public class Personality {
    
    public String toRepr() {
        String repr = String.format("%s:[];",
            this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1]
        );
        return repr;
    }

    @Override
    public Personality clone() {
        return new Personality();
    }

}
