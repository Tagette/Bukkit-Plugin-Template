package me.<Your Name>.<Plugin Name>;

import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

/**
 * @description Main class for <Plugin Name> plugin for Bukkit
 * @author <Your Name>
 */
public class <Plugin Name> extends JavaPlugin {

    private final TPlayerListener playerListener = new TPlayerListener(this);
    private final TBlockListener blockListener = new TBlockListener(this);
    private final TPluginListener pluginListener = new TPluginListener(this);
    private final TCommandListener cmder = new TCommandListener(this);
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
        getCommand("<Plugin Name>").setExecutor(cmder);
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
