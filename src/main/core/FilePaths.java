package main.core;

import java.security.DrbgParameters.Reseed;

public abstract class FilePaths {
    
    public static final String ROOT = "src/main";
    public static final String RESOURCES = ROOT + "/resources";

    // ENGINE FILES
    public static final String LOG = "logs/log.txt";
    public static final String ERROR = "logs/error.txt";
    public static final String localizationFolder_loc = RESOURCES + "/localization";
    public static final String systemText_loc = "_system_text.txt";
    public static final String descriptions_loc = "_descriptions.txt";

    // CHARACTER MANAGER FILES
    public static final String birthdate_popularity = "birthdate_popularities.JSON";
    public static final String birthyear_percentages = "birthyear_percentages.JSON";
    public static final String firstname_popularity = RESOURCES + "/names/firstname_distributions.json";
    public static final String middlename_popularity = RESOURCES + "/names/middlename_distribution.json";
    public static final String lastname_popularity = RESOURCES + "/names/lastname_distribution.json";
    public static final String nicknames = RESOURCES + "/names/nicknames.json";

    public static final String states = RESOURCES + "/states.json";
    public static final String congressional_districts = RESOURCES + "/congressional_districts.json";
    public static final String counties = RESOURCES + "/counties.json";
    public static final String cities = RESOURCES + "/cities.json";
    public static final String universities = RESOURCES + "/universities.json";

    // DEMOGRAPHICS MANAGER FILES
    public static final String blocs = RESOURCES + "/blocsNew.json";



}
