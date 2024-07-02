package com.mirror.stream;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mirror
 */
public class OutOperation {
    public static void main(String[] args) {
        //转换
        Stream<Integer> s1 = Stream.generate(new NatualSupplier());
        Stream<Integer> s2 = s1.map(n -> n * n);
        Stream<Integer> s3 = s2.map(n -> n - 1);
        System.out.println(s3); // java.util.stream.ReferencePipeline$3@49476842

        //输出为列表
        Stream<String> stream = Stream.of("Apple", "", null, "Pear", "  ", "Orange");
        List<String> list = stream.filter(s -> s != null && !s.isBlank()).collect(Collectors.toList());
        System.out.println(list);
        //输出为数组
        List<String> list1 = List.of("Apple", "Banana", "Orange");
        String[] array = list1.stream().toArray(String[]::new);

        //输出为map
        Stream<String> stream1 = Stream.of("APPL:Apple", "MSFT:Microsoft");
        Map<String, String> map = stream1
                .collect(Collectors.toMap(
                        // 把元素s映射为key:
                        s -> s.substring(0, s.indexOf(':')),
                        // 把元素s映射为value:
                        s -> s.substring(s.indexOf(':') + 1)));
        System.out.println(map);

        //分组输出
        List<String> list2 = List.of("Apple", "Banana", "Blackberry", "Coconut", "Avocado", "Cherry", "Apricots");
        Map<String, List<String>> groups = list2.stream()
                .collect(Collectors.groupingBy(s -> s.substring(0, 1), Collectors.toList()));
        System.out.println(groups);
    }
}

