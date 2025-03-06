package src.org.core.characters;

import src.org.core.characters.GovernmentOfficial;

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
