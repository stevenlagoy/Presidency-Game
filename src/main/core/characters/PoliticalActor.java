package main.core.characters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import core.JSONObject;
import main.core.Engine;
import main.core.demographics.Demographics;
import main.core.map.City;
import main.core.politics.Issue;
import main.core.politics.Position;

public class PoliticalActor extends Character implements HasPersonality {
    
    protected static final int MIN_AGE = 20;
    protected static final int MAX_AGE = 120;

    private int cash;
    private int education;
    private int[] alignments;
    private List<Experience> experiences;
    private Skills skills;
    private List<Position> positions;
    private int conviction;
    private Personality personality;

    public PoliticalActor() {
        this(new Character());
    }
    
    public PoliticalActor(String buildstring){
        super(buildstring);
    }

    public PoliticalActor(Character character) {
        super(character);
        this.cash = 0;
        this.education = 0;
        this.alignments = new int[] {0, 0};
        this.experiences = new ArrayList<Experience>();
        this.skills = new Skills();
        this.positions = new ArrayList<Position>();
        this.conviction = 100;
        this.personality = new Personality();
    }

    public PoliticalActor(Character character, int cash, int education, int[] alignments, List<Experience> experiences, Skills skills, List<Position> positions, int conviction, Personality personality) {
        super(character);
        this.cash = cash;
        this.education = education;
        if (alignments.length != 2)
            throw new IllegalArgumentException("The alignments int array must have a length of 2.");
        this.alignments = alignments;
        this.conviction = conviction;
        this.experiences = experiences != null ? experiences : new ArrayList<Experience>();
        this.skills      = skills      != null ? skills      : new Skills();
        this.positions   = positions   != null ? positions   : new ArrayList<Position>();
        this.personality = personality != null ? personality : new Personality();
    }

    public PoliticalActor(Demographics demographics, Name name, City birthplaceCity, City currentLocationCity, City residenceCity, Date birthday, CharacterModel appearance, int cash, int education, int[] alignments, List<Experience> experiences, Skills skills, List<Position> positions, int conviction, Personality personality) {
        this(new Character(demographics, name, birthplaceCity, currentLocationCity, residenceCity, birthday, appearance));
    }

    public int getCash() {
        return this.cash;
    }
    public void setCash(int cash){
        this.cash = cash;
    }
    public void addCash(int cash){
        this.cash += cash;
    }

    public void setEducation(int education){
        this.education = education;
    }
    public int getEducation() {
        return this.education;
    }
    
    public Position getPositionOnIssue(Issue issue){
        for(Position position : positions){
            if(position.getRootIssue().equals(issue)) return position;
        }
        Engine.log("INVALID ISSUE NAME", String.format("An invalid issue name, \"%s\", was supplied. Unable to determine position on non-existant issue.", issue), new Exception());
        return null;
    }
    public List<Position> getPositions() {
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
    
    protected void evalConviction() {
    }
    public int getConviction() {
        return this.conviction;
    }

    public int[] getAlignments() {
        return this.alignments;
    }

    public List<Experience> getExperience() {
        return this.experiences;
    }

    protected float getAgeMod() {
        float ageMod;
        int age = this.getAge();
        ageMod = (float) (100 * Math.pow(Math.E, -1 * Math.pow(((age - 55) / 30.0f), 2)));
        return ageMod;
    }

    public void determinePersonality() {

    }
    
    public Personality getPersonality() {
        return personality;
    }
    public void setPersonality(Personality personality) {
        this.personality = personality;
    }

    public PoliticalActor fromJson(JSONObject json) {
        return this;
    }

    @Override
    public PoliticalActor fromRepr(String repr){
        return this;
    }

    @Override
    public String toRepr() {
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
        String repr = String.format("%s:[%scash=%d;education=%d;alignments=(%d,%d);experiences=[%s];skills:%s;positions=[%s];conviction=%d;ageMod=%f;personality=%s;];",
            this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1],
            superRepr,
            cash,
            education,
            alignments[0], alignments[1],
            experiencesRepr,
            skills.toRepr(),
            positionsRepr,
            conviction,
            personality.toRepr()
        );
        return repr;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        PoliticalActor other = (PoliticalActor) obj;
        return this.toString().equals(other.toString());
    }

    @Override
    public String toString() {
        return this.toRepr();
    }
}
