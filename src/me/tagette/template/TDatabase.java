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
        String tableQuery = "CREATE TABLE players ("
                + "id INT(11) NOT NULL DEFAULT '0',"
                + "name VARCHAR(30),"
                + "awesomeness INT(11) DEFAULT '0',"
                + "PRIMARY KEY (id))";
        if(!dbm.tableExists("players") && dbm.createTable(tableQuery))
            TLogger.info("Table created. (players)");
        
        // An example on how to get and set data in the database
        //   using the DataManager is in the TemplateCmd Class
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
