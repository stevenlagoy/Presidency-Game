package politics;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Issue {
    public static List<Issue> instances = new ArrayList<>();

    private String name;
    private String description;
    private HashMap<Short, String> levels;

    public Issue(String name, String description, HashMap<Short, String> levels)
    {
        this.name = name;
        this.description = description;
        this.levels = levels;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return this.name;
    }
    public void setDesc(String description)
    {
        this.description = description;
    }
    public String getDesc()
    {
        return this.description;
    }
    public void setLevels(HashMap<Short, String> levels)
    {
        this.levels = levels;
    }
    public HashMap<Short, String> getLevels()
    {
        return this.levels;
    }
}
