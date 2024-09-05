import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

public class Bloc
{
    private static List<Bloc> instances = new ArrayList<>();
    public static int totalVoters;

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