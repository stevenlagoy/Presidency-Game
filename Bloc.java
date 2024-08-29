import java.util.List;
import java.util.ArrayList;

public class Bloc
{
    public static int totalVoters;

    public static List<Bloc> instances = new ArrayList<>();

    private int numVoters;
    private String name;

    public Bloc(String name, int numVoters)
    {
        this.name = name;
        this.numVoters = numVoters;
    }

    public Bloc(String name, float percentVoters)
    {
        this.name = name;
        this.numVoters = Math.round(percentVoters * totalVoters);
    }
}