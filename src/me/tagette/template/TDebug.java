package me.tagette.template;

import java.util.ArrayList;
import java.util.List;
import me.tagette.template.extras.DebugDetailLevel;
import org.bukkit.entity.Player;

/**
 * @description Handles debugging for the plugin.
 * @author Tagette
 */
public class TDebug {
    private static List<Player> debugees;
    private static boolean debugging;
    private static DebugDetailLevel detailLevel;
    private static Template plugin;
    
    public static void initialize(Template instance){
        plugin = instance;
        debugees = new ArrayList<Player>();
        debugging = false;
        detailLevel = TSettings.debugLevel;
    }

    /*
     * Checks is the plugin is in debug mode.
     */
    public static boolean inDebugMode() {
        return !debugees.isEmpty() || debugging;
    }

    /*
     * Checks if a player is in debug mode.
     * 
     * @param player    The player to check.
     */
    public static boolean isDebugging(final Player player) {
        return debugees.contains(player);
    }

    /*
     * Sets a players debug mode.
     * 
     * @param player    The player to set the debug mode of.
     */
    public static boolean startDebugging(final Player player) {
        if (TConstants.debugAllowed) {
            debugees.add(player);
        }
        return inDebugMode();
    }

    public static boolean startDebugging() {
        debugging = TConstants.debugAllowed;
        return inDebugMode();
    }

    public static void stopDebugging(final Player player) {
        debugees.remove(player);
    }

    public static void stopDebugging(String message) {
        for (Player player : debugees) {
            player.sendMessage(message);
        }
        debugees.clear();
        debugging = false;
    }

    public static void debug(DebugDetailLevel level, String message) {
        if (inDebugMode()) {
            if(level.ordinal() >= detailLevel.ordinal()){
                TLogger.info(message);
                for (Player player : debugees) {
                    player.sendMessage(message);
                }
            }
        }
    }
}
