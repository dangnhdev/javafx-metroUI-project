package org.model;

/**
 * @author 404NotFound
 */

public class Student {
    private String studentId;
    private String className;
    private String name;
    private String email;


    public Student() {
    }

    public Student(String id, String name, String className, String email) {
        this.studentId = id;
        this.name = name;
        this.className = className;
        this.email = email;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
