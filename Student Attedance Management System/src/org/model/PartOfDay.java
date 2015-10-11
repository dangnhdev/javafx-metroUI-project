package org.model;

/**
 * @author 404NotFound
 */


public enum PartOfDay {
    MORNING("Morning"), AFTERNOON("Afternoon");

    private final String partOfDay;

    private PartOfDay(String s) {
        partOfDay = s;
    }

    public boolean equals(String partOfDay) {
        return (partOfDay == null) ? false : this.partOfDay.equals(partOfDay);
    }

    public String toString() {
        return this.partOfDay;
    }
}
