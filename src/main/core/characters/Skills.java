package main.core.characters;

import core.JSONObject;
import main.core.Repr;

public class Skills implements Repr<Skills> {
    
    private int legislativeSkill;
    private int executiveSkill;
    private int judicialSkill;
    private int aptitude;

    public Skills() {
        this(50, 50, 50, 0);
        this.aptitude = calculateAptitude();
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

    public Skills(int legislativeSkill, int executiveSkill, int judicialSkill) {
        this(legislativeSkill, executiveSkill, judicialSkill, 0);
        this.aptitude = calculateAptitude();
    }

    public Skills(int legislativeSkill, int executiveSkill, int judicialSkill, int aptitude) {
        this.legislativeSkill = legislativeSkill;
        this.executiveSkill = executiveSkill;
        this.judicialSkill = judicialSkill;
        this.aptitude = aptitude;
    }

    private int calculateAptitude() {
        return legislativeSkill + executiveSkill + judicialSkill;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        Skills other = (Skills) obj;
        return this.toString().equals(other.toString());
    }

    @Override
    public Skills fromRepr(String repr) {
        return this;
    }

    public Skills fromJson(JSONObject json) {
        if (json == null)
            return null;
        Object legislativeObj = json.get("legislative");
        if (legislativeObj == null)
            this.legislativeSkill = 50;
        else if (legislativeObj instanceof JSONObject legislativeJson)
            this.legislativeSkill = legislativeJson.getAsNumber().intValue();
        Object executiveObj = json.get("executive");
        if (executiveObj == null)
            this.executiveSkill = 50;
        else if (executiveObj instanceof JSONObject executiveJson)
            this.executiveSkill = executiveJson.getAsNumber().intValue();
        Object judicialObj = json.get("judicial");
        if (judicialObj == null)
            this.judicialSkill = 50;
        else if (judicialObj instanceof JSONObject judicialJson)
            this.judicialSkill = judicialJson.getAsNumber().intValue();
        Object aptitudeObj = json.get("aptitude");
        if (aptitudeObj == null)
            this.aptitude = 50;
        else if (aptitudeObj instanceof JSONObject aptitudeJson)
            this.aptitude = aptitudeJson.getAsNumber().intValue();

        return this;
    }

    @Override
    public String toString() {
        return this.toRepr();
    }

    @Override
    public String toRepr() {
        String repr = String.format(
            "%s:[legislativeSkill:%d;executiveSkill:%d;judicialSkill:%d;aptitude:%d;];",
            this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1],
            this.legislativeSkill,
            this.executiveSkill,
            this.judicialSkill,
            this.aptitude
        );
        return repr;
    }

}
