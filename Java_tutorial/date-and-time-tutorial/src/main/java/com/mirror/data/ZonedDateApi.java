package com.mirror.data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author mirror
 */
public class ZonedDateApi {
    public static void main(String[] args) {
        ZonedDateTime zbj = ZonedDateTime.now(); // 默认时区
        ZonedDateTime zny = ZonedDateTime.now(ZoneId.of("America/New_York")); // 用指定时区获取当前时间
        System.out.println(zbj);
        System.out.println(zny);


        System.out.println();
        LocalDateTime ldt = LocalDateTime.of(2019, 9, 15, 15, 16, 17);
        ZonedDateTime zbj1 = ldt.atZone(ZoneId.systemDefault());
        ZonedDateTime zny1 = ldt.atZone(ZoneId.of("America/New_York"));
        System.out.println(zbj1);
        System.out.println(zny1);
        // 转换为纽约时间:
        ZonedDateTime zny2 = zbj.withZoneSameInstant(ZoneId.of("America/New_York"));
        System.out.println(zny2);

        //格式化字符串
        System.out.println();
        ZonedDateTime zdt = ZonedDateTime.now();
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm ZZZZ");
        System.out.println(formatter.format(zdt));

        var zhFormatter = DateTimeFormatter.ofPattern("yyyy MMM dd EE HH:mm", Locale.CHINA);
        System.out.println(zhFormatter.format(zdt));

        var usFormatter = DateTimeFormatter.ofPattern("E, MMMM/dd/yyyy HH:mm", Locale.US);
        System.out.println(usFormatter.format(zdt));

        System.out.println();
        var ldt4 = LocalDateTime.now();
        System.out.println(DateTimeFormatter.ISO_DATE.format(ldt4));
        System.out.println(DateTimeFormatter.ISO_DATE_TIME.format(ldt4));
    }
}
