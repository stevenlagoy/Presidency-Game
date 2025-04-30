package main.core.characters;


public class Experience {
    
    public String toRepr() {
        String repr = String.format("%s:[];",
            this.getClass().getName().replace("class ", "")
        );
        return repr;
    }

}
