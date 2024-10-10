import java.util.ArrayList;
import java.util.List;

public class Candidate extends Character
{
    public static List<Candidate> instances = new ArrayList<>();

    private short numDelegates;
    private int cash;
    private State origin;
    private int education;
    private int[] alignments = new int[2];
    private List<String> experience = new ArrayList<>();
    private int[] skills = new int[3];
    private int aptitude;
    private int conviction;
    private float ageMod;

    public Candidate(String buildstring, boolean isPlayer)
    {
        super(buildstring);
        if(isPlayer) this.genProfileInput();
        else this.genProfile();
    }

    private void genProfileInput()
    {
        this.setNameInput();
        this.setAgeInput();
        this.setPresentationInput();
        this.setOriginInput();
        this.setEducationInput();
        this.setAlignmentInput();
        this.rollSkills();
        this.rollConviction();
    }

    private void genProfile()
    {
        this.genPresentation();
        this.genOrigin();
        this.genAge();
        this.genName();
        this.genEducation();
        this.rollSkills();
        this.rollConviction();
    }

    // input functions
    private void setNameInput(){
        this.setFamilyName("");
    }
    private void setAgeInput(){
        this.setAge(0);
    }
    private void setPresentationInput(){}
    private void setOriginInput(){}
    private void setEducationInput(){}
    private void setAlignmentInput(){}

    // inherited from Character
    private void genName(){}
    private void genAge(){}
    private void genPresentation(){}

    // specific to Candidate
    private void genOrigin(){}
    private void genEducation(){}
    public void setEducation(){}
    public int getEducation(){
        return this.education;
    }
    private void rollSkills(){}
    public void setSkills(){
        this.aptitude = (short) 0;
        for(int i = 0; i < 2; i++){
            this.aptitude += this.getSkills()[i];
        }
    }
    public int[] getSkills(){
        return this.skills;
    }
    public int getAptitude(){
        return this.aptitude;
    }
    private void rollConviction(){}
    public void setConviction(){}
    public int getConviction(){
        return this.conviction;
    }

    public int getNumDelegates(){
        return this.numDelegates;
    }
    public int getCash(){
        return this.cash;
    }
    public State getOrigin(){
        return this.origin;
    }
    public int[] getAlignments(){
        return this.alignments;
    }
    public List<String> getExperience(){
        return this.experience;
    }
    public float getAgeMod(){
        return this.ageMod;
    }
}
