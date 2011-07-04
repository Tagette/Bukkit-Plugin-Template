package me.tagette.template;

import me.tagette.template.extras.PropertiesFile;
import java.io.File;
import me.tagette.template.extras.DebugDetailLevel;

/**
 * @description Handles property files
 * @author Tagette
 */
public class TSettings {

    private static final String settingsFile = "Config.properties";
    private static Template plugin;
    // Add settings here
    public static int lowestAwesome;
    public static int highestAwesome;
    // Database
    public static boolean useMySQL;
    public static String MySQLHost;
    public static String MySQLUser;
    public static String MySQLPass;
    public static String MySQLDBName;
    public static boolean LowDetailMode;
    public static DebugDetailLevel debugLevel;

    public static void initialize(Template instance) {
        TSettings.plugin = instance;
        load();
    }

    public static void load() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        // Dont know why but needed to repeat..
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        File configFile = new File(plugin.getDataFolder(), settingsFile);
        PropertiesFile file = new PropertiesFile(configFile);

        setup(file);
        file.save();
        if (!TSettings.LowDetailMode) {
            TLogger.info("Settings loaded.");
        }
    }

    private static void setup(PropertiesFile file) {
        // Declare settings here. Note that if config is not found these values should be placed into a new config file.

        // Note the settings file will be filled with these default values if it doesn't exist.
        lowestAwesome = file.getInt("lowestAwesome", 0, "The lowest allowed awesome level.");
        highestAwesome = file.getInt("highestAwesome", 100, "The highest allowed awesome level.");

        
        
        // These are default settings to disable these go into the TConstants.java class and set the respective values to false.
        // They will not be shown in the settings file if they are disabled niether will they initialize.
        if(TConstants.databaseEnabled){
            useMySQL = file.getBoolean("useMySQL", false, "If set to false, SQLite will be used instead.");
            MySQLHost = file.getString("host", "localhost", "The host of the MySQL database. Default: localhost");
            MySQLUser = file.getString("user", "root", "The username to access the MySQL database with.");
            MySQLPass = file.getString("pass", "", "The password for the user.");
            MySQLDBName = file.getString("dbname", "", "The name of the database.");
        }
        
        if(TConstants.debugAllowed){
            debugLevel = DebugDetailLevel.values()[file.getInt("debugDetailLevel", 1, "How much detail the debugger shows. (0 = Everything, 2 = Important Only) Default: 1")];
        } else {
            debugLevel = DebugDetailLevel.NORMAL;
        }

        if(TConstants.lowDetailAllowed){
            LowDetailMode = file.getBoolean("lowDetailMode", false, "Displays less info when starting if true.");
        } else {
            LowDetailMode = false;
        }
    }
}
