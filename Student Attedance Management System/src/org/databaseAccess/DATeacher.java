/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.databaseAccess;

import org.model.Teacher;

import java.util.List;

/**
 * @author Admin
 */
public class DATeacher {
    public List<Teacher> getAll() {
        List<Teacher> tc;
        String sql = "SELECT * FROM teacher";
        tc = DatabaseConnect.connectWithQuerryTeacher(sql);
        return tc;
    }

    public void add(Teacher t) {
        String sql = "INSERT INTO teacher(id,name,email) VALUES('" + t.getId() + "','" + t.getName() + "','" + t.getEmail() + "')";
        DatabaseConnect.connect(sql);
    }

    public void edit(Teacher t) {
        String sql = "UPDATE teacher SET name='" + t.getName() + "',email='" + t.getEmail() + "' WHERE id='" + t.getId() + "'";
        DatabaseConnect.connect(sql);
    }

    public void delete(String id) {
        String sql = "DELETE FROM teacher WHERE id='" + id + "'";
        DatabaseConnect.connect(sql);
    }
}
