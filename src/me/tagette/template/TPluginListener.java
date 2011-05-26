package me.tagette.template;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

/**
 * @description Handles enabling plugins
 * @author Tagette
 */
public class TPluginListener extends ServerListener {

    private final Template plugin;

    public TPluginListener(Template instance) {
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
