package main.core.characters;

import main.core.map.State;

public class Governor extends StateOfficial
{
    public Governor(){
        super("Governor", null);
    }
    public Governor(State state){
        super(state);
    } 
    public Governor(String buildstring){
        super(buildstring);
    }
}
