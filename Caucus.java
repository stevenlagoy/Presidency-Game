import java.util.ArrayList;
import java.util.List;

public class Caucus implements Convention {
    public static List<Primary> instances = new ArrayList<>();

    public boolean isClosed;

    public Caucus(boolean isClosed){
        this.isClosed = isClosed;
    }

    public void convene(){

    }
}
