/*
 * Candidate.java
 * Steven LaGoy
 * Created: August 28, 2024 at 5:42 PM
 * Modified: 29 May 2025
 */

package main.core.characters;

// IMPORTS ----------------------------------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

import core.JSONObject;
import main.core.Engine;
import main.core.NumberOperations;
import main.core.demographics.Demographics;

/**
 * A Character subclass which extends PoliticalActor and models a Candidate in the Presidential race.
 * <p>
 * Contains fields and methods particular to a Candidate's campaign details and information.
 * @see Character
 * @see PoliticalActor
 */
public class Candidate extends PoliticalActor {

    // STATIC VARIABLES ---------------------------------------------------------------------------

    /** Minimum age of a Candidate for the Presidency */
    public static final int MIN_AGE = 35;
    /** Maximum age of a Candidate */
    public static final int MAX_AGE = PoliticalActor.MAX_AGE;

    final static int SKILLS_BONUS_EDUCATION = 10;
    final static double EDUCATION_SCALER = 1.25;
    final static int EDUCATION_COST = 100;
    final static int MAX_EDUCATION_LEVEL = 8;
    final static int POINTS_PER_SKILL = 10;

    private int numDelegates;
    private double influence;

    public Candidate() {
        this(new PoliticalActor());
    }

    public Candidate(Candidate other) {

    }

    public Candidate(Candidate other, boolean addToCharacterList) {

    }

    public Candidate(Character character) {
        super(character);
    }

    public Candidate(PoliticalActor actor) {
        super(actor, false);
    }

    public Candidate(String buildstring) {

    }

    public Candidate(JSONObject json) {

    }

    public Candidate(Character character, String fields) {

    }

    public Candidate(PoliticalActor actor, String fields) {

    }

    public Candidate(String fields, int field) {

    }

    protected void generateProfile(){
        int totalPoints = 0;
        int numCategories = 4;
        switch(Engine.getGameDifficulty()){
            case LEVEL_1 :
                totalPoints = NumberOperations.randInt(400, 500);
                break;
            case LEVEL_2 :
                totalPoints = NumberOperations.randInt(400, 500);
                break;
            case LEVEL_3 :
                totalPoints = NumberOperations.randInt(400, 500);
                break;
            case LEVEL_4 : 
                totalPoints = NumberOperations.randInt(400, 500);
                break;
            case LEVEL_5 :
                totalPoints = NumberOperations.randInt(400, 500);
                break;
        }
        int allocations[] = new int[numCategories];
        int allocatedPoints = 0;
        int i = 0;
        while(allocatedPoints < totalPoints){
            int value = NumberOperations.randInt((int) Math.round((totalPoints - allocatedPoints)*1.0 / numCategories));
            allocations[i++] += value;
            allocatedPoints += value;
            i %= numCategories;
        }
        // adjust point allocations based on age, expected apportionment, etc
        int res;
        res = generateEducation(allocations[0]);
        allocations[1] += res;
        allocations[1] += Math.pow(this.getEducation().value * SKILLS_BONUS_EDUCATION, EDUCATION_SCALER);
        res = generateSkills(allocations[1]);
        allocations[2] += res;
        res = generateTraits(allocations[2]);
        allocations[3] += res;
        res = generateExperience(allocations[3]);
        res = generateFamily(res + 100);
    }
    protected int generateEducation(int points){
        // Give the maximum possible education with the provided points
        int educationLevel = points / EDUCATION_COST;
        if(educationLevel > MAX_EDUCATION_LEVEL) educationLevel = MAX_EDUCATION_LEVEL;
        this.setEducation(Education.level(points / EDUCATION_COST));
        if(this.getEducation().value > 4) this.selectEducationalInstitution();
        int refund = points - this.getEducation().value * EDUCATION_COST;

        // select a specific education type?

        return refund;
    }
    protected void selectEducationalInstitution(){
        //LocationManager.selectEducationalInstitution(this.getBirthplaceCity());
    }
    protected int generateSkills(int points){
        // Evenly distribute 40% to 60% of points across all skills categories.
        int totalPoints = points, refund = 0;
        int[] pointsAllotments = {0, 0, 0};

        int evenApportionment = Math.round(NumberOperations.randPercent(0.4f, 0.8f) * points);
        points -= evenApportionment;
        int selection = NumberOperations.randInt(2);
        pointsAllotments[selection] += Math.round(NumberOperations.randPercent(0.5f, 0.9f) * points);
        points -= pointsAllotments[selection];
        selection = (selection + NumberOperations.randInt(1)) % 3;
        pointsAllotments[selection] += Math.round(1.0 * points);
        points -= pointsAllotments[selection];

        for(int i = 0; i < 3; i++) pointsAllotments[i] += evenApportionment;

        this.skills.setBaseLegislativeSkill(pointsAllotments[0] / POINTS_PER_SKILL);
        this.skills.setBaseExecutiveSkill(pointsAllotments[1] / POINTS_PER_SKILL);
        this.skills.setBaseJudicialSkill(pointsAllotments[2] / POINTS_PER_SKILL);

        refund = totalPoints - points;
        return refund;
    }
    protected int generateTraits(int points){
        // Select traits with 25% to 75% of the points. Use the remaining points to upgrade traits.
        int pointsForSelection, pointsForUpgrade, refund = 0;
        pointsForSelection = (int) Math.floor(NumberOperations.randPercent(0.25f, 0.75f) * points);
        pointsForUpgrade = points - pointsForSelection;

        while (pointsForUpgrade > 0) {
            
        }

        return refund;
    }
    protected int generateExperience(int points){
        int refund = 0;
        return refund;
    }
    protected int generateFamily(int points){
        int refund = 0;
        return refund;
    }
    
    protected void generatePolitics(){

    }

    protected void generateAppearance(){
        CharacterManager.generateAppearance(this);
    }

    protected void genPresentation(){
        this.getDemographics().setPresentation(CharacterManager.generatePresentation(this.getDemographics()));
    }

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

    public Personality determinePersonality(){
        return null;
    }

    public String toRepr(){
        String superRepr = super.toRepr();
        String[] splitSuperRepr = superRepr.split(":\\[");
        superRepr = "";
        for (int i = 1; i < splitSuperRepr.length; i++) {
            superRepr += splitSuperRepr[i] + ":[";
        }
        superRepr = superRepr.substring(0, superRepr.length() - 5);
        String repr = String.format("%s:[%snumDelegates=%d;influence=%s;];",
            this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1],
            superRepr,
            numDelegates,
            influence
        );
        return repr;
    }
    public Candidate fromRepr(String repr){
        return null;
    }
}
