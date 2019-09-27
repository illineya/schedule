package ru.javase.util;

import java.sql.Time;

/**
 * An ordered slot collection
 * Collection elements must be implements this interface
 *
 * @author ulcigor
 * @version 1.0
 */
public interface Slot extends Comparable<Slot> {
    Time getTime();
    default int compareTo(Slot slot) {
        Long result = getTime().getTime() - slot.getTime().getTime();
        return result.intValue();
    }
}
