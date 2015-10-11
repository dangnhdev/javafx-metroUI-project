/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.databaseAccess;

import org.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Admin
 */
public class DatabaseConnect {
    private static Connection conn;
    private static Statement stmt;
    private static ResultSet rs;


    public static void connect(String sql) {
        try {
            java.lang.Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:studentmanagement.db");
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Student> connectWithQuerryStudents(String sql) {
        List<Student> st = new ArrayList<>();
        try {
            java.lang.Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:studentmanagement.db");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                st.add(new Student(rs.getString(1), rs.getString(3), rs.getString(2), rs.getString(4)));
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return st;
    }

    public static List<Subject> connectWithQuerrySubject(String sql) {
        List<Subject> st = new ArrayList<>();
        try {
            java.lang.Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:studentmanagement.db");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                st.add(new Subject(rs.getString(1), rs.getString(2), rs.getInt(3)));
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return st;
    }

    public static List<Course> connectWithQuerryCourse(String sql) {
        List<Course> st = new ArrayList<>();
        try {
            java.lang.Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:studentmanagement.db");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                st.add(new Course(rs.getString(1), rs.getString(3), rs.getString(2),
                        rs.getString(4), java.sql.Date.valueOf(rs.getString(5)).toLocalDate(),
                        java.sql.Date.valueOf(rs.getString(6)).toLocalDate(), rs.getString(7)));
            }

            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return st;
    }

    public static List<Teacher> connectWithQuerryTeacher(String sql) {
        List<Teacher> st = new ArrayList<>();
        try {
            java.lang.Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:studentmanagement.db");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                st.add(new Teacher(rs.getString(1), rs.getString(2), rs.getString(3)));
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return st;
    }

    public static List<AttendanceReport> connectWithQuerryAttendance(String sql) {
        List<AttendanceReport> st = new ArrayList<>();
        try {
            java.lang.Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:studentmanagement.db");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                st.add(new AttendanceReport(rs.getString(1),
                        rs.getString(2), rs.getString(3), java.sql.Date.valueOf(rs.getString(4)).toLocalDate(),
                        Boolean.parseBoolean(rs.getString(5)), Boolean.parseBoolean(rs.getString(6))));
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return st;
    }

    public static List<User> connectWithQuerryUser(String sql) {
        List<User> st = new ArrayList<>();
        try {
            java.lang.Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:studentmanagement.db");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                st.add(new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return st;
    }

    public static boolean checkUser(String sql) {
        boolean ok = false;
        try {
            java.lang.Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:studentmanagement.db");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ok = true;
                break;
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return ok;
    }

    public static int getSubjectById(String sql) {
        int period = 0;
        try {
            java.lang.Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:studentmanagement.db");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                period = rs.getInt(1);
                break;
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return period;
    }
}
