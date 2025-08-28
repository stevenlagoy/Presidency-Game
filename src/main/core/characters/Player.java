package main.core.characters;

import java.util.ArrayList;
import java.util.List;

import core.JSONObject;

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

    @Override
    public JSONObject toJson() {
        List<JSONObject> fields = new ArrayList<>();

        fields.add(new JSONObject("player", true));

        List<?> superFields = super.toJson().getAsList();
        for (Object obj : superFields) {
            if (obj instanceof JSONObject jsonObj) {
                fields.add(jsonObj);
            }
        }

        return new JSONObject(getName().getBiographicalName(), fields);
    }

    @Override
    public boolean equals(Object other){
        if (!(other instanceof Player)) return false;
        Player playerOther = (Player) other;
        if(!super.equals(playerOther)) return false;
        return true;
    }
}
