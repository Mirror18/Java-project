package com.mirror.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mirror
 */
public class OtherOperation {

    public static void main(String[] args) {

        //排序
        List<String> list = List.of("Orange", "apple", "Banana")
                .stream()
                .sorted()
                .collect(Collectors.toList());
        System.out.println(list);

        //去重
        List.of("A", "B", "A", "C", "B", "D")
                .stream()
                .distinct()
                .collect(Collectors.toList()); // [A, B, C, D]

        //截取
        List.of("A", "B", "C", "D", "E", "F")
                .stream()
                .skip(2) // 跳过A, B
                .limit(3) // 截取C, D, E
                .collect(Collectors.toList()); // [C, D, E]

        //合并
        Stream<String> s1 = List.of("A", "B", "C").stream();
        Stream<String> s2 = List.of("D", "E").stream();
// 合并:
        Stream<String> s = Stream.concat(s1, s2);
        System.out.println(s.collect(Collectors.toList())); // [A, B, C, D, E]


        //映射
        Stream<List<Integer>> s3 = Stream.of(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9));

        Stream<Integer> i = s3.flatMap(list2-> list2.stream());


    }
}
