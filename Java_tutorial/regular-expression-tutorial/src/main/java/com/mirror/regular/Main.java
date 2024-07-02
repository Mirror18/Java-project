package com.mirror.regular;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String regex = "20\\d\\d";
        System.out.println("2019".matches(regex)); // true
        System.out.println("2100".matches(regex)); // false

        System.out.println();
        String re1 = "abc";
        System.out.println("abc".matches(re1));
        System.out.println("Abc".matches(re1));
        System.out.println("abcd".matches(re1));
        System.out.println();
        String re2 = "a\\&c"; // 对应的正则是a\&c
        System.out.println("a&c".matches(re2));
        System.out.println("a-c".matches(re2));
        System.out.println("a&&c".matches(re2));

        System.out.println("a\u548cc".matches("a和c"));

        System.out.println();
        System.out.println("a&c".matches("a.c"));
        System.out.println("a-c".matches("a.c"));
        System.out.println("a&&c".matches("a.c"));

        System.out.println();
        String re3 = "java\\d"; // 对应的正则是java\d
        System.out.println("java9".matches(re3));
        System.out.println("java10".matches(re3));
        System.out.println("javac".matches(re3));

        String re4 = "java\\D";
        System.out.println("javax".matches(re4));
        System.out.println("java#".matches(re4));
        System.out.println("java5".matches(re4));

        System.out.println();
        String re = "java|php";
        System.out.println("java".matches(re));
        System.out.println("php".matches(re));
        System.out.println("go".matches(re));

        System.out.println();
        String re5 = "[1-9]\\d{3,5}";
        System.out.println("13666".matches(re5));

        System.out.println();
        String re6 = "learn\\s(java|php|go)";
        System.out.println("learn java".matches(re6));
        System.out.println("learn Java".matches(re6));
        System.out.println("learn php".matches(re6));
        System.out.println("learn Go".matches(re6));

        System.out.println();
        String re7 = "learn\\s([jJ]ava|[pP]hp|[gG]o)";
        System.out.println("learn java".matches(re7));
        System.out.println("learn Java".matches(re7));
        System.out.println("learn php".matches(re7));
        System.out.println("learn Go".matches(re7));

        System.out.println();
        Pattern p = Pattern.compile("(\\d{3,4})-(\\d{7,8})");
        Matcher m = p.matcher("010-12345678");
        if (m.matches()) {
            String g1 = m.group(1);
            String g2 = m.group(2);
            System.out.println(g1);
            System.out.println(g2);
        } else {
            System.out.println("匹配失败!");
        }

        System.out.println();
        Pattern pattern = Pattern.compile("(\\d{3,4})-(\\d{7,8})");
        pattern.matcher("010-12345678").matches(); // true
        pattern.matcher("021-123456").matches(); // false
        pattern.matcher("022#1234567").matches(); // false
        // 获得Matcher对象:
        Matcher matcher = pattern.matcher("010-12345678");
        if (matcher.matches()) {
            String whole = matcher.group(0); // "010-12345678", 0表示匹配的整个字符串
            String area = matcher.group(1); // "010", 1表示匹配的第1个子串
            String tel = matcher.group(2); // "12345678", 2表示匹配的第2个子串
            System.out.println(area);
            System.out.println(tel);
        }


        System.out.println();
        Pattern pattern1 = Pattern.compile("(\\d+?)(0*)");
        Matcher matcher1 = pattern1.matcher("10030000");
        if (matcher1.matches()) {
            System.out.println("group1=" + matcher1.group(1)); // "123"
            System.out.println("group2=" + matcher1.group(2)); // "0000"
        }


        System.out.println();
        String s = "the quick brown fox jumps over the lazy dog.";
         Pattern p1 = Pattern.compile("\\wo\\w");
        Matcher m1 = p1.matcher(s);
        while (m1.find()) {
            String sub = s.substring(m1.start(), m1.end());
            System.out.println(sub);
        }

        System.out.println();
        String s1 = "The     quick\t\t brown   fox  jumps   over the  lazy dog.";
        String r = s1.replaceAll("\\s+", " ");
        System.out.println(r); // "The quick brown fox jumps over the lazy dog."

        System.out.println();
        String r2 = s.replaceAll("\\s([a-z]{4})\\s", " <b>$1</b> ");
        System.out.println(r2);
    }
}
