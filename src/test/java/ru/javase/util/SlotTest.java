package ru.javase.util;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.text.ParseException;
import java.util.Calendar;

public class SlotTest {
    private static Logger logger = Logger.getLogger(SlotTest.class);

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
            e.printStackTrace();
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

        int index = schedule.indexOf(calendar.getTime(), DateUtil.getTime("10:00"));
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
        int index = schedule.indexOf(calendar.getTime(), DateUtil.getTime("10:00"));
        schedule.set(calendar.getTime(), index, slot);

        index = schedule.indexOf(calendar.getTime(), DateUtil.getTime("10:00"));
        assertEquals(index, -1);

        index = schedule.indexOf(calendar.getTime(), DateUtil.getTime("11:00"));
        assertNotEquals(index, -1);
    }
}