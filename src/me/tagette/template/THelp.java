package me.tagette.template;

import java.util.ArrayList;
import java.util.List;
import me.taylorkelly.help.Help;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * @description Handles all plugin help
 * @author Tagette
 */
public class THelp {

    private static Template plugin;
    public static Help helpPlugin;
    private static List<CommandHelp> helpList;

    private static class CommandHelp {

        public String name;
        public String desc;
        public boolean main;
        public String permission;

        public CommandHelp(String _name, String _help, boolean _main, String _permission) {
            this.name = _name;
            this.desc = _help;
            this.main = _main;
            this.permission = _permission;
        }
    }

    public static void initialize(Template instance) {
        THelp.plugin = instance;
        helpPlugin = (Help) plugin.getServer().getPluginManager().getPlugin("Help");
        if (helpPlugin != null) {
            registerHelp();
            if (!TSettings.LowDetailMode) {
                TLogger.info("Help version " + helpPlugin.getDescription().getVersion() + " loaded.");
            }
        }
    }

    public static void onEnable(Plugin plugin) {
        if (helpPlugin == null) {
            if (plugin.getDescription().getName().equals("Help")) {
                helpPlugin = (Help) plugin;
                registerHelp();
                if (!TSettings.LowDetailMode) {
                    TLogger.info("Help version " + helpPlugin.getDescription().getVersion() + " loaded.");
                }
            }
        }
    }

    public static void registerHelp() {
        helpList = new ArrayList<CommandHelp>();
        // Generic command help.
        register("template help", "Shows the commands for the " + plugin.name + " plugin.", true);
        register("template debug", "Puts you and " + plugin.name + " into debug mode.", "debug");
        register("template reload", "Shows the commands for the " + plugin.name + " plugin.", "reload");


        /* Register help here.
         * main=true is so it shows in the main help list as well as in the 
         *      sublist for your plugin if the Help plugin is hooked.
         * For example 'template help' will show if the users types '/help' but 
         *      the others won't. However if the user types '/help template'
         *      then all of these will be shown.
         * 
         */
        register("template getawesomeness", "Shows your level of awesomeness!", "awesomeness");
        register("template setawesomeness", "Sets the awesomeness of someone.", "awesomeness.set");
    }

    private static void register(String command, String help, String permission) {
        register(command, help, false, permission);
    }

    private static void register(String command, String help) {
        register(command, help, false);
    }

    private static void register(String command, String help, boolean main, String permission) {
        helpList.add(new CommandHelp(command, help, main, permission));
        helpPlugin.registerCommand(command, help, plugin, main, permission);
    }

    private static void register(String command, String help, boolean main) {
        helpList.add(new CommandHelp(command, help, main, ""));
        helpPlugin.registerCommand(command, help, plugin, main);
    }
    
    public static String getReadableHelp(){
        return getReadableHelp(null);
    }

    public static String getReadableHelp(Player player) {
        String readableHelp = "No help available.";
        if (helpPlugin == null) {
            if (!helpList.isEmpty()) {
                readableHelp = "---" + plugin.name + "Help ---\n";
                for (CommandHelp help : helpList) {
                    if (player == null || TPermissions.has(player, help.permission, player.isOp())) {
                        readableHelp += "/" + help.name + " - " + help.desc + "\n";
                    }
                }
                readableHelp = readableHelp.substring(0, readableHelp.length() - 2);
            }
        } else {
            readableHelp = "Please use '/help " + plugin.name + "' for help with this plugin.";
        }
        return readableHelp;
    }
}
