package ru.javase.util;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Temporal library.
 *
 * @author ulcigor
 * @version 1.0
 */
public class DateUtil {
    public static Calendar getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    public static Time getTime(Date date) {
        if(date == null) date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Integer hours = calendar.get(Calendar.HOUR_OF_DAY) * 3600;
        Integer minutes = calendar.get(Calendar.MINUTE) * 60;

        return new Time((hours + minutes) * 1000l);
    }

    public static Time getTime(String time) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date date = df.parse(time);
        return getTime(date);
    }
}
