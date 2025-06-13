package main.core.characters;

import java.util.ArrayList;
import java.util.List;

import main.core.Engine;
import main.core.characters.StateOfficial.StateRole;
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

    private List<LocalRole> roles;

    public LocalOfficial(){
        super();
        this.roles = new ArrayList<>();
    }
    public LocalOfficial(String buildstring){
        super(buildstring);
    }

    public MapEntity getJurisdiction() {
        return jurisdiction;
    }
    public void setJurisdiction(MapEntity jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    // Roles : List of StateRole

    public boolean addRole(LocalRole role) {
        return this.roles.add(role);
    }
}
