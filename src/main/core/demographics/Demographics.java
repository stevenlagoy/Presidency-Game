package main.core.demographics;

import java.util.ArrayList;
import java.util.List;

import core.JSONObject;
import main.core.Jsonic;
import main.core.Logger;
import main.core.Main;
import main.core.Repr;
import main.core.demographics.DemographicsManager.DemographicCategory;

public class Demographics implements Repr<Demographics>, Jsonic<Demographics> {
    
    private Bloc generation;
    private Bloc religion;
    private Bloc raceEthnicity;
    private Bloc presentation;

    public Demographics() {
        this.generation = null;
        this.religion = null;
        this.raceEthnicity = null;
        this.presentation = null;
    }

    public Demographics(Demographics other) {
        this.generation = other.getGeneration();
        this.religion = other.getReligion();
        this.raceEthnicity = other.getRaceEthnicity();
        this.presentation = other.getPresentation();
    }

    public Demographics(JSONObject demographicsJson) {
        fromJson(demographicsJson);
    }

    public Demographics(String generation, String religion, String raceEthnicity, String presentation){
        this.generation = Main.Engine().DemographicsManager().matchBlocName(generation);
        if (!this.generation.getDemographicGroup().equals(DemographicCategory.GENERATION)) {
            Logger.log("INVALID BLOC GROUP", String.format("The bloc \"%s\" of type %s was assigned to a demographic category of type %s.", generation, this.generation.getDemographicGroup(), "Generation"), new Exception());
            this.generation = null;
        }
        this.religion = Main.Engine().DemographicsManager().matchBlocName(religion);
        if (!this.religion.getDemographicGroup().equals(DemographicCategory.RELIGION)) {
            Logger.log("INVALID BLOC GROUP", String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", religion, this.religion.getDemographicGroup(), "Religion"), new Exception());
            this.religion = null;
        }
        this.raceEthnicity = Main.Engine().DemographicsManager().matchBlocName(raceEthnicity);
        if (!this.raceEthnicity.getDemographicGroup().equals(DemographicCategory.RACE_ETHNICITY)) {
            Logger.log("INVALID BLOC GROUP", String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", raceEthnicity, this.raceEthnicity.getDemographicGroup(), "Race / Ethnicity"), new Exception());
            this.raceEthnicity = null;
        }
        this.presentation = Main.Engine().DemographicsManager().matchBlocName(presentation);
        if (!this.presentation.getDemographicGroup().equals(DemographicCategory.PRESENTATION)) {
            Logger.log("INVALID BLOC GROUP", String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", presentation, this.presentation.getDemographicGroup(), "Presentation"), new Exception());
            this.presentation = null;
        }
    }

    public Demographics(Bloc generation, Bloc religion, Bloc raceEthnicity, Bloc presentation){
        this.generation = generation;
        if (!this.generation.getDemographicGroup().equals(DemographicCategory.GENERATION)) {
            Logger.log("INVALID BLOC GROUP", String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", generation, this.generation.getDemographicGroup(), "Generation"), new Exception());
            this.generation = null;
        }
        this.religion = religion;
        if (!this.religion.getDemographicGroup().equals(DemographicCategory.RELIGION)) {
            Logger.log("INVALID BLOC GROUP", String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", religion, this.religion.getDemographicGroup(), "Religion"), new Exception());
            this.religion = null;
        }
        this.raceEthnicity = raceEthnicity;
        if (!this.raceEthnicity.getDemographicGroup().equals(DemographicCategory.RACE_ETHNICITY)) {
            Logger.log("INVALID BLOC GROUP", String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", raceEthnicity, this.raceEthnicity.getDemographicGroup(), "Race / Ethnicity"), new Exception());
            this.raceEthnicity = null;
        }
        this.presentation = presentation;
        if (!this.presentation.getDemographicGroup().equals(DemographicCategory.PRESENTATION)) {
            Logger.log("INVALID BLOC GROUP", String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", presentation, this.presentation.getDemographicGroup(), "Presentation"), new Exception());
            this.presentation = null;
        }
    }

    public Demographics(String buildstring){
        this.fromRepr(buildstring);
    }

    public Bloc getGeneration(){
        return this.generation;   
    }
    public void setGeneration(Bloc generation){
        this.generation = generation;
        if (!this.generation.getDemographicGroup().equals(DemographicCategory.GENERATION)) {
            Logger.log("INVALID BLOC GROUP", String.format("The bloc \"%s\" of type %s was assigned to a demographic category of type %s.", generation, this.generation.getDemographicGroup(), "Generation"), new Exception());
            this.generation = null;
        }
    }
    public Bloc getReligion(){
        return this.religion;
    }
    public void setReligion(Bloc religion){
        this.religion = religion;
        if (!this.religion.getDemographicGroup().equals(DemographicCategory.RELIGION)) {
            Logger.log("INVALID BLOC GROUP", String.format("The bloc \"%s\" of type %s was assigned to a demographic category of type %s.", religion, this.religion.getDemographicGroup(), "Religion"), new Exception());
            this.religion = null;
        }
    }
    public Bloc getRaceEthnicity(){
        return this.raceEthnicity;
    }
    public void setRaceEthnicity(Bloc raceEthnicity){
        this.raceEthnicity = raceEthnicity;
        if (!this.raceEthnicity.getDemographicGroup().equals(DemographicCategory.RACE_ETHNICITY)) {
            Logger.log("INVALID BLOC GROUP", String.format("The bloc \"%s\" of type %s was assigned to a demographic category of type %s.", raceEthnicity, this.raceEthnicity.getDemographicGroup(), "Race / Ethnicity"), new Exception());
            this.raceEthnicity = null;
        }
    }
    public Bloc getPresentation(){
        return this.presentation;
    }
    public void setPresentation(Bloc presentation){
        this.presentation = presentation;
        if (!this.presentation.getDemographicGroup().equals(DemographicCategory.PRESENTATION)) {
            Logger.log("INVALID BLOC GROUP", String.format("The bloc \"%s\" of type %s was assigned to a demographic category of type %s.", presentation, this.presentation.getDemographicGroup(), "Presentation"), new Exception());
            this.presentation = null;
        }
    }

    public Bloc[] toBlocsArray() {
        return new Bloc[] {generation, religion, raceEthnicity, presentation};
    }

    public String toRepr(){
        String repr = String.format(
            "%s:[generation=\"%s\";religion=\"%s\";raceEthnicity=\"%s\";presentation=\"%s\";];",
            this.getClass().toString().replace("class ", ""),
            this.generation.getName(),
            this.religion.getName(),
            this.raceEthnicity.getName(),
            this.presentation.getName()
        );
        return repr;
    }
    public Demographics fromRepr(String repr){
        return this;
    }

    @Override
    public JSONObject toJson() {
        List<JSONObject> fields = new ArrayList<>();
        fields.add(new JSONObject("generation", this.generation.getName()));
        fields.add(new JSONObject("religion", this.religion.getName()));
        fields.add(new JSONObject("race_ethnicity", this.raceEthnicity.getName()));
        fields.add(new JSONObject("presentation", this.presentation.getName()));
        return new JSONObject("demographics", fields);
    }

    @Override
    public Demographics fromJson(JSONObject demographicsJson) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }
}
