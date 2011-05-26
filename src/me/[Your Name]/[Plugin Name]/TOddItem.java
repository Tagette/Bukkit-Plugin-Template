package me.<Your Name>.<Plugin Name>;

import info.somethingodd.bukkit.OddItem.OddItem;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * @description Unified custom items list and parser
 * @author <Your Name>
 */
public class TOddItem {

    public static OddItem OddItem;
    public static String name;
    public static String version;
    private static <Plugin Name> plugin;

    public static void initialize(<Plugin Name> instance) {
        TOddItem.plugin = instance;
        OddItem = (OddItem) plugin.getServer().getPluginManager().getPlugin("OddItem");
        if (OddItem != null) {
            name = OddItem.getDescription().getName();
            version = OddItem.getDescription().getVersion();
            TLogger.info(name + " version " + version + " loaded.");
        }
    }

    public static void onEnable(Plugin plugin) {
        if (OddItem == null) {
            if (plugin.getDescription().getName().equals("OddItem")) {
                OddItem = (OddItem) plugin;
                name = OddItem.getDescription().getName();
                version = OddItem.getDescription().getVersion();
                TLogger.info(name + " version " + version + " loaded.");
            }
        }
    }

    //Returns ItemStack - in case OddItem is enabled
    public static ItemStack getItemStack(String name) {
        ItemStack itemStack = null;
        int dataPosition = name.indexOf(';');
        dataPosition = (dataPosition != -1 ? dataPosition : -1);
        int dataValue = (TTools.isInt(name.substring(dataPosition + 1)) ? Integer.parseInt(name.substring(dataPosition + 1)) : 0);
        dataValue = (dataValue > 30 || dataValue < 0 ? 0 : dataValue);
        Material mat;
        if (dataPosition != -1) {
            mat = TTools.getMat(name.substring(0, dataPosition));
        } else {
            mat = TTools.getMat(name);
        }
        if (OddItem != null) {
            try {
                itemStack = OddItem.getItemStack(name);
            } catch (Exception ex) {
            }
        }
        if (mat != null && mat != Material.AIR) {
            itemStack = new ItemStack(mat, 0, (short) dataValue);
        }
        return itemStack;
    }

    //Returns Alias of the Item - in case OddItem is enabled
    public static String getItemAlias(String name) {
        String alias = "";
        if (OddItem != null) {
            Set<String> aliases = OddItem.getAliases(name);
            Iterator<String> iter = aliases.iterator();
            if (iter.hasNext()) {
                alias = iter.next();
            }
        }
        return alias;
    }
}
