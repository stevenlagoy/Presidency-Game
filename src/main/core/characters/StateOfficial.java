/*
 * StateOfficial.java
 * Steven LaGoy
 * Created: 11 October 2024 at 5:16 PM
 * Modified: 10 June 2025
 */

package main.core.characters;

import java.util.Set;

import core.JSONObject;
import main.core.Engine;
import main.core.map.MapEntity;
import main.core.map.State;

public class StateOfficial extends PoliticalActor {

    public static enum StateRole implements Role {
        GOVERNOR,
        LIEUTENANT_GOVERNOR,
        STATE_SENATOR,
        STATE_SENATE_SPEAKER,
        STATE_REPRESENTATIVE,
        STATE_SECRETARY,
        ELECTOR;

        @Override
        public String getTitle() {
            return Engine.getLocalization(this.name());
        }
    }

    // INSTANCE VARIABLES ------------------------------------------------------------------------- 

    /** The state with which this StateOfficial is associated. */
    private State state;

    private Set<StateRole> roles;

    private MapEntity jurisdiction;

    // CONSTRUCTORS -------------------------------------------------------------------------------

    public StateOfficial(){
        this(new PoliticalActor());
        this.state = null;
        CharacterManager.addCharacter(this);
    }

    public StateOfficial(StateOfficial other) {
        this(other, true);
    }

    public StateOfficial(StateOfficial other, boolean addToCharacterList) {
        super(other, false);
        this.state = other.state;
        if (addToCharacterList) CharacterManager.addCharacter(this);
    }

    public StateOfficial(Character character) {
        super(character, false);
        CharacterManager.addCharacter(this);
    }

    public StateOfficial(PoliticalActor politicalActor) {
        super(politicalActor, false);
        CharacterManager.addCharacter(this);
    }

    public StateOfficial(String buildstring){
        this(buildstring, true);
    }

    public StateOfficial(String buildstring, boolean addToCharacterList) {
        super(buildstring);
        fromRepr(buildstring);
        if (addToCharacterList) CharacterManager.addCharacter(this);
    }

    public StateOfficial(JSONObject json) {
        if (json == null) {
            throw new IllegalArgumentException("The passed JSONObject was null, and a " + getClass().getSimpleName() + " object could not be created.");
        }
        fromJson(json);
        CharacterManager.addCharacter(this);
    }

    public StateOfficial(State state){
        super();
        this.state = state;
    }


    // GETTERS AND SETTERS ------------------------------------------------------------------------

    public State getState(){
        return state;
    }
    public void setState(State state){
        this.state = state;
    }
    

    public boolean addRole(StateRole role) {
        return this.roles.add(role);
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
        StateOfficial other = (StateOfficial) obj;
        return this.toString().equals(other.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 37;
        int hash = super.hashCode();
        hash = prime * hash;
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StateOfficial clone() {
        return new StateOfficial(this);
    }
}
