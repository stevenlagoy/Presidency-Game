/*
 * LanguageManager.java
 * Steven LaGoy
 * Created: 26 August 2025
 * Modified: 26 August 2025
 */

package main.core;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.JSONObject;
import core.JSONProcessor;

/** 
 * The LanguageManager tracks the current game language and provides localization for tags.
 */
public final class LanguageManager extends Manager {

    // STATIC CLASS VARIABLES ---------------------------------------------------------------------

    /** Possible languages in which to display game text. */
    public static enum Language {
        
        EN ("English"),
        ZH ("简体中文"),
        RU ("Русский"),
        ES ("Español"),
        PT ("Português"),
        DE ("Deutsch"),
        FR ("Français"),
        JA ("日本語"),
        PL ("Polski"),
        TR ("Türkçe");

        public final String name;
        private Language(String name) { this.name = name; }
        public static Language fromName(String name) {
            for (Language lang : Language.values())
                if (lang.name.equals(name))
                    return lang;
            throw new IllegalArgumentException("Invalid language name: " + name);
        }
        public static Language label(String label) {
            String target = label.trim().toUpperCase().replace("\\s", "_");
            for (Language lang : Language.values())
                if (lang.toString().equals(target))
                    return lang;
            throw new IllegalArgumentException("Invalid language label: " + label);
        }

        public static final Language defaultLanguage = Language.EN;
    }

    // INSTANCE VARIABLES -------------------------------------------------------------------------

    /** Current language of the game. */
    private Language gameLanguage;
    /** For each language, stores tag : sentence pairs for localization tags. */
    public Map<Language, Map<String, String>> localizations;
   
   /** State of the Manager. */
    private ManagerState currentState;

    // CONSTRUCTORS -------------------------------------------------------------------------------

    /** Create an inactive LanguageManager with default values. */
    public LanguageManager() {
        currentState = ManagerState.INACTIVE;
        gameLanguage = Language.defaultLanguage;
        localizations = new HashMap<>();
    }

    // MANAGER METHODS ----------------------------------------------------------------------------

    /** Initialize and Activate this LanguageManager. */
    @Override
    public boolean init() {
        boolean successFlag = true;
        if (gameLanguage == null) gameLanguage = Language.defaultLanguage;
        if (localizations == null) localizations = new HashMap<>();
        successFlag = successFlag && loadLocalizations(gameLanguage);
        currentState = successFlag ? ManagerState.ACTIVE : ManagerState.ERROR;
        return successFlag;
    }
    
    /** Get the current State of this LanguageManager. */
    @Override
    public ManagerState getState() {
        return currentState;
    }
    
    /** Deactivate and clean up the data of this LanguageManager. */
    @Override
    public boolean cleanup() {
        boolean successFlag = true;
        currentState = ManagerState.INACTIVE;
        gameLanguage = null;
        localizations = null;
        if (!successFlag) currentState = ManagerState.ERROR;
        return successFlag;
    }
    

    // GETTERS AND SETTERS ------------------------------------------------------------------------
    
    // Game Language : Language
    public Language getGameLanguage() {
        return gameLanguage;
    }
    /**
     * Loads localization for the language, and if successful sets the game language.
     * @return {@code true} if successfully loaded and set language, {@code false} otherwise.
     */
    public boolean setGameLanguage(Language language) {
        if (loadLocalizations(language)) {
            gameLanguage = language;
            return true;
        }
        return false;
    }

    // Localizations : Map of String to String
    /** Get the localization for a given tag in the current game language. */
    public String getLocalization(String tag) {
        if (gameLanguage == null) {
            Logger.log("UNINITIALIZED GAME LANGUAGE", String.format("The game language was never initialized or was set to null."), new Exception());
            return null;
        }
        return getLocalization(tag, gameLanguage);
    }
    /** Get the localization for a given tag in the passed language. */
    public String getLocalization(String tag, Language language) {
        if (!loadLocalizations(language)) {
            return "INVALID LANGUAGE";
        }
        String res = localizations.get(language).get(tag);
        if (res == null) {
            Logger.log("INVALID LOCALIZATION TAG", String.format("Attempted to access localization tag %s for language %s, which is invalid.", tag, language.toString()), new Exception());
            return "INVALID LOCALIZATION TAG";
        }
        return res;
    }

    /** Load the localizations for a given language. */
    public boolean loadLocalizations(Language language) {
        if (localizations.containsKey(language)) return true;
        
        boolean successFlag = true;

        HashMap<String, String> local = new HashMap<>();

        Path localizationFile = Path.of(String.format("%s/%s/%s%s", FilePaths.LOCALIZATION_RESOURCES, language, language, FilePaths.SYSTEM_TEXT_LOC));
        JSONObject localizationData = JSONProcessor.processJson(localizationFile);

        for (Object entry : localizationData.getAsList()) {
            if (entry instanceof JSONObject entryJson) {
                local.put(entryJson.getKey(), entryJson.getAsString());
            }
        }
        localizations.put(language, local);

        return successFlag;
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------
    
    @Override
    public String toRepr() {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRepr'");
    }

    @Override
    public Manager fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }

    private static final Map<String, String> fieldsJsons = Map.of(
        "gameLanguage", "game_language"
    );

    @Override
    public JSONObject toJson() {
        try {
            List<JSONObject> fields = new ArrayList<>();
            for (String fieldName : fieldsJsons.keySet()) {
                Field field = getClass().getDeclaredField(fieldName);
                fields.add(new JSONObject(fieldName, field.get(this)));
            }
            return new JSONObject(this.getClass().getSimpleName(), fields);
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            currentState = ManagerState.ERROR;
            Logger.log("JSON SERIALIZATION ERROR", "Failed to serialize " + getClass().getSimpleName() + " to JSON.", e);
            return null;
        }
    }

    @Override
    public Manager fromJson(JSONObject json) {
        currentState = ManagerState.INACTIVE;
        for (String fieldName : fieldsJsons.keySet()) {
            String jsonKey = fieldsJsons.get(fieldName);
            Object value = json.get(jsonKey);
            if (value == null) continue;
            try {
                Field field = getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Class<?> type = field.getType();
                if (type.isEnum()) {
                    // For enums, convert string to enum constant
                    @SuppressWarnings({ "unchecked", "rawtypes" })
                    Object enumValue = Enum.valueOf((Class<Enum>) type, value.toString());
                    field.set(this, enumValue);
                }
                else {
                    // For other types, set directly (may need conversion for complex types)
                    field.set(this, value);
                }
            }
            catch (Exception e) {
                currentState = ManagerState.ERROR;
                Logger.log("JSON DESERIALIZATION ERROR", "Failed to set field " + fieldName + " in LanguageManager from JSON.", e);
            }
        }
        return this;
    }

}
