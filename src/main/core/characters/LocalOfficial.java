package main.core.characters;

import main.core.map.Municipality;

public class LocalOfficial extends PoliticalActor {

    private Municipality municipality;

    public LocalOfficial(){
        super();
    }
    public LocalOfficial(String buildstring){
        super(buildstring);
    }
    public LocalOfficial(Role role){
        super();
    }

    public Municipality getMunicipality() {
        return municipality;
    }
    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }
}
