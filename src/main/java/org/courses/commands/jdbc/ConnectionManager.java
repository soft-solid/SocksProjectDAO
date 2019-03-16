package org.courses.commands.jdbc;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static  Connection connect;
    protected static Connection connect(String dbFile) throws SQLException {
        String url = connectionString(dbFile);
        return DriverManager.getConnection(url);
    }

    private static  String connectionString(String dbFile) {
        Path path = Paths.get(dbFile);
        return String.format("jdbc:sqlite:%s", path.toAbsolutePath());
    }

    public static Connection getConnection(String dbFile){
       try {
           if (null == connect) {
               connect = connect(dbFile);
           }
       }
       catch (SQLException e)
       {
            System.console().printf(e.getMessage());
       }
        return connect;
    }

}
