package org.courses.DAO;

import org.courses.domain.jdbc.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Connection;

@Entity(name = "Manufacture")
public class Manufacture extends BaseEntity {
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;

    public Manufacture(Connection con) {
        super(con);
    }

    public Manufacture(String name, Connection con) {
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
