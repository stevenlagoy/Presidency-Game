import java.util.ArrayList;
import java.util.List;

public class Campaign extends Operation
{
    public static List<Operation> instances = new ArrayList<>();

    public Campaign(Character campaigner, Character[] agents)
    {
        super(campaigner, agents);
    }
}
