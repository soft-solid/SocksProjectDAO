package org.courses.commands.jdbc;

import org.courses.commands.Command;
import org.courses.commands.CommandFormatException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ListMaterialCommand extends AbstractQueryCommand implements Command {
    private String filter;

    @Override
    public void parse(String[] args) {
        if (args.length > 0) {
            dbFile = args[0];
        }
        else {
            throw new CommandFormatException("DB file is not specified");
        }

        if (args.length > 1) {
            filter = args[1];
        }
        else {
            filter = "1 = 1";
        }
    }

    @Override
    public void execute() {
        try {
            ResultSet results = select("Material", "id, name", filter);
            while (results.next()) {
                System.out.println(String.format(
                        "%d\t%s",
                        results.getInt("id"),
                        results.getString("name")));
            }
            results.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
