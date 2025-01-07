public class Demographics implements Repr
{
    private Bloc generation;
    private Bloc religion;
    private Bloc raceEthnicity;
    private Bloc presentation;

    public Demographics(String generation, String religion, String raceEthnicity, String presentation){
        this.generation = Bloc.matchBlocName(generation);
        this.religion = Bloc.matchBlocName(religion);
        this.raceEthnicity = Bloc.matchBlocName(raceEthnicity);
        this.presentation = Bloc.matchBlocName(presentation);
    }

    public Demographics(Bloc generation, Bloc religion, Bloc raceEthnicity, Bloc presentation){
        this.generation = generation;
        this.religion = religion;
        this.raceEthnicity = raceEthnicity;
        this.presentation = presentation;
    }

    public Demographics(String buildstring){
        this.fromRepr(buildstring);
    }

    public Bloc getGeneration(){
        return this.generation;   
    }
    public void setGeneration(Bloc generation){
        this.generation = generation;
    }
    public Bloc getReligion(){
        return this.religion;
    }
    public void setReligion(Bloc religion){
        this.religion = religion;
    }
    public Bloc getRaceEthnicity(){
        return this.raceEthnicity;
    }
    public void setRaceEthnicity(Bloc raceEthnicity){
        this.raceEthnicity = raceEthnicity;
    }
    public Bloc getPresentation(){
        return this.presentation;
    }
    public void setPresentation(Bloc presentation){
        this.presentation = presentation;
    }


    public String toRepr(){
        String repr = "";
        return repr;
    }
    public void fromRepr(String repr){

    }
}
