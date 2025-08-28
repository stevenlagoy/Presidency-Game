/*
 * PoliticalActor.java
 * Steven LaGoy
 * Created: 11 October 2024 at 5:16 PM
 * Modified: 31 May 2025
 */

package main.core.characters;

 // IMPORTS ---------------------------------------------------------------------------------------

 // Standard Library Imports
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Internal Imports
import core.JSONObject;
import main.core.Engine;
import main.core.Logger;
import main.core.Main;
import main.core.Repr;
import main.core.characters.attributes.CharacterModel;
import main.core.characters.attributes.Experience;
import main.core.characters.attributes.HasPersonality;
import main.core.characters.attributes.Personality;
import main.core.characters.attributes.Role;
import main.core.characters.attributes.Skills;
import main.core.characters.attributes.names.Name;
import main.core.demographics.Demographics;
import main.core.map.Municipality;
import main.core.politics.Issue;
import main.core.politics.Position;

/**
 * A Character subclass which represents a Character who may interact with political systems, like leaders, lobbyists, or candidates.
 * <p>
 * Contains fields and methods which describe a PoliticalActor's alignments and positions.
 * @see Character
 * @see HasPersonality
 */
public class PoliticalActor extends Character implements HasPersonality {
    
    /** Roles which a PoliticalActor may hold. */
    public static enum PoliticalRole implements Role {
        PARTY_LEADER,
        LOBBYIST,
        CONSULTANT,
        CANDIDATE,
        CAMPAIGN_MANAGER,
        CAMPAIGN_CABINET_MEMBER,
        UNION_LEADER,
        MEDIA,
        NONE;

        @Override
        public String getTitle() {
            return Main.Engine().LanguageManager().getLocalization(this.name());
        }
    }

    // STATIC VARIABLES ---------------------------------------------------------------------------

    /** Minimum age of a Political Actor */
    public static final int MIN_AGE = 20;
    /** Maximum age of a Political Actor */
    public static final int MAX_AGE = Character.MAX_AGE;

    /** Enum for Education levels, based on ISCED 9-level framework. */
    public static enum Education {

        /** {@code 0} <p> Early Childhood Education: For children younger than three years through Preschool or Kindergarden. */
        EARLY_CHILDHOOD (0),
        /** {@code 1} <p> Primary Education: Core knowledge in Reading, Writing, and Mathematics. Elementary School (K/1 - 5/6). */
        PRIMARY         (1),
        /** {@code 2} <p> Lower Secondary Education: English, Science, Social Studies, and Algebra. Middle School (5/6 - 8/9) */
        LOWER_SECONDARY (2),
        /** {@code 3} <p> Upper Secondary Education: Language, Political Science, Higher Sciences, Higher Mathematics. High School (9 - 12)*/
        UPPER_SECONDARY (3),
        /** {@code 4} <p> Post-Secondary Non-Tertiary Education: Vocational or Technical Skills, Certifications. */
        POST_SECONDARY  (4),
        /** {@code 5} <p> Short-Cycle Tertiary Education: Any amount of a practically-based occupationally-specific concentrated education program. */
        SHORT_TERTIARY  (5),
        /** {@code 6} <p> Bachelor's Degree or Equivalent: Intermediate academic or professional knowledge, skills, and competencies. */
        BACHELORS       (6),
        /** {@code 7} <p> Master's Degree or Equivalent: Advanced academic or professional knowledge, skills, and competencies. */
        MASTERS         (7),
        /** {@code 8} <p> Doctorate or Equivalent: Advanced research qualification, usually with submission and defense of a substantive dissertation. */
        DOCTORAL        (8);

        public final int value;
        private Education(int value) { this.value = value; }
        public static Education level(int value) {
            for (Education edu : Education.values())
                if (edu.value == value)
                    return edu;
            throw new IllegalArgumentException("Invalid education level: " + value);
        }
        public static Education label(String label) {
            String target = label.trim().toUpperCase().replace("\\s", "_");
            for (Education edu : Education.values())
                if (edu.toString().equals(target))
                    return edu;
            throw new IllegalArgumentException("Invalid education label: " + label);
        }
    }

