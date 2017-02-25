package org.chelmer.response;

import org.chelmer.model.entity.LoxUuid;

import java.util.List;

/**
 * Created by burfo on 21/02/2017.
 */
public class WeatherTimerItem {
    private final LoxUuid uuid;
    private final long lastUpdate; // EEEEEEEEK. This is unsigned!!!
    private final long nEntries; // NOT unsigned!!!
    private List<WeatherTimerItemEntry> entries;

    public WeatherTimerItem(LoxUuid uuid, long lastUpdate_unsigned, long nEntries_signed) {
        this.uuid = uuid;
        this.lastUpdate = lastUpdate_unsigned;
        this.nEntries = nEntries_signed;
    }

    @Override
    public String toString() {
        return "WeatherTimerItem{" +
                "uuid=" + uuid +
                ", lastUpdate=" + lastUpdate +
                ", nEntries=" + nEntries +
                ", entries=" + entries +
                '}';
    }

    public LoxUuid getUuid() {
        return uuid;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public List<WeatherTimerItemEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<WeatherTimerItemEntry> entries) {
        this.entries = entries;
    }

    public long getnEntries() {
        return nEntries;
    }
}
