import java.util.ArrayList;
import java.util.List;

public class Candidate extends Character
{
    private static List<Candidate> instances = new ArrayList<>();

    private short delegates;
    private int cash;
    private State origin;
    private byte education;
    private short[] alignments = new short[2];
    private List<String> experience = new ArrayList<>();
    private short[] skills = new short[3];
    private short aptitude;
    private short conviction;
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
        this.setAge((byte) 0);
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
    public byte getEducation(){
        return this.education;
    }
    private void rollSkills(){}
    public void setSkills(){
        this.aptitude = (short) 0;
        for(int i = 0; i < 2; i++){
            this.aptitude += this.getSkills()[i];
        }
    }
    public short[] getSkills(){
        short[] skillshort = new short[3];
        skillshort[0] = 0;
        skillshort[1] = 0;
        skillshort[2] = 0;
        return skillshort;
    }
    public short getAptitude(){
        return this.aptitude;
    }
    private void rollConviction(){}
    public void setConviction(){}
    public short getConviction(){
        return this.conviction;
    }
}
