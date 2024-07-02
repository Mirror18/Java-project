package com.mirror.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author mirror
 */
public class Main {
    public static void main(String[] args) {
        InputStream input = Main.class.getResourceAsStream("/book.json");
        ObjectMapper mapper = new ObjectMapper();
// 反序列化时忽略不存在的JavaBean属性:
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            Book book = mapper.readValue(input, Book.class);
            System.out.println(book.id);
            System.out.println(book.name);
            System.out.println(book.author);
            System.out.println(book.isbn);
            System.out.println(book.tags);
            System.out.println(book.pubDate);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
