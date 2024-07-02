package com.mirror.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author mirror
 */
public class Old {
    public static void main(String[] args) {
        //首先我们要明白的是在时间中有两个API，具体原因是历史原因
        //再加上这玩意儿本来就是现场查，所以这里只是展示下怎么创建，还有怎么在这新旧两种进行转换
        /*
         * 一套定义在java.util这个包里面，主要包括Date、Calendar和TimeZone这几个类；
         * 一套新的API是在Java 8引入的，定义在java.time这个包里面，主要包括LocalDateTime、ZonedDateTime、ZoneId等。
         */
        //获取当前时间戳，可以使用System.currentTimeMillis()
        //Date有两个库，一个是Java的，一个是SQL的，别搞错了
        Date dateOld = new Date();
        System.out.println(dateOld.getYear() + 1900); // 必须加上1900
        System.out.println(dateOld.getMonth() + 1); // 0~11，必须加上1
        System.out.println(dateOld.getDate()); // 1~31，不能加1
        // 转换为String:,顺便吐槽下，关于long型在打印的时候是自动转化为String,所以才没用
        System.out.println(dateOld.toString());
        // 转换为GMT时区:
        System.out.println(dateOld.toGMTString());
        // 转换为本地时区:
        System.out.println(dateOld.toLocaleString());
        var sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(dateOld));
        //Calendar
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = 1 + c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        int w = c.get(Calendar.DAY_OF_WEEK);
        int hh = c.get(Calendar.HOUR_OF_DAY);
        int mm = c.get(Calendar.MINUTE);
        int ss = c.get(Calendar.SECOND);
        int ms = c.get(Calendar.MILLISECOND);
        System.out.println(y + "-" + m + "-" + d + " " + w + " " + hh + ":" + mm + ":" + ss + "." + ms);
        //Calendar只有一种方式获取，即Calendar.getInstance()，而且一获取到就是当前时间。
        // 清除所有:
        c.clear();
        // 设置2019年:
        c.set(Calendar.YEAR, 2019);
        // 设置9月:注意8表示9月:
        c.set(Calendar.MONTH, 8);
        // 设置2日:
        c.set(Calendar.DATE, 2);
        // 设置时间:
        c.set(Calendar.HOUR_OF_DAY, 21);
        c.set(Calendar.MINUTE, 22);
        c.set(Calendar.SECOND, 23);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime()));
        // 2019-09-02 21:22:23
        //TimeZone
        TimeZone tzDefault = TimeZone.getDefault(); // 当前时区
        TimeZone tzGMT9 = TimeZone.getTimeZone("GMT+09:00"); // GMT+9:00时区
        TimeZone tzNY = TimeZone.getTimeZone("America/New_York"); // 纽约时区
        System.out.println(tzDefault.getID()); // Asia/Shanghai
        System.out.println(tzGMT9.getID()); // GMT+09:00
        System.out.println(tzNY.getID()); // America/New_York
        // 清除所有:
        c.clear();
        // 设置为北京时区:
        c.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        // 设置年月日时分秒:
        c.set(2019, 10 /* 11月 */, 20, 8, 15, 0);
        // 显示时间:
        var sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        System.out.println(sdf1.format(c.getTime()));
        // 2019-11-19 19:15:00
        // 清除所有:
        c.clear();
        // 设置年月日时分秒:
        c.set(2019, 10 /* 11月 */, 20, 8, 15, 0);
        // 加5天并减去2小时:
        c.add(Calendar.DAY_OF_MONTH, 5);
        c.add(Calendar.HOUR_OF_DAY, -2);
        // 显示时间:
        var sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = c.getTime();
        System.out.println(sdf2.format(d1));
        // 2019-11-25 6:15:00

    }
}