    // INSTANCE VARIABLES -------------------------------------------------------------------------

    /** The funds currently usable by this PoliticalActor. */
    private int cash;
    /** The highest education level attained by this PoliticalActor. */
    private Education education;
    /**
     * An int array of two values between -100 and +100, where alignments[0] gives authoritarian (+)
     * and liberatarian (-) leaning, and alignments[1] gives left (-) and right (+) leaning. Based on
     * the standard political compass model in Cartesian space.
     */
    private int[] alignments;
    /** A List of Experiences representing the experiences of this PoliticalActor. */
    private List<Experience> experiences;
    /** A Skills object representing the legislative, executive, and judicial skills (and aptitude) of this PoliticalActor. */
    protected Skills skills;
    /** A List of Positions on Issues held by this PoliticalActor. */
    private List<Position> positions;
    /** The overall conviction of this PoliticalActor. */
    private int conviction;
    /** The Personality object for this PoliticalActor's personality. */
    private Personality personality;
    /** The role of this PoliticalActor. */
    private Set<PoliticalRole> roles;

    // CONSTRUCTORS -------------------------------------------------------------------------------

    /**
     * Creates a PoliticalActor with all fields generated by the CharacterManager.
     * @see Character#Character()
     */
    public PoliticalActor() {
        this(new Character());
    }

    /**
     * Deep-copies the existing fields of another PoliticalActor object.
     * @param other PoliticalActor to copy fields from.
     */
    public PoliticalActor(PoliticalActor other) {
        this(other, true);
    }

    /**
     * Deep-copies the existing fields of another PoliticalActor object.
     * Optionally adds to CharacterManager's list of Characters.
     * @param other PoliticalActor to copy fields from.
     * @param addToCharacterList Boolean indicating whether to add the created object to the CharacterManager's list.
     */
    public PoliticalActor(PoliticalActor other, boolean addToCharacterList) {
        super(other, false);
        this.cash        = other.cash;
        this.education   = other.education;
        this.alignments  = Arrays.copyOf(other.alignments, 2);
        this.experiences = new ArrayList<Experience>();
        addAllExperiences(other.experiences);
        this.skills      = (Skills) other.skills.clone();
        this.positions   = new ArrayList<Position>();
        addAllPositions(other.positions);
        this.conviction  = other.conviction;
        this.personality = (Personality) other.personality.clone();
        this.roles       = new HashSet<PoliticalRole>() {{ add(PoliticalRole.NONE); }};

        if (addToCharacterList) CharacterManager.addCharacter(this);
    }

    /**
     * Creates a PoliticalActor by deep-copying the fields of the passed Character object,
     * with all other fields generated by the CharacterManager.
     * @param character Character to copy fields from.
     * @see Character#Character(Character)
     */
    public PoliticalActor(Character character) {
        this(character, true);
    }

    /**
     * Creates a PoliticalActor by deep-copying the fields of the passed Character object,
     * with all other fields generated by the CharacterManager.
     * @param character Character to copy fields from.
     * @param addToCharacterList Boolean indicating whether to add the created object to the CharacterManager's list.
     * @see Character#Character(Character)
     */
    public PoliticalActor(Character character, boolean addToCharacterList) {
        super(character, false);
        this.cash        = 0;
        this.education   = Education.POST_SECONDARY;
        this.alignments  = new int[] {0, 0};
        this.experiences = new ArrayList<Experience>();
        this.skills      = new Skills();
        this.positions   = new ArrayList<Position>();
        this.conviction  = 100;
        this.personality = new Personality();
        this.roles       = new HashSet<PoliticalRole>() {{ add(PoliticalRole.NONE); }};

        if (addToCharacterList) CharacterManager.addCharacter(this);
    }

