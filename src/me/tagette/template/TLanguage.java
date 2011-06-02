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
        load();
    }
    
    public static void load(){
        languages = new HashMap<String, String>();
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        File configFile = new File(plugin.getDataFolder(), settingsFile);
        PropertiesFile file = new PropertiesFile(configFile);

        setup(file);
        file.save();
        TLogger.info("Language loaded.");
    }
    
    public static void setup(PropertiesFile file){
        String key = "";
        String value = "";
        String comment = "";
        // Declare settings here. Note that if config is not found these values will be placed into a new config file.

        key = "noPermissions";
        value = "You do not have permission.";
        comment = "Message displayed when a user doesn't have permissions to do something.";
        languages.put(key, file.getString(key, value, comment));
    }

    public static String getLanguage(String key) {
        return languages.get(key);
    }
}
