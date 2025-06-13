/*
 * FederalOfficial.java
 * Steven LaGoy
 * Created: 07 November 2024 at 11:11 PM
 * Modified: 10 June 2025
 */

package main.core.characters;

import java.util.HashSet;
import java.util.Set;

import core.JSONObject;
import main.core.Engine;
import main.core.characters.StateOfficial.StateRole;
import main.core.map.MapEntity;

public class FederalOfficial extends PoliticalActor {

    public static enum FederalRole implements Role {
        PRESIDENT,
        VICE_PRESIDENT,
        SENATOR,
        REPRESENTATIVE,
        HOUSE_SPEAKER,
        CABINET_SECRETARY,
        SUPREME_COURT_JUSTICE,
        FEDERAL_RESERVE_CHAIR;

        @Override
        public String getTitle() {
            return Engine.getLocalization(this.name());
        }
    }


    private Set<FederalRole> roles;
    
    private MapEntity jurisdiction;
    
    public FederalOfficial(){
        this(new PoliticalActor());
        CharacterManager.addCharacter(this);
    }
    
    public FederalOfficial(FederalOfficial other) {
        this(other, true);
    }

    public FederalOfficial(FederalOfficial other, boolean addToCharacterList) {
        super(other, false);

        if (addToCharacterList) CharacterManager.addCharacter(this);
    }

    public FederalOfficial(Character character) {
        super(character, false);
        CharacterManager.addCharacter(this);
    }

    public FederalOfficial(PoliticalActor politicalActor) {
        super(politicalActor, false);
        this.roles = new HashSet<>();
        CharacterManager.addCharacter(this);
    } 

    public FederalOfficial(String buildstring){
        this(buildstring, true);
    }

    public FederalOfficial(String buildstring, boolean addToCharacterList) {
        super(buildstring, false);
        fromRepr(buildstring);
        if (addToCharacterList) CharacterManager.addCharacter(this);
    }

    public FederalOfficial(JSONObject json) {
        if (json == null) {
            throw new IllegalArgumentException("The passed JSONObject was null, and a " + getClass().getSimpleName() + " object could not be created.");
        }
        fromJson(json);
        CharacterManager.addCharacter(this);
    }

    public boolean addRole(FederalRole role) {
        if (this.roles == null) this.roles = new HashSet<>();
        return this.roles.add(role);
    }
    public boolean removeRole(FederalRole role) {
        return this.roles.remove(role);
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    @Override
    public PoliticalActor fromRepr(String repr) {
        // TODO Auto-generated method stub
        return super.fromRepr(repr);
    }

    @Override
    public String toRepr() {
        String superRepr = super.toRepr();
        String repr = String.format("%s:[];",
            this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1]
        );
        return repr;
    }

    @Override
    public PoliticalActor fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        return super.fromJson(json);
    }

    @Override
    public JSONObject toJson() {
        // TODO Auto-generated method stub
        return super.toJson();
    }


    @Override
    public String toString() {
        return this.toRepr();
    }

    // OBJECT METHODS -----------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        FederalOfficial other = (FederalOfficial) obj;
        return this.toString().equals(other.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 61;
        int hash = super.hashCode();
        hash = prime * hash;
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FederalOfficial clone() {
        return new FederalOfficial(this);
    }
}
