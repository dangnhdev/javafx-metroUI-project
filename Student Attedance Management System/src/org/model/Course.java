package org.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author 404NotFound
 */

public class Course {
    final private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private String courseId;
    private String teacherEmail;
    private String classId;
    private String subjectId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String partOfDay;

    public Course() {
    }

    public Course(String courseId, String teacherEmail, String classId,
                  String subjectId, LocalDate startDate, LocalDate endDate,
                  String partOfDay) {
        this.courseId = courseId;
        this.teacherEmail = teacherEmail;
        this.classId = classId;
        this.subjectId = subjectId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.partOfDay = partOfDay;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getStartDate() {
        return startDate.format(formatter);
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDefaultStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate.format(formatter);
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getDefaultEndDate() {
        return endDate;
    }

    public String getPartOfDay() {
        return partOfDay;
    }

    public void setPartOfDay(String partOfDay) {
        this.partOfDay = partOfDay;
    }

}
