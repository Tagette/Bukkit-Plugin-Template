package me.tagette.template;

import me.tagette.template.extras.CommandManager;
import java.util.HashMap;
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
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    public static String name;
    public static String version;

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
        TProtection.initialize(this);
        
        // Commands
        setupCommands();
        
        TLogger.info(name + " version " + version + " is enabled!");
    }
    
    private void setupCommands() {
        // Add command labels here.
        // For example in "/basic version" and "/basic reload" the label for both is "basic".
        // Make your commands in the template.commands package. Each command is a seperate class.
        // ## For the command to work it must be in the plugin.yml! ##
        // Look in the plugin.yml for the example.
        addCommand("template", new TemplateCmd(this));
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return commandManager.dispatch(sender, cmd, label, args);
    }
    
    private void addCommand(String command, CommandExecutor executor) {
        getCommand("template").setExecutor(executor);
        commandManager.addCommand(command, executor);
    }

    @Override
    public void onDisable() {
        TDatabase.disable();
        TLogger.info(name + " disabled.");
    }

    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        } else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }
}
