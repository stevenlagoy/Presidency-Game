package main.core.characters;

import java.util.ArrayList;
import java.util.List;

import core.JSONObject;
import main.core.Engine;
import main.core.Main;
import main.core.characters.StateOfficial.StateRole;
import main.core.characters.attributes.Role;
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
            return Main.Engine().LanguageManager().getLocalization(this.name());
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

    // Jurisdiction : Map Entity

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

    @Override
    public JSONObject toJson() {
        List<JSONObject> fields = new ArrayList<>();

        List<String> rolesStrings = new ArrayList<>();
        for (LocalRole role : roles) {
            rolesStrings.add(role.getTitle());
        }
        fields.add(new JSONObject("local_roles", rolesStrings));
        if (jurisdiction != null)
            fields.add(new JSONObject("jurisdiction", jurisdiction.getName()));

        List<?> superFields = super.toJson().getAsList();
        for (Object obj : superFields) {
            if (obj instanceof JSONObject jsonObj) {
                fields.add(jsonObj);
            }
        }

        return new JSONObject(getName().getBiographicalName(), fields);
    }
}
