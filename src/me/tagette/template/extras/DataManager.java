package me.tagette.template.extras;

import com.alta189.sqlLibrary.SQL.SQLCore;
import com.alta189.sqlLibrary.SQL.SQLCore.SQLMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.tagette.template.TLogger;
import me.tagette.template.TSettings;
import me.tagette.template.Template;
import me.tagette.template.extras.DataField.DataFieldType;

/**
 * @description Handles database connections
 * @author Tagette
 */
public class DataManager {

    private Template plugin;
    private SQLCore dbCore;
    private String selectedTable;
    private String lastRetrievedTable;
    private ResultSet lastDataRetrieved;
    private String playerFieldName;

    /*
     * Initializes the DataManager class.
     * 
     * @param instance  An instance of the plugin's main class.
     */
    public DataManager(Template instance, SQLMode mode) {
        plugin = instance;
        if (mode == SQLMode.MySQL) {
            dbCore = new SQLCore(TLogger.getLog(), TLogger.getPrefix(), TSettings.MySQLHost,
                    TSettings.MySQLUser, TSettings.MySQLPass, TSettings.MySQLDBName);
        } else if (mode == SQLMode.SQLite) {
            dbCore = new SQLCore(TLogger.getLog(), TLogger.getPrefix(),
                    plugin.getDataFolder().getPath() + "/Data", Template.name);
        }
        if (dbCore.initialize()) {
            TLogger.info("Database connection established.");
        }
        selectedTable = "";
        lastRetrievedTable = "";
        playerFieldName = "playername";
    }

    /*
     * Used for more advanced database interactions.
     */
    public SQLCore getDbCore() {
        return dbCore;
    }

    /*
     * Used to create a table in the database.
     */
    public boolean createTable(String tableName) {
        boolean wasCreated = false;
        if (!tableName.isEmpty()) {
            if (!tableExists(tableName)) {
                String query = "CREATE TABLE " + tableName + " (id INT NOT NULL DEFAULT '0', PRIMARY KEY (id))";
                wasCreated = dbCore.createTable(query);
            }
        } else {
            TLogger.error("Database.CreateTable: Could not create table because table name was empty.");
        }
        return wasCreated;
    }

    /*
     * Deletes a table from the database.
     */
    public boolean deleteTable(String tableName) {
        boolean wasDeleted = false;
        if (!tableName.isEmpty()) {
            String query = "DROP TABLE '" + tableName + "'";
            wasDeleted = dbCore.deleteQuery(query);
        } else {
            TLogger.error("Database.DeleteTable: Could not delete table because table name was empty.");
        }
        return wasDeleted;
    }

    /*
     * Selects a table in the database.
     * 
     * @param tableName The name of the table to select.
     */
    public boolean selectTable(String tableName) {
        boolean isSelected = false;
        if (!tableName.isEmpty() && dbCore.checkTable(tableName)) {
            isSelected = true;
            selectedTable = tableName;
        } else {
            selectedTable = "";
            TLogger.error("Database.SelectTable: Could not select table because table name was empty.");
        }
        return isSelected;
    }

    /*
     * Gets the selected table.
     */
    public String getSelectedTable() {
        return selectedTable;
    }

    /*
     * Checks if there is a table currently selected.
     */
    public boolean tableSelected() {
        return !selectedTable.equals("");
    }

    /*
     * Check is if a specific table has been selected.
     * 
     * @param tableName The name of the table to be confirmed is selected.
     */
    public boolean tableSelected(String tableName) {
        return selectedTable.equals(tableName);
    }

    /*
     * Adds an integer field to the selected table.
     */
    public boolean addIntField(String name, int defaultValue, int maxLength, boolean allowNull) {
        return addTableField(new DataField(name, String.valueOf(defaultValue), maxLength,
                0, allowNull, DataFieldType.INT));
    }

    /*
     * Adds an long field to the selected table.
     */
    public boolean addLongField(String name, long defaultValue, int maxLength, boolean allowNull) {
        return addTableField(new DataField(name, String.valueOf(defaultValue), maxLength,
                0, allowNull, DataFieldType.LONG));
    }

    /*
     * Adds an double field to the selected table.
     */
    public boolean addDoubleField(String name, double defaultValue, int maxLength, int maxDecimals, boolean allowNull) {
        return addTableField(new DataField(name, String.valueOf(defaultValue), maxLength,
                maxDecimals, allowNull, DataFieldType.DOUBLE));
    }

    /*
     * Adds an String field to the selected table.
     */
    public boolean addStringField(String name, String defaultValue, int maxLength, boolean allowNull) {
        return addTableField(new DataField(name, defaultValue, maxLength, 0, allowNull,
                DataFieldType.STRING));
    }

