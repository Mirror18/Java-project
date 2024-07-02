package com.mirror.create.abstract_factory;

public interface AbstractFactory {
    // 创建Html文档:
    HtmlDocument createHtml(String md);
    // 创建Word文档:
    WordDocument createWord(String md);

//    public static AbstractFactory createFactory(String name) {
//        if ("fast".equalsIgnoreCase(name)) {
//            return new FastFactory();
//        } else if ("good".equalsIgnoreCase(name)) {
//            return new GoodFactory();
//        } else {
//            throw new IllegalArgumentException("Invalid factory name");
//        }
//    }
}