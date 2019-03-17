package org.courses.domain.jdbc;

public class ColumnClass {
    private String name;
    private Class type;

    public ColumnClass() {

    }

    public ColumnClass(String name, Class type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}
