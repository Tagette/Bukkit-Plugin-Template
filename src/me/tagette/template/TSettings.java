package me.tagette.template;

import me.tagette.template.extras.PropertiesFile;
import java.io.File;
import org.bukkit.plugin.Plugin;

/**
 * @description Handles property files
 * @author Tagette
 */
public class TSettings {

    private static final String settingsFile = "Config.properties";
    private static Template plugin;
    // Add settings here
    // Nukes
    public static int maxNukes;
    public static boolean adminsObeyLimits;
    // Database
    public static boolean useMySQL;
    public static String MySQLHost;
    public static String MySQLUser;
    public static String MySQLPass;
    public static String MySQLDBName;

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

        if (file.save()) {
            TLogger.info("Settings file created.");
        } else {
            TLogger.info("Settings loaded.");
        }
    }

    private static void setup(PropertiesFile file) {
        // Declare settings here. Note that if config is not found these values should be placed into a new config file.

        // This will show in file as: "  ###  Nukes!  ###  " (without quotes) with a extra line above and below.
        file.createCategoryHeader("Nukes!");

        // Note the settings file will be filled with these default values if it doesn't exist.
        maxNukes = file.getInt("maxNukes", 10, "Maximum number of nukes any player can use.");
        adminsObeyLimits = file.getBoolean("adminsObeyLimits", false, "Whether or not admins can disobey nuke limits.");

        // Database settings
        file.createCategoryHeader("Database");

        useMySQL = file.getBoolean("useMySQL", false, "If set to false, SQLite will be used instead.");
        MySQLHost = file.getString("host", "localhost", "The host of the MySQL database. Default: localhost");
        MySQLUser = file.getString("user", "root", "The username to access the MySQL database with.");
        MySQLPass = file.getString("pass", "", "The password for the user.");
        MySQLDBName = file.getString("dbname", "", "The name of the database.");
    }
}
