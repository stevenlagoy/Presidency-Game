import java.util.List;
import java.util.ArrayList;

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

    public int getNumVoters(){
        return numVoters;
    }
    public void setNumVoters(int numVoters){
        this.numVoters = numVoters;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public List<Bloc> getInstances(){
        return instances;
    }
}