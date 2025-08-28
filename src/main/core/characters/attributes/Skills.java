/*
 * Skills.java
 * Steven LaGoy
 * Created: 28 May 2025 at 10:18 PM
 * Modified: 29 May 2025
 */

package main.core.characters.attributes;

import java.util.ArrayList;
import java.util.List;

// IMPORTS ----------------------------------------------------------------------------------------

import core.JSONObject;
import main.core.Jsonic;
import main.core.Repr;
import main.core.characters.PoliticalActor;

/**
 * Tracks the base and modified Legislative, Executive, and Judicial skills of a PoliticalActor (or subclass), as well as their Aptitude.
 * @see PoliticalActor#skills
 */
public class Skills implements Repr<Skills>, Jsonic<Skills> {
    
    // INSTANCE VARIABLES -------------------------------------------------------------------------

    /** Base Legislative Skill, representing ability to plan effectively, leverage advantages, and make lasting decisions. */
    private int baseLegislativeSkill;
    /** Legislative Skill, after modification by additive and multiplicative modifiers. */
    private int legislativeSkill;
    /** Additive modifier to the Legislative Skill. A +1 modifier results in a skill equal to baseSkill + 1. Additive multipliers are not affected by Multiplicative modifiers. */
    private int legAdd;
    /** Multiplicative modifier to the Legislative Skill. A 200% (2.0) modifier results in a skill equal to baseSkill * 2.0. Multiplicative modifiers are not affected by Additive modifiers. */
    private float legMult;
    /** Base Judicial Skill, representing ability to make decisions quickly, apply charismatic persuasion, and execute strong or decisive actions. */
    private int baseExecutiveSkill;
    /** Executive Skill, after modification by additive and multiplicative modifiers. */
    private int executiveSkill;
    /** Additive modifier to the Executive Skill. A +1 modifier results in a skill equal to baseSkill + 1. Additive multipliers are not affected by Multiplicative modifiers. */
    private int execAdd;
    /** Multiplicative modifier to the Executive Skill. A 200% (2.0) modifier results in a skill equal to baseSkill * 2.0. Multiplicative modifiers are not affected by Additive modifiers. */
    private float execMult;
    /** Base Legislative Skill, representing ability to inspect facts, reason through problems, and make informed decisions. */
    private int baseJudicialSkill;
    /** Judicial Skill, after modification by additive and multiplicative modifiers. */
    private int judicialSkill;
    /** Additive modifier to the Judicial Skill. A +1 modifier results in a skill equal to baseSkill + 1. Additive multipliers are not affected by Multiplicative modifiers. */
    private int judAdd;
    /** Multiplicative modifier to the Judicial Skill. A 200% (2.0) modifier results in a skill equal to baseSkill * 2.0. Multiplicative modifiers are not affected by Additive modifiers. */
    private float judMult;
    /** Aptitude, representing the sum of the three base skills. */
    private int aptitude;

    // CONSTRUCTORS -------------------------------------------------------------------------------

    public Skills() {
        this(50, 50, 50, 0);
        this.aptitude = calculateAptitude();
        calculateModifiedSkills();
    }

    public Skills(Skills other) {
        this.baseLegislativeSkill = other.baseLegislativeSkill;
        this.legislativeSkill = other.legislativeSkill;
        this.baseExecutiveSkill = other.baseExecutiveSkill;
        this.executiveSkill = other.executiveSkill;
        this.baseJudicialSkill = other.baseJudicialSkill;
        this.judicialSkill = other.judicialSkill;
        this.aptitude = other.aptitude;
    }

    public Skills(String buildstring) {
        if (buildstring == null || buildstring.isBlank()) {
            throw new IllegalArgumentException("The given buildstring was null, and a Skills object could not be created.");
        }
        fromRepr(buildstring);
    }

    public Skills(JSONObject json) {
        if (json == null) {
            throw new IllegalArgumentException("The passed JSON Object was null, and a Skills object could not be created.");
        }
        fromJson(json);
    }

    public Skills(int baseLegislativeSkill, int baseExecutiveSkill, int baseJudicialSkill) {
        this(baseLegislativeSkill, baseExecutiveSkill, baseJudicialSkill, 0);
        this.aptitude = calculateAptitude();
        calculateModifiedSkills();
    }

    public Skills(int legislativeSkill, int executiveSkill, int judicialSkill, int aptitude) {
        this.legislativeSkill = legislativeSkill;
        this.executiveSkill = executiveSkill;
        this.judicialSkill = judicialSkill;
        this.aptitude = aptitude;
        calculateBaseSkills();
    }

