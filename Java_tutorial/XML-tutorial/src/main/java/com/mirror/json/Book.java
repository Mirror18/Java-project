package com.mirror.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @author mirror
 */
public class Book {
    public long id;
    public String name;
    public Map<String ,String> author;
    // 表示反序列化isbn时使用自定义的IsbnDeserializer:
    @JsonDeserialize(using = IsbnDeserializer.class)
    public BigInteger isbn;
    public List<String> tags;
    public String pubDate;
}
