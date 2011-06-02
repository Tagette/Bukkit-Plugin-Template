package me.tagette.template.extras;

import java.io.*;
import java.util.*;
import me.tagette.template.TLogger;

/**
 * @description Manipulates a properties file
 * @author TaylorKelly
 */
public class PropertiesFile {

    private List<PropertyEntry> map;
    private File file;
    private boolean modified;

    public PropertiesFile(File file) {
        this.file = file;
        map = new ArrayList<PropertyEntry>();
        Scanner scan;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (!line.contains("=")) {
                    continue;
                }
                int equals = line.indexOf("=");
                int commentIndex = line.length();
                if (line.contains("#")) {
                    commentIndex = line.indexOf("#");
                }

                String key = line.substring(0, equals).trim();
                if (key.equals("")) {
                    continue;
                }
                String value = line.substring(equals + 1, commentIndex).trim();
                String comment = "";
                if (commentIndex < line.length() - 1) {
                    comment = line.substring(commentIndex + 1, line.length()).trim();
                }
                addEntry(key, value, comment);
            }
        } catch (FileNotFoundException e) {
            TLogger.error("Cannot read file " + file.getName()
                    + ". Remove the file and run server again to get a default file.");
        } catch (IOException e) {
            TLogger.error("Cannot create file " + file.getName() + ".");
        }
    }

    public void addEntry(String key, String value, String comment) {
        map.add(new PropertyEntry(key, value, comment));
    }

    public PropertyEntry getEntry(String key) {
        PropertyEntry entry = null;
        for (PropertyEntry entries : map) {
            if (entries.key.equalsIgnoreCase(key)) {
                entry = entries;
            }
        }
        return entry;
    }

    public boolean containsEntry(String key) {
        boolean found = false;
        for (PropertyEntry entries : map) {
            if (entries.key.equalsIgnoreCase(key)) {
                found = true;
            }
        }
        return found;
    }

    public boolean getBoolean(String key, Boolean defaultValue, String defaultComment) {
        if (containsEntry(key)) {
            return Boolean.parseBoolean(getEntry(key).value);
        } else {
            addEntry(key, defaultValue.toString(), defaultComment);
            modified = true;
            return defaultValue;
        }
    }

    public String getString(String key, String defaultValue, String defaultComment) {
        if (containsEntry(key)) {
            return getEntry(key).value;
        } else {
            addEntry(key, defaultValue.toString(), defaultComment);
            modified = true;
            return defaultValue;
        }
    }

    public int getInt(String key, Integer defaultValue, String defaultComment) {
        if (containsEntry(key)) {
            try {
                return Integer.parseInt(getEntry(key).value);
            } catch (Exception e) {
                TLogger.warning("Trying to get Integer from " + key + ": " + getEntry(key).value);
                return 0;
            }
        } else {
            addEntry(key, defaultValue.toString(), defaultComment);
            modified = true;
            return defaultValue;
        }
    }

    public long getLong(String key, Long defaultValue, String defaultComment) {
        if (containsEntry(key)) {
            try {
                return Long.parseLong(getEntry(key).value);
            } catch (Exception e) {
                TLogger.warning("Trying to get Long from " + key + ": " + getEntry(key).value);
                return 0;
            }
        } else {
            addEntry(key, defaultValue.toString(), defaultComment);
            modified = true;
            return defaultValue;
        }
    }

    public double getDouble(String key, Double defaultValue, String defaultComment) {
        if (containsEntry(key)) {
            try {
                return Double.parseDouble(getEntry(key).value);
            } catch (Exception e) {
                TLogger.warning("Trying to get Double from " + key + ": " + getEntry(key).value);
                return 0;
            }
        } else {
            addEntry(key, defaultValue.toString(), defaultComment);
            modified = true;
            return defaultValue;
        }
    }

    public void setDouble(String key, Double globalMemory, String defaultComment) {
        if (containsEntry(key)) {
            PropertyEntry entry = getEntry(key);
            entry.value = globalMemory.toString();
        } else {
            addEntry(key, globalMemory.toString(), defaultComment);
        }
        modified = true;
    }

    public void save() {
        if (modified) {
            BufferedWriter bwriter = null;
            FileWriter fwriter = null;
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                fwriter = new FileWriter(file);
                bwriter = new BufferedWriter(fwriter);
                for (PropertyEntry entry : map) {
                    StringBuilder builder = new StringBuilder();
                    builder.append(entry.key);
                    builder.append(" = ");
                    builder.append(entry.value);
                    if (!entry.comment.equals("")) {
                        builder.append("   #");
                        builder.append(entry.comment);
                    }
                    bwriter.write(builder.toString());
                    bwriter.newLine();
                }
                bwriter.flush();
            } catch (IOException e) {
                TLogger.error("IO Exception with file " + file.getName());
            } finally {
                try {
                    if (bwriter != null) {
                        bwriter.flush();
                        bwriter.close();
                    }
                    if (fwriter != null) {
                        fwriter.close();
                    }
                } catch (IOException e) {
                    TLogger.error("IO Exception with file " + file.getName() + " (on close)");
                }
            }
        }
    }

    private class PropertyEntry {

        public String key;
        public String value;
        public String comment;

        public PropertyEntry(String key, String value, String comment) {
            this.key = key;
            this.value = value;
            this.comment = comment;
        }
    }
}