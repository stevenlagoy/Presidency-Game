package main.core;

import java.util.List;
import java.nio.file.Path;

public abstract class FilePaths {
    
    // BASE ORGANIZATION PATHS
    public static final Path ROOT                       = Path.of("PRESIDENCY_GAME");
    public static final Path SOURCE                     = Path.of("src");
    public static final Path MAIN                       = SOURCE.resolve("main");
    public static final Path CORE                       = MAIN.resolve("core");
    public static final Path RESOURCES                  = MAIN.resolve("resources");

    // RESOURCES PATHS
    public static final Path CHARACTER_RESOURCES        = RESOURCES.resolve("characters");
    public static final Path DATES_RESOURCES            = RESOURCES.resolve("dates");
    public static final Path DEMOGRAPHICS_RESOURCES     = RESOURCES.resolve("demographics");
    public static final Path GFX_RESOURCES              = RESOURCES.resolve("gfx");
    public static final Path LOCALIZATION_RESOURCES     = RESOURCES.resolve("localization");
    public static final Path MAP_RESOURCES              = RESOURCES.resolve("map");
    public static final Path NAMES_RESOURCES            = RESOURCES.resolve("names");

    // ENGINE PATHS
    public static final Path LOGS_DIR                   = Path.of("logs");
    public static final Path LOG_FILE                   = LOGS_DIR.resolve("log.txt");
    public static final Path ERROR_LOG                  = LOGS_DIR.resolve("error.txt");
    public static final Path SAVES_DIR                  = Path.of("saves");
    public static final String SYSTEM_TEXT_LOC          = "_system_text.txt";
    public static final String DESCRIPTIONS_LOC         = "_descriptions.txt";

    // CHARACTER MANAGER PATHS
    public static final Path BIRTHDATE_DISTR            = DATES_RESOURCES.resolve("birthdate_popularities.json");
    public static final Path BIRTHYEAR_DISTR            = DATES_RESOURCES.resolve("birthyear_percentages.json");
    public static final Path FIRSTNAME_DISTR            = NAMES_RESOURCES.resolve("firstname_distributions.json");
    public static final Path MIDDLENAME_DISTR           = NAMES_RESOURCES.resolve("middlename_distribution.json");
    public static final Path LASTNAME_DISTR             = NAMES_RESOURCES.resolve("lastname_distribution.json");
    public static final Path NICKNAMES                  = NAMES_RESOURCES.resolve("nicknames.json");

    // MAP MANAGER FILES
    public static final Path STATES                     = MAP_RESOURCES.resolve("states.json");
    public static final Path CONGRESSIONAL_DISTRICTS    = MAP_RESOURCES.resolve("congressional_districts.json");
    public static final Path COUNTIES                   = MAP_RESOURCES.resolve("counties.json");
    public static final Path CITIES                     = MAP_RESOURCES.resolve("cities.json");
    public static final Path UNIVERSITIES               = MAP_RESOURCES.resolve("universities.json");

    // DEMOGRAPHICS MANAGER FILES
    public static final Path BLOCS                      = DEMOGRAPHICS_RESOURCES.resolve("blocs.json");

    // Ignored files
    public static final List<Path> IGNORED_PATHS = List.of(

    );

}
