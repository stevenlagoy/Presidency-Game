package main.core.politics;
import java.util.ArrayList;
import java.util.List;

import main.core.characters.Character;

public class GatherIntel extends Operation
{
    public static List<Operation> instances = new ArrayList<>();

    private byte aggressiveness;
    private byte directness;
    private byte secrecy;

    public GatherIntel(Character operator, Character[] agents)
    {
        super(operator, agents);
    }
}
