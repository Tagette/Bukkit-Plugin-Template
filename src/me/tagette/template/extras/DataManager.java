package me.tagette.template.extras;

import com.alta189.sqllitelib.SQLCore;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.tagette.template.TLogger;
import me.tagette.template.Template;
import me.tagette.template.extras.DataField.DataFieldType;

/**
 * @description Handles sqllite database connection
 * @author Tagette
 */
public class DataManager {

    private Template plugin;
    private SQLCore dbCore;
    private String selectedTable;
    private String lastRetrievedTable;
    private ResultSet lastDataRetrieved;
    private String playerFieldName;

    public DataManager(Template instance) {
        plugin = instance;
        dbCore = new SQLCore(TLogger.getLog(), TLogger.getPrefix(), Template.name,
                plugin.getDataFolder().getPath() + "/Data");
        dbCore.initialize();
        selectedTable = "";
        lastRetrievedTable = "";
        playerFieldName = "playername";
    }

    // Use for more advanced database querys
    public SQLCore getDbCore() {
        return dbCore;
    }

    public boolean createTable(String tableName) {
        boolean wasCreated = false;
        if (!tableName.isEmpty()) {
            if (!tableExists(tableName)) {
                String query = "CREATE TABLE '" + tableName + "' ( "
                        + "'id' INTEGER PRIMARY KEY);";
                wasCreated = dbCore.createTable(query);
            }
        } else {
            TLogger.error("Database.CreateTable: Could not create table because table name was empty.");
        }
        return wasCreated;
    }

    public boolean deleteTable(String tableName) {
        boolean wasDeleted = false;
        if (!tableName.isEmpty()) {
            String query = "DROP TABLE '" + tableName + "'";
            wasDeleted = dbCore.updateQuery(query);
        } else {
            TLogger.error("Database.DeleteTable: Could not delete table because table name was empty.");
        }
        return wasDeleted;
    }

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

    public String getSelectedTable() {
        return selectedTable;
    }

    public boolean tableSelected() {
        return !selectedTable.equals("");
    }

    public boolean tableSelected(String tableName) {
        return selectedTable.equals(tableName);
    }

    public boolean addIntField(String name, int defaultValue, int maxLength, boolean allowNull) {
        return addTableField(new DataField(name, String.valueOf(defaultValue), maxLength,
                0, allowNull, DataFieldType.INT));
    }

    public boolean addLongField(String name, long defaultValue, int maxLength, boolean allowNull) {
        return addTableField(new DataField(name, String.valueOf(defaultValue), maxLength,
                0, allowNull, DataFieldType.LONG));
    }

    public boolean addDoubleField(String name, double defaultValue, int maxLength, int maxDecimals, boolean allowNull) {
        return addTableField(new DataField(name, String.valueOf(defaultValue), maxLength,
                maxDecimals, allowNull, DataFieldType.DOUBLE));
    }

    public boolean addStringField(String name, String defaultValue, int maxLength, boolean allowNull) {
        return addTableField(new DataField(name, defaultValue, maxLength, 0, allowNull,
                DataFieldType.STRING));
    }

    public boolean addTextField(String name, String defaultValue, int maxLength, boolean allowNull) {
        return addTableField(new DataField(name, defaultValue, maxLength, 0, allowNull,
                DataFieldType.TEXT));
    }

    public boolean addBooleanField(String name, boolean defaultValue, boolean allowNull) {
        return addTableField(new DataField(name, defaultValue ? "1" : "0", 1, 0,
                allowNull, DataFieldType.STRING));
    }

    public boolean addTableField(DataField field) {
        return addTableField(field, false);
    }

