import java.util.ArrayList;
import java.util.List;

public class Operation {
    public static List<Operation> instances = new ArrayList<>();

    public Character operator;
    public Character[] agents;

    public Operation(Character operator, Character[] agents){
        this.operator = operator;
        this.agents = agents;
    }
}
