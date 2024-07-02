package com.mirror.data;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author mirror
 */
public class ConvertDate {
    public static void main(String[] args) {
        //旧转新
        // Date -> Instant:
        Instant ins1 = new Date().toInstant();

        // Calendar -> Instant -> ZonedDateTime:
        Calendar calendar = Calendar.getInstance();
        Instant ins2 = calendar.toInstant();
        ZonedDateTime zdt = ins2.atZone(calendar.getTimeZone().toZoneId());

        //新转旧
        // ZonedDateTime -> long:
        ZonedDateTime zdt1 = ZonedDateTime.now();
        long ts = zdt1.toEpochSecond() * 1000;

        // long -> Date:
        Date date = new Date(ts);

        // long -> Calendar:
        Calendar calendar1 = Calendar.getInstance();
        calendar1.clear();
        calendar1.setTimeZone(TimeZone.getTimeZone(zdt1.getZone().getId()));
        calendar1.setTimeInMillis(zdt1.toEpochSecond() * 1000);
    }
}
