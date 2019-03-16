package org.courses.DAO;

import org.courses.domain.jdbc.BaseEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.*;
import javax.persistence.Column;

@Entity(name = "Type")
public class Type extends BaseEntity implements DAOInterface {


    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    private Connection con;

    public Type(String name, Connection con) {
        this.name = name;
        this.con = con;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void Save() throws SQLException, IllegalAccessException {
            this.id = StaticDAOOperation.insert(this, this.con);
    }

    @Override
    public DAOInterface Read(int id) throws SQLException {
        String filter = String.format("id = %s", id);
        ResultSet results = StaticDAOOperation.select("SockType", "id, typeName", filter, this.con);
        results.next();
        this.id = results.getInt("id");
        this.name = results.getString("typeName");
        results.close();
        return this;
    }

    @Override
    public List<DAOInterface> ReadAll() {
        return null;
    }

    @Override
    public void Delete() {

    }
}
