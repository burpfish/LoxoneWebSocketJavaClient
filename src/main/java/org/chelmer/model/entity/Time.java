package org.chelmer.model.entity;

public class Time {
    private final int id;
    private final String name;
    private final boolean analog;

    public Time(int id, String name, boolean analog) {

        this.id = id;
        this.name = name;
        this.analog = analog;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAnalog() {
        return analog;
    }
}
