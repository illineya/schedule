package ru.javase.util;

import java.sql.Time;

/**
 * Test slot implementation
 *
 * @author ulcigor
 * @version 1.0
 */
public class SlotImpl implements Slot {
    private Time time;

    public void setTime(Time time) {
        this.time = time;
    }

    SlotImpl() {}
    SlotImpl(Time time) {
        this.time = time;
    }

    public Time getTime() {
        return time;
    }

    @Override
    public String toString() {
        return new StringBuffer("[").append("time=").append(time).append("]").toString();
    }
}
