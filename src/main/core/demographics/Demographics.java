package main.core.demographics;

import main.core.Engine;
import main.core.Repr;

public class Demographics implements Repr
{
    private Bloc generation;
    private Bloc religion;
    private Bloc raceEthnicity;
    private Bloc presentation;

    public Demographics(String generation, String religion, String raceEthnicity, String presentation){
        this.generation = Bloc.matchBlocName(generation);
        if (!this.generation.getDemographicGroup().equals("Generation")) {
            Engine.log("INVALID BLOC GROUP", String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", generation, this.generation.getDemographicGroup(), "Generation"), new Exception());
            this.generation = null;
        }
        this.religion = Bloc.matchBlocName(religion);
        if (!this.religion.getDemographicGroup().equals("Religion")) {
            Engine.log("INVALID BLOC GROUP", String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", religion, this.religion.getDemographicGroup(), "Religion"), new Exception());
            this.religion = null;
        }
        this.raceEthnicity = Bloc.matchBlocName(raceEthnicity);
        if (!this.raceEthnicity.getDemographicGroup().equals("Race / Ethnicity")) {
            Engine.log("INVALID BLOC GROUP", String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", raceEthnicity, this.raceEthnicity.getDemographicGroup(), "Race / Ethnicity"), new Exception());
            this.raceEthnicity = null;
        }
        this.presentation = Bloc.matchBlocName(presentation);
        if (!this.presentation.getDemographicGroup().equals("Presentation")) {
            Engine.log("INVALID BLOC GROUP", String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", presentation, this.presentation.getDemographicGroup(), "Presentation"), new Exception());
            this.presentation = null;
        }
    }

    public Demographics(Bloc generation, Bloc religion, Bloc raceEthnicity, Bloc presentation){
        this.generation = generation;
        if (!this.generation.getDemographicGroup().equals("Generation")) {
            Engine.log("INVALID BLOC GROUP", String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", generation, this.generation.getDemographicGroup(), "Generation"), new Exception());
            this.generation = null;
        }
        this.religion = religion;
        if (!this.religion.getDemographicGroup().equals("Religion")) {
            Engine.log("INVALID BLOC GROUP", String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", religion, this.religion.getDemographicGroup(), "Religion"), new Exception());
            this.religion = null;
        }
        this.raceEthnicity = raceEthnicity;
        if (!this.raceEthnicity.getDemographicGroup().equals("Race / Ethnicity")) {
            Engine.log("INVALID BLOC GROUP", String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", raceEthnicity, this.raceEthnicity.getDemographicGroup(), "Race / Ethnicity"), new Exception());
            this.raceEthnicity = null;
        }
        this.presentation = presentation;
        if (!this.presentation.getDemographicGroup().equals("Presentation")) {
            Engine.log("INVALID BLOC GROUP", String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", presentation, this.presentation.getDemographicGroup(), "Presentation"), new Exception());
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
        if (!this.generation.getDemographicGroup().equals("Generation")) {
            Engine.log("INVALID BLOC GROUP", String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", generation, this.generation.getDemographicGroup(), "Generation"), new Exception());
            this.generation = null;
        }
    }
    public Bloc getReligion(){
        return this.religion;
    }
    public void setReligion(Bloc religion){
        this.religion = religion;
        if (!this.religion.getDemographicGroup().equals("Religion")) {
            Engine.log("INVALID BLOC GROUP", String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", religion, this.religion.getDemographicGroup(), "Religion"), new Exception());
            this.religion = null;
        }
    }
    public Bloc getRaceEthnicity(){
        return this.raceEthnicity;
    }
    public void setRaceEthnicity(Bloc raceEthnicity){
        this.raceEthnicity = raceEthnicity;
        if (!this.raceEthnicity.getDemographicGroup().equals("Race / Ethnicity")) {
            Engine.log("INVALID BLOC GROUP", String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", raceEthnicity, this.raceEthnicity.getDemographicGroup(), "Race / Ethnicity"), new Exception());
            this.raceEthnicity = null;
        }
    }
    public Bloc getPresentation(){
        return this.presentation;
    }
    public void setPresentation(Bloc presentation){
        this.presentation = presentation;
        if (!this.presentation.getDemographicGroup().equals("Presentation")) {
            Engine.log("INVALID BLOC GROUP", String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", presentation, this.presentation.getDemographicGroup(), "Presentation"), new Exception());
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
    public void fromRepr(String repr){

    }
}
