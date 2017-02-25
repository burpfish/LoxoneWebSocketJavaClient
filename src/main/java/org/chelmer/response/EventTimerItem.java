package org.chelmer.response;

import org.chelmer.model.entity.LoxUuid;

import java.util.List;

/**
 * Created by burfo on 21/02/2017.
 */
public class EventTimerItem {
    private final LoxUuid uuid;
    private final double defaultValue;
    private final long nEntries; // NOT unsigned!!!
    private List<EventTimerItemEntry> entries;

    public EventTimerItem(LoxUuid uuid, double defaultValue, long nEntries) {
        this.uuid = uuid;
        this.defaultValue = defaultValue;
        this.nEntries = nEntries;
    }

    public LoxUuid getUuid() {
        return uuid;
    }

    public double getDefaultValue() {
        return defaultValue;
    }

    public long getnEntries() {
        return nEntries;
    }

    public List<EventTimerItemEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<EventTimerItemEntry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "EventTimerItem{" +
                "uuid=" + uuid +
                ", defValue=" + defaultValue +
                ", nEntries=" + nEntries +
                ", entries=" + entries +
                '}';
    }
}

