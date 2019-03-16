package org.courses.commands.jdbc;

import org.courses.commands.Command;
import org.courses.commands.CommandFormatException;
import org.courses.domain.hbm.Material;
import org.hibernate.Session;

public class AddMaterialCommand extends AbstractQueryCommand implements Command {
    private String materialName;

    @Override
    public void parse(String[] args) {
        if (args.length > 0) {
            materialName = args[0];
        }
        else {
            throw new CommandFormatException("Material name is not specified");
        }

    }

    @Override
    public void execute() {
        Material m = new Material();
        m.setName(materialName);

        Session session = getSessionFactory().openSession();
        session.save(m);
        session.close();
    }
}
