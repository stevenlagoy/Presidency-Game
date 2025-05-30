package main.core.characters;
import java.util.ArrayList;
import java.util.List;

import main.core.Engine;
import main.core.demographics.Demographics;

public class Candidate extends PoliticalActor {

    public static final int MIN_AGE = 35;
    public static final int MAX_AGE = 120;

    final static int SKILLS_BONUS_EDUCATION = 10;
    final static double EDUCATION_SCALER = 1.25;
    final static int EDUCATION_COST = 100;
    final static int MAX_EDUCATION_LEVEL = 8;
    final static int POINTS_PER_SKILL = 10;

    public static List<Candidate> instances = new ArrayList<>();

    private int numDelegates;
    private double influence;

    public Candidate(){
        super();
        generateAll();
    }
    public Candidate(Name name, Demographics demographics) {
        super(name, demographics);
    }
    public Candidate(String buildstring)
    {
        super(buildstring);
        this.fromRepr(buildstring);
        this.generateProfile();
    }

    private void generateAll()
    {
        super.genAge(MIN_AGE);
        this.genPresentation();
        this.genOrigin();
        this.generateProfile();
        this.generatePolitics();
        this.generateAppearance();
        this.determinePersonality();
    }

    protected void generateProfile(){
        int totalPoints;
        int numCategories = 4;
        switch(Engine.gameDifficulty){
            case LEVEL_1 :
                totalPoints = Engine.randInt(400, 500);
                break;
            case LEVEL_2 :
                totalPoints = Engine.randInt(400, 500);
                break;
            case LEVEL_3 :
                totalPoints = Engine.randInt(400, 500);
                break;
            case LEVEL_4 : 
                totalPoints = Engine.randInt(400, 500);
                break;
            case LEVEL_5 :
                totalPoints = Engine.randInt(400, 500);
                break;
            default :
                Engine.log("INVALID DIFFICULTY", String.format("The difficulty value, \"%s\", is invalid.", Engine.gameDifficulty), new Exception());
                return;
        }
        int allocations[] = new int[numCategories];
        int allocatedPoints = 0;
        int i = 0;
        while(allocatedPoints < totalPoints){
            int value = Engine.randInt((int) Math.round((totalPoints - allocatedPoints)*1.0 / numCategories));
            allocations[i++] += value;
            allocatedPoints += value;
            i %= numCategories;
        }
        // adjust point allocations based on age, expected apportionment, etc
        int res;
        res = generateEducation(allocations[0]);
        allocations[1] += res;
        allocations[1] += Math.pow(this.getEducation() * SKILLS_BONUS_EDUCATION, EDUCATION_SCALER);
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
        this.setEducation(points / EDUCATION_COST);
        if(this.getEducation() > 4) this.selectEducationalInstitution();
        int refund = points - this.getEducation() * EDUCATION_COST;

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

        int evenApportionment = Math.round(Engine.randPercent(0.4f, 0.8f) * points);
        points -= evenApportionment;
        int selection = Engine.randInt(2);
        pointsAllotments[selection] += Math.round(Engine.randPercent(0.5f, 0.9f) * points);
        points -= pointsAllotments[selection];
        selection = (selection + Engine.randInt(1)) % 3;
        pointsAllotments[selection] += Math.round(1.0 * points);
        points -= pointsAllotments[selection];

        for(int i = 0; i < 3; i++) pointsAllotments[i] += evenApportionment;

        this.setLegislativeBaseSkill(pointsAllotments[0] / POINTS_PER_SKILL);
        this.setExecutiveBaseSkill(pointsAllotments[1] / POINTS_PER_SKILL);
        this.setJudicialBaseSkill(pointsAllotments[2] / POINTS_PER_SKILL);
        this.evalAptitude();

        refund = totalPoints - points;
        return refund;
    }
    protected int generateTraits(int points){
        // Select traits with 25% to 75% of the points. Use the remaining points to upgrade traits.
        int pointsForSelection, pointsForUpgrade, refund = 0;
        pointsForSelection = (int) Math.floor(Engine.randPercent(0.25f, 0.75f) * points);
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

    public void determinePersonality(){
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
    public void fromRepr(String repr){

    }
}
