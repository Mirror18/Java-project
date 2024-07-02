package com.mirror.jackson;

import java.io.IOException;
import java.io.InputStream;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author mirror
 */
public class Main {
    public static void main(String[] args) {
        InputStream input = Main.class.getResourceAsStream("/book.xml");
        JacksonXmlModule module = new JacksonXmlModule();
        XmlMapper mapper = new XmlMapper(module);
        Book book = null;
        try {
            book = mapper.readValue(input, Book.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert book != null;
        System.out.println(book.id);
        System.out.println(book.name);
        System.out.println(book.author);
        System.out.println(book.isbn);
        System.out.println(book.tags);
        System.out.println(book.pubDate);
    }
}
