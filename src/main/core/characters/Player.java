package main.core.characters;
public class Player extends Candidate
{
    
    public Player(){
        super();
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
