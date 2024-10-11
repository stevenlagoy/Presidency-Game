import java.util.ArrayList;
import java.util.List;

public class Candidate extends PoliticalActor
{
    public static List<Candidate> instances = new ArrayList<>();

    private short numDelegates;

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
        this.rollSkills();
        this.rollConviction();
    }

    private void genAge(){}
    private void genPresentation(){}

    public int getNumDelegates(){
        return this.numDelegates;
    }
}
