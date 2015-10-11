/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.databaseAccess;

import org.model.Student;

import java.util.List;

/**
 * @author Admin
 */
public class DAStudent {

    public List<Student> getAll() {
        List<Student> st;
        String sql = "SELECT * FROM students";
        st = DatabaseConnect.connectWithQuerryStudents(sql);
        return st;
    }

    public List<Student> getByClass(String className) {
        List<Student> st;
        String sql = "SELECT * FROM students WHERE class_name='" + className + "'";
        st = DatabaseConnect.connectWithQuerryStudents(sql);
        return st;
    }

    public void add(Student student) {

        String sql = "INSERT INTO students VALUES('" + student.getStudentId() + "','" + student.getClassName() + "','" + student.getName() + "','" + student.getEmail() + "')";
        DatabaseConnect.connect(sql);
    }

    public void edit(Student student) {
        String sql = "UPDATE students SET class_name='" + student.getClassName()
                + "', name='" + student.getName() + "',email='" + student.getEmail() + "' WHERE id='" + student.getStudentId() + "'";
        DatabaseConnect.connect(sql);
    }

    public void delete(String id) {
        String sql = "DELETE FROM students WHERE id='" + id + "'";
        DatabaseConnect.connect(sql);
    }
}
