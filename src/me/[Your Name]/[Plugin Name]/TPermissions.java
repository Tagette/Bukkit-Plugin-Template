package me.<Your Name>.<Plugin Name>;

import com.nijikokun.bukkit.Permissions.Permissions;
import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * @description Handles all plugin permissions
 * @author <Your Name>
 */
public class TPermissions {

    private enum PermissionHandler {

        PERMISSIONS, GROUP_MANAGER, NONE
    }
    private static PermissionHandler handler;
    public static Plugin PermissionPlugin;
    private static <Plugin Name> plugin;

    public static void initialize(<Plugin Name> instance) {
        TPermissions.plugin = instance;
        Plugin iConomy = plugin.getServer().getPluginManager().getPlugin("iConomy");
        Plugin GroupManager = plugin.getServer().getPluginManager().getPlugin("GroupManager");
        handler = PermissionHandler.NONE;

        if (iConomy != null) {
            PermissionPlugin = iConomy;
            handler = PermissionHandler.PERMISSIONS;
            String version = PermissionPlugin.getDescription().getVersion();
            TLogger.info("Permissions enabled using: Permissions v" + version + ".");
        } else if (GroupManager != null) {
            PermissionPlugin = GroupManager;
            handler = PermissionHandler.GROUP_MANAGER;
            String version = PermissionPlugin.getDescription().getVersion();
            TLogger.info("Permissions enabled using: GroupManager v" + version + ".");
        }
    }

    public static void onEnable(Plugin plugin) {
        if (PermissionPlugin == null) {
            String pluginName = plugin.getDescription().getName();
            handler = PermissionHandler.NONE;

            if (pluginName.equals("Permissions")) {
                PermissionPlugin = plugin;
                handler = PermissionHandler.PERMISSIONS;
                String version = plugin.getDescription().getVersion();
                TLogger.info("Permissions enabled using: Permissions v" + version + ".");
            } else if (pluginName.equals("GroupManager")) {
                PermissionPlugin = plugin;
                handler = PermissionHandler.GROUP_MANAGER;
                String version = plugin.getDescription().getVersion();
                TLogger.info("Permissions enabled using: GroupManager v" + version + ".");
            }
        }
    }

    public static boolean permission(Player player, String permission, boolean defaultPerm) {
        switch (handler) {
            case PERMISSIONS:
                return ((Permissions) PermissionPlugin).getHandler().has(player, permission);
            case GROUP_MANAGER:
                return ((GroupManager) PermissionPlugin).getWorldsHolder().getWorldPermissions(player).has(player, permission);
            case NONE:
                return defaultPerm;
            default:
                return defaultPerm;
        }
    }

    public static boolean isAdmin(Player player) {
        return permission(player, "basic.admin", player.isOp());
    }
}
