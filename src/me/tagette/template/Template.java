package me.tagette.template;

import java.util.ArrayList;
import me.tagette.template.extras.CommandManager;
import java.util.List;
import java.util.logging.Logger;
import me.tagette.template.commands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

/**
 * @description Main class for Template plugin for Bukkit
 * @author Tagette
 */
public class Template extends JavaPlugin {
    
    private final TPlayerListener playerListener = new TPlayerListener(this);
    private final TBlockListener blockListener = new TBlockListener(this);
    private final TPluginListener pluginListener = new TPluginListener(this);
    private final CommandManager commandManager = new CommandManager(this);
    private final List<Player> debugees = new ArrayList<Player>();
    public static String name;
    public static String version;
    private static boolean debugging;

    /*
     * This method runs when the plugin is enabled.
     */
    @Override
    public void onEnable() {
        name = this.getDescription().getName();
        version = this.getDescription().getVersion();

        // Logger
        TLogger.initialize(Logger.getLogger("Minecraft"));

        PluginManager pm = getServer().getPluginManager();
        // Makes sure all plugins are correctly loaded.
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, pluginListener, Priority.Monitor, this);
        // Register our events
        pm.registerEvent(Event.Type.SIGN_CHANGE, blockListener, Priority.Low, this); // placin' sign duh
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Low, this); // Clickin' and such

        // Settings
        TSettings.initialize(this);
        TLanguage.initialize(this);

        // Database
        TDatabase.initialize(this);

        // Supported plugins
        THelp.initialize(this);
        TOddItem.initialize(this);
        TPermissions.initialize(this);
        //TProtection.initialize(this);

        // Commands
        setupCommands();

        TLogger.info(name + " version " + version + " is enabled!");
    }

    /*
     * Sets up the core commands of the plugin.
     */
    private void setupCommands() {
        // Add command labels here.
        // For example in "/template version" and "/template reload" the label for both is "template".
        // Make your commands in the template.commands package. Each command is a seperate class.
        addCommand("template", new TemplateCmd(this));
    }

    /*
     * Executes a command when a command event is received.
     * 
     * @param sender    The thing that sent the command.
     * @param cmd       The complete command object.
     * @param label     The label of the command.
     * @param args      The arguments of the command.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return commandManager.dispatch(sender, cmd, label, args);
    }

    /*
     * Adds the specified command to the command manager and server.
     * 
     * @param command   The label of the command.
     * @param executor  The command class that excecutes the command.
     */
    private void addCommand(String command, CommandExecutor executor) {
        getCommand(command).setExecutor(executor);
        commandManager.addCommand(command, executor);
    }

    /*
     * This method runs when the plugin is disabling.
     */
    @Override
    public void onDisable() {
        TDatabase.disable();
        TLogger.info(name + " disabled.");
    }
    
    /*
     * Checks is the plugin is in debug mode.
     */
    public boolean inDebugMode(){
        return !debugees.isEmpty() || debugging;
    }

    /*
     * Checks if a player is in debug mode.
     * 
     * @param player    The player to check.
     */
    public boolean isDebugging(final Player player) {
        return debugees.contains(player);
    }

    /*
     * Sets a players debug mode.
     * 
     * @param player    The player to set the debug mode of.
     */
    public void startDebugging(final Player player) {
        debugees.add(player);
    }
    
    public void startDebugging() {
        debugging = true;
    }
    
    public void stopDebugging(final Player player) {
        debugees.remove(player);
    }
    
    public void stopDebugging() {
        for(Player player : debugees) {
            player.sendMessage("You are no longer in debug mode.");
        }
        debugees.clear();
        debugging = false;
    }
}
