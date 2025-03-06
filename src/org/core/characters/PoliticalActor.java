package src.org.core.characters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import src.org.core.Engine;
import src.org.core.characters.Experience;
import src.org.core.politics.Issue;
import src.org.core.characters.Personality;
import src.org.core.politics.Position;
import src.org.core.Repr;

public class PoliticalActor extends Character implements Repr, HasPersonality
{

    private int cash;
    private int education;
    private int[] alignments = new int[2];
    private List<Experience> experiences = new ArrayList<Experience>();
    
    private int legislativeSkill;
    private int executiveSkill;
    private int judicialSkill;
    private int aptitude;

    private List<Position> positions = new ArrayList<Position>();
    private int conviction;
    private float ageMod;

    private Personality personality;

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
    public void setSkills(int legislativeSkill, int executiveSkill, int judicialSkill){
        this.legislativeSkill = legislativeSkill;
        this.executiveSkill = executiveSkill;
        this.judicialSkill = judicialSkill;
        this.evalModifiedStats();
        this.evalAptitude();
    }
    public int getLegislativeBaseSkill(){
        return legislativeSkill;
    }
    public int getLegislativeModifiedSkill(){
        return legislativeSkill;
    }
    public void setLegislativeBaseSkill(int legislativeSkill){
        this.legislativeSkill = legislativeSkill;
        this.evalAptitude();
    }
    public int getExecutiveBaseSkill(){
        return executiveSkill;
    }
    public int getExecutiveModifiedSkill(){
        return executiveSkill;
    }
    public void setExecutiveBaseSkill(int executiveSkill){
        this.executiveSkill = executiveSkill;
        this.evalAptitude();
    }
    public int getJudicialBaseSkill(){
        return judicialSkill;
    }
    public int getJudicialModifiedSkill(){
        return judicialSkill;
    }
    public void setJudicialBaseSkill(int judicialSkill){
        this.judicialSkill = judicialSkill;
        this.evalAptitude();
    }
    protected void evalModifiedStats(){
        // multiply the base stats by the agemod and other modifiers
    }
    protected void evalAptitude(){
        this.aptitude = legislativeSkill + executiveSkill + judicialSkill;
    }
    public int getAptitude(){
        return this.aptitude;
    }
    public Position getPositionOnIssue(Issue issue){
        for(Position position : positions){
            if(position.getRootIssue().equals(issue)) return position;
        }
        Engine.log("INVALID ISSUE NAME", String.format("An invalid issue name, \"%s\", was supplied. Unable to determine position on non-existant issue.", issue), new Exception());
        return null;
    }
    public List<Position> getPositions(){
        return this.positions;
    }
    public void addPosition(Position position){
        this.positions.add(position);
        evalConviction();
    }
    public void addPositions(Collection<? extends Position> position){
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
    public List<Experience> getExperience(){
        return this.experiences;
    }
    protected void evalAgeMod(){
        if(this.getAgeYears() < 20) ageMod = 0;
        else if(this.getAgeYears() < 75) ageMod = (float) -((Math.pow(this.getAgeYears()-55, 2))/20)+100;
        else if(this.getAgeYears() < 120) ageMod = (float) (-(12*this.getAgeYears())/15)+140;
        else ageMod = 0;
    }
    public float getAgeMod(){
        return this.ageMod;
    }
    public void determinePersonality(){

    }

    public void fromRepr(String repr){

    }
    public String toRepr(){
        String repr = "";
        return repr;
    }
}
