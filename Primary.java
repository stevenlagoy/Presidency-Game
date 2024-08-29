import java.util.ArrayList;
import java.util.List;

public class Primary implements Convention {
    public static List<Primary> instances = new ArrayList<>();

    public boolean isClosed;
    public Party associatedParty;

    public Primary(boolean isClosed){
        this.isClosed = isClosed;
    }

    public void convene(){

    }
}
