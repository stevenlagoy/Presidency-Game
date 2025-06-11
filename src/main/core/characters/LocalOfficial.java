package main.core.characters;

import main.core.Engine;
import main.core.map.MapEntity;
import main.core.map.Municipality;

public class LocalOfficial extends PoliticalActor {

    public static enum LocalRole implements Role {
        MAYOR,
        CITY_COUNCILOR,
        COUNTY_COMMISSIONER,
        MUNICIPAL_JUDGE;

        @Override
        public String getTitle() {
            return Engine.getLocalization(this.name());
        }
    }

    private MapEntity jurisdiction;

    public LocalOfficial(){
        super();
    }
    public LocalOfficial(String buildstring){
        super(buildstring);
    }
    public LocalOfficial(Role role){
        super();
    }

    public MapEntity getJurisdiction() {
        return jurisdiction;
    }
    public void setJurisdiction(MapEntity jurisdiction) {
        this.jurisdiction = jurisdiction;
    }
}
