package org.courses.commands.jdbc;

import org.courses.commands.Command;
import org.courses.commands.CommandFormatException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable implements Command {
    private String dbFile;
    private String tableName;

    @Override
    public void parse(String[] args) {
        if (args.length > 0) {
            dbFile = args[0];
        }
        else {
            throw new CommandFormatException("DB file is not specified");
        }

        if (args.length > 1) {
            tableName = args[1];
        }
        else {
            throw new CommandFormatException("Table name is not specified");
        }
    }

    @Override
    public void execute() {
        try {
            String url = connectionString();
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            boolean result = statement.execute(
                    String.format("CREATE TABLE IF NOT EXISTS %s (id INTEGER PRIMARY KEY AUTOINCREMENT)", tableName));
            statement.close();
            connection.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private String connectionString() {
        Path path = Paths.get(dbFile);
        return String.format("jdbc:sqlite:%s", path.toAbsolutePath());
    }
}