    /**
     * Creates a PoliticalActor parsed from the Repr buildstring.
     * @param buildstring Valid Repr-format buildstring.
     * @see #PoliticalActor(String, boolean)
     */
    public PoliticalActor(String buildstring) {
        this(buildstring, true);
    }

    /**
     * Creates a PoliticalActor parsed from the Repr buildstring.
     * @param buildstring Valid Repr-format buildstring.
     * @param addToCharacterList Boolean indicating whether to add the created object to the CharacterManager's list.
     */
    public PoliticalActor(String buildstring, boolean addToCharacterList) {
        if (buildstring == null || buildstring.isBlank()) {
            throw new IllegalArgumentException("The given buildstring was null, and a " + getClass().getSimpleName() + " object could not be created.");
        }
        fromRepr(buildstring);
        if (addToCharacterList) CharacterManager.addCharacter(this);
    }

    public PoliticalActor(JSONObject json) {
        if (json == null) {
            throw new IllegalArgumentException("The passed JSONObject was null, and a " + getClass().getSimpleName() + " object could not be created.");
        }
        fromJson(json);
        CharacterManager.addCharacter(this);
    }

    /**
     * Creates a PoliticalActor by deep-copying the fields of the passed Character object,
     * and fills the other fields with the passed values. The CharacterManager will generate
     * values for any {@code null} values passed.
     * @param character Character to copy fields from.
     * @param cash Funds currently usable by this PoliticalActor.
     * @param education Highest education level attained by this PoliticalActor.
     * @param alignments Int array of two values between {@code -100} and {@code +100}, which represents the PoliticalActor's Auth/Lib and Right/Left alignments.
     * @param experiences List of Experiences representing the experiences of this PoliticalActor.
     * @param skills Skills object representing the skills and aptitude of this PoliticalActor.
     * @param positions List of the positions on issues held by this PoliticalActor.
     * @param personality Personality object for this PoliticalActor's personality.
     * @param roles Set of Roles for this PoliticalActor's roles. If empty or null, {@code Role.NONE} will be added.
     */
    public PoliticalActor(Character character, int cash, Education education, int[] alignments, List<Experience> experiences, Skills skills, List<Position> positions, Personality personality, Set<PoliticalRole> roles) {
        super(character);
        if (alignments.length != 2)
            throw new IllegalArgumentException("The alignments int array must have a length of 2, not " + String.valueOf(alignments.length));
        this.cash = cash;
        this.education = education;
        this.alignments = alignments;
        this.conviction = evalConviction();
        this.experiences = experiences != null ? experiences : new ArrayList<Experience>();
        this.skills      = skills      != null ? skills      : new Skills();
        this.positions   = positions   != null ? positions   : new ArrayList<Position>();
        this.personality = personality != null ? personality : new Personality();
        this.roles       = roles       != null ? roles       : new HashSet<PoliticalRole>() {{ add(PoliticalRole.NONE); }};
    }

    /**
     * Creates a PoliticalActor, filling fields with the passed values. The CharacterManager
     * will generate values for any {@code null} values passed.
     * @param demographics Demographics object representing this Character's demographic memberships.
     * @param name Name object representing this Character's name.
     * @param birthplaceMunicipality Municipality in which this Character was born.
     * @param currentLocationMunicipality Municipality to which this Character is currently nearest.
     * @param residenceMunicipality Municipality in which this Character currently resides (primary residence).
     * @param birthday Date on which this character was born.
     * @param appearance CharacterModel object to be rendered for this Character.
     * @param cash Funds currently usable by this PoliticalActor.
     * @param education Highest education level attained by this PoliticalActor.
     * @param alignments Int array of two values between -100 and +100, which represents the PoliticalActor's Auth/Lib and Right/Left alignments.
     * @param experiences List of Experiences representing the experiences of this PoliticalActor.
     * @param skills Skills object representing the skills and aptitude of this PoliticalActor.
     * @param positions List of the positions on issues held by this PoliticalActor.
     * @param personality Personality object for this PoliticalActor's personality.
     * @param roles Set of roles for this PoliticalActor's roles. If empty or null, {@code Role.NONE} will be added.
     * @see Character#Character(Demographics, Name, Municipality, Municipality, Municipality, Date, CharacterModel)
     * @see #PoliticalActor(Character, int, Education, int[], List, Skills, List, Personality, Role)
     */
    public PoliticalActor(Demographics demographics, Name name, Municipality birthplaceMunicipality, Municipality currentLocationMunicipality, Municipality residenceMunicipality, Date birthday, CharacterModel appearance, int cash, Education education, int[] alignments, List<Experience> experiences, Skills skills, List<Position> positions, Personality personality, Set<PoliticalRole> roles) {
        this(
            new Character(demographics, name, birthplaceMunicipality, currentLocationMunicipality, residenceMunicipality, birthday, appearance),
            cash, education, alignments, experiences, skills, positions, personality, roles
        );
    }