    public Skills(int baseLegislativeSkill, int legislativeSkill, int baseExecutiveSkill, int executiveSkill, int baseJudicialSkill, int judicialSkill) {
        this(baseLegislativeSkill, baseExecutiveSkill, baseJudicialSkill);
        this.legislativeSkill = legislativeSkill;
        this.executiveSkill = executiveSkill;
        this.judicialSkill = judicialSkill;
        this.aptitude = calculateAptitude();
        calculateModifiedSkills();
    }

    // GETTERS AND SETTERS ------------------------------------------------------------------------

    // Base Legislative Skill
    public int getBaseLegislativeSkill() {
        return baseLegislativeSkill;
    }
    public void setBaseLegislativeSkill(int skill) {
        this.baseLegislativeSkill = Math.clamp(skill, 0, 100);
        calculateAptitude();
    }
    public void addBaseLegislativeSkill(int skill) {
        this.baseLegislativeSkill = Math.clamp(baseLegislativeSkill + skill, 0, 100);
        calculateAptitude();
    }
    public void multiplyBaseLegislativeSkill(float factor) {
        this.baseLegislativeSkill = Math.clamp((long) (baseLegislativeSkill * factor), 0, 100);
    }

    // Legislative Skill
    public int getLegislativeSkill() {
        return legislativeSkill;
    }
    public void addLegislativeSkill(int skill) {
        this.legAdd += skill;
        calculateLegislativeSkill();
    }
    public void multiplyLegislativeSkill(float factor) {
        this.legMult += factor;
        calculateLegislativeSkill();
    }

    // Base Executive Skill
    public int getBaseExecutiveSkill() {
        return baseExecutiveSkill;
    }
    public void setBaseExecutiveSkill(int skill) {
        this.baseExecutiveSkill = Math.clamp(skill, 0, 100);
        calculateAptitude();
    }
    public void addBaseExecutiveSkill(int skill) {
        this.baseExecutiveSkill = Math.clamp(baseExecutiveSkill + skill, 0, 100);
        calculateAptitude();
    }

    // Executive Skill
    public int getExecutiveSkill() {
        return executiveSkill;
    }
    public void addExecutiveSkill(int skill) {
        this.execAdd += skill;
        calculateExecutiveSkill();
    }
    public void multiplyExecutiveSkill(float factor) {
        this.execMult += factor;
        calculateExecutiveSkill();
    }

    // Base Judicial Skill
    public int getBaseJudicialSkill() {
        return baseJudicialSkill;
    }
    public void setBaseJudicialSkill(int skill) {
        this.baseJudicialSkill = Math.clamp(skill, 0, 100);
        calculateAptitude();
    }
    public void addBaseJudicialSkill(int skill) {
        this.baseJudicialSkill = Math.clamp(baseJudicialSkill + skill, 0, 100);
        calculateAptitude();
    }

    // Judicial Skill
    public int getJudicialSkill() {
        return judicialSkill;
    }
    public void addJudicialSkill(int skill) {
        this.judAdd += skill;
        calculateJudicialSkill();
    }
    public void multiplyJudicialSkill(float factor) {
        this.judMult += factor;
        calculateJudicialSkill();
    }

    // Aptitude
    public int getAptitude() {
        return aptitude;
    }

    // CALCULATION FUNCTIONS ----------------------------------------------------------------------

    private int calculateAptitude() {
        this.aptitude = baseLegislativeSkill + baseExecutiveSkill + baseJudicialSkill;
        return this.aptitude;
    }
    
    private void calculateBaseSkills() {
        if (aptitude < 0) {
            throw new IllegalStateException("Aptitude cannot be negative");
        }

        // Handle special case where all skills are 0
        if (legislativeSkill == 0 && executiveSkill == 0 && judicialSkill == 0) {
            baseLegislativeSkill = aptitude / 3;
            baseExecutiveSkill = aptitude / 3;
            baseJudicialSkill = aptitude - (2 * (aptitude / 3));
            return;
        }

        // Calculate initial ratios
        double total = legislativeSkill + executiveSkill + judicialSkill;
        double lr = legislativeSkill / total;
        double er = executiveSkill / total;
        double jr = judicialSkill / total;

        // Initial distribution
        int l = (int) (aptitude * lr);
        int e = (int) (aptitude * er);
        int j = (int) (aptitude * jr);
        
        // Distribute remaining points based on largest difference from target ratio
        int remainder = aptitude - (l + e + j);
        while (remainder > 0) {
            double currentTotal = l + e + j;
            double lDiff = Math.abs(lr - (l / currentTotal));
            double eDiff = Math.abs(er - (e / currentTotal));
            double jDiff = Math.abs(jr - (j / currentTotal));
            
            if (lDiff >= eDiff && lDiff >= jDiff) {
                l++; remainder--;
            } else if (eDiff >= jDiff) {
                e++; remainder--;
            } else {
                j++; remainder--;
            }
        }

        baseLegislativeSkill = l;
        baseExecutiveSkill = e;
        baseJudicialSkill = j;

        legAdd = legislativeSkill - baseLegislativeSkill;
        execAdd = executiveSkill - baseExecutiveSkill;
        judAdd = judAdd - baseJudicialSkill;
    }

