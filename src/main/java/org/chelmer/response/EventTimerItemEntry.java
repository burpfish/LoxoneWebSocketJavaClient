package org.chelmer.response;

/**
 * Created by burfo on 21/02/2017.
 */
public class EventTimerItemEntry {
    private final long nMode; // 32­Bit Integer (little endian) number of mode
    private final long nFrom; // 32­Bit Integer (little endian) from­time in minutes since midnight
    private final long nTo; // 32­Bit Integer (little endian) to­time in minutes since midnight
    private final long bNeedActivate; // 32­Bit Integer (little endian) need activate (trigger)
    private final double dValue; // 64­Bit Float (little endian) value (if analog daytimer)


    public EventTimerItemEntry(long nMode, long nFrom, long nTo, long bNeedActivate, double dValue) {
        this.nMode = nMode;
        this.nFrom = nFrom;
        this.nTo = nTo;
        this.bNeedActivate = bNeedActivate;
        this.dValue = dValue;
    }

    public long getnMode() {
        return nMode;
    }

    public long getnFrom() {
        return nFrom;
    }

    public long getnTo() {
        return nTo;
    }

    public long getbNeedActivate() {
        return bNeedActivate;
    }

    @Override
    public String toString() {
        return "EventTimerItemEntry{" +
                "nMode=" + nMode +
                ", nFrom=" + nFrom +
                ", nTo=" + nTo +
                ", bNeedActivate=" + bNeedActivate +
                ", dValue=" + dValue +
                '}';
    }

    public double getdValue() {
        return dValue;
    }
}