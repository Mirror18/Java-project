package com.mirror.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author mirror
 */
@Component
public class MailService {
    @Autowired
    UserService userService;

    public String sendMail() {
//        ZoneId zoneId = userService.zoneId;
        ZoneId zoneId = userService.getZoneId();//用方法访问字段
//        ZoneId zoneId = userService.getFinalZoneId();//这个方法也是错的，因为final无法继承
        String dt = ZonedDateTime.now(zoneId).toString();//null
        return "hello, it is " + dt;

    }
}
