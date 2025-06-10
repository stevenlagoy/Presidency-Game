package main.core.characters;

import main.core.map.State;

public class StateOfficial extends PoliticalActor {

    private State state;

    public StateOfficial(){

    }
    public StateOfficial(String buildstring){
        super(buildstring);
    }
    public StateOfficial(Role role, State state){
        super();
    }
    public StateOfficial(State state){
        super();
        this.state = state;
    }

    public State getState(){
        return state;
    }
    public void setState(State state){
        this.state = state;
    }
}
