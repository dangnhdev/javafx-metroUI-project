package org.databaseAccess;

import org.model.Subject;

import java.util.List;

/**
 * @author Admin
 */
public class DASubject {

    public List<Subject> getAll() {
        List<Subject> sub;
        String sql = "SELECT * FROM subject";
        sub = DatabaseConnect.connectWithQuerrySubject(sql);
        return sub;
    }

    public void add(Subject s) {
        String sql = "INSERT INTO subject(id,name,periodNum) VALUES('" + s.getId() + "','" + s.getName() + "'," + s.getPeriod() + ")";
        DatabaseConnect.connect(sql);
    }

    public void edit(Subject s) {
        String sql = "UPDATE subject SET name='" + s.getName() + "',periodNum=" + s.getPeriod() + " WHERE id='" + s.getId() + "'";
        DatabaseConnect.connect(sql);
    }

    public void delete(String id) {
        String sql = "DELETE FROM subject WHERE id='" + id + "'";
        DatabaseConnect.connect(sql);
    }

    public int getSubjectById(String id) {
        String sql = "SELECT periodNum FROM subject WHERE id = '" + id + "'";
        return DatabaseConnect.getSubjectById(sql);
    }
}
