package org.cursebyte.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static Connection connection;
    private static String dbPath;

    public static void init(String pluginFolderPath) {
        try {
            dbPath = pluginFolderPath + "/data.db";
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
                stmt.execute("PRAGMA journal_mode = WAL;");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                try (java.sql.Statement stmt = connection.createStatement()) {
                    stmt.execute("PRAGMA optimize;");
                }

                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
