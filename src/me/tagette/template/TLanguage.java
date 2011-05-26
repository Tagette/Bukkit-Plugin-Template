package me.tagette.template;

import me.tagette.template.extras.PropertiesFile;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @description Handles the language used in the plugin
 * @author brenda
 */
public class TLanguage {

    private static final String settingsFile = "Language.properties";
    private static Map<String, String> languages;
    private static Template plugin;

    public static void initialize(Template instance) {
        TLanguage.plugin = instance;
        languages = new HashMap<String, String>();
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        File configFile = new File(plugin.getDataFolder(), settingsFile);
        PropertiesFile file = new PropertiesFile(configFile);
        String key = "";
        String value = "";
        String comment = "";
        // Declare settings here. Note that if config is not found these values will be placed into a new config file.

        // This will show in file as: "  ###  Nukes!  ###  " (without quotes) with a extra line above and below.
        file.createCategoryHeader("Nukes!");

        key = "nukeAdminWithLimits";
        value = "HOLY CRAP UR ADMIN! You can only have lil' bit o' nukes... atleast your not a noob.";
        comment = "Message displayed when an admin types the nuke command with limited nukes.";
        languages.put(key, file.getString(key, value, comment));

        key = "nukeAdminWithoutLimits";
        value = "HOLY CRAP UR ADMIN! You can has so many nukes!.";
        comment = "Message displayed when an admin types the nuke command without limited nukes.";
        languages.put(key, file.getString(key, value, comment));

        key = "nukeNonAdmin";
        value = "Ha.. your not admin. You can only have lil' bit o' nukes... noob.";
        comment = "Message displayed when a normal player types the nuke command.";
        languages.put(key, file.getString(key, value, comment));

        if(file.save()){
            TLogger.info("Language file created.");
        } else {
            TLogger.info("Language loaded.");
        }
    }

    public static String getLanguage(String key) {
        return languages.get(key);
    }
}
