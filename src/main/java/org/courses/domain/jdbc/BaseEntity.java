package org.courses.domain.jdbc;

import org.courses.DAO.DAOInterface;
import org.courses.DAO.StaticDAOOperation;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class BaseEntity implements DAOInterface {

    private Connection con;
    String tableName = null;
    Collection<ColumnClass> columnClasses = null;

    public String getNameTable() {
        if (null == tableName) {
            Class _class = this.getClass();
            Entity jpaEntity = (Entity) _class.getAnnotation(Entity.class);
            tableName = jpaEntity.name();
        }
        return tableName;
    }

    public BaseEntity(Connection con) {
        this.con = con;
    }

//    public BaseEntity() {
//    }

    public Collection<ColumnClass> getColumn() {
        if (null == columnClasses) {

            columnClasses = new ArrayList<>();

            Class _class = this.getClass();

            for (Field field : _class.getDeclaredFields()) {

                Column jpaColumn = field.getAnnotation(Column.class);
                if (null == jpaColumn || jpaColumn.name().equals("id"))//я так понял что аннотация @id относится к hibernate и в этом проекте мы ее не юзаем
                    continue;

                ColumnClass definition = new ColumnClass();
                definition.setName(jpaColumn.name());
                definition.setType(field.getType());
                columnClasses.add(definition);
            }
            columnClasses = Collections.unmodifiableCollection(columnClasses);
        }
        return columnClasses;
    }

    // как от такого дублирования лучше избавиться?
    public Collection<ColumnClass> getAllColumn() {
        if (null == columnClasses) {

            columnClasses = new ArrayList<>();

            Class _class = this.getClass();

            for (Field field : _class.getDeclaredFields()) {

                Column jpaColumn = field.getAnnotation(Column.class);
                if (null == jpaColumn)
                    continue;

                ColumnClass definition = new ColumnClass();
                definition.setName(jpaColumn.name());
                definition.setType(field.getType());
                columnClasses.add(definition);
            }
            columnClasses = Collections.unmodifiableCollection(columnClasses);
        }
        return columnClasses;
    }

    public Object getColumn(ColumnClass definition) throws IllegalAccessException {
        Class _class = this.getClass();

        for (Field field : _class.getDeclaredFields()) {
            Column jpaColumn = field.getAnnotation(Column.class);
            if (null == jpaColumn
                    || !jpaColumn.name().equals(definition.getName()))
                continue;

            if (!field.isAccessible())
                field.setAccessible(true);
            return field.get(this);
        }
        return null;
    }

    @Override
    public int Save() {
        return StaticDAOOperation.insert(this, con);
    }

    @Override
    public BaseEntity Read(int id) {
        String filter = String.format("id = %s", id);

        Collection<ColumnClass> collectionColumns = null;
        try {
            collectionColumns = this.getAllColumn();
            StringBuilder columns = new StringBuilder();

            for (ColumnClass column : collectionColumns) {
                if (columns.length() > 0)
                    columns.append(", ");
                columns.append(column.getName());
            }
            ResultSet rs = StaticDAOOperation.select(this.getNameTable(), columns.toString(), filter, con);
            rs.next();
            Class _class = this.getClass();

            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();

            for (int i = 1; i <= cols; i++) {

                String colName = rsmd.getColumnName(i);
                int colType = rsmd.getColumnType(i);

                Field field = _class.getDeclaredField(colName);
                if (!field.isAccessible())
                    field.setAccessible(true);

                if (colType == Types.VARCHAR)
                    field.set(this, rs.getString(colName));
                else if (colType == Types.INTEGER)
                    field.set(this, rs.getInt(colName));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return this;
    }

    @Override
    public List<DAOInterface> ReadAll() {
        //циклический вызов Read(). Если нужно - доработаю.
        return null;
    }

    @Override
    public void Delete(int id) {
        StaticDAOOperation.delete(this.getNameTable(),id,con);
    }
}
