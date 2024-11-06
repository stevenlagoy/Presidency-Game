import java.util.ArrayList;
import java.util.List;

public class Candidate extends PoliticalActor
{
    public static List<Candidate> instances = new ArrayList<>();

    private int numDelegates;
    private double influence;

    public Candidate(){
        super();
    }
    public Candidate(String buildstring)
    {
        super(buildstring);
        this.genProfile();
    }

    private void genProfile()
    {
        this.genPresentation();
        this.genOrigin();
        this.genAge();
        super.genGivenName();
        super.genMiddleName();
        super.genFamilyName();
        super.genEducation();
        this.genSkills();
        this.evalConviction();
    }

    protected void genAge(){
        super.genAge(35);
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
}
