import java.util.ArrayList;
import java.util.Collection;
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
    private List<String> positions = new ArrayList<>();

    public PoliticalActor(){
        super();
    }
    public PoliticalActor(String buildstring){
        super(buildstring);
    }

    public int getCash(){
        return this.cash;
    }
    public void setCash(int cash){
        this.cash = cash;
    }
    public void addCash(int cash){
        this.cash += cash;
    }

    protected void genEducation(){
    }
    public void setEducation(int education){
        this.education = education;
    }
    public int getEducation(){
        return this.education;
    }
    protected void genSkills(){
    }
    public void setSkills(int[] skills){
        this.skills = skills;
        this.evalAptitude();
    }
    public int[] getSkills(){
        return this.skills;
    }
    private void evalAptitude(){
        this.aptitude = 0;
        for(int i = 0; i < 2; i++){
            this.aptitude += this.getSkills()[i];
        }
    }
    public int getAptitude(){
        return this.aptitude;
    }
    public List<String> getPositions(){
        return this.positions;
    }
    public void addPosition(String position){
        this.positions.add(position);
        evalConviction();
    }
    public void addPositions(Collection<? extends String> position){
        this.positions.addAll(position);
        evalConviction();
    }
    protected void evalConviction(){
    }
    public int getConviction(){
        return this.conviction;
    }

    public int[] getAlignments(){
        return this.alignments;
    }
    public List<String> getExperience(){
        return this.experience;
    }
    protected void evalAgeMod(){
        if(this.getAge() < 20) ageMod = 0;
        else if(this.getAge() < 75) ageMod = (float) -((Math.pow(this.getAge()-55, 2))/20)+100;
        else if(this.getAge() < 120) ageMod = (-(12*this.getAge())/15)+140;
        else ageMod = 0;
    }
    public float getAgeMod(){
        return this.ageMod;
    }
}
