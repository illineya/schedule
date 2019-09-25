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
    void add(Date date, Slot slot);
    void add(Date date, Slot slot, Boolean ignore_collision);
    Slot get(Date date, Slot slot);
    Slot get(Date date, Time time);
    void remove(Date date, Slot slot);
    void remove(Date date, Time time);
    void set(Date date, Integer index, Slot slot);
    boolean contains(Date date, Slot slot);
    boolean contains(Date date, Time time);
    int indexOf(Date date, Slot slot);
    int indexOf(Date date, Time time);
    void clear();
    String toString();
}
