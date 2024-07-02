package com.mirror.action.interpreter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author mirror
 */
public class Test {
    public static void main(String[] args) {
        log("[{}] start {} at {}...", LocalTime.now().withNano(0), "engine", LocalDate.now());
    }

    public static void log(String format, Object... args) {
        int length = format.length();
        int index = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if ('{' == format.charAt(i)) {
                if (i < length - 1) {
                    if ('}' == format.charAt(i + 1)) {
                        stringBuilder.append(args[index]);
                        index++;
                        i++;
                    }
                }
            } else {
                stringBuilder.append(format.charAt(i));
            }
        }
        System.out.println(stringBuilder);
    }
}