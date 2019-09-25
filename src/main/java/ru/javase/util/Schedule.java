package ru.javase.util;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * Permit create schedules.
 * As example
 *
 * Schedule<Slot> schedule = new ScheduleList<>(1, Unit.HOUR);
 * schedule.add(somedate, myslot);
 *
 * @author ulcigor
 * @version 1.0
 */
public interface Schedule<T extends Slot> extends Serializable {
    enum Unit {
        SECOND(1), MINUTE(60), HOUR(3600);

        private Integer duration;
        Unit(Integer duration) {
            this.duration = duration;
        }

        public Integer getDuration() {
            return duration;
        }
    }
    void add(Date date, T slot);
    void add(Date date, T slot, Boolean ignore_collision);
    T get(Date date, T slot);
    void remove(Date date, T slot);
    void set(Date date, Integer index, T slot);
    boolean contains(Date date, T slot);
    int indexOf(Date date, T slot);
    void clear();
    String toString();
}