    private void calculateModifiedSkills() {
        calculateLegislativeSkill();
        calculateExecutiveSkill();
        calculateJudicialSkill();
    }
    private void calculateLegislativeSkill() {
        this.legislativeSkill = Math.clamp(Math.round(baseLegislativeSkill * legMult + legAdd), 0, 100);
    }
    private void calculateExecutiveSkill() {
        this.executiveSkill = Math.clamp(Math.round(baseExecutiveSkill * execMult + execAdd), 0, 100);
    }
    private void calculateJudicialSkill() {
        this.judicialSkill = Math.clamp(Math.round(baseJudicialSkill * judMult + judAdd), 0, 100);
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Skills fromRepr(String repr) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toRepr() {
        String repr = String.format(
            "%s:[baseLegislativeSkill:%d;baseExecutiveSkill:%d;baseJudicialSkill:%d;aptitude:%d;];",
            this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1],
            this.baseLegislativeSkill,
            this.baseExecutiveSkill,
            this.baseJudicialSkill,
            this.aptitude
        );
        return repr;
    }

    /**
     * {@inheritDoc}
     */
    public Skills fromJson(JSONObject json) {
        if (json == null)
            return null;
        Object baseLegislativeObj = json.get("base_legislative");
        if (baseLegislativeObj == null)
            this.baseLegislativeSkill = 50;
        else if (baseLegislativeObj instanceof JSONObject baseLegislativeJson)
            this.baseLegislativeSkill = baseLegislativeJson.getAsNumber().intValue();
        Object baseExecutiveObj = json.get("base_executive");
        if (baseExecutiveObj == null)
            this.baseExecutiveSkill = 50;
        else if (baseExecutiveObj instanceof JSONObject baseExecutiveJson)
            this.baseExecutiveSkill = baseExecutiveJson.getAsNumber().intValue();
        Object baseJudicialObj = json.get("base_judicial");
        if (baseJudicialObj == null)
            this.baseJudicialSkill = 50;
        else if (baseJudicialObj instanceof JSONObject baseJudicialJson)
            this.baseJudicialSkill = baseJudicialJson.getAsNumber().intValue();
        Object aptitudeObj = json.get("aptitude");
        if (aptitudeObj == null)
            this.aptitude = 50;
        else if (aptitudeObj instanceof JSONObject aptitudeJson)
            this.aptitude = aptitudeJson.getAsNumber().intValue();

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJson() {
        List<JSONObject> fields = new ArrayList<>();
        fields.add(new JSONObject("base_legislative_skill", baseLegislativeSkill));
        fields.add(new JSONObject("legislative_additive_modifier", legAdd));
        fields.add(new JSONObject("legislative_multiplicative_modifier", legMult));

        fields.add(new JSONObject("base_executive_skill", baseExecutiveSkill));
        fields.add(new JSONObject("executive_additive_modifier", execAdd));
        fields.add(new JSONObject("executive_multiplicative_modifier", execMult));

        fields.add(new JSONObject("base_judicial_skill", baseJudicialSkill));
        fields.add(new JSONObject("judicial_additive_modifier", judAdd));
        fields.add(new JSONObject("judicial_multiplicative_modifier", judMult));

        return new JSONObject("skills", fields);
    }

    /**
     * @see #toRepr()
     */
    @Override
    public String toString() {
        return this.toRepr();
    }

    // OBJECT METHODS -----------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        Skills other = (Skills) obj;
        return this.toString().equals(other.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 19;
        int hash = 9;
        hash = prime * hash + (legislativeSkill * 1);
        hash = prime * hash + (executiveSkill * 101);
        hash = prime * hash + (judicialSkill * 1009);
        hash = prime * hash + (aptitude * 10007);
        hash /= 1e5;
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Skills clone() {
        return new Skills(this);
    }

}
