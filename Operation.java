import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class Operation {
    public static List<Operation> instances = new ArrayList<>();

    private Character operator;
    private Set<Character> agents;

    public Operation(Character operator){
        this.operator = operator;
        this.agents = new HashSet<Character>();
    }
    public Operation(Character operator, Character[] agents){
        this.operator = operator;
        this.agents = new HashSet<Character>();
        for(Character c : agents){
            this.agents.add(c);
        }
    }

    public Character getOperator(){
        return this.operator;
    }
    public void setOperator(Character c){
        this.operator = c;
    }
    public Set<Character> getAgents(){
        return this.agents;
    }
    public void addAgent(Character c){
        this.agents.add(c);
    }
    public void removeAgent(Character c){
        this.agents.remove(c);
    }
    public void resetAgents(){
        this.agents.clear();
    }
}
