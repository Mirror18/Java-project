package com.mirror.exception;

import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Person p = new Person();
        System.out.println(p.address.city.toLowerCase(Locale.ROOT));

        /*
        对于空指针的错误，不应该catch，应当早暴露，早处理
         */
    }

}

class Person {
    String[] name = new String[2];
    Address address = new Address();
}
class Address{
    String city;
    String street;
    String zipcode;
}
