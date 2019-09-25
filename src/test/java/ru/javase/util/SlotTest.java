package ru.javase.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
        schedule.add(calendar.getTime(), new SlotImpl(DateUtil.getTime("09:00")));
        schedule.add(calendar.getTime(), new SlotImpl(DateUtil.getTime("10:00")));
        schedule.add(calendar.getTime(), new SlotImpl(DateUtil.getTime("11:00")));
        schedule.add(calendar.getTime(), new SlotImpl(DateUtil.getTime("12:00")));
        schedule.add(calendar.getTime(), new SlotImpl(DateUtil.getTime("13:00")));
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
        } catch (ElementCollisionException e) {

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

        int index = schedule.indexOf(calendar.getTime(), new SlotImpl(DateUtil.getTime("10:00")));
        assertEquals(index, -1);
    }

    @Test
    public void testChange() throws ParseException {
        Calendar calendar = DateUtil.getDate();
        Schedule schedule = new ScheduleList(1, Schedule.Unit.HOUR);

        //Create timeslots
        schedule.add(calendar.getTime(), new SlotImpl(DateUtil.getTime("09:00")));
        schedule.add(calendar.getTime(), new SlotImpl(DateUtil.getTime("10:00")));

        //Change timeslot
        Slot slot = new SlotImpl(DateUtil.getTime("11:00"));
        int index = schedule.indexOf(calendar.getTime(), new SlotImpl(DateUtil.getTime("10:00")));
        schedule.set(calendar.getTime(), index, slot);

        //Slot with time of 10:00 must be undefined
        index = schedule.indexOf(calendar.getTime(), new SlotImpl(DateUtil.getTime("10:00")));
        assertEquals(index, -1);

        //Slot with time of 11:00 must be found
        index = schedule.indexOf(calendar.getTime(), new SlotImpl(DateUtil.getTime("11:00")));
        assertNotEquals(index, -1);
    }
}