package org.courses.DAO;

import org.courses.domain.jdbc.BaseEntity;
import org.courses.domain.jdbc.ColumnClass;

import java.sql.*;
import java.util.Collection;

public class StaticDAOOperation {

    public static int insert(BaseEntity entity, Connection con) {
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
            Object value = null;
            try {
                value = entity.getColumn(definition);
            } catch (Exception e) {
            }
            if (null == value)
                values.append("NULL");
            else if (value instanceof String)
                values.append(String.format("'%s'", (String) value));
            else
                values.append(value.toString());
        }

        return insert(table, columns.toString(), values.toString(), con);
    }

    static int insert(String table, String columns, String values, Connection con) {
        int resultID = -1;
        try {
            Statement statement = con.createStatement();
            statement.execute(String.format("INSERT INTO %s" +
                    "(%s) " +
                    "VALUES" +
                    "(%s)", table, columns, values));
            ResultSet rs = statement.executeQuery("SELECT last_insert_rowid()");
            rs.next();
            resultID = rs.getInt("last_insert_rowid()");//statement.close() закрывает и возвращаемый ResultSet, поэтому завел переменную
            statement.close();
        } catch (Exception e) {
        }

        return resultID;
    }

    public static ResultSet select(String table, String columns, String filter, Connection con) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet results = statement.executeQuery(String.format("SELECT %s " +
                "FROM %s " +
                "WHERE %s ", columns, table, filter));
        return results;
    }

    //нагуглил сущность PreparedStatement, которая позволяет предварительно сформировать текст запроса перед выполнением с учетом параметров.+ можно избавиться от преобразования строковых параметров. и дает прирост скорости для некоторых БД
    public static void delete(String table, int filter, Connection con) {
        String sql = "DELETE FROM "+ table +" WHERE id = ?";

        //и удобный способ высвобождения ресурсов. аналог using() c#
        try (Connection conn = con;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, filter);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
