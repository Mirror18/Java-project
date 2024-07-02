package com.mirror.core;

import lombok.Data;
import lombok.Generated;
import lombok.Getter;
import lombok.Value;

public class ChangEnum {
    public static void main(String[] args) {
        Employee employee = new Employee();
        employee.setRestDay(Weekday2.SAT);
//        employee.setRestDay(6);
        /*
        对入参进行限制
        从int转换为integer，包装类
        但因为存在自动装箱，所以没变
         */
    }
}

@Data
class Employee {
    //    private int restDay;
//    private Integer restDay;
    private Weekday2 restDay;

    //    public void setRestDay(Integer restDay) {
//        if (restDay > 7 || restDay < 0) {
//            throw new RuntimeException("");
//        }
//        this.restDay = restDay;
//    }
    public void setRestDay(Weekday2 weekday2) {
        this.restDay = weekday2;
    }
}
@Getter
class Weekday2 {
//    public static final Integer SUN = 0;
//    public static final Integer MON = 1;
//    public static final Integer TUE = 2;
//    public static final Integer WED = 3;
//    public static final Integer THU = 4;
//    public static final Integer FRI = 5;
//    public static final Integer SAT = 6;


//public static final Weekday2 SUN = new Weekday2();
//public static final Weekday2 MON = new Weekday2();
//public static final Weekday2 TUE = new Weekday2();
//public static final Weekday2 WED = new Weekday2();
//public static final Weekday2 THU = new Weekday2();
//public static final Weekday2 FRI = new Weekday2();
//public static final Weekday2 SAT = new Weekday2();

    public static final Weekday2 SUN = new Weekday2(0, "sunday");
    public static final Weekday2 MON = new Weekday2(1, "monday");
    public static final Weekday2 TUE = new Weekday2(2, "tuesday");
    public static final Weekday2 WED = new Weekday2(3, "wednesday");
    public static final Weekday2 THU = new Weekday2(4, "Thursday");
    public static final Weekday2 FRI = new Weekday2(5, "friday");
    public static final Weekday2 SAT = new Weekday2(6, "saturday");

    public static final Weekday2[] VALUES = new Weekday2[]{SUN,MON,TUE,WED,THU,FRI,SAT};
    //private Weekday2(){}
    private final Integer code;
    private final String desc;

    private Weekday2(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static Weekday2 of(Integer code){
        for(Weekday2 weekday2:VALUES){
            if(weekday2.code.equals(code)){
                return weekday2;
            }
        }
        throw new IllegalArgumentException("");
    }

    public static Weekday2[] values(){
        return VALUES;
    }

    public static String getDesc(Integer code){
        Weekday2[] weekday2s = Weekday2.values();
        for(Weekday2 weekday2 :weekday2s){
            if(weekday2.getCode().equals(code)){
                return weekday2.getDesc();
            }
        }
        throw new IllegalArgumentException("");
    }
}
