package org.chelmer.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Created by burfo on 14/02/2017.
 */
public class LoxUuid {
    private String UUID;

    public LoxUuid() {

    }

    @JsonCreator
    public LoxUuid(String UUID) {
        this.UUID = UUID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoxUuid loxUuid = (LoxUuid) o;

        return UUID != null ? UUID.equals(loxUuid.UUID) : loxUuid.UUID == null;
    }

    @Override
    public int hashCode() {
        return UUID != null ? UUID.hashCode() : 0;
    }

    @Override
    public String toString() {
        return UUID;
    }

    public String getUUID() {
        return UUID;
    }
}
