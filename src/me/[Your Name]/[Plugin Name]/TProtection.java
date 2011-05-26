package me.<Your Name>.<Plugin Name>;

import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import com.griefcraft.model.ProtectionTypes;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.yi.acru.bukkit.Lockette.Lockette;

/**
 * @description Handles block and item protection
 * @author <Your Name>
 */
public class TProtection {

    public static LWC lwc = null;
    public static Lockette lockette = null;
    private static <Plugin Name> plugin;

    public static void initialize(<Plugin Name> instance) {
        TProtection.plugin = instance;
        lwc = (LWC) plugin.getServer().getPluginManager().getPlugin("LWC");
        lockette = (Lockette) plugin.getServer().getPluginManager().getPlugin("Lockette");
        if (lwc != null) {
            TLogger.info("LWC has be hooked into.");
        }
        if (lockette != null) {
            TLogger.info("Lockette has be hooked into.");
        }
    }

    public static void onEnable(Plugin plugin) {
        String pluginName = plugin.getDescription().getName();
        if (pluginName.equals("LWC") && lwc == null) {
            lwc = (LWC) plugin;
            TLogger.info("LWC has be hooked into.");
        }
        if (pluginName.equals("Lockette") && lockette == null) {
            lockette = (Lockette) plugin;
            TLogger.info("Lockette has be hooked into.");
        }
    }

    public static boolean isProtected(Block chest) {
        if (lwc != null) {
            Protection lwcProt = lwc.findProtection(chest);
            if (lwcProt != null) {
                return true;
            }
        }
        if (lockette != null) {
            boolean lockProt = Lockette.isProtected(chest);
            if (lockProt) {
                return true;
            }
        }
        return false;

    }

    public static String protectedByWho(Block chest) {
        if (chest == null) {
            return null;
        }
        String name = "";
        if (lwc != null) {
            Protection prot = lwc.findProtection(chest);
            if (prot != null) {
                name = prot.getOwner();
            }
        }
        if (lockette != null) {
            name = Lockette.getProtectedOwner(chest);
        }
        return name;
    }

    public static boolean protectBlock(Block block, String name) {
        boolean protect = false;
        if (lwc != null) {
            lwc.getPhysicalDatabase().registerProtection(block.getTypeId(), ProtectionTypes.PRIVATE, block.getWorld().getName(), name, "", block.getX(), block.getY(), block.getZ());
            protect = true;
        }
        return protect;
    }
}
