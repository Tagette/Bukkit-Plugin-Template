package me.<Your Name>.<Plugin Name>;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

/**
 * @description Handles enabling plugins
 * @author <Your Name>
 */
public class TPluginListener extends ServerListener {

    private final <Plugin Name> plugin;

    public TPluginListener(<Plugin Name> instance) {
        plugin = instance;
        TEconomy.initialize();
    }

    @Override
    public void onPluginEnable(PluginEnableEvent event) {
        if (event.getPlugin() != plugin) {
            // Try to load again!
            TPermissions.onEnable(event.getPlugin());
            THelp.onEnable(event.getPlugin());
            TOddItem.onEnable(event.getPlugin());
            TEconomy.onEnable(event.getPlugin());
            TProtection.onEnable(event.getPlugin());
        }
    }

    @Override
    public void onPluginDisable(PluginDisableEvent event) {
    }
}
