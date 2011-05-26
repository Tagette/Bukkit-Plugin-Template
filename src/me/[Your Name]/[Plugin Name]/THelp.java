package me.<Your Name>.<Plugin Name>;

import me.taylorkelly.help.Help;
import org.bukkit.plugin.Plugin;

/**
 * @description Handles all plugin help
 * @author <Your Name>
 */
public class THelp {

    private static <Plugin Name> plugin;
    public static Help helpPlugin;

    public static void initialize(<Plugin Name> instance) {
        THelp.plugin = instance;
        helpPlugin = (Help) plugin.getServer().getPluginManager().getPlugin("Help");
        if (helpPlugin != null) {
            registerCommands();
            TLogger.info("Help support enabled.");
        }
    }

    public static void onEnable(Plugin plugin) {
        if (helpPlugin == null) {
            if (plugin.getDescription().getName().equals("Help")) {
                helpPlugin = (Help) plugin;
                registerCommands();
                TLogger.info("Help support enabled.");
            }
        }
    }

    public static void registerCommands() {
        // Register help here.
        // main=true is so it shows in the main help list as well as in the sublist for your plugin.
        helpPlugin.registerCommand("<Plugin Name> help", "Shows the commands for the " + <Plugin Name>.name + " plugin.", plugin, true);
        helpPlugin.registerCommand("<Plugin Name> nuke", "Shows message about your nukes!", plugin, false);
        helpPlugin.registerCommand("<Plugin Name> getawesomeness", "Shows your level of awesomeness!", plugin, false, "<Plugin Name>.awesomeness");
        helpPlugin.registerCommand("<Plugin Name> getawesomeness", "Shows your level of awesomeness!", plugin, false, "<Plugin Name>.awesomeness");
    }
}
