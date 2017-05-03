package org.chelmer.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Time {
    private final int id;
    private final String name;
    private final boolean analog;

    @JsonCreator
    public Time(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("analog") boolean analog) {
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
