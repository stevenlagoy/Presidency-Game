import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

public class Candidate extends PoliticalActor implements Repr, HasPersonality
{
    final static int SKILLS_BONUS_EDUCATION = 10;
    final static double EDUCATION_SCALER = 1.25;
    final static int MIN_AGE = 35;
    final static int MAX_AGE = 120;
    final static int POINTSPERSKILL = 10;

    public static List<Candidate> instances = new ArrayList<>();

    private int numDelegates;
    private double influence;

    public Candidate(){
        super();
    }
    public Candidate(String buildstring)
    {
        super(buildstring);
        this.fromRepr(buildstring);
        this.genProfile();
    }

    private void generateAll()
    {
        this.generateAge();
        this.genPresentation();
        this.genOrigin();
        this.generateProfile();
        this.generatePolitics();
        this.generateAppearance();
        this.determinePersonality();
    }

    private void generateProfile(){

    }

    protected void genPresentation(){}

    public int getNumDelegates(){
        return this.numDelegates;
    }
    public void setNumDelegates(int numDelegates){
        this.numDelegates = numDelegates;
    }
    public void addNumDelegates(int numDelegates){
        this.numDelegates += numDelegates;
    }
    public double getInfluence(){
        return influence;
    }
    public void setInfluence(double influence){
        this.influence = influence;
    }
    public void addInfluence(double influence){
        this.influence += influence;
    }

    public void determinePersonality(){

    }

    public String toRepr(){
        String repr = String.format("");
        return repr;
    }
    public void fromRepr(String repr){

    }
}
