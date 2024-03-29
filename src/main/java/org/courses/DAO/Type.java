package org.courses.DAO;

import org.courses.domain.jdbc.BaseEntity;

import java.sql.Connection;
import javax.persistence.*;

@Entity(name = "Type")
public class Type extends BaseEntity {

    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;

    public Type(Connection con) {
        super(con);
    }

    public Type(String name, Connection con) {
        super(con);
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
