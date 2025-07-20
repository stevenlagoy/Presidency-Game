package main.core;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LanguageManager extends Manager {

    // STATIC CLASS VARIABLES ---------------------------------------------------------------------

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

    // CONSTRUCTORS -------------------------------------------------------------------------------

    public LanguageManager() {
        localizations = new HashMap<>();
    }

    // MANAGER METHODS ----------------------------------------------------------------------------
    
    @Override
    public boolean init() {
        boolean successFlag = true;
        successFlag = successFlag && setGameLanguage(Language.defaultLanguage);
        return successFlag;
    }
    
    @Override
    public ManagerState getState() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getState'");
    }
    
    @Override
    public boolean cleanup() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cleanup'");
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
    public String getLocalization(String tag) {
        if (gameLanguage == null) {
            Engine.log("UNINITIALIZED GAME LANGUAGE", String.format("The game language was never initialized or was set to null."), new Exception());
            return null;
        }
        return getLocalization(tag, gameLanguage);
    }
    public String getLocalization(String tag, Language language) {
        if (!loadLocalizations(language)) {
            return "INVALID LANGUAGE";
        }
        String res = localizations.get(language).get(tag);
        if (res == null) {
            Engine.log("INVALID LOCALIZATION TAG", String.format("Attempted to access localization tag %s for language %s, which is invalid.", tag, language.toString()), new Exception());
            return "INVALID LOCALIZATION TAG";
        }
        return res;
    }
    public boolean loadLocalizations(Language language) {
        boolean successFlag = true;

        HashMap<String, String> local = new HashMap<>();

        List<String> contents = IOUtil.readFile(Path.of(String.format("%s/%s%s", FilePaths.LOCALIZATION_RESOURCES, language, FilePaths.SYSTEM_TEXT_LOC)));
        if (contents == null) successFlag = false;
        for (String line : contents) {
            if (line == null || line.isBlank()) continue;
            String[] parts = StringOperations.splitByUnquotedString(line, ":", 2);
            if (parts.length != 2) {
                Engine.log("INVALID LOCALIZATION ENTRY", String.format("In localization file for language %s, the entry \"%s\" was invalid.", language.toString(), line), new Exception());
                continue;
            }
            local.put(parts[0], parts[1]);
        }
        if (local.size() == 0) successFlag = false;
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

}