    // Adds a field to the selected table
    private boolean addTableField(DataField field, boolean overwrite) {
        boolean wasAdded = false;
        if (field != null) {
            if (tableSelected()) {
                if (!field.Name.equalsIgnoreCase(playerFieldName) || overwrite) {
                    if (!fieldExists(field.Name)) {
                        String query = "ALTER TABLE '" + selectedTable + "' ADD '" + field.Name + "'";
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
                        query += " " + (field.AllowNull ? "" : "NOT NULL");
                        wasAdded = dbCore.updateQuery(query);
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

    // Used on a already existing field. Useful when field already has values that should be kept. 
    // Assuming the data type isn't changed of course.
    public boolean editTableField(DataField field) {
        boolean wasAdded = false;
        if (field != null) {
            if (tableSelected()) {
                if (!field.Name.equalsIgnoreCase(playerFieldName)) {
                    if (fieldExists(field.Name)) {
                        String query = "ALTER TABLE '" + selectedTable + "' ALTER COLUMN '" + field.Name + "'";
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
                        wasAdded = dbCore.updateQuery(query);
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
        return wasAdded;
    }

    public boolean removeTableField(String fieldName) {
        boolean wasRemoved = false;
        if (!fieldName.isEmpty()) {
            if (tableSelected()) {
                if (!fieldName.equalsIgnoreCase(playerFieldName)) {
                    String query = "ALTER TABLE '" + selectedTable + "' DROP COLUMN '" + fieldName + "'";
                    wasRemoved = dbCore.updateQuery(query);
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
                ResultSet results = dbCore.sqlQuery("SELECT * FROM " + selectedTable
                        + " WHERE '" + playerFieldName + "' = '" + player + "'");
                if (results.next()) {
                    lastDataRetrieved = results;
                    lastRetrievedTable = selectedTable;
                    get = results.getInt(fieldToGet);
                }
            }
        } catch (SQLException se2) {
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
                ResultSet results = dbCore.sqlQuery("SELECT * FROM " + selectedTable
                        + " WHERE '" + playerFieldName + "' = '" + player + "'");
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
                ResultSet results = dbCore.sqlQuery("SELECT * FROM " + selectedTable
                        + " WHERE '" + playerFieldName + "' = '" + player + "'");
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
                ResultSet results = dbCore.sqlQuery("SELECT * FROM " + selectedTable
                        + " WHERE '" + playerFieldName + "' = '" + player + "'");
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
                ResultSet results = dbCore.sqlQuery("SELECT * FROM " + selectedTable
                        + " WHERE '" + playerFieldName + "' = '" + player + "'");
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
        if (!userHasData(player)) {
            addUserToData(player);
        }
        boolean exists = fieldExists(fieldToSet);
        if (exists) {
            update("UPDATE '" + selectedTable + "' SET '" + fieldToSet + "' = '" + value + "' WHERE '" + playerFieldName + "' = '" + player + "'");
        } else {
            TLogger.error("Database.UpdateIntByName: Could not set field value "
                    + "because field doesn't exist. (table: " + selectedTable
                    + " field: " + fieldToSet + ")");
        }
    }

    public void setLongByName(String fieldToSet, String player, long value) {
        if (!userHasData(player)) {
            addUserToData(player);
        }
        boolean exists = fieldExists(fieldToSet);
        if (exists) {
            update("UPDATE '" + selectedTable + "' SET '" + fieldToSet + "' = '" + value + "' WHERE '" + playerFieldName + "' = '" + player + "'");
        } else {
            TLogger.error("Database.UpdateLongByName: Could not set field value "
                    + "because field doesn't exist. (table: " + selectedTable
                    + " field: " + fieldToSet + ")");
        }
    }

    public void setDoubleByName(String fieldToSet, String player, double value) {
        if (!userHasData(player)) {
            addUserToData(player);
        }
        boolean exists = fieldExists(fieldToSet);
        if (exists) {
            update("UPDATE '" + selectedTable + "' SET '" + fieldToSet + "' = '" + value + "' WHERE '" + playerFieldName + "' = '" + player + "'");
        } else {
            TLogger.error("Database.UpdateDoubleByName: Could not set field value "
                    + "because field doesn't exist. (table: " + selectedTable
                    + " field: " + fieldToSet + ")");
        }
    }

    public void setStringByName(String fieldToSet, String player, String value) {
        if (!userHasData(player)) {
            addUserToData(player);
        }
        boolean exists = fieldExists(fieldToSet);
        if (exists) {
            update("UPDATE '" + selectedTable + "' SET '" + fieldToSet + "' = '" + value + "' WHERE '" + playerFieldName + "' = '" + player + "'");
        } else {
            TLogger.error("Database.UpdateStringByName: Could not set field value "
                    + "because field doesn't exist. (table: " + selectedTable
                    + " field: " + fieldToSet + ")");
        }
    }

    public void setTextByName(String fieldToSet, String player, String value) {
        setStringByName(fieldToSet, player, value);
    }

    public void setBoolByName(String fieldToSet, String player, boolean value) {
        if (!userHasData(player)) {
            addUserToData(player);
        }
        boolean exists = fieldExists(fieldToSet);
        if (exists) {
            update("UPDATE '" + selectedTable + "' SET '" + fieldToSet + "' = '" + (value ? 1 : 0) + "' WHERE '" + playerFieldName + "' = '" + player + "'");
        } else {
            TLogger.error("Database.UpdateBoolByName: Could not set field value "
                    + "because field doesn't exist. (table: " + selectedTable
                    + " field: " + fieldToSet + ")");
        }
    }

    public boolean addUserToData(String player) {
        boolean wasAdded = false;
        boolean canAdd = false;
        if (!fieldExists(playerFieldName)) {
            DataField playerField = new DataField(playerFieldName, null, 30, 0, true, DataFieldType.STRING);
            canAdd = addTableField(playerField, true);
            if (canAdd) {
                TLogger.info("Field added to database for user support. (" + playerFieldName + ")");
            }
        } else {
            canAdd = true;
        }
        if (canAdd) {
            // Adds player to data and fills default values for fields.
            wasAdded = insert("INSERT INTO '" + selectedTable + "' ('" + playerFieldName + "') VALUES ('" + player + "')");
        }
        return wasAdded;
    }

    // Removes all data for a player.
    public boolean removeDataByName(String player, String value) {
        return update("DELETE FROM '" + selectedTable + "' WHERE '" + playerFieldName + "' = '" + player + "'");
    }

    public boolean userHasData(String player) {
        boolean hasData = false;
        try {
            String query = "SELECT * FROM '" + selectedTable + "' WHERE '" + playerFieldName + "' = '" + player + "'";
            ResultSet results = query(query);
            hasData = results.next();
        } catch (SQLException se) {
        }
        return hasData;
    }

    public boolean update(String query) {
        return dbCore.updateQuery(query);
    }

    public boolean insert(String query) {
        return dbCore.insertQuery(query);
    }

    public ResultSet query(String query) {
        return dbCore.sqlQuery(query);
    }

    public boolean tableExists(String tableName) {
        return dbCore.checkTable(tableName);
    }

    public boolean fieldExists(String fieldName) {
        return dbCore.checkField(selectedTable, fieldName);
    }
}
