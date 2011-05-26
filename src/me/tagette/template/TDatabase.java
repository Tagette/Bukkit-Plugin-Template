package me.tagette.template;

import com.alta189.sqllitelib.SQLCore;
import me.tagette.template.extras.DataField;
import me.tagette.template.extras.DataField.DataFieldType;
import me.tagette.template.extras.DataManager;

/**
 * @description Handles sqllite database connection
 * @author Tagette
 */
public class TDatabase {
    
    private static Template plugin;
    public static DataManager dbm;
    
    public static void initialize(Template instance){
        TDatabase.plugin = instance;
        dbm = new DataManager(plugin);
        
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
        DataField custom = new DataField("custom", "Oh hai!", 7, 0, true, 
                DataFieldType.TEXT);
        if(dbm.addTableField(custom))
            TLogger.info("Field created in table " + dbm.getSelectedTable() + ". (custom)");
        
        // An example on how to get and set data in the database 
        //   using the DataManager is in the CommandListenerClass
    }
    
    // Closes the connection to the database
    public static void disable(){
        dbm.getDbCore().close();
    }
    
    // Gets the Database core
    // Used for more advance databases.
    public static SQLCore getCore() {
        return dbm.getDbCore();
    }
}