    /*
     * Adds an text field to the selected table.
     */
    public boolean addTextField(String name, int maxLength, boolean allowNull) {
        return addTableField(new DataField(name, null, maxLength, 0, allowNull,
                DataFieldType.TEXT));
    }

    /*
     * Adds an boolean field to the selected table.
     */
    public boolean addBooleanField(String name, boolean defaultValue, boolean allowNull) {
        return addTableField(new DataField(name, defaultValue ? "1" : "0", 1, 0,
                allowNull, DataFieldType.STRING));
    }

    /*
     * Adds an custom field to the selected table.
     * 
     * @param field An instance of a custom created field.
     */
    public boolean addTableField(DataField field) {
        return addTableField(field, false);
    }

    /*
     * Adds a field to the selected table.&nbsp;Not recommended to use.
     * 
     * @param field An instance of a created field.
     * @param overwrite Specifies whether the function should overwrite the standard playerFieldName.
     */
    private boolean addTableField(DataField field, boolean overwrite) {
        boolean wasAdded = false;
        if (field != null) {
            if (tableSelected()) {
                if (!field.Name.equalsIgnoreCase(playerFieldName) || overwrite) {
                    if (!fieldExists(field.Name)) {
                        String query = "ALTER TABLE " + selectedTable + " ADD " + field.Name;
                        switch (field.Type) {
                            case INT:
                                query += " INT";
                                break;
                            case LONG:
                                query += " LONG";
                                break;
                            case DOUBLE:
                                query += " DOUBLE";
                                break;
                            case STRING:
                                query += " VARCHAR";
                                break;
                            case TEXT:
                                query += " TEXT";
                                break;
                            case BOOL:
                                query += " INT";
                                break;
                        }
                        query += "(" + String.valueOf(field.MaxLength) + ")";
                        query += (field.DefaultValue != null && !field.DefaultValue.isEmpty() ? " DEFAULT '" + field.DefaultValue + "'" : "");
                        query += " " + (field.AllowNull ? "" : "NOT NULL");
                        wasAdded = dbCore.createTable(query);
                    }
                } else {
                    TLogger.error("Database.AddTableField: Could not add field "
                            + "because field name can not be the same as \"playername\".");
                }
            } else {
                TLogger.error("Database.RemoveTableField: Could not add field "
                        + "because no table was selected. (table: " + selectedTable
                        + " field: " + field.Name + ")");
            }
        } else {
            TLogger.error("Database.AddTableField: Could not add field because field was null.");
        }
        return wasAdded;
    }

    /*
     * Edits an already existing field.&nbsp;Useful when field already has values that should be kept.
     * Assuming the data type is not changed of course.
     * 
     * @param field An instance of a created field.
     */
    public boolean editTableField(DataField field) {
        boolean wasEdited = false;
        if (field != null) {
            if (tableSelected()) {
                if (!field.Name.equalsIgnoreCase(playerFieldName)) {
                    if (fieldExists(field.Name)) {
                        String query = "ALTER TABLE " + selectedTable + " ALTER COLUMN " + field.Name;
                        switch (field.Type) {
                            case INT:
                                query += " INT";
                                break;
                            case LONG:
                                query += " LONG";
                                break;
                            case DOUBLE:
                                query += " DOUBLE";
                                break;
                            case STRING:
                                query += " VARCHAR";
                                break;
                            case TEXT:
                                query += " TEXT";
                                break;
                            case BOOL:
                                query += " INT";
                                break;
                        }
                        query += "(" + String.valueOf(field.MaxLength) + ")";
                        query += " DEFAULT '" + field.DefaultValue + "'";
                        query += " " + (field.AllowNull ? "IS NULL" : "IS NOT NULL");
                        wasEdited = dbCore.createTable(query);
                    }
                } else {
                    TLogger.error("Database.EditTableField: Could not edit field "
                            + "because field name can not be the same as \"playername\".");
                }
            } else {
                TLogger.error("Database.RemoveTableField: Could not edit field "
                        + "because no table was selected. (table: " + selectedTable
                        + " field: " + field.Name + ")");
            }
        } else {
            TLogger.error("Database.EditTableField: Could not edit field because field was null.");
        }
        return wasEdited;
    }

    public boolean removeTableField(String fieldName) {
        boolean wasRemoved = false;
        if (!fieldName.isEmpty()) {
            if (tableSelected()) {
                if (!fieldName.equalsIgnoreCase(playerFieldName)) {
                    String query = "ALTER TABLE " + selectedTable + " DROP COLUMN " + fieldName;
                    wasRemoved = execute(query);
                } else {
                    TLogger.error("Database.RemoveTableField: Could not add field "
                            + "because field name can not be the same as \"playername\".");
                }
            } else {
                TLogger.error("Database.RemoveTableField: Could not remove field "
                        + "because no table was selected. (table: " + selectedTable
                        + " field: " + fieldName + ")");
            }
        } else {
            TLogger.error("Database.RemoveTableField: Could not remove field because field name was empty.");
        }
        return wasRemoved;
    }

