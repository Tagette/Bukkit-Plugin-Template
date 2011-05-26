package me.<Your Name>.<Plugin Name>;

import org.bukkit.Material;

/**
 * @description Useful tools
 * @author <Your Name>
 */
public class TTools {

    public static boolean isInt(String i) {
        boolean is = false;
        try {
            Integer.parseInt(i);
            is = true;
        } catch (Exception e) {
        }
        return is;
    }

    public static boolean isFloat(String i) {
        boolean is = false;
        try {
            Float.parseFloat(i);
            is = true;
        } catch (Exception e) {
        }
        return is;
    }

    // Gets the Material from bukkit enum
    public static Material getMat(String name) {
        Material mat = null;
        if (isInt(name)) {
            mat = getMat(Integer.parseInt(name));
        } else {
            mat = Material.getMaterial(getMatID(name));
        }
        return mat;
    }

    // Gets the Material from ID
    public static Material getMat(int id) {
        return Material.getMaterial(id);
    }

    // Gets the id of a Material
    public static int getMatID(String name) {
        int matID = -1;
        Material[] mat = Material.values();
        int temp = 9999;
        Material tmp = null;
        for (Material m : mat) {
            if (m.name().toLowerCase().replaceAll("_", "").startsWith(name.toLowerCase().replaceAll("_", "").replaceAll(" ", ""))) {
                if (m.name().length() < temp) {
                    tmp = m;
                    temp = m.name().length();
                }
            }
        }
        if (tmp != null) {
            matID = tmp.getId();
        }
        return matID;
    }
}
