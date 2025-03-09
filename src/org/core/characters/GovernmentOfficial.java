package org.core.characters;

import org.core.characters.PoliticalActor;

public class GovernmentOfficial extends PoliticalActor
{
    private String position;

    public GovernmentOfficial(){
        super();
    }
    public GovernmentOfficial(String buildstring){
        super(buildstring);
    }
    public GovernmentOfficial(String position, Object o){
        super();
        this.position = position;
    }

    public String getPosition(){
        return position;
    }
    public void setPosition(String position){
        this.position = position;
    }

}