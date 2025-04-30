package main.core.characters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import main.core.Engine;
import main.core.Repr;
import main.core.characters.Experience;
import main.core.characters.Personality;
import main.core.demographics.Demographics;
import main.core.politics.Issue;
import main.core.politics.Position;

public class PoliticalActor extends Character implements Repr, HasPersonality {
    
    protected static final int MIN_AGE = 20;
    protected static final int MAX_AGE = 120;

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
    public PoliticalActor(Name name, Demographics demographics) {
        super(name, demographics);
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
    public Personality getPersonality() {
        return personality;
    }
    public void setPersonality(Personality personality) {
        this.personality = personality;
    }

    public void fromRepr(String repr){

    }
    public String toRepr(){
        String superRepr = super.toRepr();
        String[] splitSuperRepr = superRepr.split(":\\[");
        superRepr = "";
        for (int i = 1; i < splitSuperRepr.length; i++) {
            superRepr += splitSuperRepr[i] + ":[";
        }
        superRepr = superRepr.substring(0, superRepr.length() - 4);
        String[] experiencesStrings = new String[experiences.size()];
        for (int i = 0; i < experiences.size(); i++) {
            experiencesStrings[i] = experiences.get(i).toRepr();
        }
        String experiencesRepr = Engine.arrayToReprList(experiencesStrings);
        String[] positionsStrings = new String[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            positionsStrings[i] = experiences.get(i).toRepr();
        }
        String positionsRepr = Engine.arrayToReprList(positionsStrings);
        String repr = String.format("%s:[%scash=%d;education=%d;alignments=(%d,%d);experiences=[%s];legislativeSkill=%d;executiveSkill=%d;judicialSkill=%d;aptitude=%d;positions=[%s];conviction=%d;ageMod=%f;personality=%s;];",
            this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1],
            superRepr,
            cash,
            education,
            alignments[0], alignments[1],
            experiencesRepr,
            legislativeSkill,
            executiveSkill,
            judicialSkill,
            aptitude,
            positionsRepr,
            conviction,
            ageMod,
            personality.toRepr()
        );
        return repr;
    }
}
