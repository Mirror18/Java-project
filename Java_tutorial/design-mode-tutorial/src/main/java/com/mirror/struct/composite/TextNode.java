package com.mirror.struct.composite;

import java.util.List;

public class TextNode implements Node {
    private final String text;

    public TextNode(String text) {
        this.text = text;
    }

    @Override
    public Node add(Node node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Node> children() {
        return List.of();
    }

    @Override
    public String toXml() {
        return text;
    }
}