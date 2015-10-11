package org.model;

import javafx.beans.property.BooleanProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 404NotFound on 10/9/2015.
 */
public class StudentAttendance {
    private String studentId;
    private String studentName;
    private List<BooleanProperty> listStatus = new ArrayList<>();

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public List<BooleanProperty> getListStatus() {
        return listStatus;
    }

    public void setListStatus(List<BooleanProperty> listStatus) {
        this.listStatus = listStatus;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getAbsentNo() {
        return (int) this.listStatus.stream().filter(status -> !status.get()).count();
    }
}
