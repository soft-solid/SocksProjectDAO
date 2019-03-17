package org.courses.commands.jdbc;

import org.courses.domain.hbm.Manufacture;
import org.courses.domain.hbm.Material;
import org.courses.domain.hbm.Socks;
import org.courses.domain.jdbc.BaseEntity;
import org.courses.domain.jdbc.ColumnClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Collection;

public abstract class AbstractQueryCommand {
    protected String dbFile;

    protected Connection connect() throws SQLException {
        String url = connectionString();
        return DriverManager.getConnection(url);
    }

    private String connectionString() {
        Path path = Paths.get(dbFile);
        return String.format("jdbc:sqlite:%s", path.toAbsolutePath());
    }

    void insert(String table, String columns, String values) throws SQLException {
        Connection connection = connect();
        Statement statement = connection.createStatement();
        statement.execute(String.format("INSERT INTO %s" +
                "(%s) " +
                "VALUES" +
                "(%s)", table, columns, values));
        statement.close();
        connection.close();
    }

    void insert(BaseEntity entity) throws IllegalAccessException, SQLException {
        String table = entity.getNameTable();
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        Collection<ColumnClass> definitions = entity.getColumn();
        for (ColumnClass definition : definitions) {
            if (columns.length() > 0)
                columns.append(", ");
            columns.append(definition.getName());

            if (values.length() > 0)
                values.append(", ");
            Object value = entity.getColumn(definition);
            if (null == value)
                values.append("NULL");
            else if (value instanceof String)
                values.append(String.format("'%s'", (String)value));
            else
                values.append(value.toString());
        }

        insert(table, columns.toString(), values.toString());
    }

    ResultSet select(String table, String columns, String filter) throws SQLException {
        Connection connection = connect();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery(String.format("SELECT %s " +
                "FROM %s " +
                "WHERE %s ", columns, table, filter));
        return results;
    }

    SessionFactory sessionFactory;

    SessionFactory getSessionFactory() {
        if (null == sessionFactory) {
            sessionFactory = new Configuration()
                    //.configure("/hbm/hibernate.cfg.xml")
                    .setProperty("connection.driver_class", "org.sqlite.JDBC")
                    .setProperty("dialect", "org.hibernate.dialect.SQLiteDialect")
                    .setProperty("connection.pool_size", "1")
                    .setProperty("show_sql", "true")
                    .setProperty("format_sql", "true")
                    .setProperty("hibernate.jdbc.batch_size", "30")
                    .setProperty("hibernate.connection.url", "jdbc:sqlite:test.db")
                    .addAnnotatedClass(Manufacture.class)
                    .addAnnotatedClass(Material.class)
                    .addAnnotatedClass(Socks.class)
                    .buildSessionFactory();
        }
        return sessionFactory;
    }
}
