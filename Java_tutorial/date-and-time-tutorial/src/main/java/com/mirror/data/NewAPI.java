package com.mirror.data;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/**
 * @author mirror
 */
public class NewAPI {
    public static void main(String[] args) {
        /*
        本地日期和时间：LocalDateTime，LocalDate，LocalTime；
        带时区的日期和时间：ZonedDateTime；
        时刻：Instant；
        时区：ZoneId，ZoneOffset；
        时间间隔：Duration。
         */
        LocalDateTime dt = LocalDateTime.now(); // 当前日期和时间
//        LocalDate d = LocalDate.now(); // 当前日期
        LocalDate d = dt.toLocalDate(); // 转换到当前日期
//        LocalTime t = LocalTime.now(); // 当前时间
        LocalTime t = dt.toLocalTime(); // 转换到当前时间
        System.out.println(d); // 严格按照ISO 8601格式打印
        System.out.println(t); // 严格按照ISO 8601格式打印
        System.out.println(dt); // 严格按照ISO 8601格式打印
        LocalDate d2 = LocalDate.of(2019, 11, 30); // 2019-11-30, 注意11=11月
        LocalTime t2 = LocalTime.of(15, 16, 17); // 15:16:17
        LocalDateTime dt2 = LocalDateTime.of(2019, 11, 30, 15, 16, 17);
        LocalDateTime dt3 = LocalDateTime.of(d2, t2);
        LocalDateTime dt4 = LocalDateTime.parse("2019-11-19T15:16:17");
        LocalDate d3 = LocalDate.parse("2019-11-19");
        LocalTime t3 = LocalTime.parse("15:16:17");
        // 自定义格式化:
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        System.out.println(dtf.format(LocalDateTime.now()));

        // 用自定义格式解析:
        LocalDateTime dt5 = LocalDateTime.parse("2019/11/30 15:16:17", dtf);
        System.out.println(dt5);


        System.out.println();
        LocalDateTime dt8 = LocalDateTime.of(2019, 10, 26, 20, 30, 59);
        System.out.println(dt8);
        // 加5天减3小时:
        LocalDateTime dt6 = dt8.plusDays(5).minusHours(3);
        System.out.println(dt6); // 2019-10-31T17:30:59
        // 减1月:
        LocalDateTime dt7 = dt6.minusMonths(1);
        System.out.println(dt7); // 2019-09-30T17:30:59

/*
 * 调整年：withYear()
 * 调整月：withMonth()
 * 调整日：withDayOfMonth()
 * 调整时：withHour()
 * 调整分：withMinute()
 * 调整秒：withSecond()
 */
        System.out.println();
        // 日期变为31日:
        LocalDateTime dt9 = dt8.withDayOfMonth(31);
        System.out.println(dt9); // 2019-10-31T20:30:59
        // 月份变为9:
        LocalDateTime dt10 = dt9.withMonth(9);
        System.out.println(dt10); // 2019-09-30T20:30:59


        System.out.println();
        // 本月第一天0:00时刻:
        LocalDateTime firstDay = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        System.out.println(firstDay);

        // 本月最后1天:
        LocalDate lastDay = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        System.out.println(lastDay);

        // 下月第1天:
        LocalDate nextMonthFirstDay = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
        System.out.println(nextMonthFirstDay);

        // 本月第1个周一:
        LocalDate firstWeekday = LocalDate.now().with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        System.out.println(firstWeekday);


        System.out.println();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime target = LocalDateTime.of(2019, 11, 19, 8, 15, 0);
        System.out.println(now.isBefore(target));
        System.out.println(LocalDate.now().isBefore(LocalDate.of(2019, 11, 19)));
        System.out.println(LocalTime.now().isAfter(LocalTime.parse("08:15:00")));

        //Duration表示两个时刻之间的时间间隔。
        // 另一个类似的Period表示两个日期之间的天数：
        System.out.println();
        LocalDateTime start = LocalDateTime.of(2019, 11, 19, 8, 15, 0);
        LocalDateTime end = LocalDateTime.of(2020, 1, 9, 19, 25, 30);
        Duration duration = Duration.between(start, end);
        System.out.println(duration); // PT1235H10M30S

        Period p = LocalDate.of(2019, 11, 19).until(LocalDate.of(2020, 1, 9));
        System.out.println(p); // P1M21D
        Duration duration11 = Duration.ofHours(10); // 10 hours
        Duration duration12 = Duration.parse("P1DT2H3M"); // 1 day, 2 hours, 3 minutes
    }
}
