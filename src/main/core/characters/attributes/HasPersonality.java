package main.core.characters.attributes;
public interface HasPersonality {

    public Personality getPersonality();
    public void setPersonality(Personality personality);

    /**
     * Evaluate the given action in alignment with this Character's personality.
     * @param action
     * @return A float between +1 and -1 which represents the liklihood of this Character to take the given action.
     */
    public float evaluateAction();

    public Personality determinePersonality();
}
