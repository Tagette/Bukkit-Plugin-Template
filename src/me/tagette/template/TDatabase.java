package me.tagette.template;

import com.alta189.sqlLibrary.SQL.SQLCore;
import com.alta189.sqlLibrary.SQL.SQLCore.SQLMode;
import me.tagette.template.extras.DataField;
import me.tagette.template.extras.DataField.DataFieldType;
import me.tagette.template.extras.DataManager;

/**
 * @description Handles SQL database connection
 * @author Tagette
 */
public class TDatabase {
    
    private static Template plugin;
    public static DataManager dbm;
    
    /*
     * Initializes the plugins database connection.
     * 
     * @param instance  An instance of the plugin's main class.
     */
    public static void initialize(Template instance){
        TDatabase.plugin = instance;
        SQLMode dataMode;
        if(TSettings.useMySQL)
            dataMode = SQLMode.MySQL;
        else
            dataMode = SQLMode.SQLite;
        dbm = new DataManager(plugin, dataMode);
        
        // Create database here
        
        // -- Example --
        // This will create a table with the name "players".
        // No need to check if table or field already exists.
        if(dbm.createTable("players"))
            TLogger.info("Table created. (players)");
        // Must select table.
        dbm.selectTable("players");
        // The field called "name" is neccessary to use the get and set methods 
        //   in the Datamanager Class.
        // A new string field with the field name of "nickname", the default value 
        //     of null, the max length of 15 and null is allowed.
        if(dbm.addStringField("nickname", null, 15, true))
            TLogger.info("Field created in table " + dbm.getSelectedTable() + ". (nickname)");
        // A new integer field with the field name of "awesomeness", the default
        //   value of 0, the max length of 11 and null is not allowed.
        if(dbm.addIntField("awesomeness", 0, 11, false))
            TLogger.info("Field created in table " + dbm.getSelectedTable() + ". (awesomeness)");
        // A new custom field with the field name of "custom", the default value
        //   of "Oh hai!", the max length of 7, 0 decimals, null is allowed, and the field type of TEXT.
        DataField custom = new DataField("custom", null, 7, 0, true, 
                DataFieldType.TEXT);
        if(dbm.addTableField(custom))
            TLogger.info("Field created in table " + dbm.getSelectedTable() + ". (custom)");
        
        // An example on how to get and set data in the database 
        //   using the DataManager is in the CommandListenerClass
    }
    
    /*
     * Closes the connection to the database.
     */
    public static void disable(){
        dbm.getDbCore().close();
    }
    
    /*
     * Gets the Database core.
     * Used for more advanced databasing. :)
     */
    public static SQLCore getCore() {
        return dbm.getDbCore();
    }
}
