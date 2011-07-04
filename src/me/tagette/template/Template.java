package me.tagette.template;

import java.util.ArrayList;
import me.tagette.template.extras.CommandManager;
import java.util.List;
import java.util.logging.Level;
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
    public String name;
    public String version;

    /*
     * This method runs when the plugin is enabled.
     */
    @Override
    public void onEnable() {
        name = this.getDescription().getName();
        version = this.getDescription().getVersion();

        PluginManager pm = getServer().getPluginManager();
        // Makes sure all plugins are correctly loaded.
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, pluginListener, Priority.Monitor, this);

        // Register our events
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Low, this);

        // Logger
        TLogger.initialize(this, Logger.getLogger("Minecraft"));

        // Settings
        TSettings.initialize(this);
        TLanguage.initialize(this);

        // Debugger
        TDebug.initialize(this);

        // Auto-Updater
        TUpdater.initialize(this);

        // Database
        if (TConstants.databaseEnabled) {
            TDatabase.initialize(this);
        }

        // Supported plugins
        THelp.initialize(this);
        TOddItem.initialize(this);
        TPermissions.initialize(this);
        TProtection.initialize(this);

        // Commands
        setupCommands();

        TLogger.info(name + " version " + version + " is enabled!");
    }

    /*
     * This method runs when the plugin is disabling.
     */
    @Override
    public void onDisable() {
        if (TConstants.databaseEnabled) {
            TDatabase.disable();
        }
        TLogger.info(name + " disabled.");
    }

    /*
     * Sets up the core commands of the plugin.
     */
    private void setupCommands() {
        // Add command labels here.
        // For example in "/template version" and "/template reload" the label for both is "template".
        // Make your commands in the me.tagette.template.commands package. Each command is a seperate class.
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
}