    public int getIntByName(String fieldToGet, String player) {
        int get = -1;
        boolean existed = false;
        try {
            if (lastDataRetrieved != null && lastRetrievedTable.equals(selectedTable)) {
                get = lastDataRetrieved.getInt(fieldToGet);
                existed = true;
            }
        } catch (SQLException se) {
        }
        try {
            if (!existed) {
                ResultSet results = query("SELECT * FROM " + selectedTable
                        + " WHERE " + playerFieldName + " = '" + player + "'");
                if (results.next()) {
                    lastDataRetrieved = results;
                    lastRetrievedTable = selectedTable;
                    get = results.getInt(fieldToGet);
                }
            }
        } catch (SQLException se) {
            TLogger.error("Database.getIntByName");
        }
        return get;
    }

    public long getLongByName(String fieldToGet, String player) {
        long get = -1;
        boolean existed = false;
        try {
            if (lastDataRetrieved != null && lastRetrievedTable.equals(selectedTable)) {
                get = lastDataRetrieved.getLong(fieldToGet);
                existed = true;
            }
        } catch (SQLException se) {
        }
        try {
            if (!existed) {
                ResultSet results = query("SELECT * FROM " + selectedTable
                        + " WHERE " + playerFieldName + " = '" + player + "'");
                if (results.next()) {
                    lastDataRetrieved = results;
                    lastRetrievedTable = selectedTable;
                    get = results.getLong(fieldToGet);
                }
            }
        } catch (SQLException se) {
        }
        return get;
    }

    public double getDoubleByName(String fieldToGet, String player) {
        double get = -1;
        boolean existed = false;
        try {
            if (lastDataRetrieved != null && lastRetrievedTable.equals(selectedTable)) {
                get = lastDataRetrieved.getDouble(fieldToGet);
                existed = true;
            }
        } catch (SQLException se) {
        }
        try {
            if (!existed) {
                ResultSet results = query("SELECT * FROM " + selectedTable
                        + " WHERE " + playerFieldName + " = '" + player + "'");
                if (results.next()) {
                    lastDataRetrieved = results;
                    lastRetrievedTable = selectedTable;
                    get = results.getDouble(fieldToGet);
                }
            }
        } catch (SQLException se) {
        }
        return get;
    }

    public String getStringByName(String fieldToGet, String player) {
        String get = "";
        boolean existed = false;
        try {
            if (lastDataRetrieved != null && lastRetrievedTable.equals(selectedTable)) {
                get = lastDataRetrieved.getString(fieldToGet);
                existed = true;
            }
        } catch (SQLException se) {
        }
        try {
            if (!existed) {
                ResultSet results = query("SELECT * FROM " + selectedTable
                        + " WHERE " + playerFieldName + " = '" + player + "'");
                if (results.next()) {
                    lastDataRetrieved = results;
                    lastRetrievedTable = selectedTable;
                    get = results.getString(fieldToGet);
                }
            }
        } catch (SQLException se) {
        }
        return get;
    }

    public String getTextByName(String fieldToGet, String player) {
        return getStringByName(fieldToGet, player);
    }

    public boolean getBoolByName(String fieldToGet, String player) {
        boolean get = false;
        boolean existed = false;
        try {
            if (lastDataRetrieved != null && lastRetrievedTable.equals(selectedTable)) {
                get = lastDataRetrieved.getInt(fieldToGet) == 1;
                existed = true;
            }
        } catch (SQLException se) {
        }
        try {
            if (!existed) {
                ResultSet results = query("SELECT * FROM " + selectedTable
                        + " WHERE " + playerFieldName + " = '" + player + "'");
                if (results.next()) {
                    lastDataRetrieved = results;
                    lastRetrievedTable = selectedTable;
                    get = results.getInt(fieldToGet) == 1;
                }
            }
        } catch (SQLException se) {
        }
        return get;
    }

    public void setIntByName(String fieldToSet, String player, int value) {
        addPlayerNameField();
        if (!userHasData(player)) {
            addUserToData(player);
        }
        if (fieldExists(fieldToSet)) {
            update("UPDATE " + selectedTable + " SET " + fieldToSet + " = '" + value + "' WHERE " + playerFieldName + " = '" + player + "'");
        } else {
            TLogger.error("Database.UpdateIntByName: Could not set field value "
                    + "because field doesn't exist. (table: " + selectedTable
                    + " field: " + fieldToSet + ")");
        }
    }