    // GETTERS AND SETTERS ------------------------------------------------------------------------

    // Cash
    public int getCash() {
        return this.cash;
    }
    public void setCash(int cash) {
        this.cash = cash;
    }
    public void addCash(int cash) {
        this.cash += cash;
    }

    // Education
    public void setEducation(Education education) {
        this.education = education;
    }
    public Education getEducation() {
        return this.education;
    }

    // Alignment(s)
    public int[] getAlignments() {
        return this.alignments;
    }
    public void setAlignments(int[] alignments) {
        if (alignments.length != 2)
            throw new IllegalArgumentException("The alignments int array must have a length of 2, not " + String.valueOf(alignments.length));
        setAuthLibAlignment(alignments[0]);
        setRightLeftAlignment(alignments[1]);
    }
    public int getAuthLibAlignment() {
        return this.alignments[0];
    }
    public void setAuthLibAlignment(int alignment) {
        if (alignment < -100 || alignment > 100)
            throw new IllegalArgumentException("Alignment values must be between -100 and +100, not " + String.valueOf(alignment));
        this.alignments[0] = alignment;
    }
    public int getRightLeftAlignment() {
        return this.alignments[1];
    }
    public void setRightLeftAlignment(int alignment) {
        if (alignment < -100 || alignment > 100)
            throw new IllegalArgumentException("Alignment values must be between -100 and +100, not " + String.valueOf(alignment));
        this.alignments[1] = alignment;
    }

    // Experiences
    public List<Experience> getExperience() {
        return this.experiences;
    }
    public void addExperience(Experience experience) {
        this.experiences.add(experience);
    }
    public void addAllExperiences(Collection<Experience> experiences) {
        this.experiences.addAll(experiences);
    }

    // Skills
    public Skills getSkills() {
        return this.skills;
    }
    public void setSkills(Skills skills) {
        this.skills = skills;
    }
    
    // Position(s)
    public Position getPositionOnIssue(Issue issue) {
        for(Position position : positions) {
            if(position.getRootIssue().equals(issue)) return position;
        }
        Logger.log("INVALID ISSUE NAME", String.format("An invalid issue name, \"%s\", was supplied. Unable to determine position on non-existant issue.", issue), new Exception());
        return null;
    }
    public List<Position> getPositions() {
        return this.positions;
    }
    public void addPosition(Position position) {
        this.positions.add(position);
        evalConviction();
    }
    public void addAllPositions(Collection<Position> positions) {
        this.positions.addAll(positions);
        evalConviction();
    }
    
    // Conviction
    protected int evalConviction() {
        // TODO Complete this function
        return 75;
    }
    public int getConviction() {
        return this.conviction;
    }

    // Personality
    @Override
    public Personality determinePersonality() {
        return new Personality();
    }
    public Personality getPersonality() {
        return personality;
    }
    public void setPersonality(Personality personality) {
        this.personality = personality;
    }
    @Override
    public float evaluateAction() {
        // TODO Complete this function
        return 0.5f;
    }

