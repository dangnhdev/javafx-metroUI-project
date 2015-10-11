package org.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author 404NotFound
 */


public class Class {
    private SimpleStringProperty classId;


    public Class() {
    }

    public Class(String id) {
        this.classId = new SimpleStringProperty(id);
    }

    public String getClassId() {
        return classId.get();
    }

    public void setClassId(String id) {
        classId.set(id);
    }
}
