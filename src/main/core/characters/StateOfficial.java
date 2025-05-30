package main.core.characters;

import main.core.characters.GovernmentOfficial;
import main.core.map.State;

public class StateOfficial extends GovernmentOfficial
{
    private State stateSeat;

    public StateOfficial(){

    }
    public StateOfficial(String buildstring){
        super(buildstring);
    }
    public StateOfficial(String position, State stateSeat){
        super(position);
    }
    public StateOfficial(State state){
        super();
        this.stateSeat = state;
    }

    public State getState(){
        return stateSeat;
    }
    public void setState(State state){
        this.stateSeat = state;
    }
}
