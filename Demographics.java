public class Demographics implements Repr
{
    public Bloc generation;
    public Bloc religion;
    public Bloc raceEthnicity;

    public Demographics(String generation, String religion, String raceEthnicity){
        this.generation = Bloc.matchBlocName(generation);
        this.religion = Bloc.matchBlocName(religion);
        this.raceEthnicity = Bloc.matchBlocName(raceEthnicity);
    }

    public Demographics(Bloc generation, Bloc religion, Bloc raceEthnicity){
        this.generation = generation;
        this.religion = religion;
        this.raceEthnicity = raceEthnicity;
    }

    public Demographics(String buildstring){
        this.fromRepr(buildstring);
    }

    public String toRepr(){
        String repr = "";
        return repr;
    }
    public void fromRepr(String repr){

    }
}
