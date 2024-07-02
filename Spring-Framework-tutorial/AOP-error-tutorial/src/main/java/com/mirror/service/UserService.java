package com.mirror.service;

import org.springframework.stereotype.Component;

import java.time.ZoneId;

/**
 * @author mirror
 */
@Component
public class UserService {
    public final ZoneId zoneId = ZoneId.systemDefault();

    public UserService(){
        System.out.println("userService(): init...");
        System.out.println("userService(): zoneId = " + this.zoneId);
    }

    public ZoneId getZoneId(){
        return zoneId;
    }
    public final ZoneId getFinalZoneId(){
        return  zoneId;
    }
}
