package org.courses.domain.jdbc;

import org.courses.DAO.DAOInterface;
import org.courses.DAO.StaticDAOOperation;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class BaseEntity {
    String tableName = null;
    Collection<Column> columns = null;

    public String getNameTable() {
        if (null == tableName) {
            Class _class = this.getClass();
            Entity jpaEntity = (Entity)_class.getAnnotation(Entity.class);
            tableName = jpaEntity.name();
        }
        return tableName;
    }

    public Collection<Column> getColumns() {
        if (null == columns) {
            Class _class = this.getClass();
            columns = new ArrayList<>();
            for (Field field : _class.getDeclaredFields()) {

                javax.persistence.Column jpaColumn = field.getAnnotation(javax.persistence.Column.class);
                if (null == jpaColumn || jpaColumn.name().equals("id"))//я так понял что аннотация @id относится к hibernate и в этом проекте мы ее не юзаем
                    continue;

                Column definition = new Column();
                definition.setName(jpaColumn.name());
                definition.setType(field.getType());
                columns.add(definition);
            }
            columns = Collections.unmodifiableCollection(columns);
        }
        return columns;
    }

    public Object getColumn(Column definition) throws IllegalAccessException {
        Class _class = this.getClass();
        for (Field field : _class.getDeclaredFields()) {
            javax.persistence.Column jpaColumn = field.getAnnotation(javax.persistence.Column.class);
            if (null == jpaColumn
                    || !jpaColumn.name().equals(definition.getName()))
                continue;

            if (!field.isAccessible())
                field.setAccessible(true);
            return field.get(this);
        }
        return null;
    }



//    public void setColumn(Column definition, Object value) {
//
//    }
}
