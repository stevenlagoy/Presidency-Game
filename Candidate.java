public class Candidate extends Character
{
    private static List instances = new ArrayList();

    private short delegates;
    private int cash;
    private State origin;
    private byte education;
    private short[] alignments = new short[2];
    private List experience = new ArrayList();
    private short[] skills = new short[3];
    private short aptitude;
    private short conviction;
    private float ageMod;

    public Candidate(String buildstring, boolean isPlayer)
    {
        super(buildstring);
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
        this.name = "";
    }
    private void setAgeInput();
    private void setPresentationInput();
    private void setOriginInput();
    private void setEducationInput()

    // inherited from Character
    private void genName();
    private void genAge();
    private void genPresentation();

    // specific to Candidate
    private void genEducation();
    public void setEducation();
    public byte getEducation();
    private void rollSkills();
    public void setSkills();
    public short[] getSkills();
    public short getAptitude();
    private void rollConviction();
    public void setConviction();
    public short getConviction();
}
