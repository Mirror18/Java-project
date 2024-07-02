package com.mirror.stream;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class UseStream {
    public static void main(String[] args) {
        //第一种
        Stream<String> stream = Stream.of("A","B","C");
        stream.forEach(System.out::println);

        //第二种
        Stream<String> stream1 = Arrays.stream(new String[]{"A","B","C"});
        Stream<String> stream2 = List.of("x","Y","Z").stream();
        stream1.forEach(System.out::println);
        stream2.forEach(System.out::println);

        // 第三种
        Stream<Integer> natual = Stream.generate(new NatualSupplier());
        natual.limit(10).forEach(System.out::println);

        //其他方法
        Pattern p = Pattern.compile("\\s+");
        Stream<String> s = p.splitAsStream("The quick brown fox jumps over the lazy dog");
        s.forEach(System.out::println);

        //基本类型
        // 将int[]数组变为IntStream:
        IntStream is = Arrays.stream(new int[] { 1, 2, 3 });
// 将Stream<String>转换为LongStream:
        LongStream ls = List.of("1", "2", "3").stream().mapToLong(Long::parseLong);
    }
}
class NatualSupplier implements Supplier<Integer>{
    int n = 0;
    @Override
    public Integer get() {
        n++;
        return n;
    }
}