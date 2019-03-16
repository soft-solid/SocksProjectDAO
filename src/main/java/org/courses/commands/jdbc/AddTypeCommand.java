package org.courses.commands.jdbc;

import org.courses.commands.Command;
import org.courses.commands.CommandFormatException;

import java.sql.SQLException;

public class AddTypeCommand extends AbstractQueryCommand implements Command {
    private String typeName;

    @Override
    public void parse(String[] args) {
        if (args.length > 0) {
            dbFile = args[0];
        }
        else {
            throw new CommandFormatException("DB file is not specified");
        }

        if (args.length > 1) {
            typeName = args[1];
        }
        else {
            throw new CommandFormatException("Type name is not specified");
        }
    }

    @Override
    public void execute() {
        try {
            insert("Type", "name", String.format("'%s'", typeName));
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}