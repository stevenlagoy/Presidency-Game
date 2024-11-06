public class StateOfficial extends GovernmentOfficial
{
    private State stateSeat;

    public StateOfficial(){
        super();
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
