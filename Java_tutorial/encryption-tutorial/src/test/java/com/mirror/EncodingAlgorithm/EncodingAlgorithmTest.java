package com.mirror.EncodingAlgorithm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class EncodingAlgorithmTest {

    @ParameterizedTest
    @MethodSource
    void testEncoding(String string,String result){
        assertEquals(result, URLEncoder.encode(string, StandardCharsets.UTF_8));
    }

    static List<Arguments> testEncoding(){
        return List.of(
                Arguments.of("中文!","%E4%B8%AD%E6%96%87%21")
        );
    }

   @ParameterizedTest
   @CsvSource("%E4%B8%AD%E6%96%87%21,中文!")
    void testDecoding(String input,String result){
        assertEquals(result, URLDecoder.decode(input, StandardCharsets.UTF_8));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "./test-base54.csv",numLinesToSkip = 1)
    void testBase54(ArgumentsAccessor accessor){
        String input1 = accessor.getString(0);
        String result1 = accessor.getString(1);
        //解码
        byte[] input = Base64.getDecoder().decode(input1);
        assertEquals(result1,new String(input));
        //编码
        String result = Base64.getEncoder().encodeToString(result1.getBytes(StandardCharsets.UTF_8));
        assertEquals(input1,result);


    }
}
