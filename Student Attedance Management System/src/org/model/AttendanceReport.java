package org.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.time.LocalDate;

/**
 * @author 404NotFound
 */


public class AttendanceReport {
    private String studentId;
    private String courseId;
    private String studentName;
    private LocalDate dateCreated;
    private BooleanProperty status;
    private BooleanProperty isReported;

    public AttendanceReport() {
        this.status = null;
    }

    public AttendanceReport(String studentId, String studentName, String courseId) {
        this(studentId, studentName, courseId, null, false, false);
    }

//    public AttendanceReport(String studentId, String studentName, Boolean status){
//        this(studentId, studentName, null, null, status);
//    }
//    
//    public AttendanceReport(String studentId, String studentName, String courseId, LocalDate dateCreated){
//        this(studentId, studentName, courseId, dateCreated, false);
//    }

    public AttendanceReport(String studentId, String studentName, String courseId, LocalDate dateCreated, boolean status,
                            boolean isReported) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseId = courseId;
        this.dateCreated = dateCreated;
        this.status = new SimpleBooleanProperty(status);
        this.isReported = new SimpleBooleanProperty(isReported);
    }

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }


    public void setStatus(boolean status) {
        this.status.setValue(status);
    }

    public boolean getIsReported() {
        return isReported.get();
    }

    public void setIsReported(boolean isReported) {
        this.isReported.set(isReported);
    }

    public BooleanProperty statusProperty() {
        return status;
    }

    public BooleanProperty isReportedProperty() {
        return isReported;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
