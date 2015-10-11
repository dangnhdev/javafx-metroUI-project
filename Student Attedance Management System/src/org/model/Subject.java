package org.model;

public class Subject {
    private String id;
    private String name;
    private Integer period;

    public Subject() {
    }


    public Subject(String id, String name, int period) {
        this.id = id;
        this.name = name;
        this.period = period;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
