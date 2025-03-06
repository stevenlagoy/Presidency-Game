package src.org.core.characters;
public class Player extends Candidate
{
    
    public Player(){
        super();
        genProfileInput();
    }

    private void genProfileInput(){
        this.setNameInput();
        this.setAgeInput();
        this.setPresentationInput();
        this.setOriginInput();
        this.setEducationInput();
        this.setAlignmentInput();
        this.genSkills();
        this.evalConviction();
    }

    public void setNameInput(){

    }
    public void setAgeInput(){

    }
    public void setPresentationInput(){

    }
    public void setOriginInput(){

    }
    public void setEducationInput(){

    }
    public void setAlignmentInput(){

    }

    public boolean equals(Player other){
        if(!super.equals(other)) return false;
        return true;
    }
}