    public void setLongByName(String fieldToSet, String player, long value) {
        addPlayerNameField();
        if (!userHasData(player)) {
            addUserToData(player);
        }
        if (fieldExists(fieldToSet)) {
            update("UPDATE " + selectedTable + " SET " + fieldToSet + " = '" + value + "' WHERE " + playerFieldName + " = '" + player + "'");
        } else {
            TLogger.error("Database.UpdateLongByName: Could not set field value "
                    + "because field doesn't exist. (table: " + selectedTable
                    + " field: " + fieldToSet + ")");
        }
    }

    public void setDoubleByName(String fieldToSet, String player, double value) {
        addPlayerNameField();
        if (!userHasData(player)) {
            addUserToData(player);
        }
        if (fieldExists(fieldToSet)) {
            update("UPDATE " + selectedTable + " SET " + fieldToSet + " = '" + value + "' WHERE " + playerFieldName + " = '" + player + "'");
        } else {
            TLogger.error("Database.UpdateDoubleByName: Could not set field value "
                    + "because field doesn't exist. (table: " + selectedTable
                    + " field: " + fieldToSet + ")");
        }
    }

    public void setStringByName(String fieldToSet, String player, String value) {
        addPlayerNameField();
        if (!userHasData(player)) {
            addUserToData(player);
        }
        if (fieldExists(fieldToSet)) {
            update("UPDATE " + selectedTable + " SET " + fieldToSet + " = '" + value + "' WHERE " + playerFieldName + " = '" + player + "'");
        } else {
            TLogger.error("Database.UpdateStringByName: Could not set field value "
                    + "because field doesn't exist. (table: " + selectedTable
                    + " field: " + fieldToSet + ")");
        }
    }

    public void setTextByName(String fieldToSet, String player, String value) {
        addPlayerNameField();
        setStringByName(fieldToSet, player, value);
    }

    public void setBoolByName(String fieldToSet, String player, boolean value) {
        addPlayerNameField();
        if (!userHasData(player)) {
            addUserToData(player);
        }
        if (fieldExists(fieldToSet)) {
            update("UPDATE " + selectedTable + " SET " + fieldToSet + " = '" + (value ? 1 : 0) + "' WHERE " + playerFieldName + " = '" + player + "'");
        } else {
            TLogger.error("Database.UpdateBoolByName: Could not set field value "
                    + "because field doesn't exist. (table: " + selectedTable
                    + " field: " + fieldToSet + ")");
        }
    }

    public boolean addUserToData(String player) {
        boolean wasAdded = false;
        // Adds player to data and fills default values for fields.
        wasAdded = insert("INSERT INTO " + selectedTable + " (" + playerFieldName + ") VALUES ('" + player + "')");
        if(!wasAdded) {
            TLogger.error("Database.addUserToData: Could not add user to data.");
        }
        return wasAdded;
    }

    // Removes all data for a player.
    public boolean removeDataByName(String player, String value) {
        return update("DELETE FROM " + selectedTable + " WHERE " + playerFieldName + " = '" + player + "'");
    }

    public boolean userHasData(String player) {
        boolean hasData = false;
        try {
            String query = "SELECT * FROM " + selectedTable + " WHERE " + playerFieldName + " = '" + player + "'";
            ResultSet results = query(query);
            hasData = results.next();
        } catch (SQLException se) {
            TLogger.error("Database.userHasData: SQL Error: " + se);
        }
        return hasData;
    }
    
    public boolean addPlayerNameField(){
        boolean added = true;
        if (!fieldExists(playerFieldName)) {
            DataField playerField = new DataField(playerFieldName, null, 30, 0, true, DataFieldType.STRING);
            added = addTableField(playerField, true);
            if (added) {
                TLogger.info("Field added to database for user support. (" + playerFieldName + ")");
            }
        }
        return added;
    }

    public boolean execute(String query) {
        if (plugin.debugging) {
            TLogger.info("Database.execute Query: \"" + query + "\"");
        }
        return dbCore.createTable(query);
    }

    public boolean update(String query) {
        if (plugin.debugging) {
            TLogger.info("Database.update Query: \"" + query + "\"");
        }
        return dbCore.updateQuery(query);
    }

    public boolean insert(String query) {
        if (plugin.debugging) {
            TLogger.info("Database.insert Query: \"" + query + "\"");
        }
        return dbCore.insertQuery(query);
    }

    public ResultSet query(String query) {
        if (plugin.debugging) {
            TLogger.info("Database.query Query: \"" + query + "\"");
        }
        return dbCore.sqlQuery(query);
    }

    public boolean tableExists(String tableName) {
        return dbCore.checkTable(tableName);
    }

    public boolean fieldExists(String fieldName) {
        return dbCore.checkField(selectedTable, fieldName);
    }
}
