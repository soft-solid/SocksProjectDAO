package org.courses.DAO;

import org.courses.domain.jdbc.BaseEntity;

import java.sql.SQLException;
import java.util.List;

public interface DAOInterface {
    int Save() throws SQLException, IllegalAccessException;

    BaseEntity Read(int id) throws SQLException;

    List<DAOInterface> ReadAll();

    void Delete(int id);
}
