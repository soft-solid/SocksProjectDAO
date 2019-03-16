package org.courses.DAO;

import java.sql.SQLException;
import java.util.List;

public interface DAOInterface {
   void Save() throws SQLException, IllegalAccessException;
   DAOInterface Read(int id) throws SQLException;
   List<DAOInterface> ReadAll();
   void Delete();
}
