package org.courses.DAO;

import org.courses.domain.jdbc.BaseEntity;
import org.courses.domain.jdbc.Column;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

public class StaticDAOOperation {

  public static   int insert(BaseEntity entity, Connection con) throws IllegalAccessException, SQLException {
        String table = entity.getNameTable();
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        Collection<Column> definitions = entity.getColumns();
        for (Column definition : definitions) {
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

       return  insert(table, columns.toString(), values.toString(),con);
    }

    static  int insert(String table, String columns, String values, Connection con) throws SQLException {
        Statement statement = con.createStatement();
        statement.execute(String.format("INSERT INTO %s" +
                "(%s) " +
                "VALUES" +
                "(%s)", table, columns, values));
        ResultSet rs = statement.executeQuery("SELECT last_insert_rowid()");
        rs.next();
        int resultID = rs.getInt("last_insert_rowid()");//statement.close() закрывает и возвращаемый ResultSet, поэтому завел переменную
        statement.close();
        return resultID;
    }

   public static ResultSet select(String table, String columns, String filter, Connection con) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet results = statement.executeQuery(String.format("SELECT %s " +
                "FROM %s " +
                "WHERE %s ", columns, table, filter));
        return results;
    }

}
