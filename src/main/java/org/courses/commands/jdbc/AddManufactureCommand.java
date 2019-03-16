package org.courses.commands.jdbc;

import org.courses.commands.Command;
import org.courses.commands.CommandFormatException;

import java.sql.SQLException;

public class AddManufactureCommand extends AbstractQueryCommand implements Command {
    private String manufactureName;

    @Override
    public void parse(String[] args) {
        if (args.length > 0) {
            dbFile = args[0];
        }
        else {
            throw new CommandFormatException("DB file is not specified");
        }

        if (args.length > 1) {
            manufactureName = args[1];
        }
        else {
            throw new CommandFormatException("Manufacture name is not specified");
        }

    }

    @Override
    public void execute() {
        try {
            insert("Manufacture", "name", String.format("'%s'", manufactureName));
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
