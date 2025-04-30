package main.core.politics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;

public class Issue {
    public static List<Issue> instances = new ArrayList<>();

    private String name;
    private String description;
    private List<Position> positions = new ArrayList<Position>();

    public Issue(String name, String description, List<Position> positions)
    {
        this.name = name;
        this.description = description;
        this.positions = positions;
    }

    public <T extends Collection<Position>> Issue(String name, String description, T positions)
    {
        this.name = name;
        this.description = description;
        this.positions = positions.stream().toList();
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
    public void setLevels(List<Position> positions)
    {
        this.positions = positions;
    }
    public <T extends Collection<Position>> void setLevels(T levels)
    {
        this.positions = levels.stream().toList();
    }
    public List<Position> getPositions()
    {
        return this.positions;
    }
}
