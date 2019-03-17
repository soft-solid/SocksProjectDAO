package org.courses.domain.jdbc;

import javax.persistence.*;
import javax.persistence.Column;

@Entity(name = "Type")
public class SockType  {
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String typeName;

    public SockType() {

    }

    public SockType(int id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return typeName;
    }

    public void setName(String typeName) {
        this.typeName = typeName;
    }
}
