package main.core.characters;

import main.core.characters.GovernmentOfficial;

public class LocalOfficial extends GovernmentOfficial {
    public LocalOfficial(){
        super("Local Official", null);
    }
    public LocalOfficial(String buildstring){
        super(buildstring);
    }
    public LocalOfficial(String position, Object o){
        super(position, null);
    }
}
