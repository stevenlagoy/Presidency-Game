package main.core.characters;

import main.core.map.State;

public class CongressPerson extends StateOfficial
{
    public CongressPerson(){
        super("Congressperson", null);
    }
    public CongressPerson(State state){
        super(state);
    }
}
