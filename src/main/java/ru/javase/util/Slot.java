package ru.javase.util;

import java.sql.Time;

/**
 * An ordered slot collection
 * Collection elements must be implements this interface
 *
 * @author ulcigor
 * @version 1.0
 */
public interface Slot {
    Time getTime();
    Integer getNum();
    Boolean getAvailable();
}
