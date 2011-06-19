package me.tagette.template;

import me.taylorkelly.help.Help;
import org.bukkit.plugin.Plugin;

/**
 * @description Handles all plugin help
 * @author Tagette
 */
public class THelp {

    private static Template plugin;
    public static Help helpPlugin;

    public static void initialize(Template instance) {
        THelp.plugin = instance;
        helpPlugin = (Help) plugin.getServer().getPluginManager().getPlugin("Help");
        if (helpPlugin != null) {
            registerCommands();
            if (!TSettings.LowDetailMode) {
                TLogger.info("Help version " + helpPlugin.getDescription().getVersion() + " loaded.");
            }
        }
    }

    public static void onEnable(Plugin plugin) {
        if (helpPlugin == null) {
            if (plugin.getDescription().getName().equals("Help")) {
                helpPlugin = (Help) plugin;
                registerCommands();
                if (!TSettings.LowDetailMode) {
                    TLogger.info("Help version " + helpPlugin.getDescription().getVersion() + " loaded.");
                }
            }
        }
    }

    public static void load() {
    }

    public static void registerCommands() {
        // Register help here.
        // main=true is so it shows in the main help list as well as in the sublist for your plugin.
        helpPlugin.registerCommand("template help", "Shows the commands for the " + Template.name + " plugin.", plugin, true);
        helpPlugin.registerCommand("template nuke", "Shows message about your nukes!", plugin, false);
        helpPlugin.registerCommand("template getawesomeness", "Shows your level of awesomeness!", plugin, false, "template.awesomeness");
        helpPlugin.registerCommand("template getawesomeness", "Shows your level of awesomeness!", plugin, false, "template.awesomeness");
    }
}