    // Role
    public Set<PoliticalRole> getRoles() {
        return roles;
    }
    public void setRoles(Set<PoliticalRole> roles) {
        this.roles = new HashSet<>(roles);
        if (this.roles.size() == 0)
            addRole(PoliticalRole.NONE);
    }
    public boolean addRole(PoliticalRole role) {
        if (this.roles.contains(PoliticalRole.NONE))
            removeRole(PoliticalRole.NONE);
        return this.roles.add(role);
    }
    public boolean removeRole(PoliticalRole role) {
        return this.roles.remove(role);
    }

    protected float getAgeMod() {
        float ageMod;
        int age = this.getAge();
        ageMod = (float) (100 * Math.pow(Math.E, -1 * Math.pow(((age - 55) / 30.0f), 2)));
        return ageMod;
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public PoliticalActor fromRepr(String repr) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toRepr() {
        String superRepr = super.toRepr();
        String[] splitSuperRepr = superRepr.split(":\\[");
        superRepr = "";
        for (int i = 1; i < splitSuperRepr.length; i++) {
            superRepr += splitSuperRepr[i] + ":[";
        }
        superRepr = superRepr.substring(0, superRepr.length() - 4);
        String[] experiencesStrings = new String[experiences.size()];
        for (int i = 0; i < experiences.size(); i++) {
            experiencesStrings[i] = experiences.get(i).toRepr();
        }
        String experiencesRepr = Repr.arrayToReprList(experiencesStrings);
        String[] positionsStrings = new String[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            positionsStrings[i] = experiences.get(i).toRepr();
        }
        String positionsRepr = Repr.arrayToReprList(positionsStrings);
        String repr = String.format("%s:[%scash=%d;education=%d;alignments=(%d,%d);experiences=[%s];skills:%s;positions=[%s];conviction=%d;ageMod=%f;personality=%s;];",
            this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1],
            superRepr,
            cash,
            education,
            alignments[0], alignments[1],
            experiencesRepr,
            skills.toRepr(),
            positionsRepr,
            conviction,
            personality.toRepr()
        );
        return repr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PoliticalActor fromJson(JSONObject json) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJson() {
        List<JSONObject> fields = new ArrayList<>();
        fields.add(new JSONObject("cash", cash));
        fields.add(new JSONObject("education", education.value));
        fields.add(new JSONObject("alignments", List.of(alignments[0], alignments[1])));
        fields.add(new JSONObject("experiences")); // TODO
        fields.add(skills.toJson());
        fields.add(new JSONObject("conviction", conviction));
        fields.add(personality.toJson());
        List<String> rolesStrings = new ArrayList<>();
        for (PoliticalRole role : roles) {
            rolesStrings.add(role.getTitle());
        }
        fields.add(new JSONObject("roles", rolesStrings));

        List<?> superFields = super.toJson().getAsList();
        for (Object obj : superFields) {
            if (obj instanceof JSONObject jsonObj) {
                fields.add(jsonObj);
            }
        }

        return new JSONObject(getName().getBiographicalName(), fields);
    }

    /**
     * {@inheritdoc}
     */
    @Override
    public String toString() {
        String characterString = super.toString();
        String roleString = "";
        int i = 0;
        for (PoliticalRole role : roles) {
            roleString += role.getTitle();
            if (i < roles.size() - 2) {
                roleString += ", ";
            }
            else if (i == roles.size() - 2) {
                roleString += ", and ";
            }
            i++;
        }
        return String.format("%s %s", characterString, roleString);
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
        PoliticalActor other = (PoliticalActor) obj;
        return this.toString().equals(other.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 43;
        int hash = super.hashCode();
        hash = prime * hash + (skills == null ? 0 : skills.hashCode());
        hash = prime * hash + (conviction);
        hash = prime * hash + (personality.hashCode());
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PoliticalActor clone() {
        return new PoliticalActor(this);
    }
}
