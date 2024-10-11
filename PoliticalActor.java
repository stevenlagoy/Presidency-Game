import java.util.ArrayList;
import java.util.List;

public class PoliticalActor extends Character
{

    private int cash;
    private int education;
    private int[] alignments = new int[2];
    private List<String> experience = new ArrayList<>();
    private int[] skills = new int[3];
    private int aptitude;
    private int conviction;
    private float ageMod;

    public PoliticalActor(){
        super();
    }
    public PoliticalActor(String buildstring){
        super(buildstring);
    }

    protected void genEducation(){}
    public void setEducation(){}
    public int getEducation(){
        return this.education;
    }
    protected void rollSkills(){}
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
    protected void rollConviction(){}
    public void setConviction(){}
    public int getConviction(){
        return this.conviction;
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
