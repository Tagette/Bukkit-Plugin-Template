package com.alta189.sqllitelib;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {
    /*
     * @author: alta189
     * 
     */

    private SQLCore core;
    private Connection connection;
    private File SQLFile;

    public DatabaseHandler(SQLCore core, File SQLFile) {
        this.core = core;
        this.SQLFile = SQLFile;
    }

    public Connection getConnection() {
        if (connection == null) {
            initialize();
        }
        return connection;
    }

    public void closeConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException ex) {
                this.core.writeError("Error on Connection close: " + ex, true);
            }
        }
    }

    public Boolean initialize() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + SQLFile.getAbsolutePath());
            return true;
        } catch (SQLException ex) {
            core.writeError("SQLite exception on initialize " + ex, true);
        } catch (ClassNotFoundException ex) {
            core.writeError("You need the SQLite library " + ex, true);
        }
        return false;
    }

    public Boolean createTable(String query) {
        try {
            if (query == null) {
                core.writeError("SQL Create Table query empty.", true);
                return false;
            }

            Statement statement = connection.createStatement();
            statement.execute(query);
            return true;
        } catch (SQLException ex) {
            core.writeError(ex.getMessage(), true);
            return false;
        }
    }

    public ResultSet sqlQuery(String query) {
        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();

            ResultSet result = statement.executeQuery(query);

            return result;
        } catch (SQLException ex) {
            core.writeError("Error at SQL Query: " + ex.getMessage(), false);
        }
        return null;
    }

    public boolean insertQuery(String query) {
        boolean success = false;
        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();

            statement.execute(query);
            
            success = true;
        } catch (SQLException ex) {
            core.writeError("Error at SQL Query: " + ex.getMessage(), false);
        }
        return success;
    }

    public boolean updateQuery(String query) {
        boolean success = false;
        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();

            statement.executeUpdate(query);
            
            success = true;
        } catch (SQLException ex) {
            core.writeError("Error at SQL Query: " + ex.getMessage(), false);
        }
        return success;
    }

    public Boolean checkTable(String table) {
        DatabaseMetaData dbm;
        try {
            dbm = this.getConnection().getMetaData();
            ResultSet tables = dbm.getTables(null, null, table, null);
            if (tables.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            core.writeError("Failed to check if table \"" + table + "\" exists: " + e.getMessage(), true);
            return false;
        }

    }

    public Boolean checkField(String table, String column) {
        DatabaseMetaData dbm;
        boolean exists = false;
        try {
            dbm = this.getConnection().getMetaData();
            ResultSet columns = dbm.getColumns(null, null, table, null);
            while(columns.next()){
                String columnName = columns.getString("COLUMN_NAME");
                if(columnName.equals(column)){
                    exists = true;
                    break;
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            core.writeError("Failed to check if column \"" + column + "\" exists: " + e.getMessage(), true);
            exists = false;
        }
        return exists;
    }
}
