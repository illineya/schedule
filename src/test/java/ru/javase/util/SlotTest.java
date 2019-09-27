package ru.javase.util;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.sql.Time;
import java.text.ParseException;
import java.util.Calendar;

public class SlotTest {
    private static Logger logger = Logger.getLogger(SlotTest.class);

    class SlotImpl implements Slot {
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

    @Test
    public void test() throws ParseException {
        Calendar calendar = DateUtil.getDate();
        Schedule schedule = new ScheduleList(1, Schedule.Unit.HOUR);

        //Create timeslots
        schedule.add(calendar.getTime(), new SlotImpl(DateUtil.getTime("10:00")));
        schedule.add(calendar.getTime(), new SlotImpl(DateUtil.getTime("09:00")));
        schedule.add(calendar.getTime(), new SlotImpl(DateUtil.getTime("13:00")));
        schedule.add(calendar.getTime(), new SlotImpl(DateUtil.getTime("12:00")));
        schedule.add(calendar.getTime(), new SlotImpl(DateUtil.getTime("11:00")));
    }

    @Test
    public void testCollision() throws ParseException {
        Calendar calendar = DateUtil.getDate();
        Schedule schedule = new ScheduleList(1, Schedule.Unit.HOUR);

        //Create timeslots
        schedule.add(calendar.getTime(), new SlotImpl(DateUtil.getTime("09:00")));

        //Create timeslot with collision
        try {
            schedule.add(calendar.getTime(), new SlotImpl(DateUtil.getTime("09:30")));
        } catch (ElementConflictException e) {
            //e.printStackTrace();
        }
    }

    @Test
    public void testDelete() throws ParseException {
        Calendar calendar = DateUtil.getDate();
        Schedule schedule = new ScheduleList(1, Schedule.Unit.HOUR);

        //Create timeslots
        schedule.add(calendar.getTime(), new SlotImpl(DateUtil.getTime("09:00")));
        schedule.add(calendar.getTime(), new SlotImpl(DateUtil.getTime("10:00")));

        schedule.remove(calendar.getTime(), new SlotImpl(DateUtil.getTime("10:00")));

        Slot slot = schedule.get(calendar.getTime(), new SlotImpl(DateUtil.getTime("10:00")));
        assertEquals(slot, null);
    }
}